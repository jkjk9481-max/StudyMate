package com.studymate.backend.user.repository;


import com.studymate.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User , Long> {

    boolean existsByEmail(String email); // 이메일 중복 방지


}
