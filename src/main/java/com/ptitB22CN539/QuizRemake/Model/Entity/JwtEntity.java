package com.ptitB22CN539.QuizRemake.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "jwtToken")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtEntity {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "expires")
    private Date expires;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userEmail", referencedColumnName = "email")
    private UserEntity user;
}