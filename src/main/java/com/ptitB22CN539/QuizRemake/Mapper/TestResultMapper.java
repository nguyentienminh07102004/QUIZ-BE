package com.ptitB22CN539.QuizRemake.Mapper;

import com.ptitB22CN539.QuizRemake.DTO.Response.AnswerSelectedResponse;
import com.ptitB22CN539.QuizRemake.DTO.Response.TestResultResponse;
import com.ptitB22CN539.QuizRemake.Model.Entity.AnswerEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.AnswerQuestionResultEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.QuestionResultEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.TestResultEntity;
import com.ptitB22CN539.QuizRemake.Repository.IQuestionResultRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestResultMapper {
    private final ModelMapper modelMapper;
    private final TestMapper testMapper;
    private final UserMapper userMapper;
    private final IQuestionResultRepository questionResultRepository;

    public TestResultResponse entityToResponse(TestResultEntity testResultEntity) {
        TestResultResponse testResultResponse = modelMapper.map(testResultEntity, TestResultResponse.class);
        testResultResponse.setUser(userMapper.entityToResponse(testResultEntity.getUser()));
        testResultResponse.setTest(testMapper.entityToResponse(testResultEntity.getTest()));
        List<QuestionResultEntity> questionResults = questionResultRepository.findAllByTestResult_Id(testResultEntity.getId());
        List<AnswerSelectedResponse> listAnswerSelectedResponse = new ArrayList<>();
        for (QuestionResultEntity questionResult : questionResults) {
            AnswerSelectedResponse answerSelectedResponse = new AnswerSelectedResponse();
            answerSelectedResponse.setStatus(questionResult.getStatus());
            answerSelectedResponse.setQuestionId(questionResult.getQuestion().getId());
            answerSelectedResponse.setAnswerIds(questionResult.getAnswers().stream().map(AnswerQuestionResultEntity::getAnswer).map(AnswerEntity::getId).toList());
            listAnswerSelectedResponse.add(answerSelectedResponse);
        }
        testResultResponse.setAnswerSelected(listAnswerSelectedResponse);
        return testResultResponse;
    }
}
