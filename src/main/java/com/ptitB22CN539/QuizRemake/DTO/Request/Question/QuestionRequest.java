package com.ptitB22CN539.QuizRemake.DTO.Request.Question;

import com.ptitB22CN539.QuizRemake.DTO.Request.Answer.AnswerRequest;
import com.ptitB22CN539.QuizRemake.Validator.QuestionHasLeastCorrectAnswer;
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
    private String title;
    private String shortDescription;
    private String content;
    private String categoryCode;
    @QuestionHasLeastCorrectAnswer
    private List<AnswerRequest> answers;
}
