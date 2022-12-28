package com.example.Dietagram.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String id;
    private String nickname;
}
