package com.ptitB22CN539.QuizRemake.JpaRepository;

import com.ptitB22CN539.QuizRemake.Model.Entity.CategoryEntity;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<CategoryEntity, String> {
    Window<CategoryEntity> findTop10ByOrderByCodeAsc(ScrollPosition scrollPosition);
}
