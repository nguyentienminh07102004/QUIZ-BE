package com.ptitB22CN539.QuizRemake.DTO.Request.Answer;

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
public class AnswerRequest {
    private String id;
    private String content;
    private Boolean isCorrect;
}
