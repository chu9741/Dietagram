package com.example.Dietagram.controller;

import com.example.Dietagram.domain.User;
import com.example.Dietagram.dto.NutritionDTO;
import com.example.Dietagram.dto.UserEditDTO;
import com.example.Dietagram.service.NodeService;
import com.example.Dietagram.service.OAuthService;
import com.example.Dietagram.service.UserService;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import com.fasterxml.jackson.core.json.UTF8StreamJsonParser;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class HomeController {

    @Autowired
    private NodeService nodeService;

    @Autowired
    UserService userService;

    @Autowired
    private OAuthService oAuthService;


    @GetMapping
    public ResponseEntity<?> home() {
        return ResponseEntity.ok().body("DIETAGRAM");
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestParam String id) throws SQLException {
        return ResponseEntity.ok().body(nodeService.verification(id));
    }

    @PostMapping("/test")
    public ResponseEntity<?> testEditMypage(@RequestHeader String token,
                                            @RequestBody UserEditDTO userEditDTO) throws SQLException{
        User userFromRepo = oAuthService.getUserByToken(token);
        Mono<User> user = nodeService.testEdit(userEditDTO);
        System.out.println(user.block());
        return ResponseEntity.ok().body(userFromRepo);

    }


    @PostMapping("/test/img")
    public ResponseEntity<?> testMLImage(@RequestParam MultipartFile image)throws IOException {


//        Map<String,String> dto = nodeService.nodeMLImage(image);
        UTF8StreamJsonParser dto = nodeService.nodeMLImage(image);
        System.out.println(dto);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("test/img")
    public ResponseEntity<?> testHome(){
        String testString = nodeService.testHome();
        return ResponseEntity.ok().body(testString);

    }







    //======================= test service ==================//
    @PostMapping("/test/user")
    public ResponseEntity<?> testEditMypage(@RequestBody UserEditDTO userEditDTO){
        User user=userService.getUserFromRepo(userEditDTO.getOriginName());
        userService.editMyPage(userEditDTO,user);
        return ResponseEntity.ok().body(user);
    }
}
