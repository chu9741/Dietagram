package com.example.Dietagram.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFeedDTO {

    private long id;
    private String content;
    private String nickname;
    private LocalDateTime createdDate;
//    private LocalDateTime created_at;
    private ResponseFeedImageDTO responseFeedImageDTO;
    private List<ResponseFeedCommentDTO> responseFeedCommentDTOList;


    //     response - 메인 피드에 렌더링할 피드 array (친구들 포함)
    //    각 피드별로 필요한 데이터 : 작성자 닉네임, 피드 content, 피드 등록 날짜, imageurl, nutritiondto,  댓글(작성자, 댓글 내용)



}
