package com.example.Dietagram.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFeedImageDTO {
    private long id;

    private String imageName;
    private String imageUrl;

    private String name;
    private String weight_g;
    private String calorie_kcal;
    private String Carbohydrate_g;
    private String sugars_g;
    private String fat_g;
    private String protein_g;
    private String calcium_mg;
    private String phosphorus_g;
    private String sodium_mg;
    private String potassium_mg;
    private String magnesium_mg;
    private String iron_mg;
    private String zinc_mg;
    private String cholesterol_mg;
    private String transFat_g;


}
