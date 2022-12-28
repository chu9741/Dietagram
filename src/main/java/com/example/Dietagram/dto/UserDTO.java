package com.example.Dietagram.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private long id;
    private String attributeId;
    private String nickname;
    private long calorie_goal;
    private String token;
    private long weight;
    private long height;

    private List<ResponseFeedDTO> responseFeedDTO;
    private List<Long> followingList;
    private List<Long> followerList;




}
