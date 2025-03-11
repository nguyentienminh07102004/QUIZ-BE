package com.ptitB22CN539.QuizRemake.DTO.Request.User;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUploadAvatarRequest {
    @NotNull(message = "AVATAR_FILE_EMPTY")
    private MultipartFile avatar;
    private String email;
}
