package com.ptitB22CN539.QuizRemake.Mapper;

import com.ptitB22CN539.QuizRemake.DTO.Request.Answer.AnswerRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.AnswerResponse;
import com.ptitB22CN539.QuizRemake.Model.Entity.AnswerEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.AnswerOfQuestionTestEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.QuestionTestEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerMapper {
    private final ModelMapper modelMapper;

    public AnswerEntity requestToEntity(AnswerRequest answerRequest) {
        return this.modelMapper.map(answerRequest, AnswerEntity.class);
    }
    public AnswerResponse entityToResponse(AnswerOfQuestionTestEntity answerEntity) {
        return this.modelMapper.map(answerEntity, AnswerResponse.class);
    }

    public AnswerResponse entityToResponse(AnswerEntity answerEntity) {
        return this.modelMapper.map(answerEntity, AnswerResponse.class);
    }

    public AnswerOfQuestionTestEntity answerToAnswerOfQuestionTest(AnswerEntity answerEntity, QuestionTestEntity questionTestEntity) {
        AnswerOfQuestionTestEntity answer = this.modelMapper.map(answerEntity, AnswerOfQuestionTestEntity.class);
        answer.setId(null);
        answer.setQuestion(questionTestEntity);
        return answer;
    }
}
