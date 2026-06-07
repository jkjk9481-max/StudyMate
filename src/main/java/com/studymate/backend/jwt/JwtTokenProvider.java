package com.studymate.backend.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

    private final String secret; // properties에서 읽어온 원본 비밀 문자열
    private final long expiration; // 토큰 만료시간
    private final SecretKey secretKey; // JWT가 실제 서명에 사용할 Key객체


}
