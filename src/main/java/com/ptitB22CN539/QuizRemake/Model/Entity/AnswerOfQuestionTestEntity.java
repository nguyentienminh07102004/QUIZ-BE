package com.ptitB22CN539.QuizRemake.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "answerOfQuestionTests")
@Getter
@Setter
public class AnswerOfQuestionTestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    @Column(name = "isCorrect")
    private Boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "questionId", referencedColumnName = "id")
    private QuestionTestEntity question;

    @OneToMany(mappedBy = "answer")
    private List<AnswerQuestionResultEntity> answerQuestionResults;
}
