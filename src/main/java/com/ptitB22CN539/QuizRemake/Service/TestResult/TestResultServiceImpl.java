package com.ptitB22CN539.QuizRemake.Service.TestResult;

import com.ptitB22CN539.QuizRemake.BeanApp.AnswerSelectedStatus;
import com.ptitB22CN539.QuizRemake.BeanApp.TestResultStatus;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.AnswerSelectedRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultFinish;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultStart;
import com.ptitB22CN539.QuizRemake.DTO.Response.Chart.NumberOfPlayerParticipatingForTime;
import com.ptitB22CN539.QuizRemake.DTO.Response.Chart.NumberOfPlayerParticipatingTestResponse;
import com.ptitB22CN539.QuizRemake.Domains.AnswerEntity;
import com.ptitB22CN539.QuizRemake.Domains.AnswerQuestionResultEntity;
import com.ptitB22CN539.QuizRemake.Domains.QuestionResultEntity;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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
        testResultEntity.setStartedDate(testResultStart.getStartedDate());
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
        List<QuestionResultEntity> listQuestionResult = new ArrayList<>();
        int score = 0;
        for (QuestionEntity questionEntity : listQuestion) {
            List<String> answerSelectedRequest = listAnswerSelected.stream()
                    .filter(question -> questionEntity.getId().equals(question.getQuestionId()))
                    .findFirst()
                    .map(AnswerSelectedRequest::getAnswerIds)
                    .orElse(null);
            QuestionResultEntity questionResult = new QuestionResultEntity();
            questionResult.setQuestion(questionEntity);
            questionResult.setTestResult(testResultEntity);
            List<AnswerQuestionResultEntity> listAnswerQuestionResult = new ArrayList<>();
            if (answerSelectedRequest == null || answerSelectedRequest.isEmpty()) {
                questionResult.setAnswers(listAnswerQuestionResult);
                questionResult.setStatus(AnswerSelectedStatus.NOT_ANSWERED);
            } else {
                boolean isSuccess = true;
                List<AnswerQuestionResultEntity> listAnswerForQuestionCurrent = new ArrayList<>();
                for (String answerId : answerSelectedRequest) {
                    AnswerEntity answer = answerRepository.findById(answerId)
                            .orElseThrow(() -> new DataInvalidException(ExceptionVariable.ANSWER_NOT_FOUND));
                    if (!answer.getIsCorrect()) isSuccess = false;
                    listAnswerForQuestionCurrent.add(new AnswerQuestionResultEntity(answer, questionResult));
                }
                questionResult.setAnswers(listAnswerForQuestionCurrent);
                if (isSuccess) {
                    score++;
                    questionResult.setStatus(AnswerSelectedStatus.CORRECT);
                } else {
                    questionResult.setStatus(AnswerSelectedStatus.INCORRECT);
                }
            }
            listQuestionResult.add(questionResult);
        }
        testResultEntity.setScore(score);
        testResultEntity.setFinishDate(testResultFinish.getFinishDate());
        testResultEntity.getAnswerSelecteds().addAll(listQuestionResult);
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

    @Override
    @Transactional(readOnly = true)
    public List<NumberOfPlayerParticipatingTestResponse> numberOfPlayerParticipatingTest(Long limit) {
        List<List<String>> response = testResultRepository.findTop10By();
        return response.stream()
                .limit(limit)
                .map(data -> new NumberOfPlayerParticipatingTestResponse(data.get(0),
                        Long.valueOf(data.get(1))))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NumberOfPlayerParticipatingForTime> numberOfPlayerParticipatingForTime() {
        Date now = new Date(System.currentTimeMillis());
        Date lastWeek = Date.from(LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault()).minusDays(7L).atZone(ZoneId.systemDefault()).toInstant());
        List<NumberOfPlayerParticipatingForTime> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            java.sql.Date date = java.sql.Date.valueOf(LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault()).minusDays(i).toLocalDate());
            list.add(new NumberOfPlayerParticipatingForTime(date, 0L));
        }
        testResultRepository.findNumberOfPlayerParticipatingTest(lastWeek, now)
                .forEach(data -> {
                    java.sql.Date date = java.sql.Date.valueOf(data.get(0));
                    list.removeIf(test -> date.equals(test.getDate()));
                    list.add(new NumberOfPlayerParticipatingForTime(java.sql.Date.valueOf(data.get(0)), Long.parseLong(data.get(1))));
                });
        list.sort(Comparator.comparing(NumberOfPlayerParticipatingForTime::getDate));
        return list;
    }
}
