package com.ptitB22CN539.QuizRemake.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class CategoryEntity {
    @Id
    @Column(name = "code", unique = true)
    private String code;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "category")
    private List<TestEntity> tests;

    @OneToMany(mappedBy = "category")
    private List<QuestionEntity> questions;
}