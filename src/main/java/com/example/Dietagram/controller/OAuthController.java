package com.example.Dietagram.controller;

import com.example.Dietagram.domain.User;
import com.example.Dietagram.service.OAuthService;
import com.example.Dietagram.service.TokenProvider;
import com.example.Dietagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @Autowired
    UserService userService;

    @Autowired
    OAuthService oAuthService;

    @GetMapping("/loginInfo")
    public ResponseEntity<?> oauthLoginInfo(Authentication authentication){
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        User user = userService.getUserFromRepo((String)attributes.get("name")+(String) attributes.get("email"));
        return ResponseEntity.ok().body(oAuthService.setTokenInUser(user));
    }
}
