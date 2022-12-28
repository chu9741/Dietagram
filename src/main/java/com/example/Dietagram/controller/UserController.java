package com.example.Dietagram.controller;


import com.amazonaws.Response;
import com.example.Dietagram.domain.User;
import com.example.Dietagram.dto.CalorieByDateDTO;
import com.example.Dietagram.dto.UserDTO;
import com.example.Dietagram.dto.UserEditDTO;
import com.example.Dietagram.dto.UserPrimeDTO;
import com.example.Dietagram.service.FeedService;
import com.example.Dietagram.service.OAuthService;
import com.example.Dietagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseExtractor;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    OAuthService oAuthService;

    @Autowired
    FeedService feedService;

    @GetMapping("/mypage")
    public ResponseEntity<?> getUser(@RequestHeader String token){
        User userFromRepo = oAuthService.getUserByToken(token);

        if(userFromRepo == null){
            return ResponseEntity.badRequest().body("Cannot found user.");
        }

//        feedService.getMyResponseFeedDTOList(userFromRepo); // userdto

        UserDTO userDTO = UserDTO.builder().id(userFromRepo.getId())
                .attributeId(userFromRepo.getAttributeId()).nickname(userFromRepo.getNickname())
                .calorie_goal(userFromRepo.getCalorie_goal()).token(userFromRepo.getToken())
                .weight(userFromRepo.getWeight()).height(userFromRepo.getHeight())
                .followerList(userFromRepo.getFollowerList()).followingList(userFromRepo.getFollowingList())
                .responseFeedDTO(feedService.getMyResponseFeedDTOList(userFromRepo))
                .build();

        return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping("/mypage/edit")
    public ResponseEntity<?> editUserNamePage(@RequestHeader String token){
        User userFromRepo = oAuthService.getUserByToken(token);
        if(userFromRepo == null){
            return ResponseEntity.badRequest().body("Cannot found user.");
        }
        return ResponseEntity.ok().body("not use.");
    }


    @PostMapping("/mypage/edit")
    public ResponseEntity<?> changeUserName(@RequestHeader String token,
                                            @RequestBody UserEditDTO userEditDTO){
        User userFromRepo = oAuthService.getUserByToken(token);
        if(userFromRepo == null){
            return ResponseEntity.badRequest().body("Cannot found user.");
        }
        userService.editMyPage(userEditDTO, userFromRepo);
        return ResponseEntity.ok().body("edit succeed.");
    }

    @GetMapping("/mypage/following")
    public ResponseEntity<?> getFollowingList(@RequestHeader String token){
        User userFromRepo = oAuthService.getUserByToken(token);
        if(userFromRepo == null){
            return ResponseEntity.badRequest().body("Cannot found user.");
        }
        List<UserPrimeDTO> userPrimeDTOList = userService.getFollowingList(userFromRepo);
        return ResponseEntity.ok().body(userPrimeDTOList);
    }

    @GetMapping("/mypage/follower")
    public ResponseEntity<?> getFollowerList(@RequestHeader String token){
        User userFromRepo = oAuthService.getUserByToken(token);
        if(userFromRepo == null){
            return ResponseEntity.badRequest().body("Cannot found user.");
        }
        List<UserPrimeDTO> userPrimeDTOList = userService.getFollowerList(userFromRepo);
        return ResponseEntity.ok().body(userPrimeDTOList);
    }



    @GetMapping("/userpage/{id}")
    public ResponseEntity<?> getUserPage(@RequestHeader String token,
                                         @PathVariable String id){
        User user = userService.getUserById(id);
        if(user == null){
            return ResponseEntity.badRequest().body("Cannot found user by Id.");
        }
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/userpage/follow")
    public ResponseEntity<?> followUser(@RequestHeader String token, @RequestParam String targetId){
        User user = oAuthService.getUserByToken(token);
        User targetUser = userService.getUserById(targetId);
        if(userService.followUser(user,targetUser).equals("failed"))
            return ResponseEntity.badRequest().body("already followed.");
        else return ResponseEntity.ok().body("follow succeed.");
    }

    @DeleteMapping("/userpage/unfollow")
    public ResponseEntity<?> unfollowUser(@RequestHeader String token, @RequestParam String targetId){
        User user = oAuthService.getUserByToken(token);
        User targetUser = userService.getUserById(targetId);
        if(userService.unfollowUser(user,targetUser).equals("failed"))
            return ResponseEntity.badRequest().body("Cannot find target user in following list.");
        return ResponseEntity.ok().body("unfollow succeed.");

    }

    @PostMapping("/user/search")
    public ResponseEntity<?> searchUser(@RequestHeader String token, @RequestParam String keyword){
        List<UserPrimeDTO> searchUserList =userService.searchUser(keyword);
        return ResponseEntity.ok().body(searchUserList);
    }

    @GetMapping("/mypage/calorie")
    public ResponseEntity<?> getFeedCalories(@RequestHeader String token){
        User userFromRepo = oAuthService.getUserByToken(token);

        List<CalorieByDateDTO> calorieByDateDTOList = feedService.getAllCalorieByDate(userFromRepo);

        return ResponseEntity.ok().body(calorieByDateDTOList);
    }

    @GetMapping("/mypage/calorie/minute")
    public ResponseEntity<?> getFeedCaloriesByMinute(@RequestHeader String token){
        User userFromRepo = oAuthService.getUserByToken(token);

        List<CalorieByDateDTO> calorieByDateDTOList = feedService.getAllCalorieByMinute(userFromRepo);

        return ResponseEntity.ok().body(calorieByDateDTOList);
    }




}
