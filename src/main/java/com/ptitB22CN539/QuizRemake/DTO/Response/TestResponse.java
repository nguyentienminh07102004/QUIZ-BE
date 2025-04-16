package com.ptitB22CN539.QuizRemake.DTO.Response;

import com.ptitB22CN539.QuizRemake.Common.Enum.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResponse {
    private String id;
    private String title;
    private String description;
    private Difficulty difficulty;
    private CategoryResponse category;
    private List<QuestionResponse> questions;
    private TestRatingResponse testRating;
}
