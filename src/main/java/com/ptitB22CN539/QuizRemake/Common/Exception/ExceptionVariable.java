package com.ptitB22CN539.QuizRemake.Common.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionVariable {
    EMAIL_NOT_FOUND(400, "Email is not exists", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS(400, "Email already exists", HttpStatus.CONFLICT),
    EMAIL_INVALID(400, "Email is invalid", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(400, "Role is not exists", HttpStatus.BAD_REQUEST),
    FULL_NAME_NOT_NULL_OR_EMPTY(400, "Full name is not null or empty", HttpStatus.BAD_REQUEST),
    EMAIL_PASSWORD_NOT_CORRECT(400, "Email or password is not correct", HttpStatus.BAD_REQUEST),
    SERVER_ERROR(500, "Server error", HttpStatus.INTERNAL_SERVER_ERROR),
    TOKEN_INVALID(400, "Token is invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_CONFIRM_PASSWORD_NOT_MATCH(400, "Password confirmation password is not match", HttpStatus.BAD_REQUEST),
    PASSWORD_LENGTH_NOT_CORRECT(400, "Password length is not correct", HttpStatus.BAD_REQUEST),
    ACCOUNT_LOGIN_MAX_DEVICE(400, "Account login max device", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(400, "User Id is not exists", HttpStatus.BAD_REQUEST),
    USER_LOCKED(400, "User locked", HttpStatus.BAD_REQUEST),
    AVATAR_FILE_EMPTY(400, "Avatar file is empty", HttpStatus.BAD_REQUEST),
    OLD_PASSWORD_NEW_PASSWORD_MATCH(400, "Old password is not match", HttpStatus.BAD_REQUEST),

    QUESTION_HAS_LEAST_ONE_CORRECT_ANSWER(400, "Question has least one correct answer", HttpStatus.BAD_REQUEST),
    QUESTION_NOT_FOUND(400, "Question is not exists", HttpStatus.BAD_REQUEST),
    QUESTION_TITLE_NOT_NULL_EMPTY(400, "Question title is null or empty", HttpStatus.BAD_REQUEST),
    QUESTION_CATEGORY_NOT_NULL_EMPTY(400, "Question category is null or empty", HttpStatus.BAD_REQUEST),
    QUESTION_CONTENT_NOT_NULL_EMPTY(400, "Question content is null or empty", HttpStatus.BAD_REQUEST),
    QUESTION_HAS_SAME_CATEGORY(400, "Question has same category", HttpStatus.BAD_REQUEST),

    CATEGORY_NOT_FOUND(400, "Category is not exists", HttpStatus.BAD_REQUEST),

    TEST_MUST_HAS_LEAST_QUESTION(400, "Test must has one question", HttpStatus.BAD_REQUEST),
    TEST_TITLE_NOT_NULL_OR_EMPTY(400, "Test title is null or empty", HttpStatus.BAD_REQUEST),
    TEST_NOT_FOUND(400, "Test is not exists", HttpStatus.BAD_REQUEST),

    TEST_RESULT_NOT_FOUND(400, "Test result is not exists", HttpStatus.BAD_REQUEST),
    ANSWER_NOT_FOUND(400, "Answer is not exists", HttpStatus.BAD_REQUEST),

    CODE_INVALID(400, "Code is invalid", HttpStatus.BAD_REQUEST),

    FILE_TYPE_NOT_SUPPORT(400, "File type don't support", HttpStatus.BAD_REQUEST),
    ;
    private final Integer code;
    private final String message;
    private final HttpStatus status;

    ExceptionVariable(Integer code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
