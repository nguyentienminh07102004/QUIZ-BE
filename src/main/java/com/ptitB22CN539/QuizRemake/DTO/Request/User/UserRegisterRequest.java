package com.ptitB22CN539.QuizRemake.DTO.Request.User;

import com.ptitB22CN539.QuizRemake.Common.BeanApp.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class UserRegisterRequest {
    private String fullName;
    @Email
    private String email;
    @NotBlank
    @Size(min = 8)
    private String password;
    @NotBlank
    @Size(min = 8)
    private String confirmPassword;
    private String roleCode;
    private UserStatus status;
}
