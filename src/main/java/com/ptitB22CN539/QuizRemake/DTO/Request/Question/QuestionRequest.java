package com.ptitB22CN539.QuizRemake.DTO.Request.Question;

import com.ptitB22CN539.QuizRemake.DTO.Request.Answer.AnswerRequest;
import com.ptitB22CN539.QuizRemake.Validator.QuestionHasLeastCorrectAnswer;
import jakarta.validation.constraints.NotBlank;
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
public class QuestionRequest {
    private String id;
    @NotBlank(message = "QUESTION_TITLE_NOT_NULL_EMPTY")
    private String title;
    private String shortDescription;
    @NotBlank(message = "QUESTION_CONTENT_NOT_NULL_EMPTY")
    private String content;
    @NotBlank(message = "QUESTION_CATEGORY_NOT_NULL_EMPTY")
    private String categoryCode;
    @QuestionHasLeastCorrectAnswer
    private List<AnswerRequest> answers;
}
