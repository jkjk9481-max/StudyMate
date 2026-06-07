package com.studymate.backend.user.service;


import com.studymate.backend.user.dto.UserLoginRequest;
import com.studymate.backend.user.dto.UserResponse;
import com.studymate.backend.user.dto.UserSignUpRequest;
import com.studymate.backend.user.entity.User;
import com.studymate.backend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository ,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse signUpUser(UserSignUpRequest request) {
        boolean existsEmail = userRepository.existsByEmail(request.getEmail());

        if (existsEmail) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String rawPassword = request.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword); // DB에 저장할 암호화된 비밀번호


        User user = new User(request.getName(), request.getEmail() , encodedPassword);
        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser);
    }

    public UserResponse loginUser(UserLoginRequest request) {
        User user =  userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("올바른 이메일이 아닙니다"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("올바른 비밀번호가 아닙니다");
        }

        return new UserResponse(user);

    }
}
