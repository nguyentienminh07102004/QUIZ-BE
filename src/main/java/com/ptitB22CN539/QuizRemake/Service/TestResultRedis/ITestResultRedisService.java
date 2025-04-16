package com.ptitB22CN539.QuizRemake.Service.TestResultRedis;

import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultStart;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ITestResultRedisService {
    String startTestResult(TestResultStart testResultStart);
    void saveTestResult(String testResultId, String questionId, String answerId);
    List<String> findAnswersByQuestionId(String testResultId, String questionId);
    Map<String, List<String>> findAnswersByTestResultId(String testResultId);
    String START_TIME = "startTime";
    String TEST_ID = "testId";
    Date findStartTimeByTestResultId(String testResultId);
    String findTestIdByTestResultId(String testResultId);
    void deleteTestResult(String testResultId);
}
