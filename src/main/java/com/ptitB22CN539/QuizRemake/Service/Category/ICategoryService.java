package com.ptitB22CN539.QuizRemake.Service.Category;

import com.ptitB22CN539.QuizRemake.DTO.Request.Category.CategoryRequest;
import com.ptitB22CN539.QuizRemake.Model.Entity.CategoryEntity;
import org.springframework.data.domain.Page;

public interface ICategoryService {
    CategoryEntity saveCategory(CategoryRequest category);
    CategoryEntity findByCode(String code);
    Page<CategoryEntity> findAll(Integer page, Integer limit);
    Long countAllCategory();
}
