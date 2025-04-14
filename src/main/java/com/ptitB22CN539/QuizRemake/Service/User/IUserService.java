package com.ptitB22CN539.QuizRemake.Service.User;

import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserChangePasswordRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserLoginRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserRegisterRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserSearchRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserSocialLogin;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserUploadAvatarRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.JwtResponse;
import com.ptitB22CN539.QuizRemake.Entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserService {
    UserEntity save(UserRegisterRequest userRegisterRequest);
    JwtResponse login(UserLoginRequest loginRequest);
    void changeStatus(List<String> ids);
    UserEntity getUserById(String id);
    UserEntity getUserByEmail(String email);
    void logout(HttpServletRequest request);
    void changePassword(UserChangePasswordRequest userChangePassword);
    Page<UserEntity> getAllUsers(UserSearchRequest userSearchRequest);
    UserEntity getMyInfo();
    JwtResponse loginSocial(UserSocialLogin userSocialLogin);
    Long countALlUsers();
    UserEntity uploadAvatar(UserUploadAvatarRequest avatarRequest);
}