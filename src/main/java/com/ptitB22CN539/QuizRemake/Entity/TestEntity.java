package com.ptitB22CN539.QuizRemake.Entity;

import com.ptitB22CN539.QuizRemake.Common.BeanApp.Difficulty;
import com.ptitB22CN539.QuizRemake.Common.BeanApp.TestStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;

@Entity
@Table(name = "tests")
@Getter
@Setter
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "difficulty")
    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private TestStatus status;

    @OneToMany(mappedBy = "test", orphanRemoval = true)
    @Cascade(value = {CascadeType.REMOVE})
    private List<TestRatingEntity> testRatings;

    @ManyToMany
    @JoinTable(name = "questionTest",
            joinColumns = @JoinColumn(name = "testId"),
            inverseJoinColumns = @JoinColumn(name = "questionId"))
    private List<QuestionEntity> questions;

    @OneToMany(mappedBy = "test", orphanRemoval = true)
    @Cascade(value = {CascadeType.REMOVE})
    private List<TestResultEntity> testResults;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoryCode", referencedColumnName = "code")
    private CategoryEntity category;
}