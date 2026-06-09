package com.studymate.backend.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
// 요청 들어옴 -> Authorization 헤더 확인 -> Bearer 토큰인지 확인 -> 토큰 문자열만 꺼냄 -> jwtTokenProvider.validateToken(token) -> 토큰에서 email, userId 추출 -> Spring SecurityContext에 인증 정보 저장 -> Controller로 요청 넘김
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // Controller에 도착하기 전에 JWT 토큰을 확인해서 , 정상토큰이면 Spring Security에게 "이 사용자는 로그인한 사용자다"라고 알려주는 클래스
    // 요청 한 번당 이 필터를 한 번만 실행하겠다

    private final JwtTokenProvider jwtTokenProvider;


    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    // HttpServletRequest -> 클라이언트가 보낸 요청정보(URL , 헤더 , 파라미터)등을 꺼낼수있음 , HttpServletResponse -> 서버가 클라이언트에게 보낼 응답 정보 ,  FilterChain filterChain -> 다음 필터 또는 Controller로 요청을 넘기기 위한 객체
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Authorization 헤더 꺼내기 -> 클라이언트가 보낸 요청 헤더에서 Authorization 값을 꺼낸다
        String authorization = request.getHeader("Authorization");

        // 2. 헤더가 null이 아니고 "Bearer "로 시작하는지 확인
        if(authorization != null && authorization.startsWith("Bearer ")) {
            // 3. "Bearer " 뒤의 실제 토큰만 자르기
            String token = authorization.substring(7); //Bearer = 6글자 , 공백 = 1글자  , 총 = 7글자


            // 4. jwtTokenProvider.validateToken(token)으로 검증
            if(jwtTokenProvider.validateToken(token)) {
                // 5. 검증 성공하면 email 꺼내기
                String email = jwtTokenProvider.getEmailFromToken(token);

                // 6. Authentication 객체 만들기
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email , null , Collections.emptyList());

                // 7. SecurityContextHolder에 저장하기
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // 8. 다음 필터로 넘기기
        filterChain.doFilter(request, response);
    }
}
