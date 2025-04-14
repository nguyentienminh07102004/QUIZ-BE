package com.ptitB22CN539.QuizRemake.Service.Jwt;

import com.ptitB22CN539.QuizRemake.Entity.JwtEntity;
import com.ptitB22CN539.QuizRemake.Common.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Common.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.Repository.IJwtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    @Transactional
    @Scheduled(cron = "@daily")
    protected void deleteJwtExpired() {
        jwtRepository.deleteByExpiresBefore(new Date(System.currentTimeMillis()));
    }
}
