package com.example.Dietagram.controller;


import com.example.Dietagram.domain.User;
import com.example.Dietagram.dto.UserEditDTO;
import com.example.Dietagram.service.OAuthService;
import com.example.Dietagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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






}
