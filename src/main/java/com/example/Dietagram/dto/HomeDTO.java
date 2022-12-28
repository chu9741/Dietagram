package com.example.Dietagram.dto;

import com.example.Dietagram.domain.Feed;
import com.example.Dietagram.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class HomeDTO {

    String token;
    String nickname;
//    List<Feed> feedList;

}
