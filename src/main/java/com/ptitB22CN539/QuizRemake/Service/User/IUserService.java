package com.ptitB22CN539.QuizRemake.Service.User;

import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserChangePasswordRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserLoginRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserRegisterRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserSocialLogin;
import com.ptitB22CN539.QuizRemake.Domains.JwtEntity;
import com.ptitB22CN539.QuizRemake.Domains.UserEntity;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface IUserService {
    UserEntity save(UserRegisterRequest userRegisterRequest);
    JwtEntity login(UserLoginRequest loginRequest);
    void changeStatus(List<String> ids);
    UserEntity getUserById(String id);
    UserEntity getUserByEmail(String email);
    void logout(HttpServletRequest request);
    void changePassword(UserChangePasswordRequest userChangePassword);
    List<UserEntity> getAllUsers();
    UserEntity getMyInfo();
    JwtEntity loginSocial(UserSocialLogin userSocialLogin);
}