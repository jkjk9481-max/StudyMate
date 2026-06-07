package com.studymate.backend.user.repository;


import com.studymate.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User , Long> {

    boolean existsByEmail(String email); // 이메일 중복 방지

    Optional<User> findByEmail(String email); // 이메일 조회


}
