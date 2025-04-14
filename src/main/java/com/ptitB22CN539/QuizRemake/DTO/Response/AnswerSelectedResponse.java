package com.ptitB22CN539.QuizRemake.DTO.Response;

import com.ptitB22CN539.QuizRemake.Common.BeanApp.AnswerSelectedStatus;
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
public class AnswerSelectedResponse {
    private List<String> answerIds;
    private String questionId;
    private AnswerSelectedStatus status;
}
