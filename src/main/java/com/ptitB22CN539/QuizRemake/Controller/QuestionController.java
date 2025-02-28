package com.ptitB22CN539.QuizRemake.Controller;

import com.ptitB22CN539.QuizRemake.DTO.APIResponse;
import com.ptitB22CN539.QuizRemake.DTO.Request.Question.QuestionRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Question.QuestionSearchRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.QuestionResponse;
import com.ptitB22CN539.QuizRemake.Domains.QuestionEntity;
import com.ptitB22CN539.QuizRemake.Mapper.QuestionMapper;
import com.ptitB22CN539.QuizRemake.Service.Question.IQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/${api}/questions")
public class QuestionController {
    private final IQuestionService questionService;
    private final QuestionMapper questionMapper;

    @PostMapping(value = "/")
    public ResponseEntity<APIResponse> saveQuestion(@Valid @RequestBody QuestionRequest questionRequest) {
        QuestionEntity questionEntity = questionService.saveQuestion(questionRequest);
        QuestionResponse questionResponse = questionMapper.entityToResponse(questionEntity);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("SUCCESS")
                .data(questionResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/")
    public ResponseEntity<APIResponse> findQuestion(@RequestPart QuestionSearchRequest questionSearchRequest) {
        Page<QuestionEntity> entityPage = questionService.findAll(questionSearchRequest);
        PagedModel<QuestionResponse> responsePagedModel = new PagedModel<>(entityPage.map(questionMapper::entityToResponse));
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .data(responsePagedModel)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
