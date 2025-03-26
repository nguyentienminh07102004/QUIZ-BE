package com.ptitB22CN539.QuizRemake.Repository;

import com.ptitB22CN539.QuizRemake.Domains.TestRatingEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITestRatingRepository extends JpaRepository<TestRatingEntity, String> {
    TestRatingEntity findTop1ByUser_EmailAndTest_Id(String userEmail, String testId, Sort sort);
    Integer countByUser_EmailAndTest_Id(String userEmail, String testId);
}
