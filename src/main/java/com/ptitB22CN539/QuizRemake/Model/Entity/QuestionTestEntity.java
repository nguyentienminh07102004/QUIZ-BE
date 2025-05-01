package com.ptitB22CN539.QuizRemake.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "questionTests")
@Getter
@Setter
public class QuestionTestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column()
    private String id;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String shortDescription;
    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "question", orphanRemoval = true)
    @Cascade(value = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<AnswerOfQuestionTestEntity> answers;

    @ManyToOne
    @JoinColumn(name = "testId")
    private TestEntity test;

    @OneToMany(mappedBy = "question")
    private List<QuestionResultEntity> answerSelecteds;

    @ManyToOne
    @JoinColumn(name = "categoryCode", referencedColumnName = "code")
    private CategoryEntity category;
}