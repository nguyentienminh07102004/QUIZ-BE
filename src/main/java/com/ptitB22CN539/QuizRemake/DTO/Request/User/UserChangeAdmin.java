package com.ptitB22CN539.QuizRemake.DTO.Request.User;

import jakarta.validation.constraints.Email;
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
public class UserChangeAdmin {
    @Email
    private String email;
    @NotBlank(message = "CODE_INVALID")
    @NotNull(message = "CODE_INVALID")
    private String code;
}
