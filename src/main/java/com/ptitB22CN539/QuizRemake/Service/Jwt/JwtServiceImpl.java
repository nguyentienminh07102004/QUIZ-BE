package com.ptitB22CN539.QuizRemake.Service.Jwt;

import com.ptitB22CN539.QuizRemake.Domains.JwtEntity;
import com.ptitB22CN539.QuizRemake.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.Repository.IJwtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements IJwtService {
    private final IJwtRepository jwtRepository;

    @Override
    @Transactional(readOnly = true)
    public JwtEntity findById(String id) {
        return jwtRepository.findById(id)
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.TOKEN_INVALID));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return jwtRepository.existsById(id);
    }
}
