package com.ptitB22CN539.QuizRemake.DTO.Response;

import com.ptitB22CN539.QuizRemake.Common.Enum.UserStatus;
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
public class UserResponse {
    private String id;
    private String email;
    private String fullName;
    private String avatar;
    private UserStatus status;
    private RoleResponse role;
}
