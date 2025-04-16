package com.ptitB22CN539.QuizRemake.Service.TestResult;

import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultFinish;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultStart;
import com.ptitB22CN539.QuizRemake.DTO.Response.Chart.NumberOfPlayerParticipatingForTime;
import com.ptitB22CN539.QuizRemake.DTO.Response.Chart.NumberOfPlayerParticipatingTestResponse;
import com.ptitB22CN539.QuizRemake.Model.Entity.TestEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.TestResultEntity;

import java.util.List;

public interface ITestResultService {
    String startTestResult(TestResultStart testResultStart);
    TestResultEntity finishTest(TestResultFinish testResultFinish);
    TestResultEntity findById(String id);
    void saveAnswerOfTestResult(String testResultId, String questionId, String answerId);
    TestEntity findTestByTestResultId(String testResultId);
    Long countAllTestResult();

    List<String> findAnswerSelectedIdsOfTestResult(String testResultId, String questionId);

    List<NumberOfPlayerParticipatingTestResponse> numberOfPlayerParticipatingTest(Long limit);
    List<NumberOfPlayerParticipatingForTime> numberOfPlayerParticipatingForTime();
}
