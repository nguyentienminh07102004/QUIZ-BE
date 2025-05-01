package com.ptitB22CN539.QuizRemake.Service.Question;

import com.ptitB22CN539.QuizRemake.DTO.Request.Question.QuestionRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Question.QuestionSearchRequest;
import com.ptitB22CN539.QuizRemake.Model.Entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IQuestionService {
    QuestionEntity saveQuestion(QuestionRequest question);
    List<QuestionEntity> saveQuestion(List<QuestionRequest> questions);
    List<QuestionEntity> saveFromFile(MultipartFile file);
    QuestionEntity findById(String id);
    boolean existsById(String id);
    Page<QuestionEntity> findAll(QuestionSearchRequest searchRequest);
    void deleteQuestion(List<String> ids);
    QuestionEntity updateQuestion(QuestionRequest question);
    Long countAllQuestions();
}
