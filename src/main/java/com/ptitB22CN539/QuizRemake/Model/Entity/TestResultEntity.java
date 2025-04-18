package com.ptitB22CN539.QuizRemake.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "testResults")
@Getter
@Setter
public class TestResultEntity {
    @Id
    @Column(name = "id")
    private String id;
    @ManyToOne
    @JoinColumn(name = "testId")
    private TestEntity test;
    @Column(name = "score")
    private Integer score;
    @Column(name = "startedDate")
    private Date startedDate;
    @Column(name = "finishDate")
    private Date finishDate;
    @ManyToOne
    @JoinColumn(name = "userEmail", referencedColumnName = "email")
    private UserEntity user;
    @OneToMany(mappedBy = "testResult", orphanRemoval = true)
    @Cascade(value = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<QuestionResultEntity> answerSelecteds;
}
