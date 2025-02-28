package com.ptitB22CN539.QuizRemake.Service.Question;

import com.ptitB22CN539.QuizRemake.DTO.Request.Question.QuestionRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Question.QuestionSearchRequest;
import com.ptitB22CN539.QuizRemake.Domains.QuestionEntity;
import org.springframework.data.domain.Page;

public interface IQuestionService {
    QuestionEntity saveQuestion(QuestionRequest question);
    QuestionEntity findById(String id);
    boolean existsById(String id);
    Page<QuestionEntity> findAll(QuestionSearchRequest searchRequest);
}
