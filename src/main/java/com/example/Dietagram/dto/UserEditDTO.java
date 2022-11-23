package com.example.Dietagram.dto;


import com.example.Dietagram.domain.Gender;
import com.example.Dietagram.domain.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEditDTO {
    private Gender gender;
    private String originName;
    private String newName;
    private long calorie_goal;
    private long weight;
    private long height;
}

//public User toEntity(){
//    return User.builder().gender(gender).calorie_goal(calorie_goal)
//            .nickname(newName).height(height).weight(weight).build();
//    }
//}
