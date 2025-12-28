package com.jfintech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 1. Tắt chống giả mạo (quan trọng khi test API)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/register").permitAll() // 1. Cho phép truy cập không cần đăng nhập đến /register
                .anyRequest().authenticated() // 2. Tất cả mọi request đều phải đăng nhập
            )
            .httpBasic(withDefaults()); // 3. Dùng Basic Auth thay vì Form Login

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}