package com.ptitB22CN539.QuizRemake.Service.Category;

import com.ptitB22CN539.QuizRemake.DTO.Request.Category.CategoryRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Category.CategoryUpdate;
import com.ptitB22CN539.QuizRemake.Model.Entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICategoryService {
    CategoryEntity saveCategory(CategoryRequest category);
    CategoryEntity updateCategory(CategoryUpdate category);
    CategoryEntity findByCode(String code);
    Page<CategoryEntity> findAll(Integer page, Integer limit);
    Long countAllCategory();
    List<CategoryEntity> findAll();
    List<CategoryEntity> saveFromExcel(MultipartFile file);
}
