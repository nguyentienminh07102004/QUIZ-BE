package com.ptitB22CN539.QuizRemake.Service.Jwt;

import com.ptitB22CN539.QuizRemake.Model.Entity.JwtEntity;

public interface IJwtService {
    JwtEntity findById(String id);
    boolean existsById(String id);
}
