package com.example.Dietagram.config;


import com.example.Dietagram.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfile {

    private String name;
    private String email;
    private String provider;
    private String nickname;
//    private String token;

    public User toUser() {
        return User.builder()
                .name(name)
                .email(email)
                .provider(provider)
                .nickname(name+email)
//                .token(token)
                .build();
    }

}
