package com.maveric.demo.repo;

import com.maveric.demo.model.Like;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@DataMongoTest
public class LikeRepoTest {

    @Autowired
    LikeRepo likeRepo;
    @BeforeEach
    void initUseCase() {
        Like like = createLikeList1();
        likeRepo.save(like);
        Like like1 = createLikeList2();
        likeRepo.save(like1);
    }



    @AfterEach
    public void destroyByAll() {
        likeRepo.deleteAll();
    }


    @Test
    void findBypostorcommentId() {

        //List<Comment> comments= createCommentList();
        Optional<List<Like>> likes = likeRepo.findBypostorcommentId("2");
        assertEquals(2, likes.get().size());
    }

    private Like createLikeList1() {

        List<Like> likes = new ArrayList<>();
        Like like = new Like();
        like.setLikeId("1");
        like.setPostorcommentId("2");
        like.setCreatedAt(null);
        like.setLikedBy("123");
        return like;
    }

    private Like createLikeList2() {

        List<Like> likes = new ArrayList<>();
        Like like2 = new Like();
        like2.setLikeId("2");
        like2.setPostorcommentId("2");
        like2.setCreatedAt(null);
        like2.setLikedBy("1234");
        return like2;
    }
}