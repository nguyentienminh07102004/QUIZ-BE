package com.ptitB22CN539.QuizRemake.Repository;

import com.ptitB22CN539.QuizRemake.Common.Enum.TestStatus;
import com.ptitB22CN539.QuizRemake.Model.Entity.TestEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITestRepository extends JpaRepository<TestEntity, String>, JpaSpecificationExecutor<TestEntity> {
    List<TestEntity> findAllTestByCategory_CodeAndStatus(String categoryCode, TestStatus status, Pageable pageable);
}
