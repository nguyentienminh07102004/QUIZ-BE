package com.ptitB22CN539.QuizRemake.Controller;

import com.ptitB22CN539.QuizRemake.DTO.APIResponse;
import com.ptitB22CN539.QuizRemake.DTO.Request.Test.TestRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Test.TestSearchRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.TestResponse;
import com.ptitB22CN539.QuizRemake.Domains.TestEntity;
import com.ptitB22CN539.QuizRemake.Mapper.TestMapper;
import com.ptitB22CN539.QuizRemake.Service.Test.ITestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
