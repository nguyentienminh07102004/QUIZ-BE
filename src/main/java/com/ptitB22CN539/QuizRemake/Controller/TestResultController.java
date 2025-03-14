package com.ptitB22CN539.QuizRemake.Controller;

import com.ptitB22CN539.QuizRemake.DTO.APIResponse;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultFinish;
import com.ptitB22CN539.QuizRemake.DTO.Request.TestResult.TestResultStart;
import com.ptitB22CN539.QuizRemake.DTO.Response.Chart.NumberOfPlayerParticipatingForTime;
import com.ptitB22CN539.QuizRemake.DTO.Response.Chart.NumberOfPlayerParticipatingTestResponse;
import com.ptitB22CN539.QuizRemake.DTO.Response.TestResultResponse;
import com.ptitB22CN539.QuizRemake.Domains.TestResultEntity;
import com.ptitB22CN539.QuizRemake.Mapper.TestResultMapper;
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
@RequestMapping(value = "/${api}/test-result")
public class TestResultController {
    private final ITestResultService testResultService;
    private final TestResultMapper testResultMapper;

    @PostMapping(value = "/start")
    public ResponseEntity<APIResponse> startTest(@Valid @RequestBody TestResultStart testResultStart) {
        TestResultEntity entity = testResultService.startTestResult(testResultStart);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .data(testResultMapper.entityToResponse(entity))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/finish")
    public ResponseEntity<APIResponse> finishTest(@Valid @RequestBody TestResultFinish testResultFinish) {
        TestResultEntity entity = testResultService.finishTest(testResultFinish);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .data(testResultMapper.entityToResponse(entity))
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
