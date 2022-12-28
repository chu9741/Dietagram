package com.example.Dietagram.dto;

import com.example.Dietagram.domain.Feed;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@Builder
public class CalorieByDateDTO {
    public Double calorieSum;
    public LocalDateTime date;

    public static CalorieByDateDTO createDTO(Feed feed){
        return CalorieByDateDTO.builder().calorieSum(Double.parseDouble(feed.getFeedFeedImage().getCalorie_kcal()))
                .date(feed.createdDate).build();
    }

}
