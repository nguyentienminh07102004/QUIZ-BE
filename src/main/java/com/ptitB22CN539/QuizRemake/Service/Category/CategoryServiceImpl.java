package com.ptitB22CN539.QuizRemake.Service.Category;

import com.ptitB22CN539.QuizRemake.DTO.Request.Category.CategoryRequest;
import com.ptitB22CN539.QuizRemake.Domains.CategoryEntity;
import com.ptitB22CN539.QuizRemake.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.Mapper.CategoryMapper;
import com.ptitB22CN539.QuizRemake.Repository.ICategoryRepository;
import com.ptitB22CN539.QuizRemake.Utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {
    private final CategoryMapper categoryMapper;
    private final ICategoryRepository categoryRepository;
    @Override
    public CategoryEntity saveCategory(CategoryRequest category) {
        CategoryEntity categoryEntity = categoryMapper.requestToEntity(category);
        return categoryRepository.save(categoryEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryEntity findByCode(String code) {
        return categoryRepository.findById(code)
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.CATEGORY_NOT_FOUND));
    }

    @Override
    public Page<CategoryEntity> findAll(Integer page, Integer limit) {
        return categoryRepository.findAll(PaginationUtils.getPageable(page, limit));
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAllCategory() {
        return categoryRepository.count();
    }
}
