package com.maveric.demo.controller;

import com.maveric.demo.dto.LikeDto;
import com.maveric.demo.exception.LikeNotFoundException;
import com.maveric.demo.model.Like;
import com.maveric.demo.repo.LikeRepo;
import com.maveric.demo.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.util.List;

import static com.maveric.demo.constant.LikeConstant.LIKENOTFOUND;

@CrossOrigin(value = "*")
@RestController
@RequestMapping("/api/v1/postsOrComments/{postorcommentId}/likes")
public class LikeController {

    @Autowired
    LikeService likeService;
    @Autowired
    LikeRepo likeRepo;

    @GetMapping
    public ResponseEntity<List<LikeDto>> getLikes(@PathVariable("postorcommentId") String postorcommentId, @QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize){

            return new ResponseEntity<>(likeService.getAllLikes(postorcommentId, page, pageSize), HttpStatus.OK);

        }

    @PostMapping
    public ResponseEntity<LikeDto> createLike(@Valid @RequestBody Like like, @PathVariable("postorcommentId") String postorcommentId)
    {


            return new ResponseEntity<>(likeService.createLike(like,postorcommentId),HttpStatus.CREATED);


    }
    @GetMapping("/count")
    public ResponseEntity<Object> getLikesCount(@PathVariable("postorcommentId") String postorcommentId)
    {
        return new ResponseEntity<>(likeService.getLikesCount(postorcommentId), HttpStatus.OK);
    }

    @GetMapping("/counts")
    public Long getLikesCounts(@PathVariable("postorcommentId") String postorcommentId)
    {
        return likeService.getLikesCount(postorcommentId);
    }

    @GetMapping("/{likeId}")
    public ResponseEntity <LikeDto> getLikeDetails(@PathVariable("postorcommentId") String postorcommentId, @PathVariable("likeId") String likeId)
    {
        if( Boolean.TRUE.equals(likeService.IsLikeIdExists(postorcommentId,likeId)))
            return new ResponseEntity<>(likeService.getLikeDetails(likeId), HttpStatus.OK);
        else throw new LikeNotFoundException(LIKENOTFOUND + likeId);
    }


    @DeleteMapping("/{likeId}")
    public ResponseEntity<String> removeLike(@PathVariable("postorcommentId") String postorcommentId, @PathVariable("likeId") String likeId) {
        if( Boolean.TRUE.equals(likeService.IsLikeIdExists(postorcommentId,likeId)))
            return new ResponseEntity<>(likeService.removeLike(likeId), HttpStatus.OK);
        else throw new LikeNotFoundException(LIKENOTFOUND + likeId);

    }


}
