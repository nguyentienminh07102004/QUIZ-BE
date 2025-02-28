package com.ptitB22CN539.QuizRemake.DTO.Request.TestResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResultStart {
    private String testId;
    private Date startDate;
}
