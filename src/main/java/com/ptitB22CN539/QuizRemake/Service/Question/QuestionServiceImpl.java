package com.ptitB22CN539.QuizRemake.Service.Question;

import com.ptitB22CN539.QuizRemake.DTO.Request.Question.QuestionRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Question.QuestionSearchRequest;
import com.ptitB22CN539.QuizRemake.Domains.CategoryEntity_;
import com.ptitB22CN539.QuizRemake.Domains.QuestionEntity;
import com.ptitB22CN539.QuizRemake.Domains.QuestionEntity_;
import com.ptitB22CN539.QuizRemake.Domains.TestEntity_;
import com.ptitB22CN539.QuizRemake.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.Mapper.QuestionMapper;
import com.ptitB22CN539.QuizRemake.Repository.IQuestionRepository;
import com.ptitB22CN539.QuizRemake.Utils.PaginationUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements IQuestionService {
    private final IQuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    @Override
    @Transactional
    public QuestionEntity saveQuestion(QuestionRequest question) {
        QuestionEntity questionEntity = questionMapper.requestToEntity(question);
        return questionRepository.save(questionEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionEntity findById(String id) {
        return this.questionRepository.findById(id)
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.QUESTION_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return this.questionRepository.existsById(id);
    }

    @Override
    public Page<QuestionEntity> findAll(QuestionSearchRequest searchRequest) {
        Specification<QuestionEntity> specification = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (StringUtils.hasText(searchRequest.getTitle())) {
                predicate = builder.and(builder.like(root.get(QuestionEntity_.TITLE),
                        String.join("", "%", searchRequest.getTitle(), "%")));
            }
            if (StringUtils.hasText(searchRequest.getId())) {
                predicate = builder.and(builder.like(root.get(QuestionEntity_.ID),
                        String.join("", "%", searchRequest.getId(), "%")));
            }
            if (StringUtils.hasText(searchRequest.getCategoryCode())) {
                predicate = builder.and(builder.equal(root.get(TestEntity_.CATEGORY).get(CategoryEntity_.CODE),
                        searchRequest.getCategoryCode()));
            }
            return predicate;
        };
        return questionRepository.findAll(specification,
                PaginationUtils.getPageable(searchRequest.getPage(), searchRequest.getLimit()));
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAllQuestions() {
        return questionRepository.count();
    }
}
