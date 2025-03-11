package com.ptitB22CN539.QuizRemake.Service.TestResult;

import com.ptitB22CN539.QuizRemake.BeanApp.AnswerSelectedStatus;
import com.ptitB22CN539.QuizRemake.BeanApp.TestResultStatus;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.AnswerSelectedRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultFinish;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultStart;
import com.ptitB22CN539.QuizRemake.Domains.AnswerEntity;
import com.ptitB22CN539.QuizRemake.Domains.AnswerSelectedEntity;
import com.ptitB22CN539.QuizRemake.Domains.QuestionEntity;
import com.ptitB22CN539.QuizRemake.Domains.TestEntity;
import com.ptitB22CN539.QuizRemake.Domains.TestResultEntity;
import com.ptitB22CN539.QuizRemake.Domains.UserEntity;
import com.ptitB22CN539.QuizRemake.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.Repository.IAnswerRepository;
import com.ptitB22CN539.QuizRemake.Repository.ITestResultRepository;
import com.ptitB22CN539.QuizRemake.Service.Test.ITestService;
import com.ptitB22CN539.QuizRemake.Service.User.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestResultServiceImpl implements ITestResultService {
    private final ITestResultRepository testResultRepository;
    private final IUserService userService;
    private final ITestService testService;
    private final IAnswerRepository answerRepository;

    @Override
    @Transactional
    public TestResultEntity startTestResult(TestResultStart testResultStart) {
        TestResultEntity testResultEntity = new TestResultEntity();
        testResultEntity.setStartedDate(testResultStart.getStartDate());
        TestEntity test = testService.findById(testResultStart.getTestId());
        testResultEntity.setTest(test);
        testResultEntity.setStatus(TestResultStatus.NOT_COMPLETE);
        UserEntity user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        testResultEntity.setUser(user);
        return testResultRepository.save(testResultEntity);
    }

    @Override
    @Transactional
    public TestResultEntity finishTest(TestResultFinish testResultFinish) {
        TestResultEntity testResultEntity = this.findById(testResultFinish.getId());
        TestEntity test = testResultEntity.getTest();
        List<QuestionEntity> listQuestion = test.getQuestions();
        List<AnswerSelectedRequest> listAnswerSelected = testResultFinish.getAnswerSelecteds();
        List<AnswerSelectedEntity> listAnswerSelectedEntity = new ArrayList<>();
        int score = 0;
        for (QuestionEntity questionEntity : listQuestion) {
            List<String> answerSelectedRequest = listAnswerSelected.stream()
                    .filter(question -> questionEntity.getId().equals(question.getQuestionId()))
                    .findFirst()
                    .map(AnswerSelectedRequest::getAnswerIds)
                    .orElse(null);
            if (answerSelectedRequest != null) {
                boolean isSuccess = true;
                List<AnswerSelectedEntity> listAnswerForQuestionCurrent = new ArrayList<>();
                for (String answerId : answerSelectedRequest) {
                    AnswerEntity answer = answerRepository.findById(answerId)
                            .orElseThrow(() -> new DataInvalidException(ExceptionVariable.ANSWER_NOT_FOUND));
                    if (!answer.getIsCorrect()) isSuccess = false;
                    AnswerSelectedEntity answerSelectedEntity = new AnswerSelectedEntity();
                    answerSelectedEntity.setTestResult(testResultEntity);
                    answerSelectedEntity.setQuestion(questionEntity);
                    answerSelectedEntity.setAnswer(answer);
                    listAnswerForQuestionCurrent.add(answerSelectedEntity);
                }
                if (isSuccess) {
                    score++;
                    listAnswerForQuestionCurrent.forEach(answer -> answer.setStatus(AnswerSelectedStatus.CORRECT));
                } else {
                    listAnswerForQuestionCurrent.forEach(answer -> answer.setStatus(AnswerSelectedStatus.INCORRECT));
                }
                listAnswerSelectedEntity.addAll(listAnswerForQuestionCurrent);
            } else {
                AnswerSelectedEntity answerSelectedEntity = new AnswerSelectedEntity();
                answerSelectedEntity.setTestResult(testResultEntity);
                answerSelectedEntity.setQuestion(questionEntity);
                answerSelectedEntity.setStatus(AnswerSelectedStatus.NOT_ANSWERED);
                listAnswerSelectedEntity.add(answerSelectedEntity);
            }
        }
        testResultEntity.setScore(score);
        testResultEntity.setFinishDate(testResultFinish.getFinishDate());
        testResultEntity.getAnswerSelecteds().addAll(listAnswerSelectedEntity);
        testResultEntity.setStatus(TestResultStatus.COMPLETE);
        return testResultRepository.save(testResultEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public TestResultEntity findById(String id) {
        return testResultRepository.findById(id)
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.TEST_RESULT_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAllTestResult() {
        return testResultRepository.count();
    }
}
