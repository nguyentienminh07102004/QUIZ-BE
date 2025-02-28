package com.ptitB22CN539.QuizRemake.Mapper;

import com.ptitB22CN539.QuizRemake.DTO.Request.Answer.AnswerRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.AnswerResponse;
import com.ptitB22CN539.QuizRemake.Domains.AnswerEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerMapper {
    private final ModelMapper modelMapper;

    public AnswerEntity requestToEntity(AnswerRequest answerRequest) {
        return modelMapper.map(answerRequest, AnswerEntity.class);
    }
    public AnswerResponse entityToResponse(AnswerEntity answerEntity) {
        return modelMapper.map(answerEntity, AnswerResponse.class);
    }

}
