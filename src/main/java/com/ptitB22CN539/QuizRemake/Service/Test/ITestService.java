package com.ptitB22CN539.QuizRemake.Service.Test;

import com.ptitB22CN539.QuizRemake.DTO.Request.Test.TestRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Test.TestSearchRequest;
import com.ptitB22CN539.QuizRemake.Domains.TestEntity;
import org.springframework.data.domain.Page;

public interface ITestService {
    TestEntity saveTest(TestRequest testRequest);
    Page<TestEntity> findAll(TestSearchRequest testSearchRequest);
    TestEntity findById(String id);
}
