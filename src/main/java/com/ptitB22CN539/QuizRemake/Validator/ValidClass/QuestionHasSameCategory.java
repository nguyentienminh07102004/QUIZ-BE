package com.ptitB22CN539.QuizRemake.Validator.ValidClass;

import com.ptitB22CN539.QuizRemake.Domains.QuestionEntity;
import com.ptitB22CN539.QuizRemake.Service.Question.IQuestionService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionHasSameCategory implements ConstraintValidator<com.ptitB22CN539.QuizRemake.Validator.QuestionHasSameCategory, List<String>> {
    private final IQuestionService questionService;

    @Override
    public boolean isValid(List<String> ids, ConstraintValidatorContext constraintValidatorContext) {
        if (ids.size() <= 1) {
            return true;
        }
        QuestionEntity question = questionService.findById(ids.get(0));
        for (int i = 1; i < ids.size(); i++) {
            boolean isSameCategory = questionService.findById(ids.get(i)).getCategory().getCode().equals(question.getCategory().getCode());
            if (!isSameCategory) {
                return false;
            }
        }
        return true;
    }
}
