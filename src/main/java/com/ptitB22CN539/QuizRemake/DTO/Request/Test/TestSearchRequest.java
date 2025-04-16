package com.ptitB22CN539.QuizRemake.DTO.Request.Test;

import com.ptitB22CN539.QuizRemake.Common.Enum.Difficulty;
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
public class TestSearchRequest {
    private String id;
    private String title;
    private String category;
    private Difficulty difficulty;
    private Integer page;
    private Integer limit;
}
