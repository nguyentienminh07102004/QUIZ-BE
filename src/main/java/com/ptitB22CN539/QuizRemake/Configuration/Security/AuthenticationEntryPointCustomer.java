package com.ptitB22CN539.QuizRemake.Configuration.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptitB22CN539.QuizRemake.DTO.APIResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointCustomer implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        APIResponse apiResponse = APIResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .data(authException.getMessage())
                .build();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.getWriter().flush();
    }
}
