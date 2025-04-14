package com.ptitB22CN539.QuizRemake.Repository;

import com.ptitB22CN539.QuizRemake.Entity.QuestionResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuestionResultRepository extends JpaRepository<QuestionResultEntity, String> {
    List<QuestionResultEntity> findAllByTestResult_Id(String testResultId);
}
