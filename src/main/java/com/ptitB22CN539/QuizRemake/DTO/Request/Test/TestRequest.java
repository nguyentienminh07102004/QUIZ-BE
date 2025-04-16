package com.ptitB22CN539.QuizRemake.DTO.Request.Test;

import com.ptitB22CN539.QuizRemake.Common.Enum.Difficulty;
import com.ptitB22CN539.QuizRemake.Common.Validator.QuestionExistsValid;
import com.ptitB22CN539.QuizRemake.Common.Validator.QuestionHasSameCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class TestRequest {
    private String id;
    @NotNull(message = "TEST_TITLE_NOT_NULL_OR_EMPTY")
    @NotBlank(message = "TEST_TITLE_NOT_NULL_OR_EMPTY")
    private String title;
    private String description;
    private String categoryCode;
    private Difficulty difficulty;
    @NotNull(message = "TEST_MUST_HAS_LEAST_QUESTION")
    @NotEmpty(message = "TEST_MUST_HAS_LEAST_QUESTION")
    @QuestionExistsValid
    @QuestionHasSameCategory
    private List<String> questionIds;
}
