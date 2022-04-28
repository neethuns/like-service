package com.maveric.demo.service;


import com.maveric.demo.constant.LikeConstant;
import com.maveric.demo.dto.LikeDto;
import com.maveric.demo.dto.UserDto;
import com.maveric.demo.exception.CustomFeignException;
import com.maveric.demo.exception.LikeNotFoundException;
import com.maveric.demo.feign.UserFeign;
import com.maveric.demo.model.Like;
import com.maveric.demo.repo.LikeRepo;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.xml.stream.events.Comment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@ContextConfiguration(classes = {LikeServiceImpl.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LikeServiceTest {

    @Mock
    LikeRepo likeRepo;

   @InjectMocks
    LikeServiceImpl likeService;

    @Mock
    UserFeign userFeign;

    @Test
    public void testRemoveLike() {

        Like like = new Like();
        like.setLikeId("1");
        like.setLikedBy("12");
        like.setCreatedAt(LocalDateTime.now());
        like.setPostorcommentId("1");
       Mockito.when(likeRepo.findById("1")).thenReturn(Optional.of(like));
       likeService.removeLike("1");
      verify(likeRepo,times(1)).deleteById("1");
     //   when(this.likeService.removeLike("1")).thenReturn(LikeConstant.DELETELIKE);
        // doNothing().when(this.likeRepo).deleteById((String) any());


    }

    @Test
    public void testExceptionThrownWhenIdNotFound() {
        Mockito.doThrow(LikeNotFoundException.class).when(likeRepo).deleteById(any());
        Exception likeNotFoundException = assertThrows(LikeNotFoundException.class, () -> likeService.removeLike("1"));
        assertTrue(likeNotFoundException.getMessage().contains(LikeConstant.LIKENOTFOUND));
    }

    @Test
    public void testGetAllLikes() {
        UserDto userDto = new UserDto();
        when(this.userFeign.getUserDetails((String) any())).thenReturn(ResponseEntity.of(Optional.of(userDto)));

        Like like = new Like();
        like.setLikeId("1");
        like.setLikedBy("12");
        like.setCreatedAt(LocalDateTime.now());
        like.setPostorcommentId("1");

        LikeDto likeDto = new LikeDto();
        likeDto.setLikeId("1");
        likeDto.setLikedBy(userDto);
        likeDto.setCreatedAt(LocalDateTime.now());
        likeDto.setPostorcommentId("1");

        Mockito.when(likeRepo.save(like)).thenReturn(like);

        List<Like> likeList = new ArrayList<>();
        likeList.add(like);

        List<LikeDto> likeListDto = new ArrayList<>();
        likeListDto.add(likeDto);


        Mockito.when(likeRepo.findBypostorcommentId((String) any())).thenReturn(Optional.of(likeList));
      //  when(this.likeService.getAllLikes("1",1,1)).thenReturn(likeListDto);
        //  List<LikeDto> actualLikes = this.likeService.getAllLikes("1",1,1);
       // assertEquals(1, likeListDto.size());
        assertEquals(1, likeService.getAllLikes("1", 1, 3).size());
//        LikeDto getResult = actualLikes.get(0);
        assertEquals("1", likeListDto.get(0).getLikeId());
    }

    @Test
    public void testExceptionThrownWhenLikeNotFoundByPostOrCommentId() {
         List<Like> likeList = new ArrayList<>();
       Mockito. when(likeRepo.findBypostorcommentId((String) any())).thenReturn(Optional.of(likeList));
        assertThrows(LikeNotFoundException.class, () -> this.likeService.getAllLikes("1", 1, 1));
    }

    @Test
    public void testExceptionThrownForFeignConnectionIssueWhenGetAllLikes() {
        Mockito.when(userFeign.getUserDetails((String) any())).thenThrow(mock(CustomFeignException.class));
        Like like = new Like();
        like.setCreatedAt(LocalDateTime.now());
        like.setLikeId("1");
        like.setLikedBy("Feign Test");
        like.setPostorcommentId("1");

     List<Like> likeList = new ArrayList<>();
        likeList.add(like);
        Mockito.when(likeRepo.findBypostorcommentId((String) any())).thenReturn(Optional.of(likeList));
        assertThrows(CustomFeignException.class, () -> this.likeService.getAllLikes("1", 1, 3));
    }

    @Test
    void getLikesCountById() {
        List<Like> likes = new ArrayList<>();
        Like like1 = new Like();
        like1.setLikeId("1");
        like1.setLikedBy("1");

        Like like2 = new Like();
        like2.setLikeId("2");
        like2.setLikedBy("2");

        likes.add(like1);
        likes.add(like2);
        Mockito.when(likeRepo.findBypostorcommentId("1")).thenReturn(Optional.of(likes));
        assertThat(likeService.getLikesCount("1"));
    }

    @Test
    public void testExceptionThrownWhenLikesNotFoundById() {
        Mockito.doThrow(LikeNotFoundException.class).when(likeRepo).findBypostorcommentId(any());
         assertThrows(LikeNotFoundException.class, () -> likeService.getLikesCount("1"));
       // assertTrue(likeNotFoundException.getMessage().contains(LikeConstant.LIKENOTFOUND));
    }

    @Test
    public void testGetLikeDetails() {
        UserDto userDto = new UserDto("12","First","Second","Third","1",LocalDate.of(1989,10,13),"MALE","1","O_POS","anug@mail.com","chennai");

       Mockito.when(userFeign.getUserDetails((String) any())).thenReturn(ResponseEntity.of(Optional.of(userDto)));

//        when(this.userFeign.getUserDetails((String) any())).thenThrow(mock(FeignException.class));
//        List<Like> likeList=new ArrayList<>();
        Like like = new Like();
        like.setLikeId("1");
        like.setLikedBy("12");
        like.setCreatedAt(LocalDateTime.now());
        like.setPostorcommentId("1");
//        likeList.add(like);

        LikeDto likeDto = new LikeDto();
        likeDto.setLikeId("1");
        likeDto.setLikedBy(userDto);
        likeDto.setCreatedAt(LocalDateTime.now());
        likeDto.setPostorcommentId("1");

        Mockito.when(likeRepo.findById((String) any())).thenReturn(Optional.of(like));
        assertEquals(likeService.getLikeDetails("1"),likeDto);
        assertEquals(like.getLikedBy(), likeDto.getLikedBy().getUserId());
//        assertThrows(LikeNotFoundException.class, () -> this.likeService.getLikeDetails("1"));
    }
    @Test
    public void testExceptionThrownWhenGetLikeDetails()
    {
       Mockito.when(userFeign.getUserDetails((String) any())).thenThrow(mock(FeignException.class));
        assertThrows(LikeNotFoundException.class, () -> likeService.getLikeDetails("1"));
    }

    @Test
    public void testCreateLike() {
        UserDto userDto = new UserDto("12", "First", "Second", "Third", "9090345678", LocalDate.of(1989, 10, 13), "MALE", "12345", "O_POS", "anug@mail.com", "Chennai");

        Mockito.when(userFeign.getUserDetails((String) any())).thenReturn(ResponseEntity.of(Optional.of(userDto)));

        Like like = new Like();
        like.setLikeId("1");
        like.setLikedBy("12");
        like.setCreatedAt(null);
        like.setPostorcommentId("1");

        LikeDto likeDto = new LikeDto();
        likeDto.setLikeId("1");
        likeDto.setLikedBy(userDto);
        likeDto.setCreatedAt(null);
        likeDto.setPostorcommentId("1");

        Mockito.when(likeRepo.save(like)).thenReturn(like);
       when(likeService.createLike(like,"1")).thenReturn(likeDto);
//        assertNull(actualCreateLikeResult.getLikeId());
        assertEquals("12",likeDto.getLikedBy().getUserId());
        assertEquals("1", likeDto.getPostorcommentId());
    }

    @Test
    void testExceptionThrownWhenFeignConnectionIssueForCreateLike() {
        Like like = new Like("1", "1","12",null);
        when(userFeign.getUserDetails((String) any())).thenThrow(mock(CustomFeignException.class));
        when(likeRepo.save(like)).thenThrow(mock(CustomFeignException.class));
        assertThrows(CustomFeignException.class, () -> likeService.createLike(like,"1"));
    }
    @Test
    public void isLikeIdExists() {
        Like LikeSelected = new Like();
        Like like = new Like();
        ArrayList<Like> likes = new ArrayList();

        LikeSelected.setLikedBy("12");
        LikeSelected.setLikeId("1");
        LikeSelected.setPostorcommentId("1");

       likes.add(LikeSelected);
        Mockito.when(likeRepo.findBypostorcommentId("1")).thenReturn(Optional.of(likes));
        assertEquals(likeService.IsLikeIdExists("1", "1"), true);
    }
}