package com.ptitB22CN539.QuizRemake.Service.Test;

import com.ptitB22CN539.QuizRemake.Common.Enum.TestStatus;
import com.ptitB22CN539.QuizRemake.Common.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Common.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.DTO.Request.Test.TestRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Test.TestSearchRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.TestRatingResponse;
import com.ptitB22CN539.QuizRemake.Mapper.TestMapper;
import com.ptitB22CN539.QuizRemake.Model.Entity.CategoryEntity_;
import com.ptitB22CN539.QuizRemake.Model.Entity.TestEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.TestEntity_;
import com.ptitB22CN539.QuizRemake.Model.Entity.TestRatingEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.TestRatingEntity_;
import com.ptitB22CN539.QuizRemake.Model.Entity.UserEntity;
import com.ptitB22CN539.QuizRemake.Repository.ITestRatingRepository;
import com.ptitB22CN539.QuizRemake.Repository.ITestRepository;
import com.ptitB22CN539.QuizRemake.Service.User.IUserService;
import com.ptitB22CN539.QuizRemake.Utils.PaginationUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements ITestService {
    private final ITestRepository testRepository;
    private final TestMapper testMapper;
    private final ITestRatingRepository testRatingRepository;
    private final IUserService userService;

    @Override
    @Transactional
    public TestEntity saveTest(TestRequest testRequest) {
        TestEntity testEntity = testMapper.requestToEntity(testRequest);
        return testRepository.save(testEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TestEntity> findAll(TestSearchRequest testSearchRequest) {
        Specification<TestEntity> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
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
            predicates.add(builder.equal(root.get(TestEntity_.STATUS), TestStatus.ACTIVE));
            if (query != null) {
                query.distinct(true);
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

    @Override
    @Transactional(readOnly = true)
    public List<TestEntity> findAllTestsHasSameCategory(String category) {
        return testRepository.findAllTestByCategory_CodeAndStatus(category, TestStatus.ACTIVE, PaginationUtils.getPageable(1, 10));
    }

    @Override
    @Transactional(readOnly = true)
    public TestRatingResponse findLastTestRatingByTestIdAndUser(String testId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        TestRatingEntity testRating = testRatingRepository.findTop1ByUser_EmailAndTest_Id(email, testId, Sort.by(Sort.Direction.DESC, TestRatingEntity_.CREATED_DATE));
        TestRatingResponse testRatingResponse = new TestRatingResponse();
        if (testRating == null) {
            testRatingResponse.setRating(0.0);
            testRatingResponse.setNumberOfRatings(0);
        } else {
            testRatingResponse.setNumberOfRatings(testRatingRepository.countByUser_EmailAndTest_Id(email, testId));
            DecimalFormat format = new DecimalFormat("#.##");
            testRatingResponse.setRating(Double.valueOf(format.format(testRating.getRating())));
        }
        return testRatingResponse;
    }

    @Override
    @Transactional
    public void ratingTest(String testId, Double rate) {
        TestRatingEntity testRatingEntity = new TestRatingEntity();
        testRatingEntity.setRating(rate);
        UserEntity user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        testRatingEntity.setUser(user);
        testRatingEntity.setTest(this.findById(testId));
        testRatingRepository.save(testRatingEntity);
    }

    @Override
    @Transactional
    public void deleteTest(List<String> ids) {
        List<TestEntity> entityList = new ArrayList<>();
        for (String id : ids) {
            TestEntity testEntity = this.findById(id);
            testEntity.setStatus(TestStatus.INACTIVE);
            entityList.add(testEntity);
        }
        testRepository.saveAll(entityList);
    }
}
