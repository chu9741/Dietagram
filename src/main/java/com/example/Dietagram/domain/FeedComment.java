package com.example.Dietagram.domain;


import com.example.Dietagram.dto.ResponseFeedCommentDTO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class FeedComment extends BaseTimeDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_comment_id")
    private long id;

    private String nickname;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feedCommentFeed;


    public ResponseFeedCommentDTO toResponseDTO(){
        return ResponseFeedCommentDTO.builder().id(id)
                .nickname(nickname).content(content)
                .build();
    }



}
