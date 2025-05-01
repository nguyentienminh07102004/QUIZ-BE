package com.ptitB22CN539.QuizRemake.Mapper;

import com.ptitB22CN539.QuizRemake.DTO.Request.Answer.AnswerRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Question.QuestionRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.AnswerResponse;
import com.ptitB22CN539.QuizRemake.DTO.Response.QuestionResponse;
import com.ptitB22CN539.QuizRemake.Model.Entity.AnswerEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.AnswerOfQuestionTestEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.QuestionEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.QuestionTestEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.TestEntity;
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
        questionEntity.setCategory(categoryService.findByCode(question.getCategoryCode()));
        return questionEntity;
    }

    public QuestionResponse entityToResponse(QuestionTestEntity questionTestEntity) {
        QuestionResponse questionResponse = this.modelMapper.map(questionTestEntity, QuestionResponse.class);
        List<AnswerResponse> answerResponses = questionTestEntity.getAnswers().stream()
                .map(answerMapper::entityToResponse)
                .toList();
        questionResponse.setAnswers(answerResponses);
        questionResponse.setCategory(this.categoryMapper.entityToResponse(questionTestEntity.getCategory()));
        return questionResponse;
    }

    public QuestionResponse entityToResponse(QuestionEntity questionEntity) {
        QuestionResponse questionResponse = this.modelMapper.map(questionEntity, QuestionResponse.class);
        List<AnswerResponse> answerResponses = questionEntity.getAnswers().stream()
                .map(answerMapper::entityToResponse)
                .toList();
        questionResponse.setAnswers(answerResponses);
        questionResponse.setCategory(this.categoryMapper.entityToResponse(questionEntity.getCategory()));
        return questionResponse;
    }

    public QuestionTestEntity questionToQuestionTest(QuestionEntity questionEntity, TestEntity test) {
        QuestionTestEntity questionTestEntity = this.modelMapper.map(questionEntity, QuestionTestEntity.class);
        List<AnswerOfQuestionTestEntity> answers = new ArrayList<>();
        for (AnswerEntity answerEntity : questionEntity.getAnswers()) {
            AnswerOfQuestionTestEntity answer = this.answerMapper.answerToAnswerOfQuestionTest(answerEntity, questionTestEntity);
            answers.add(answer);
        }
        questionTestEntity.setAnswers(answers);
        questionTestEntity.setId(null);
        questionTestEntity.setTest(test);
        return questionTestEntity;
    }
}
