package com.ptitB22CN539.QuizRemake.Mapper;

import com.ptitB22CN539.QuizRemake.DTO.Response.JwtResponse;
import com.ptitB22CN539.QuizRemake.Domains.JwtEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtMapper {
    private final ModelMapper modelMapper;

    public JwtResponse entityToResponse(JwtEntity jwtEntity) {
        return modelMapper.map(jwtEntity, JwtResponse.class);
    }
}
