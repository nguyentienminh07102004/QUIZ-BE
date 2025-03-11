package com.ptitB22CN539.QuizRemake.Domains;

import com.ptitB22CN539.QuizRemake.BeanApp.TestResultStatus;
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

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "test_results")
@Getter
@Setter
public class TestResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @ManyToOne
    @JoinColumn(name = "test_id")
    private TestEntity test;
    @Column(name = "score")
    private Integer score;
    @Column(name = "started_date")
    private Date startedDate;
    @Column(name = "finish_date")
    private Date finishDate;
    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private UserEntity user;
    @OneToMany(mappedBy = "testResult", orphanRemoval = true)
    @Cascade(value = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<AnswerSelectedEntity> answerSelecteds;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private TestResultStatus status;
}
