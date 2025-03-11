package com.ptitB22CN539.QuizRemake.Service.TestResult;

import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultFinish;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultStart;
import com.ptitB22CN539.QuizRemake.Domains.TestResultEntity;

public interface ITestResultService {
    TestResultEntity startTestResult(TestResultStart testResultStart);
    TestResultEntity finishTest(TestResultFinish testResultFinish);
    TestResultEntity findById(String id);
    Long countAllTestResult();
}
