package com.ptitB22CN539.QuizRemake.DTO.Request.TestResult;

import jakarta.validation.constraints.NotBlank;
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
public class AnswerSelectedRequest {
    @NotBlank
    private String testResultId;
    @NotBlank
    private String questionId;
    @NotBlank
    private String answerId;
}
