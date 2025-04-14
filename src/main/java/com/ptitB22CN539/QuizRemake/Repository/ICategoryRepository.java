package com.ptitB22CN539.QuizRemake.Repository;

import com.ptitB22CN539.QuizRemake.Entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<CategoryEntity, String> {
}
