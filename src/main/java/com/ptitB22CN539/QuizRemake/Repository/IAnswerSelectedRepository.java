package com.ptitB22CN539.QuizRemake.Repository;

import com.ptitB22CN539.QuizRemake.Domains.AnswerSelectedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAnswerSelectedRepository extends JpaRepository<AnswerSelectedEntity, String> {
    List<AnswerSelectedEntity> findAllByTestResult_IdAndQuestion_Id(String testResultId, String questionId);
}
