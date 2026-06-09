package com.studymate.backend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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

    // JWT가 우리 서버가 만든 정상 토큰인지 확인
    // 1.토큰 형식이 JWT 형식인가?
    // 2.우리 서버의 secretKey로 서명된 토큰인가?
    // 3.만료 시간이 지나지 않았는가?
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    // JWT를 열어서 subject 값을 꺼내는 메서드
    // subject에는 우리가 email을 넣었으므로 결과가 email이다
    public String getEmailFromToken(String token) {
        String email = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return email;
    }

    // JWT안에 넣어둔 "userId" claim 값을 꺼내서 Long으로 반환하는 메서드
    // 1.token 문자열을 받는다
    // 2.이 token이 JWT라고 보고 해석한다.
    // 3.JWT안의 payload를 꺼낸다
    // 4.payload 안에서 "userId"값을 찾는다
    // 그 값을 Long 타입으로 바꿔서 돌려준다
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser() // Claims는 JWT 안의 데이터 묶음 쉽게 말하면 payload이다 예를 들면 email , userId , 발급시간 , 만료시간이 들어있다
                .verifyWith(secretKey) // JWT를 만들때 사용한 서버의 비밀키 , JWT 검증할떄도 같은 키를 사용함
                .build()
                .parseSignedClaims(token) // 서명된 JWT를 해석한다 , 올바른지 확인한다
                .getPayload(); // JWT에서 실제 데이터 대부분을 꺼낸다 (header , payload , signature) 여기서 우리가 피요한건 payload 이 안에 userId가 들어있음

        // secretKey를 이용해서 JWT token을 검증하고 , 정상 토큰이면 payload 데이터를 꺼내서 , claims 변수에 저장한다

        Number userId = claims.get("userId" , Number.class);
        // claims안에서 userId 값을 꺼낸다 -> 숫자로 꺼낸다
        // Number -> Integer , Long 같은 숫자 타입을 넓게 받을 수 있는 타입
        
        return userId.longValue();
    }





}
