package com.example.Dietagram.dto;

import com.example.Dietagram.domain.FeedComment;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFeedCommentDTO {
    // FeedComment display
    private long id;
    private String nickname;
    private String content;
    private LocalDateTime createdDate;


}
