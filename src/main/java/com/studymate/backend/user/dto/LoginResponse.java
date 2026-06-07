package com.studymate.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken; // 실제 JWT 문자열
    private String tokenType; // 보통 "Bearer"
    private Long userId; // 사용자 ID
    private String email; // 사용자 Email
    private String name; // 사용자 이름

}
