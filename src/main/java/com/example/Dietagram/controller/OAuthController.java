package com.example.Dietagram.controller;

import com.example.Dietagram.domain.User;
import com.example.Dietagram.service.OAuthService;
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
        System.out.println("attributes = "+ attributes);
        System.out.println("oAuthUser = "+ oAuth2User);
        User user = userService.getUserFromAttributeId(attributes);
        return ResponseEntity.ok().body(oAuthService.setTokenInUser(user));
    }
}
