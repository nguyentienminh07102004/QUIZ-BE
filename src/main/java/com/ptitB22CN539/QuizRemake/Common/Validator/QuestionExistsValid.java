package com.ptitB22CN539.QuizRemake.Common.Validator;

import com.ptitB22CN539.QuizRemake.Common.Validator.ValidClass.QuestionValidator;
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
@Constraint(validatedBy = {QuestionValidator.class})
public @interface QuestionExistsValid {
    String message() default "QUESTION_NOT_FOUND";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
