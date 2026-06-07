package com.studymate.backend.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private LocalDateTime createdAt; // 생성된 일시
    private LocalDateTime updatedAt; // 수정된 일시

    public User(String name , String email){
        this.name = name;
        this.email = email;
    }

    @PrePersist // DB에 처음 저장되기 직전에 실행
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate // DB에서 수정되기 직전에 실행
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public User(String name , String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }


}
