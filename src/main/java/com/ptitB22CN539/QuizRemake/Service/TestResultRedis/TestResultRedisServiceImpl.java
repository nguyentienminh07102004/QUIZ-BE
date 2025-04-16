package com.ptitB22CN539.QuizRemake.Service.TestResultRedis;

import com.ptitB22CN539.QuizRemake.Common.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Common.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultStart;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestResultRedisServiceImpl implements ITestResultRedisService {
    private final HashOperations<String, String, Object> hashOperations;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public String startTestResult(TestResultStart testResultStart) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String testResultId = UUID.randomUUID().toString();
        String id = this.getKey(email, testResultId);
        this.hashOperations.put(id, ITestResultRedisService.START_TIME, testResultStart.getStartedDate());
        this.hashOperations.put(id, ITestResultRedisService.TEST_ID, testResultStart.getTestId());
        this.redisTemplate.expire(id, Duration.ofSeconds(86400));
        return testResultId;
    }

    @Override
    @Transactional
    public void saveTestResult(String testResultId, String questionId, String answerId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String id = this.getKey(email, testResultId);
        if (!this.hashOperations.hasKey(id, ITestResultRedisService.TEST_ID)) {
            throw new DataInvalidException(ExceptionVariable.TEST_RESULT_NOT_FOUND);
        }
        Map<String, Object> listAnswerOfQuestions = this.hashOperations.entries(id);
        List<String> answers = (List<String>) listAnswerOfQuestions.getOrDefault(questionId, new ArrayList<>());
        if (answers.contains(answerId)) {
            answers.remove(answerId);
        } else {
            answers.add(answerId);
        }
        listAnswerOfQuestions.put(questionId, answers);
        this.hashOperations.put(id, questionId, answers);
        this.redisTemplate.expire(id, Duration.ofSeconds(86400));
    }

    @Override
    @Transactional
    public List<String> findAnswersByQuestionId(String testResultId, String questionId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> listAnswerOfQuestions = hashOperations.entries(this.getKey(email, testResultId));
        return (List<String>) listAnswerOfQuestions.getOrDefault(questionId, new ArrayList<>());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<String>> findAnswersByTestResultId(String testResultId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> data = this.hashOperations.entries(this.getKey(email, testResultId));
        Map<String, List<String>> answers = new HashMap<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() instanceof List) {
                answers.put(entry.getKey(), (List<String>) entry.getValue());
            }
        }
        return answers;
    }

    @Override
    @Transactional(readOnly = true)
    public Date findStartTimeByTestResultId(String testResultId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Object result = this.hashOperations.get(this.getKey(email, testResultId), ITestResultRedisService.START_TIME);
        if (result == null) {
            throw new DataInvalidException(ExceptionVariable.TEST_RESULT_NOT_FOUND);
        }
        return (Date) result;
    }

    @Override
    @Transactional(readOnly = true)
    public String findTestIdByTestResultId(String testResultId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Object result = this.hashOperations.get(this.getKey(email, testResultId), ITestResultRedisService.TEST_ID);
        if (result == null) {
            throw new DataInvalidException(ExceptionVariable.TEST_RESULT_NOT_FOUND);
        }
        return (String) result;
    }

    @Override
    public void deleteTestResult(String testResultId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        this.redisTemplate.delete(this.getKey(email, testResultId));
    }

    private String getKey(String email, String testResultId) {
        return (new StringBuilder()).append(email).append(":").append(testResultId).toString();
    }
}
