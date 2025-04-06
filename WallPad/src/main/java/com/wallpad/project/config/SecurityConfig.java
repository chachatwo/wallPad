package com.wallpad.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.wallpad.project.service.CustomUserDetailsService;

import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // AuthenticationManager 자동 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // CSRF 보호 비활성화
            .authorizeHttpRequests()
                .antMatchers("/signup", "/login", "/find-id", "/find-password", "/reset-password", "/check-username", "/check-email", "/verify-email", "/users/insert", "/css/**", "/js/**") // 접근 허용 경로
                .permitAll()  
                .anyRequest().authenticated() 
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) 
            .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(new CustomLoginSuccessHandler())
                .failureUrl("/login?error")
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/logout")  // 로그아웃 URL
                .logoutSuccessUrl("/login")  // 로그아웃 후 리디렉션 URL
                .clearAuthentication(true)  // 인증 정보 삭제
                .invalidateHttpSession(true)  // 세션 무효화
                .deleteCookies("JSESSIONID", "saveid", "remember-me")  // 쿠키 삭제
                .permitAll();

        return http.build();
    }
}
