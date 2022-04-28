package com.maveric.demo.model;

import com.maveric.demo.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection="like")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    @Id
    private String likeId;
    private String postorcommentId;
    @NotNull(message="LikedBy can not be blank")
    private String likedBy;
    private LocalDateTime createdAt;


}
