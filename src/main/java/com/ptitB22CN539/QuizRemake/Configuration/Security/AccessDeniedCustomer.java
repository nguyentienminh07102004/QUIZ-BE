package com.ptitB22CN539.QuizRemake.Configuration.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptitB22CN539.QuizRemake.DTO.APIResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccessDeniedCustomer implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        APIResponse apiResponse = APIResponse.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .data(accessDeniedException.getMessage())
                .build();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.getWriter().flush();
    }
}
