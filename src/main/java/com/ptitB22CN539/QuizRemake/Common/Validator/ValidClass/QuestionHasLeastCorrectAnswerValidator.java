package com.ptitB22CN539.QuizRemake.Common.Validator.ValidClass;

import com.ptitB22CN539.QuizRemake.DTO.Request.Answer.AnswerRequest;
import com.ptitB22CN539.QuizRemake.Common.Validator.QuestionHasLeastCorrectAnswer;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionHasLeastCorrectAnswerValidator implements ConstraintValidator<QuestionHasLeastCorrectAnswer, List<AnswerRequest>> {
    private int minCorrectAnswer;

    @Override
    public void initialize(QuestionHasLeastCorrectAnswer constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.minCorrectAnswer = constraintAnnotation.minCorrect();
    }

    @Override
    public boolean isValid(List<AnswerRequest> answerRequests, ConstraintValidatorContext constraintValidatorContext) {
        if (answerRequests == null) {
            return true;
        }
        List<AnswerRequest> list = answerRequests.stream().filter(AnswerRequest::getIsCorrect).toList();
        return list.size() >= this.minCorrectAnswer;
    }

}
