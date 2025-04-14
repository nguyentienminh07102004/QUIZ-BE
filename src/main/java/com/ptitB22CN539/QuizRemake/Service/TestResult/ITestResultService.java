package com.ptitB22CN539.QuizRemake.Service.TestResult;

import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultFinish;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultStart;
import com.ptitB22CN539.QuizRemake.DTO.Response.Chart.NumberOfPlayerParticipatingForTime;
import com.ptitB22CN539.QuizRemake.DTO.Response.Chart.NumberOfPlayerParticipatingTestResponse;
import com.ptitB22CN539.QuizRemake.Entity.TestResultEntity;

import java.util.List;

public interface ITestResultService {
    TestResultEntity startTestResult(TestResultStart testResultStart);
    TestResultEntity finishTest(TestResultFinish testResultFinish);
    TestResultEntity findById(String id);
    Long countAllTestResult();
    List<NumberOfPlayerParticipatingTestResponse> numberOfPlayerParticipatingTest(Long limit);
    List<NumberOfPlayerParticipatingForTime> numberOfPlayerParticipatingForTime();
}
