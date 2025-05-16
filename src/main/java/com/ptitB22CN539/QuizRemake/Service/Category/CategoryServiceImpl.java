package com.ptitB22CN539.QuizRemake.Service.Category;

import com.ptitB22CN539.QuizRemake.Common.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Common.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.DTO.Request.Category.CategoryRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Category.CategoryUpdate;
import com.ptitB22CN539.QuizRemake.DTO.Response.CategoryResponse;
import com.ptitB22CN539.QuizRemake.JpaRepository.ICategoryRepository;
import com.ptitB22CN539.QuizRemake.Mapper.CategoryMapper;
import com.ptitB22CN539.QuizRemake.Model.Entity.CategoryEntity;
import com.ptitB22CN539.QuizRemake.Utils.PaginationUtils;
import com.ptitB22CN539.QuizRemake.Utils.ReadExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {
    private final CategoryMapper categoryMapper;
    private final ICategoryRepository categoryRepository;
    private final ReadExcelUtil readExcelUtil;

    @Override
    public CategoryEntity saveCategory(CategoryRequest category) {
        CategoryEntity categoryEntity = this.categoryMapper.requestToEntity(category);
        return this.categoryRepository.save(categoryEntity);
    }

    @Override
    public CategoryEntity updateCategory(CategoryUpdate category) {
        CategoryEntity categoryEntity = this.categoryMapper.toEntity(category);
        return this.categoryRepository.save(categoryEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryEntity findByCode(String code) {
        return this.categoryRepository.findById(code)
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.CATEGORY_NOT_FOUND));
    }

    @Override
    public Page<CategoryEntity> findAll(Integer page, Integer limit) {
        return categoryRepository.findAll(PaginationUtils.getPageable(page, limit));
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAllCategory() {
        return this.categoryRepository.count();
    }

    @Override
    public List<CategoryEntity> findAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    @Transactional
    public List<CategoryEntity> saveFromExcel(MultipartFile file) {
        try {
            List<CategoryRequest> categoryRequests = this.readExcelUtil.readExcel(file, 0, CategoryRequest.class);
            List<CategoryEntity> result = new ArrayList<>();
            for (CategoryRequest categoryRequest : categoryRequests) {
                CategoryEntity categoryEntity = this.saveCategory(categoryRequest);
                result.add(categoryEntity);
            }
            return result;
        } catch (Exception e) {
            throw new DataInvalidException(ExceptionVariable.CATEGORY_NOT_FOUND);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Window<CategoryResponse> findAllCategoryWindow(Integer page, Integer limit) {
        Pageable pageable = PaginationUtils.getPageable(page, limit);
        ScrollPosition scrollPosition = ScrollPosition.offset(pageable.getOffset());
        Window<CategoryEntity> categories = this.categoryRepository.findTop10ByOrderByCodeAsc(scrollPosition);
        return categories.map(this.categoryMapper::entityToResponse);
    }
}
