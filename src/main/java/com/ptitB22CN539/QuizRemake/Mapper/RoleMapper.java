package com.ptitB22CN539.QuizRemake.Mapper;

import com.ptitB22CN539.QuizRemake.DTO.Response.RoleResponse;
import com.ptitB22CN539.QuizRemake.Entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleMapper {
    private final ModelMapper modelMapper;

    public RoleResponse entityToResponse(RoleEntity roleEntity) {
        return modelMapper.map(roleEntity, RoleResponse.class);
    }
}
