package com.ptitB22CN539.QuizRemake.Controller;

import com.ptitB22CN539.QuizRemake.DTO.APIResponse;
import com.ptitB22CN539.QuizRemake.DTO.Request.Question.QuestionRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Question.QuestionSearchRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.QuestionResponse;
import com.ptitB22CN539.QuizRemake.Mapper.QuestionMapper;
import com.ptitB22CN539.QuizRemake.Model.Entity.QuestionEntity;
import com.ptitB22CN539.QuizRemake.Service.Question.IQuestionService;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/questions")
public class QuestionController {
    private final IQuestionService questionService;
    private final QuestionMapper questionMapper;

    @PostMapping()
    public ResponseEntity<APIResponse> saveQuestion(@Valid @RequestBody List<QuestionRequest> questionRequests) {
        List<QuestionEntity> questionEntities = this.questionService.saveQuestion(questionRequests);
        List<QuestionResponse> questionResponses = questionEntities.stream().map(this.questionMapper::entityToResponse).toList();
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("SUCCESS")
                .data(questionResponses)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/save-from-file")
    public ResponseEntity<APIResponse> saveFromFile(@Valid @RequestPart MultipartFile file) {
        List<QuestionEntity> questionEntities = this.questionService.saveFromFile(file);
        List<QuestionResponse> questionResponses = questionEntities.stream().map(questionMapper::entityToResponse).toList();
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("SUCCESS")
                .data(questionResponses)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping()
    public ResponseEntity<APIResponse> updateQuestion(@RequestBody QuestionRequest questionRequest) {
        QuestionEntity questionEntity = this.questionService.updateQuestion(questionRequest);
        QuestionResponse questionResponse = this.questionMapper.entityToResponse(questionEntity);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .data(questionResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping()
    public ResponseEntity<APIResponse> findQuestion(@ModelAttribute QuestionSearchRequest questionSearchRequest) {
        Page<QuestionEntity> entityPage = this.questionService.findAll(questionSearchRequest);
        PagedModel<QuestionResponse> responsePagedModel = new PagedModel<>(entityPage.map(questionMapper::entityToResponse));
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .data(responsePagedModel)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/count")
    public ResponseEntity<APIResponse> countAllQuestions() {
        Long countAllQuestions = this.questionService.countAllQuestions();
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .data(countAllQuestions)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{ids}")
    public ResponseEntity<APIResponse> deleteQuestion(@PathVariable List<String> ids) {
        this.questionService.deleteQuestion(ids);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .code(HttpStatus.OK.value())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
