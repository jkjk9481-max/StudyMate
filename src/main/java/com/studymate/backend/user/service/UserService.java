package com.studymate.backend.user.service;

import com.studymate.backend.user.dto.UserCreateRequest;
import com.studymate.backend.user.dto.UserResponse;
import com.studymate.backend.user.entity.User;
import com.studymate.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserCreateRequest request) {
        boolean existsEmail = userRepository.existsByEmail(request.getEmail());

        if (existsEmail) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = new User(request.getName(), request.getEmail());
        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser);
    }
}
