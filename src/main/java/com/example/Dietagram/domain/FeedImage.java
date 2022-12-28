package com.example.Dietagram.domain;


import com.example.Dietagram.dto.NutritionDTO;
import com.example.Dietagram.dto.ResponseFeedImageDTO;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class FeedImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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



    @OneToOne(mappedBy = "feedFeedImage")
    private Feed feedImageFeed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User feedImageUser;

    //===========================================//

    public static FeedImage FromNutritionDTO(NutritionDTO nutritionDTO){
        return FeedImage.builder().name(nutritionDTO.getName())
                .weight_g(nutritionDTO.getWeight_g())
                .calorie_kcal(nutritionDTO.getCalorie_kcal())
                .Carbohydrate_g(nutritionDTO.getCarbohydrate_g())
                .sugars_g(nutritionDTO.getSugars_g())
                .fat_g(nutritionDTO.getFat_g())
                .protein_g(nutritionDTO.getProtein_g())
                .calcium_mg(nutritionDTO.getCalcium_mg())
                .phosphorus_g(nutritionDTO.getPhosphorus_g())
                .sodium_mg(nutritionDTO.getSodium_mg())
                .potassium_mg(nutritionDTO.getPotassium_mg())
                .magnesium_mg(nutritionDTO.getMagnesium_mg())
                .iron_mg(nutritionDTO.getIron_mg())
                .zinc_mg(nutritionDTO.getZinc_mg())
                .cholesterol_mg(nutritionDTO.getCholesterol_mg())
                .transFat_g(nutritionDTO.getTransFat_g())
                .build();
    }

    public ResponseFeedImageDTO toResponseDto(){
        return ResponseFeedImageDTO.builder().id(id).imageName(imageName)
                .imageUrl(imageUrl).name(name).weight_g(weight_g)
                .calorie_kcal(calorie_kcal).Carbohydrate_g(Carbohydrate_g)
                .sugars_g(sugars_g).fat_g(fat_g).protein_g(protein_g)
                .calcium_mg(calcium_mg).phosphorus_g(phosphorus_g)
                .sodium_mg(sodium_mg).potassium_mg(potassium_mg)
                .iron_mg(iron_mg).zinc_mg(zinc_mg).cholesterol_mg(cholesterol_mg)
                .transFat_g(transFat_g)
                .build();
    }
}
