package com.ptitB22CN539.QuizRemake.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Checks;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "testRatings")
@Getter
@Setter
@Checks(value = {
        @Check(name = "rating", constraints = "rating >= 0")
})
@EntityListeners(value = AuditingEntityListener.class)
public class TestRatingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @ManyToOne
    @JoinColumn(name = "testId")
    private TestEntity test;
    @ManyToOne
    @JoinColumn(name = "userEmail", referencedColumnName = "email")
    private UserEntity user;
    @Column(name = "rating")
    private Double rating;

    @Column(name = "createdDate", columnDefinition = "TIMESTAMP")
    @CreatedDate
    private Date createdDate;
}
