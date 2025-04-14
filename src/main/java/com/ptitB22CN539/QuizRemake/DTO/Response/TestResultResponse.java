package com.ptitB22CN539.QuizRemake.DTO.Response;

import com.ptitB22CN539.QuizRemake.Common.BeanApp.TestResultStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResultResponse {
    private String id;
    private TestResponse test;
    private UserResponse user;
    private Integer score;
    private Date startedDate;
    private Date finishDate;
    private List<AnswerSelectedResponse> answerSelected;
    private TestResultStatus status;
}
