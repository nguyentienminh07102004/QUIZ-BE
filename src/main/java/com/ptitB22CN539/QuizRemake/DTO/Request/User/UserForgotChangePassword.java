package com.ptitB22CN539.QuizRemake.DTO.Request.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class UserForgotChangePassword {
    @NotNull(message = "TOKEN_INVALID")
    @NotBlank(message = "TOKEN_INVALID")
    private String code;
    @Size(min = 8, message = "PASSWORD_LENGTH_NOT_CORRECT")
    private String newPassword;
    @Size(min = 8, message = "PASSWORD_LENGTH_NOT_CORRECT")
    private String confirmPassword;
}
