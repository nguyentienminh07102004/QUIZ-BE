package com.ptitB22CN539.QuizRemake.Service.TestResult;

import com.ptitB22CN539.QuizRemake.Common.Enum.AnswerSelectedStatus;
import com.ptitB22CN539.QuizRemake.Common.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Common.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultFinish;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultStart;
import com.ptitB22CN539.QuizRemake.DTO.Response.Chart.NumberOfPlayerParticipatingForTime;
import com.ptitB22CN539.QuizRemake.DTO.Response.Chart.NumberOfPlayerParticipatingTestResponse;
import com.ptitB22CN539.QuizRemake.Model.Entity.AnswerEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.AnswerQuestionResultEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.QuestionEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.QuestionResultEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.TestEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.TestResultEntity;
import com.ptitB22CN539.QuizRemake.Repository.IAnswerRepository;
import com.ptitB22CN539.QuizRemake.Repository.ITestResultRepository;
import com.ptitB22CN539.QuizRemake.Service.Test.ITestService;
import com.ptitB22CN539.QuizRemake.Service.TestResultRedis.ITestResultRedisService;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TestResultServiceImpl implements ITestResultService {
    private final ITestResultRepository testResultRepository;
    private final IAnswerRepository answerRepository;
    private final ITestResultRedisService testResultRedisService;
    private final IUserService userService;
    private final ITestService testService;

    @Override
    @Transactional
    public String startTestResult(TestResultStart testResultStart) {
        return this.testResultRedisService.startTestResult(testResultStart);
    }

    @Override
    @Transactional
    public TestResultEntity finishTest(TestResultFinish testResultFinish) {
        Map<String, List<String>> answers = this.testResultRedisService.findAnswersByTestResultId(testResultFinish.getId());
        TestResultEntity testResultEntity = new TestResultEntity();
        testResultEntity.setId(testResultFinish.getId());
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        testResultEntity.setUser(this.userService.getUserByEmail(email));
        String testId = this.testResultRedisService.findTestIdByTestResultId(testResultEntity.getId());
        TestEntity test = this.testService.findById(testId);
        testResultEntity.setTest(test);
        Date startDate = this.testResultRedisService.findStartTimeByTestResultId(testResultEntity.getId());
        testResultEntity.setStartedDate(startDate);
        testResultEntity.setFinishDate(testResultFinish.getFinishDate());
        List<QuestionResultEntity> listOfQuestionResult = new ArrayList<>();
        Integer score = 0;
        for (QuestionEntity question : test.getQuestions()) {
            QuestionResultEntity questionResult = new QuestionResultEntity();
            questionResult.setQuestion(question);
            questionResult.setTestResult(testResultEntity);
            List<String> listOfAnswers = answers.getOrDefault(question.getId(), null);
            if (listOfAnswers != null && !listOfAnswers.isEmpty()) {
                List<AnswerQuestionResultEntity> listOfAnswerQuestionResult = new ArrayList<>();
                boolean isCorrect = true;
                for (String answer : listOfAnswers) {
                    AnswerEntity answerEntity = this.answerRepository.findById(answer)
                            .orElseThrow(() -> new DataInvalidException(ExceptionVariable.ANSWER_NOT_FOUND));
                    AnswerQuestionResultEntity answerQuestionResult = new AnswerQuestionResultEntity(answerEntity, questionResult);
                    if (!answerEntity.getIsCorrect()) {
                        isCorrect = false;
                    }
                    listOfAnswerQuestionResult.add(answerQuestionResult);
                }
                questionResult.setAnswers(listOfAnswerQuestionResult);
                if (isCorrect) {
                    questionResult.setStatus(AnswerSelectedStatus.CORRECT);
                    score++;
                } else {
                    questionResult.setStatus(AnswerSelectedStatus.INCORRECT);
                }
            } else {
                questionResult.setStatus(AnswerSelectedStatus.NOT_ANSWERED);
            }
            listOfQuestionResult.add(questionResult);
        }
        testResultEntity.setAnswerSelecteds(listOfQuestionResult);
        testResultEntity.setScore(score);
        this.testResultRedisService.deleteTestResult(testResultEntity.getId());
        return this.testResultRepository.save(testResultEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public TestResultEntity findById(String id) {
        return testResultRepository.findById(id)
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.TEST_RESULT_NOT_FOUND));
    }

    @Override
    @Transactional
    public void saveAnswerOfTestResult(String testResultId, String questionId, String answerId) {
        this.testResultRedisService.saveTestResult(testResultId, questionId, answerId);
    }

    @Override
    public TestEntity findTestByTestResultId(String testResultId) {
        String testId = this.testResultRedisService.findTestIdByTestResultId(testResultId);
        return this.testService.findById(testId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAllTestResult() {
        return testResultRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findAnswerSelectedIdsOfTestResult(String testResultId, String questionId) {
        return this.testResultRedisService.findAnswersByQuestionId(testResultId, questionId);
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
