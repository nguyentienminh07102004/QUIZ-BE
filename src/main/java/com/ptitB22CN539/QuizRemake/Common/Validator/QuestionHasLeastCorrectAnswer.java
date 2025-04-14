package com.ptitB22CN539.QuizRemake.Common.Validator;

import com.ptitB22CN539.QuizRemake.Common.Validator.ValidClass.QuestionHasLeastCorrectAnswerValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {QuestionHasLeastCorrectAnswerValidator.class})
public @interface QuestionHasLeastCorrectAnswer {
    String message() default "QUESTION_HAS_LEAST_ONE_CORRECT_ANSWER";
    Class<?>[] groups() default {};
    int minCorrect() default 1;
    Class<? extends Payload>[] payload() default {};
}
