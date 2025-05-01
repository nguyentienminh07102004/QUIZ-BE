package com.ptitB22CN539.QuizRemake.Model.Entity;

import com.ptitB22CN539.QuizRemake.Common.Enum.Difficulty;
import com.ptitB22CN539.QuizRemake.Common.Enum.TestStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @OneToMany(mappedBy = "test", orphanRemoval = true)
    @Cascade(value = {CascadeType.REMOVE})
    private List<TestResultEntity> testResults;

    @OneToMany(mappedBy = "test")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Cascade(value = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE})
    private List<QuestionTestEntity> questions;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoryCode", referencedColumnName = "code")
    private CategoryEntity category;
}