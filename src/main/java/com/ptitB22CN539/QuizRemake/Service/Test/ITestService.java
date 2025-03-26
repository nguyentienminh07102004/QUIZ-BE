package com.ptitB22CN539.QuizRemake.Service.Test;

import com.ptitB22CN539.QuizRemake.DTO.Request.Test.TestRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Test.TestSearchRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.TestRatingResponse;
import com.ptitB22CN539.QuizRemake.Domains.TestEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ITestService {
    TestEntity saveTest(TestRequest testRequest);
    Page<TestEntity> findAll(TestSearchRequest testSearchRequest);
    TestEntity findById(String id);
    Long countAllTest();
    List<TestEntity> findAllTestsHasSameCategory(String category);
    TestRatingResponse findLastTestRatingByTestIdAndUser(String testId);
    void ratingTest(String testId, Double rate);
}
