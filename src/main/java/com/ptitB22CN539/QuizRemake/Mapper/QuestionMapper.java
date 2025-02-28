package com.ptitB22CN539.QuizRemake.Mapper;

import com.ptitB22CN539.QuizRemake.DTO.Request.Answer.AnswerRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Question.QuestionRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.AnswerResponse;
import com.ptitB22CN539.QuizRemake.DTO.Response.QuestionResponse;
import com.ptitB22CN539.QuizRemake.Domains.AnswerEntity;
import com.ptitB22CN539.QuizRemake.Domains.CategoryEntity;
import com.ptitB22CN539.QuizRemake.Domains.QuestionEntity;
import com.ptitB22CN539.QuizRemake.Service.Category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionMapper {
    private final ModelMapper modelMapper;
    private final AnswerMapper answerMapper;
    private final CategoryMapper categoryMapper;
    private final ICategoryService categoryService;

    public QuestionEntity requestToEntity(QuestionRequest question) {
        QuestionEntity questionEntity = modelMapper.map(question, QuestionEntity.class);
        List<AnswerEntity> listAnswer = new ArrayList<>();
        for (AnswerRequest answerRequest : question.getAnswers()) {
            AnswerEntity answerEntity = answerMapper.requestToEntity(answerRequest);
            answerEntity.setQuestion(questionEntity);
            listAnswer.add(answerEntity);
        }
        questionEntity.setAnswers(listAnswer);
        CategoryEntity categoryEntity = categoryService.findByCode(question.getCategoryCode());
        questionEntity.setCategory(categoryEntity);
        return questionEntity;
    }

    public QuestionResponse entityToResponse(QuestionEntity questionEntity) {
        QuestionResponse questionResponse = modelMapper.map(questionEntity, QuestionResponse.class);
        List<AnswerResponse> answerResponses = questionEntity.getAnswers().stream()
                .map(answerMapper::entityToResponse)
                .toList();
        questionResponse.setAnswers(answerResponses);
        questionResponse.setCategory(categoryMapper.entityToResponse(questionEntity.getCategory()));
        return questionResponse;
    }
}
