package com.ptitB22CN539.QuizRemake.DTO.Request.TestResult;

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
public class AnswerSelectedRequest {
    private String questionId;
    private List<String> answerIds;
}
