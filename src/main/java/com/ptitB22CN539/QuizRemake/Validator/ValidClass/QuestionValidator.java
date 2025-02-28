package com.ptitB22CN539.QuizRemake.Validator.ValidClass;

import com.ptitB22CN539.QuizRemake.Service.Question.IQuestionService;
import com.ptitB22CN539.QuizRemake.Validator.QuestionExistsValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionValidator implements ConstraintValidator<QuestionExistsValid, List<String>> {
    private final IQuestionService questionService;

    @Override
    public boolean isValid(List<String> questionIds, ConstraintValidatorContext constraintValidatorContext) {
        if (questionIds == null) return true;
        for (String questionId : questionIds) {
            if (!this.questionService.existsById(questionId)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void initialize(QuestionExistsValid questionValid) {
        ConstraintValidator.super.initialize(questionValid);
    }
}
