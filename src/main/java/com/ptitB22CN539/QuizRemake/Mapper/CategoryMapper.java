package com.ptitB22CN539.QuizRemake.Mapper;

import com.ptitB22CN539.QuizRemake.DTO.Request.Category.CategoryRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.CategoryResponse;
import com.ptitB22CN539.QuizRemake.Domains.CategoryEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    private final ModelMapper modelMapper;

    public CategoryEntity requestToEntity(CategoryRequest categoryRequest) {
        return modelMapper.map(categoryRequest, CategoryEntity.class);
    }

    public CategoryResponse entityToResponse(CategoryEntity categoryEntity) {
        return modelMapper.map(categoryEntity, CategoryResponse.class);
    }
}
