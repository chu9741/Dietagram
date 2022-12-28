package com.example.Dietagram.dto;

import com.example.Dietagram.domain.Gender;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrimeDTO {
    private long id;
    private String name;
    private String email;
    private String nickname;
    private Gender gender;

}
