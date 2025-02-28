package com.ptitB22CN539.QuizRemake.DTO.Response;

import java.util.List;

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
public class QuestionResponse {
    private String id;
    private String title;
    private String shortDescription;
    private String content;
    private CategoryResponse category;
    private List<AnswerResponse> answers;
}
