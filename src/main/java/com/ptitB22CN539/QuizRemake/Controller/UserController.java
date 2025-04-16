package com.ptitB22CN539.QuizRemake.Controller;

import com.ptitB22CN539.QuizRemake.DTO.APIResponse;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserChangePasswordRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserLoginRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserRegisterRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserSearchRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserSocialLogin;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserUploadAvatarRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.JwtResponse;
import com.ptitB22CN539.QuizRemake.DTO.Response.UserResponse;
import com.ptitB22CN539.QuizRemake.Model.Entity.UserEntity;
import com.ptitB22CN539.QuizRemake.Mapper.UserMapper;
import com.ptitB22CN539.QuizRemake.Service.User.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {
    private final IUserService userService;
    private final UserMapper userMapper;

    @PostMapping(value = "/register")
    public ResponseEntity<APIResponse> registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        UserEntity userEntity = userService.save(userRegisterRequest);
        UserResponse userResponse = userMapper.entityToResponse(userEntity);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("SUCCESS")
                .data(userResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<APIResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        JwtResponse jwt = userService.login(userLoginRequest);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message("SUCCESS")
                .data(jwt)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/login/google")
    public ResponseEntity<APIResponse> loginGoogle(@Valid @RequestBody UserSocialLogin userSocialLogin) {
        JwtResponse jwt = userService.loginSocial(userSocialLogin);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message("SUCCESS")
                .data(jwt)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/")
    public ResponseEntity<APIResponse> finAllUsers(@ModelAttribute UserSearchRequest userSearchRequest) {
        Page<UserEntity> entityPage = userService.getAllUsers(userSearchRequest);
        Page<UserResponse> responsePage = entityPage.map(userMapper::entityToResponse);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message("SUCCESS")
                .data(new PagedModel<>(responsePage))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/count")
    public ResponseEntity<APIResponse> countAllUsers() {
        Long countAllUsers = userService.countALlUsers();
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message("SUCCESS")
                .data(countAllUsers)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<APIResponse> logout(HttpServletRequest request) {
        userService.logout(request);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message("SUCCESS")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/change-status/{ids}")
    public ResponseEntity<APIResponse> changeUserStatus(@PathVariable List<String> ids) {
        userService.changeStatus(ids);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message("SUCCESS")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<APIResponse> changePassword(@Valid @RequestBody UserChangePasswordRequest request) {
        userService.changePassword(request);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message("SUCCESS")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/upload-avatar")
    public ResponseEntity<APIResponse> uploadAvatar(@Valid @ModelAttribute UserUploadAvatarRequest avatar) {
        UserEntity userEntity = userService.uploadAvatar(avatar);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message("SUCCESS")
                .data(userMapper.entityToResponse(userEntity))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
