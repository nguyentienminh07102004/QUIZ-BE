package com.ptitB22CN539.QuizRemake.Controller;

import com.ptitB22CN539.QuizRemake.DTO.APIResponse;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.AnswerSelectedRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultFinish;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultStart;
import com.ptitB22CN539.QuizRemake.DTO.Response.Chart.NumberOfPlayerParticipatingForTime;
import com.ptitB22CN539.QuizRemake.DTO.Response.Chart.NumberOfPlayerParticipatingTestResponse;
import com.ptitB22CN539.QuizRemake.DTO.Response.TestResponse;
import com.ptitB22CN539.QuizRemake.DTO.Response.TestResultResponse;
import com.ptitB22CN539.QuizRemake.Mapper.TestMapper;
import com.ptitB22CN539.QuizRemake.Mapper.TestResultMapper;
import com.ptitB22CN539.QuizRemake.Model.Entity.TestEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.TestResultEntity;
import com.ptitB22CN539.QuizRemake.Service.TestResult.ITestResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/test-result")
public class TestResultController {
    private final ITestResultService testResultService;
    private final TestResultMapper testResultMapper;
    private final TestMapper testMapper;

    @PostMapping(value = "/start")
    public ResponseEntity<APIResponse> startTest(@Valid @RequestBody TestResultStart testResultStart) {
        String testResultId = this.testResultService.startTestResult(testResultStart);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.CREATED.value())
                .data(testResultId)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/save-answer-test-result")
    public ResponseEntity<APIResponse> saveAnswerOfTestResult(@Valid @RequestBody AnswerSelectedRequest answerSelectedRequest) {
        this.testResultService.saveAnswerOfTestResult(answerSelectedRequest.getTestResultId(), answerSelectedRequest.getQuestionId(), answerSelectedRequest.getAnswerId());
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/finish")
    public ResponseEntity<APIResponse> finishTest(@Valid @RequestBody TestResultFinish testResultFinish) {
        TestResultEntity entity = this.testResultService.finishTest(testResultFinish);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .data(testResultMapper.entityToResponse(entity))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/test/{testResultId}")
    public ResponseEntity<APIResponse> findAnswerOfTestResult(@PathVariable String testResultId) {
        TestEntity test = this.testResultService.findTestByTestResultId(testResultId);
        TestResponse testResponse = this.testMapper.entityToResponse(test);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .data(testResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<APIResponse> findTestById(@PathVariable String id) {
        TestResultEntity testResultEntity = testResultService.findById(id);
        TestResultResponse testResultResponse = testResultMapper.entityToResponse(testResultEntity);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .data(testResultResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/count")
    public ResponseEntity<APIResponse> countAllTestResult() {
        Long countAllTestResult = testResultService.countAllTestResult();
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .data(countAllTestResult)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/{testResultId}/question/{questionId}")
    public ResponseEntity<APIResponse> findAnswerSelectedIdsOfTestResult(@PathVariable String testResultId, @PathVariable String questionId) {
        List<String> answerIds = this.testResultService.findAnswerSelectedIdsOfTestResult(testResultId, questionId);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .data(answerIds)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/number-of-player-participating-test")
    public ResponseEntity<APIResponse> numberOfPlayerParticipatingTest(@RequestParam(required = false, defaultValue = "10") Long limit) {
        List<NumberOfPlayerParticipatingTestResponse> responses = testResultService.numberOfPlayerParticipatingTest(limit);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .data(responses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/number-of-player-participating-test-for-time")
    public ResponseEntity<APIResponse> numberOfPlayerParticipatingForTime() {
        List<NumberOfPlayerParticipatingForTime> responses = testResultService.numberOfPlayerParticipatingForTime();
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .data(responses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
