package com.ptitB22CN539.QuizRemake.DTO.Request.Question;

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
public class QuestionSearchRequest {
    private String id;
    private String title;
    private String categoryCode;
    private Integer page;
    private Integer limit;
}
