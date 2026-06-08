package com.studymate.backend.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // 로그인 성공한 사용자의 정보를 이용해서 JWT 토큰 문자열을 만들어주는 클래스

    private final String secret; // properties에서 읽어온 원본 비밀 문자열
    private final long expiration; // 토큰 만료시간
    private final SecretKey secretKey; // JWT가 실제 서명에 사용할 Key객체

    public JwtTokenProvider(@Value("${jwt.secret}") String secret ,@Value("${jwt.expiration}") long expiration){
        this.secret = secret;
        this.expiration = expiration;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        // secret 문자열 -> 바이트 배열로 변환 -> JWT 서명에 사용할 SecretKey 객체로 변환
    }
    
    
    public String createToken(Long userId , String email){
        Date now = new Date(); // 현재 시간
        Date expiryDate = new Date(now.getTime() + expiration); // 현재시간에 만료시간을 더한다

        return Jwts.builder() // JWT 만들기 시작
                .subject(email) // JWT 주인 이메일을 주인으로 넣음
                .claim("userId" , userId) // JWT 안에 추가 정보를 넣는다 
                .issuedAt(now) // 토큰 발급 시간을 넣는다
                .expiration(expiryDate) // 토큰 만료시간을 넣는다
                .signWith(secretKey) // 토큰에 서명한다 , 이게 중요한데 누군가 토큰 내용을 몰래 바꾸면 서명이 맞기 않게 된다 서버는 나중에 이 서명을보고 이 토큰이 내가 만든 토큰이 맞는지 확인할 수 있어야한다
                .compact(); // JWT 문자열을 완성
    }





}
