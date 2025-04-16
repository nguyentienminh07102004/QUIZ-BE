package com.ptitB22CN539.QuizRemake.Mapper;

import com.ptitB22CN539.QuizRemake.Common.Bean.ConstantConfiguration;
import com.ptitB22CN539.QuizRemake.Common.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Common.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserRegisterRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.RoleResponse;
import com.ptitB22CN539.QuizRemake.DTO.Response.UserResponse;
import com.ptitB22CN539.QuizRemake.Model.Entity.RoleEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.UserEntity;
import com.ptitB22CN539.QuizRemake.Service.Role.IRoleService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final IRoleService roleService;
    private final RoleMapper roleMapper;

    public UserEntity registerToEntity(UserRegisterRequest userRegisterRequest) {
        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getConfirmPassword())) {
            throw new DataInvalidException(ExceptionVariable.PASSWORD_CONFIRM_PASSWORD_NOT_MATCH);
        }
        UserEntity userEntity = modelMapper.map(userRegisterRequest, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        if (StringUtils.isNotBlank(userRegisterRequest.getRoleCode())) {
            RoleEntity role = roleService.findByCode(userRegisterRequest.getRoleCode());
            userEntity.setRole(role);
        } else {
            RoleEntity role = roleService.findByCode(ConstantConfiguration.ROLE_DEFAULT);
            userEntity.setRole(role);
        }
        return userEntity;
    }

    public UserResponse entityToResponse(UserEntity userEntity) {
        UserResponse userResponse = modelMapper.map(userEntity, UserResponse.class);
        RoleResponse roleResponse = roleMapper.entityToResponse(userEntity.getRole());
        userResponse.setRole(roleResponse);
        if (userEntity.getAvatar() != null && !userEntity.getAvatar().contains("https")) {
            userResponse.setAvatar("https://drive.google.com/thumbnail?id=%s".formatted(userEntity.getAvatar()));
        }
        return userResponse;
    }
}
