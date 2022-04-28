package com.maveric.demo.repo;

import com.maveric.demo.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepo extends MongoRepository<Like, String> {
    Optional<List<Like>> findBypostorcommentId(String postorcommentId);

}
