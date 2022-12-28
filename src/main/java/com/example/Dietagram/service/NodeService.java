package com.example.Dietagram.service;

import com.example.Dietagram.domain.User;
import com.example.Dietagram.dto.NutritionDTO;
import com.example.Dietagram.dto.UserEditDTO;
import com.fasterxml.jackson.core.json.UTF8StreamJsonParser;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.SQLException;

@Service
public class NodeService {

    private WebClient webClient;

    @PostConstruct
    public void initWebClient() {
//        webClient = WebClient.create("https://api.withrun.click");
//        webClient = WebClient.create("http://localhost:8080");
        webClient = WebClient.create("http://118.67.135.208:3000");
    }

    public String verification(String studentId) throws SQLException {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("studentId", studentId);

        return webClient.get()
                .uri("/")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .body(BodyInserters.fromFormData(formData))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
//                .block();
    }


    public Mono<User> testEdit(UserEditDTO userEditDTO) throws SQLException {
//        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//        formData.add("studentId", studentId);
        return webClient.post()
                .uri("/test/user")

//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromValue(userEditDTO))
//                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(User.class);
//                .block();
    }

    public UTF8StreamJsonParser nodeMLImage(MultipartFile imagefile) throws IOException {

        MultiValueMap<String, Object> image = new LinkedMultiValueMap<String, Object>();
        image.add("image", new ByteArrayResource(imagefile.getBytes()));
//
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("image", imagefile.getResource());
//        System.out.println(fileResource);
//        System.out.println(parameters);

        //noinspection unchecked
        return webClient.post().uri("/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
//                .bodyValue(image)
                .retrieve()
//                .bodyToMono(NutritionDTO.class)

                .bodyToMono(UTF8StreamJsonParser.class)
                .block();
    }

    public String testHome(){

        return webClient.get().uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(String.class)
                .block();
    }


}
