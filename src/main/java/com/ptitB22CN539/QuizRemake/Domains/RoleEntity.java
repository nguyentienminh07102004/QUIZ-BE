package com.ptitB22CN539.QuizRemake.Domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity {
    @Column(name = "name")
    private String name;
    @Id
    @Column(name = "code")
    private String code;

    @OneToMany(mappedBy = "role", targetEntity = UserEntity.class)
    @JsonIgnore
    private List<UserEntity> users;

    public RoleEntity(String name, String code) {
        this.name = name;
        this.code = code;
    }
}