package com.maveric.demo.service;

import com.maveric.demo.constant.LikeConstant;
import com.maveric.demo.exception.CustomFeignException;
import com.maveric.demo.exception.LikeNotFoundException;
import com.maveric.demo.feign.UserFeign;
import com.maveric.demo.dto.LikeDto;
import com.maveric.demo.dto.UserDto;
import com.maveric.demo.model.Like;
import com.maveric.demo.repo.LikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.maveric.demo.constant.LikeConstant.*;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    LikeRepo likeRepo;

    @Autowired
    UserFeign userFeign;


    @Override
    public List<LikeDto> getAllLikes(String postorcommentId, Integer page, Integer pageSize) throws LikeNotFoundException {

       try
       {
           if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        Optional<List<Like>> likesList = likeRepo.findBypostorcommentId(postorcommentId);
        if (likesList.get().isEmpty()) {
            throw new LikeNotFoundException(LIKENOTFOUND + postorcommentId);
        } else {
            List<LikeDto> like1 = new ArrayList<>();
            for (Like like : likesList.get()) {
                UserDto userDto = userFeign.getUserDetails(like.getLikedBy()).getBody();

                like1.add(new LikeDto(like.getLikeId(), like.getPostorcommentId(), userDto, like.getCreatedAt()));
            }
            return like1;
        }
    }
       catch (feign.FeignException e)
    {
        throw new CustomFeignException(FEIGNEXCEPTON);
    }
//      catch (com.netflix.hystrix.exception.HystrixRuntimeException e){
//        throw new CustomFeignException(FEIGNEXCEPTON);
//    }

    }

    @Override
    public LikeDto createLike(Like like, String postorcommentId) {
try {
    Optional<List<Like>> likeSelected = likeRepo.findBypostorcommentId(postorcommentId);

    if (postorcommentId.equals(like.getPostorcommentId())) {

        like.setPostorcommentId(postorcommentId);
        like.setCreatedAt(LocalDateTime.now());
        UserDto userDto = userFeign.getUserDetails(like.getLikedBy()).getBody();
        likeRepo.save(like);

        return new LikeDto(like.getLikeId(), like.getPostorcommentId(), userDto, like.getCreatedAt());

    } else {
        throw new LikeNotFoundException(POSTORCOMMENTIDMISMATCH + postorcommentId);
    }
} catch (feign.FeignException e)
{
    throw new CustomFeignException(FEIGNEXCEPTON);
}
//catch (com.netflix.hystrix.exception.HystrixRuntimeException e){
//    throw new CustomFeignException(FEIGNEXCEPTON);
//}
}





    @Override
    public Long getLikesCount(String postorcommentId) {
        Optional<List<Like>> like = likeRepo.findBypostorcommentId(postorcommentId);

        if(like.get().isEmpty()) {
            throw new LikeNotFoundException(LIKENOTFOUND + postorcommentId);}
        else
        {
            return like.get().stream().count();
        }

    }

    public LikeDto getLikeDetails(String likeId)
    {
        try {
            Like like = likeRepo.findById(likeId).get();
            UserDto userDto = userFeign.getUserDetails(like.getLikedBy()).getBody();
            LikeDto likeDto = new LikeDto(like.getLikeId(), like.getPostorcommentId(), userDto, like.getCreatedAt());
            return likeDto;
        }

        catch (feign.FeignException e)
        {
            throw new CustomFeignException(FEIGNEXCEPTON);
        }
//        catch (com.netflix.hystrix.exception.HystrixRuntimeException e){
//            throw new CustomFeignException(FEIGNEXCEPTON);
//        }
        catch (Exception e){
            throw new LikeNotFoundException(LIKENOTFOUND+ likeId);
        }
//        Like like=likeRepo.findById(likeId).get();
//        UserDto userDto = userFeign.getUserDetails(like.getLikedBy()).getBody();
//        LikeDto likeDto = new LikeDto(like.getLikeId(),like.getPostorcommentId(),userDto,like.getCreatedAt());
//        return likeDto;
    }

//    @Override
//    public Optional<Like> getLikeDetails(String likeId) {
//        return likeRepo.findBylikeId(likeId);
//    }


    @Override
    public Boolean IsLikeIdExists(String postorcommentId, String likeId) {
        {
            Optional<List<Like>> like = likeRepo.findBypostorcommentId(postorcommentId);
            List<Like> likeList = like.get();
            for (Like like1 : likeList) {
                if (like1.getLikeId().equals(likeId))
                    return true;
            }
            return false;
        }
    }

    @Override
    public String removeLike(String likeId)
    {
        Optional<Like> likeSelect=likeRepo.findById(likeId);

        if(likeSelect.isPresent())
        {
            likeRepo.deleteById(likeId);
        return LikeConstant.DELETELIKE +likeId;
    }
else
        {
            throw new LikeNotFoundException(LIKENOTFOUND + likeId);
        }
    }


}
