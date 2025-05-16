package com.ptitB22CN539.QuizRemake.Service.Question;

import com.ptitB22CN539.QuizRemake.Common.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Common.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.DTO.Request.Answer.AnswerRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Question.QuestionRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.Question.QuestionSearchRequest;
import com.ptitB22CN539.QuizRemake.Mapper.QuestionMapper;
import com.ptitB22CN539.QuizRemake.Model.Entity.CategoryEntity_;
import com.ptitB22CN539.QuizRemake.Model.Entity.QuestionEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.QuestionEntity_;
import com.ptitB22CN539.QuizRemake.Model.Entity.TestEntity_;
import com.ptitB22CN539.QuizRemake.JpaRepository.IQuestionRepository;
import com.ptitB22CN539.QuizRemake.Utils.PaginationUtils;
import com.ptitB22CN539.QuizRemake.Utils.ReadExcelUtil;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements IQuestionService {
    private final IQuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final ReadExcelUtil readExcelUtil;

    @Transactional
    @Override
    public QuestionEntity saveQuestion(QuestionRequest question) {
        QuestionEntity questionEntity = questionMapper.requestToEntity(question);
        return questionRepository.save(questionEntity);
    }

    @Override
    @Transactional
    public List<QuestionEntity> saveQuestion(List<QuestionRequest> questions) {
        List<QuestionEntity> questionEntities = new ArrayList<>();
        for (QuestionRequest question : questions) {
            questionEntities.add(this.saveQuestion(question));
        }
        return questionEntities;
    }

    @Override
    @Transactional
    public List<QuestionEntity> saveFromFile(MultipartFile file) {
        try {
            List<QuestionRequest> questionRequests = this.readExcelUtil.readExcel(file, 0, QuestionRequest.class);
            List<QuestionEntity> questionEntities = new ArrayList<>();
            for (QuestionRequest questionRequest : questionRequests) {
                QuestionEntity questionEntity = this.saveQuestion(questionRequest);
                questionEntities.add(questionEntity);
            }
            return questionEntities;
        } catch (Exception exception) {
            throw new DataInvalidException(ExceptionVariable.QUESTION_NOT_FOUND);
        }
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
    @Transactional(readOnly = true)
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
            if (query != null) {
                query.distinct(true);
            }
            return predicate;
        };
        return this.questionRepository.findAll(specification,
                PaginationUtils.getPageable(searchRequest.getPage(), searchRequest.getLimit()));
    }

    @Transactional
    @Override
    public void deleteQuestion(List<String> ids) {
        for (String id : ids) {
            QuestionEntity question = this.findById(id);
            this.questionRepository.delete(question);
        }
    }

    @Override
    @Transactional
    public QuestionEntity updateQuestion(QuestionRequest question) {
        QuestionEntity questionEntity = this.findById(question.getId());
        List<String> answerIds = question.getAnswers().stream().map(AnswerRequest::getId).toList();
        questionEntity.getAnswers().removeIf(answer -> {
            boolean isRemove = !answerIds.contains(answer.getId());
            if (isRemove) {
                answer.setQuestion(null);
            }
            return isRemove;
        });
        questionEntity = this.questionMapper.requestToEntity(question);
        return this.questionRepository.save(questionEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAllQuestions() {
        return this.questionRepository.count();
    }
}
