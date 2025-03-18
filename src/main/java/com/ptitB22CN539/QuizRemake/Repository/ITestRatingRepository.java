package com.ptitB22CN539.QuizRemake.Repository;

import com.ptitB22CN539.QuizRemake.Domains.TestRatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITestRatingRepository extends JpaRepository<TestRatingEntity, String> {
    List<TestRatingEntity> findByUser_EmailAndTest_Id(String userEmail, String testId);
}
