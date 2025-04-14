package com.ptitB22CN539.QuizRemake.Repository;

import com.ptitB22CN539.QuizRemake.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
