package com.ptitB22CN539.QuizRemake.Mapper;

import com.ptitB22CN539.QuizRemake.Common.BeanApp.TestStatus;
import com.ptitB22CN539.QuizRemake.DTO.Request.Test.TestRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.QuestionResponse;
import com.ptitB22CN539.QuizRemake.DTO.Response.TestRatingResponse;
import com.ptitB22CN539.QuizRemake.DTO.Response.TestResponse;
import com.ptitB22CN539.QuizRemake.Entity.QuestionEntity;
import com.ptitB22CN539.QuizRemake.Entity.TestEntity;
import com.ptitB22CN539.QuizRemake.Entity.TestRatingEntity;
import com.ptitB22CN539.QuizRemake.Service.Category.ICategoryService;
import com.ptitB22CN539.QuizRemake.Service.Question.IQuestionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestMapper {
    private final ModelMapper modelMapper;
    private final QuestionMapper questionMapper;
    private final CategoryMapper categoryMapper;
    private final ICategoryService categoryService;
    private final IQuestionService questionService;

    public TestEntity requestToEntity(TestRequest testRequest) {
        TestEntity testEntity = modelMapper.map(testRequest, TestEntity.class);
        List<QuestionEntity> listQuestion = new ArrayList<>();
        for (String questionId : testRequest.getQuestionIds()) {
            QuestionEntity questionEntity = questionService.findById(questionId);
            listQuestion.add(questionEntity);
        }
        testEntity.setCategory(categoryService.findByCode(testRequest.getCategoryCode()));
        testEntity.setQuestions(listQuestion);
        testEntity.setStatus(TestStatus.ACTIVE);
        return testEntity;
    }

    public TestResponse entityToResponse(TestEntity testEntity) {
        TestResponse testResponse = modelMapper.map(testEntity, TestResponse.class);
        List<QuestionResponse> listQuestion = testEntity.getQuestions().stream()
                .map(questionMapper::entityToResponse)
                .toList();
        testResponse.setCategory(categoryMapper.entityToResponse(testEntity.getCategory()));
        testResponse.setQuestions(listQuestion);
        // set test rating
        if (testEntity.getTestRatings() != null && !testEntity.getTestRatings().isEmpty()) {
            Double totalRating = testEntity.getTestRatings().stream()
                    .map(TestRatingEntity::getRating)
                    .reduce(0.0D, Double::sum) / testEntity.getTestRatings().size();
            TestRatingResponse testRatingResponse = new TestRatingResponse();
            DecimalFormat format = new DecimalFormat("#.##");
            testRatingResponse.setRating(Double.valueOf(format.format(totalRating)));
            testRatingResponse.setNumberOfRatings(testEntity.getTestRatings().size());
            testResponse.setTestRating(testRatingResponse);
        }
        return testResponse;
    }
}
