package com.ptitB22CN539.QuizRemake.DTO.Request.Category;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CategoryRequest {
    @NotBlank
    @NotNull
    private String name;
    @NotBlank
    @NotNull
    private String code;
    private String description;
}
