package com.ptitB22CN539.QuizRemake.JpaRepository;

import com.ptitB22CN539.QuizRemake.Model.Entity.AnswerOfQuestionTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAnswerRepository extends JpaRepository<AnswerOfQuestionTestEntity, String> {
}
