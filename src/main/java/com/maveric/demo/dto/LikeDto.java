package com.maveric.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDto {

    @Id
    private String likeId;
    private String postorcommentId;
    private UserDto likedBy;
    private LocalDateTime createdAt;
}
