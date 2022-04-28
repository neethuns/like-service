package com.maveric.demo.service;

import com.maveric.demo.dto.LikeDto;
import com.maveric.demo.model.Like;

import java.util.List;

public interface LikeService {


    List<LikeDto> getAllLikes(String postorcommentId,Integer page,Integer pageSize);

    LikeDto createLike(Like like, String postorcommentId);

    Long getLikesCount(String postorcommentId);


    LikeDto getLikeDetails(String likeId);

    String removeLike(String likeId);

    Boolean IsLikeIdExists(String postorcommentId,String likeId);



}
