package com.ptitB22CN539.QuizRemake.DTO.Request.User;

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
public class UserSearchRequest {
    private String fullName;
    private String email;
    private Integer page;
    private Integer limit;
}
