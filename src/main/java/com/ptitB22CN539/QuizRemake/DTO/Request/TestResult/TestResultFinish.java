package com.ptitB22CN539.QuizRemake.DTO.Request.TestResult;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResultFinish {
    @NotNull
    @NotBlank
    private String id;
    private Map<String, List<String>> answerSelecteds;
    @NotNull
    private Date finishDate;
}
