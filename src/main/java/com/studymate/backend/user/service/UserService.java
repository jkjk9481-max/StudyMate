package com.studymate.backend.user.service;


import com.studymate.backend.jwt.JwtTokenProvider;
import com.studymate.backend.user.dto.LoginResponse;
import com.studymate.backend.user.dto.UserLoginRequest;
import com.studymate.backend.user.dto.UserResponse;
import com.studymate.backend.user.dto.UserSignUpRequest;
import com.studymate.backend.user.entity.User;
import com.studymate.backend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository ,  PasswordEncoder passwordEncoder ,  JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
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

    // 1.사용자가 로그인 요청을 보냄
    // 2.이메일로 DB에서 User 찾음
    // 3.비밀번호가 맞는지 확인함
    // 4.맞으면 JWT 토큰 생성
    // 5.LoginResponse에 토큰과 사용자 정보를 담아서 반환
    public LoginResponse loginUser(UserLoginRequest request) {
        User user =  userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("올바른 이메일이 아닙니다"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("올바른 비밀번호가 아닙니다");
        }

        String accessToken = jwtTokenProvider.createToken(user.getId()  , user.getEmail());
        // 로그인 성공한 사용자의 id와 email을 이용해서 JWT 토큰 문자열을 만든다

        return new LoginResponse(
                accessToken , // JWT 토큰 문자열
                "Bearer" , // 토큰 타입
                user.getId() ,
                user.getEmail() ,
                user.getName()
        );
    }


}
