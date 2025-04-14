package com.ptitB22CN539.QuizRemake.Controller;

import com.ptitB22CN539.QuizRemake.DTO.APIResponse;
import com.ptitB22CN539.QuizRemake.DTO.Request.Test.TestRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Test.TestSearchRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.TestRatingResponse;
import com.ptitB22CN539.QuizRemake.DTO.Response.TestResponse;
import com.ptitB22CN539.QuizRemake.Entity.TestEntity;
import com.ptitB22CN539.QuizRemake.Mapper.TestMapper;
import com.ptitB22CN539.QuizRemake.Service.Test.ITestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/${api}/tests")
public class TestController {
    private final ITestService testService;
    private final TestMapper testMapper;

    @PostMapping(value = "/")
    public ResponseEntity<APIResponse> saveTest(@Valid @RequestBody TestRequest testRequest) {
        TestEntity testEntity = testService.saveTest(testRequest);
        TestResponse testResponse = testMapper.entityToResponse(testEntity);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("SUCCESS")
                .data(testResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/")
    public ResponseEntity<APIResponse> findTest(@ModelAttribute TestSearchRequest testSearchRequest) {
        Page<TestEntity> testPage = testService.findAll(testSearchRequest);
        PagedModel<TestResponse> responsePagedModel = new PagedModel<>(testPage.map(testMapper::entityToResponse));
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message("SUCCESS")
                .data(responsePagedModel)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<APIResponse> findById(@PathVariable String id) {
        TestEntity testEntity = testService.findById(id);
        TestResponse testResponse = testMapper.entityToResponse(testEntity);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message("SUCCESS")
                .data(testResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/count")
    public ResponseEntity<APIResponse> countAllTest() {
        Long countAllQuestions = testService.countAllTest();
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message("SUCCESS")
                .data(countAllQuestions)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/same-category")
    public ResponseEntity<APIResponse> findAllTestByCategory(@RequestParam String categoryCode) {
        List<TestEntity> listTest = testService.findAllTestsHasSameCategory(categoryCode);
        List<TestResponse> testResponses = listTest.stream()
                .map(testMapper::entityToResponse)
                .toList();
        APIResponse response = APIResponse.builder()
                .code(200)
                .message("SUCCESS")
                .data(testResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/rating/user/{testId}")
    public ResponseEntity<APIResponse> findTestRatingByTestIdAndUser(@PathVariable String testId) {
        TestRatingResponse testRatingResponse = testService.findLastTestRatingByTestIdAndUser(testId);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(200)
                .data(testRatingResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/rate/{testId}/{rate}")
    public ResponseEntity<APIResponse> ratingTest(@PathVariable String testId, @PathVariable Double rate) {
        testService.ratingTest(testId, rate);
        APIResponse response = APIResponse.builder()
                .code(200)
                .message("SUCCESS")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{ids}")
    public ResponseEntity<APIResponse> deleteTest(@PathVariable List<String> ids) {
        testService.deleteTest(ids);
        APIResponse response = APIResponse.builder()
                .code(200)
                .message("SUCCESS")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
