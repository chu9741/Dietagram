package com.example.Dietagram.controller;


import com.example.Dietagram.domain.User;
import com.example.Dietagram.dto.UserEditDTO;
import com.example.Dietagram.dto.UserPrimeDTO;
import com.example.Dietagram.service.OAuthService;
import com.example.Dietagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    OAuthService oAuthService;

    @GetMapping("/mypage")
    public ResponseEntity<?> getUser(@RequestHeader String token){
        User userFromRepo = oAuthService.getUserByToken(token);
        if(userFromRepo == null){
            return ResponseEntity.badRequest().body("Cannot found user.");
        }
        return ResponseEntity.ok().body(userFromRepo);
    }

    @GetMapping("/mypage/edit")
    public ResponseEntity<?> editUserNamePage(@RequestHeader String token){
        User userFromRepo = oAuthService.getUserByToken(token);
        if(userFromRepo == null){
            return ResponseEntity.badRequest().body("Cannot found user.");
        }
        return ResponseEntity.ok().body(userFromRepo);
    }


    @PostMapping("/mypage/edit")
    public ResponseEntity<?> changeUserName(@RequestHeader String token,
                                            @RequestBody UserEditDTO userEditDTO){
        User userFromRepo = oAuthService.getUserByToken(token);
        if(userFromRepo == null){
            return ResponseEntity.badRequest().body("Cannot found user.");
        }
        userService.editMyPage(userEditDTO, userFromRepo);
        return ResponseEntity.ok().body(userFromRepo);
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




}
