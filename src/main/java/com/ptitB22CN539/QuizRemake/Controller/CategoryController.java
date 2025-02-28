package com.ptitB22CN539.QuizRemake.Controller;

import com.ptitB22CN539.QuizRemake.DTO.APIResponse;
import com.ptitB22CN539.QuizRemake.DTO.Request.Category.CategoryRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.CategoryResponse;
import com.ptitB22CN539.QuizRemake.Domains.CategoryEntity;
import com.ptitB22CN539.QuizRemake.Mapper.CategoryMapper;
import com.ptitB22CN539.QuizRemake.Service.Category.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/${api}/categories")
public class CategoryController {
    private final ICategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping(value = "/")
    public ResponseEntity<APIResponse> saveCategory(@Valid @RequestBody CategoryRequest category) {
        CategoryEntity categoryEntity = categoryService.saveCategory(category);
        CategoryResponse categoryResponse = categoryMapper.entityToResponse(categoryEntity);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("SUCCESS")
                .data(categoryResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/")
    public ResponseEntity<APIResponse> findAllCategories(@RequestParam(required = false) Integer page,
                                                         @RequestParam(required = false) Integer limit) {
        Page<CategoryEntity> entityPage = categoryService.findAll(page, limit);
        PagedModel<CategoryResponse> responsePagedModel = new PagedModel<>(entityPage.map(categoryMapper::entityToResponse));
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("SUCCESS")
                .data(responsePagedModel)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
