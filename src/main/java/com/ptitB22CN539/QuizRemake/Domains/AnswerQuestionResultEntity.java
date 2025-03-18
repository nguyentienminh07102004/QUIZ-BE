package com.ptitB22CN539.QuizRemake.Domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "answerQuestionResult")
@Getter
@Setter
@NoArgsConstructor
public class AnswerQuestionResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @ManyToOne
    @JoinColumn(name = "answerId")
    private AnswerEntity answer;
    @ManyToOne
    @JoinColumn(name = "questionResultId")
    private QuestionResultEntity questionResult;

    public AnswerQuestionResultEntity(AnswerEntity answer, QuestionResultEntity questionResult) {
        this.answer = answer;
        this.questionResult = questionResult;
    }
}
