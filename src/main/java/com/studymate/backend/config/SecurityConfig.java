package com.studymate.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // 요쳥이 들어왔을떄 어떤 주소는 허용하고 , 어떤 주소는 막을지 정하는 보안 규칙
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/signup", "/api/users/login").permitAll()
                        .anyRequest().authenticated() // 위에서 허용한 주소 말고 나머지 모든 요청은 인증된 사용자만 가능
                );

        return http.build(); // 작성한 보안 규칙을 실제 Spring Security 설정 객체로 만들어서 반환하는 코드
    }
}
