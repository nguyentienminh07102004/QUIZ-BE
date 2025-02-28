package com.ptitB22CN539.QuizRemake.Controller;

import com.ptitB22CN539.QuizRemake.DTO.APIResponse;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserLoginRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserRegisterRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserSocialLogin;
import com.ptitB22CN539.QuizRemake.DTO.Response.JwtResponse;
import com.ptitB22CN539.QuizRemake.DTO.Response.UserResponse;
import com.ptitB22CN539.QuizRemake.Domains.JwtEntity;
import com.ptitB22CN539.QuizRemake.Domains.UserEntity;
import com.ptitB22CN539.QuizRemake.Mapper.JwtMapper;
import com.ptitB22CN539.QuizRemake.Mapper.UserMapper;
import com.ptitB22CN539.QuizRemake.Service.User.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/${api}/users")
public class UserController {
    private final IUserService userService;
    private final UserMapper userMapper;
    private final JwtMapper jwtMapper;

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
        JwtEntity jwt = userService.login(userLoginRequest);
        JwtResponse jwtResponse = jwtMapper.entityToResponse(jwt);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message("SUCCESS")
                .data(jwtResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/login/google")
    public ResponseEntity<APIResponse> loginGoogle(@Valid @RequestBody UserSocialLogin userSocialLogin) {
        JwtEntity jwt = userService.loginSocial(userSocialLogin);
        JwtResponse jwtResponse = jwtMapper.entityToResponse(jwt);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message("SUCCESS")
                .data(jwtResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
