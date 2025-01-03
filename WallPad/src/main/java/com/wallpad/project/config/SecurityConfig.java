//package com.wallpad.project.config;  // 실제 패키지에 맞게 수정하세요
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests()  // 권한 설정 시작
//                .antMatchers("/login", "/signup", "/css/**", "/js/**").permitAll()  // 해당 경로는 모두 접근 허용
//                .anyRequest().authenticated()  // 나머지 요청은 인증 필요
//            .and()
//            .formLogin()
//                .loginPage("/login")  // 로그인 페이지 경로
//                .permitAll()  // 로그인 페이지는 누구나 접근 가능
//            .and()
//            .logout()
//                .permitAll();  // 로그아웃도 누구나 접근 가능
//
//        return http.build();
//    }
//}