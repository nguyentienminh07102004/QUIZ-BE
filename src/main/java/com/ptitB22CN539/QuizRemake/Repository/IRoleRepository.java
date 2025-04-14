package com.ptitB22CN539.QuizRemake.Repository;

import com.ptitB22CN539.QuizRemake.Entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, String> {
}
