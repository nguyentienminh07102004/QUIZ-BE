package com.ptitB22CN539.QuizRemake.Service.Test;

import com.ptitB22CN539.QuizRemake.DTO.Request.Test.TestRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Test.TestSearchRequest;
import com.ptitB22CN539.QuizRemake.Domains.CategoryEntity_;
import com.ptitB22CN539.QuizRemake.Domains.TestEntity;
import com.ptitB22CN539.QuizRemake.Domains.TestEntity_;
import com.ptitB22CN539.QuizRemake.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.Mapper.TestMapper;
import com.ptitB22CN539.QuizRemake.Repository.ITestRepository;
import com.ptitB22CN539.QuizRemake.Utils.PaginationUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements ITestService {
    private final ITestRepository testRepository;
    private final TestMapper testMapper;

    @Override
    @Transactional
    public TestEntity saveTest(TestRequest testRequest) {
        TestEntity testEntity = testMapper.requestToEntity(testRequest);
        return testRepository.save(testEntity);
    }

    @Override
    public Page<TestEntity> findAll(TestSearchRequest testSearchRequest) {
        Specification<TestEntity> specification = (root, query, builder) -> {
            List<Predicate> predicates= new ArrayList<>();
            if (StringUtils.hasText(testSearchRequest.getTitle())) {
                predicates.add(builder.like(builder.lower(root.get(TestEntity_.TITLE)),
                        String.join("", "%", testSearchRequest.getTitle().toLowerCase(), "%")));
            }
            if (StringUtils.hasText(testSearchRequest.getId())) {
                predicates.add(builder.like(root.get(TestEntity_.ID),
                        String.join("", "%", testSearchRequest.getId(), "%")));
            }
            if (testSearchRequest.getDifficulty() != null) {
                predicates.add(builder.equal(root.get(TestEntity_.difficulty), testSearchRequest.getDifficulty()));
            }
            if (StringUtils.hasText(testSearchRequest.getCategory())) {
                predicates.add(builder.equal(root.get(TestEntity_.CATEGORY).get(CategoryEntity_.CODE),
                        testSearchRequest.getCategory()));
            }
            if (query != null) {
                query.distinct(true);
//                query.groupBy(root.get(TestEntity_.ID));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
        return testRepository.findAll(specification,
                PaginationUtils.getPageable(testSearchRequest.getPage(), testSearchRequest.getLimit()));
    }

    @Override
    @Transactional(readOnly = true)
    public TestEntity findById(String id) {
        return this.testRepository.findById(id)
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.TEST_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAllTest() {
        return testRepository.count();
    }
}
