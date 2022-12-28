package com.example.Dietagram.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FoodImageDTO {

    private String url;
    private String filename;
}
