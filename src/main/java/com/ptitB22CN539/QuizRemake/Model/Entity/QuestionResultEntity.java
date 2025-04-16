package com.ptitB22CN539.QuizRemake.Model.Entity;

import com.ptitB22CN539.QuizRemake.Common.Enum.AnswerSelectedStatus;
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

import java.util.List;

@Entity
@Table(name = "questionResults")
@Getter
@Setter
public class QuestionResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @ManyToOne
    @JoinColumn(name = "questionId")
    private QuestionEntity question;
    @ManyToOne
    @JoinColumn(name = "testResultId")
    private TestResultEntity testResult;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private AnswerSelectedStatus status;

    @OneToMany(mappedBy = "questionResult", orphanRemoval = true)
    @Cascade(value = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private List<AnswerQuestionResultEntity> answers;
}
