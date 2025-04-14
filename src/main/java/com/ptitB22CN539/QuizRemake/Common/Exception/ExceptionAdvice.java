package com.ptitB22CN539.QuizRemake.Common.Exception;

import com.ptitB22CN539.QuizRemake.DTO.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(value = DataInvalidException.class)
    public ResponseEntity<APIResponse> handleDataInvalidException(DataInvalidException exception) {
        APIResponse response = APIResponse.builder()
                .message(exception.getExceptionVariable().getMessage())
                .code(exception.getExceptionVariable().getCode())
                .data(exception.getMessage())
                .build();
        return ResponseEntity.status(exception.getExceptionVariable().getStatus()).body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ExceptionVariable exceptionVariable = ExceptionVariable.valueOf(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage());
        APIResponse response = APIResponse.builder()
                .code(exceptionVariable.getCode())
                .message(exceptionVariable.getMessage())
                .build();
        return ResponseEntity.status(exceptionVariable.getStatus()).body(response);
    }
}
