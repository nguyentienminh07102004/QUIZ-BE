package com.ptitB22CN539.QuizRemake.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRatingResponse {
    private Double rating;
    private Integer numberOfRatings;
}
