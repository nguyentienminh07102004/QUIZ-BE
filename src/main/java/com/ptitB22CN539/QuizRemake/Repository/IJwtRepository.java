package com.ptitB22CN539.QuizRemake.Repository;

import com.ptitB22CN539.QuizRemake.Domains.JwtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface IJwtRepository extends JpaRepository<JwtEntity, String> {
    void deleteByExpiresBefore(Date expires);
}
