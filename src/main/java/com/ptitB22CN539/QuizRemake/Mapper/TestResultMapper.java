package com.ptitB22CN539.QuizRemake.Mapper;

import com.ptitB22CN539.QuizRemake.DTO.Response.AnswerSelectedResponse;
import com.ptitB22CN539.QuizRemake.DTO.Response.TestResultResponse;
import com.ptitB22CN539.QuizRemake.Domains.AnswerSelectedEntity;
import com.ptitB22CN539.QuizRemake.Domains.QuestionEntity;
import com.ptitB22CN539.QuizRemake.Domains.TestResultEntity;
import com.ptitB22CN539.QuizRemake.Repository.IAnswerSelectedRepository;
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
    private final IAnswerSelectedRepository answerSelectedRepository;

    public TestResultResponse entityToResponse(TestResultEntity testResultEntity) {
        TestResultResponse testResultResponse = modelMapper.map(testResultEntity, TestResultResponse.class);
        testResultResponse.setUser(userMapper.entityToResponse(testResultEntity.getUser()));
        testResultResponse.setTest(testMapper.entityToResponse(testResultEntity.getTest()));
        List<QuestionEntity> listQuestion = testResultEntity.getTest().getQuestions();
        List<AnswerSelectedResponse> listAnswerSelectedResponse = new ArrayList<>();
        for (QuestionEntity question : listQuestion) {
            AnswerSelectedResponse answerSelectedResponse = new AnswerSelectedResponse();
            List<AnswerSelectedEntity> answerSelectedEntities = answerSelectedRepository.findAllByTestResult_IdAndQuestion_Id(testResultEntity.getId(), question.getId());
            if (!answerSelectedEntities.isEmpty()) {
                answerSelectedResponse.setStatus(answerSelectedEntities.get(0).getStatus());
                answerSelectedResponse.setQuestionId(question.getId());
                answerSelectedResponse.setAnswerIds(answerSelectedEntities.stream().map(AnswerSelectedEntity::getId).toList());
                listAnswerSelectedResponse.add(answerSelectedResponse);
            }
        }
        testResultResponse.setAnswerSelected(listAnswerSelectedResponse);
        return testResultResponse;
    }
}
