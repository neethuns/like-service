package com.maveric.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.maveric.demo.constant.LikeConstant;
import com.maveric.demo.dto.LikeDto;
import com.maveric.demo.dto.UserDto;
import com.maveric.demo.model.Like;
import com.maveric.demo.repo.LikeRepo;
import com.maveric.demo.service.LikeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(LikeController.class)
public class LikeControllerTest {

    @MockBean
    LikeService likeService;
    @MockBean
    LikeRepo likeRepo;

    @Autowired
    MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetLikes()throws Exception {
        List<LikeDto> likeDto = createLikeList();

        Mockito.when(likeService.getAllLikes("1", 1, 2)).thenReturn(likeDto);

        mockMvc.perform(get("/api/v1/postsOrComments/1/likes?page=1&pageSize=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].postorcommentId", Matchers.is("TestOne")))
                .andExpect(jsonPath("$[1].postorcommentId", Matchers.is("TestTwo")));
    }

    private List<LikeDto> createLikeList() {
        List<LikeDto> likeDtoList = new ArrayList<>();

        LikeDto likeDto = new LikeDto();
        likeDto.setLikeId("1");
        likeDto.setPostorcommentId("TestOne");
        likeDto.setLikedBy(new UserDto("1234","SecondTest","middleTest","lastTest","123",LocalDate.now(),"MALE","123","B_POS","aug@gmail.com","Bangalore"));

        likeDto.setCreatedAt(null);

        LikeDto likeDto1 = new LikeDto();
        likeDto1.setLikeId("2");
        likeDto1.setPostorcommentId("TestTwo");
        likeDto1.setLikedBy(new UserDto("1234","SecondTest","middleTest","lastTest","123",LocalDate.now(),"MALE","123","B_POS","aug@gmail.com","Bangalore"));
        likeDto1.setCreatedAt(null);

        likeDtoList.add(likeDto);
        likeDtoList.add(likeDto1);
        return likeDtoList;
    }

    @Test
    public void testCreateLike() throws Exception {
        Like like = createOneLikeToPost();
        LikeDto likeDto = new LikeDto();

        Mockito.when(likeService.createLike(like,"1")).thenReturn(likeDto);
        mockMvc.perform(post("/api/v1/postsOrComments/1/likes")
                        .content(asJsonString(like))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    private Like createOneLikeToPost() {
        Like like = new Like();
        like.setLikeId("1");
        like.setPostorcommentId("1");
        like.setLikedBy(String.valueOf(new UserDto("1234","SecondTest","middleTest","lastTest","123",LocalDate.now(),"MALE","123","B_POS","aug@gmail.com","Bangalore")));
        return like;
    }

    @Test
    public void testGetLikeDetails() throws Exception {
        LikeDto likeDto = createOneLike();
        Mockito.when(likeService.IsLikeIdExists("1","2")).thenReturn(true);

        Mockito.when(likeService.getLikeDetails("2")).thenReturn(likeDto);

        mockMvc.perform(get("/api/v1/postsOrComments/1/likes/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.aMapWithSize(4)))
                .andExpect(jsonPath("$.postorcommentId", Matchers.is("LikeTest")));
    }

    private LikeDto createOneLike() {
        LikeDto likeDto = new LikeDto();
        likeDto.setLikeId("2");
        likeDto.setPostorcommentId("LikeTest");
        likeDto.setLikedBy(new UserDto("1234","SecondTest","middleTest","lastTest","123",LocalDate.now(),"MALE","123","B_POS","aug@gmail.com","Bangalore"));

        return likeDto;
    }

    @Test
    public void testRemoveLike() throws Exception {
        LikeDto likeDto = createOneLike();
        Mockito.when(likeService.IsLikeIdExists("1","2")).thenReturn(true);
        Mockito.when(likeService.removeLike("2")).thenReturn(LikeConstant.DELETELIKE);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/postsOrComments/1/likes/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetLikesCount() throws Exception {
        Integer count = createLikesToCount();

        Mockito.when(likeService.getLikesCount("1")).thenReturn(Long.valueOf(count));
        mockMvc.perform(get("/api/v1/postsOrComments/1/likes/count"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private Integer createLikesToCount() {
        List<Like> likes = new ArrayList<>();

        Like like1 = new Like();
        Like like2 = new Like();
        Like like3 = new Like();

        likes.add(like1);
        likes.add(like2);
        likes.add(like3);
        return likes.size();
    }
}
