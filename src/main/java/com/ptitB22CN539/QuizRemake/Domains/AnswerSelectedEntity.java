package com.ptitB22CN539.QuizRemake.Domains;

import com.ptitB22CN539.QuizRemake.BeanApp.AnswerSelectedStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "answerSelecteds")
@Getter
@Setter
public class AnswerSelectedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @ManyToOne
    @JoinColumn(name = "questionId")
    private QuestionEntity question;
    @ManyToOne
    @JoinColumn(name = "answerId")
    private AnswerEntity answer;
    @ManyToOne
    @JoinColumn(name = "test_resultId")
    private TestResultEntity testResult;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private AnswerSelectedStatus status;
}
