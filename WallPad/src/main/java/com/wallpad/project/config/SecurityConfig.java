package com.wallpad.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private CustomUserDetailsService customUserDetailsService;  

    @Autowired
    private PasswordEncoder passwordEncoder;  


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // CSRF 보호 비활성화
            .authorizeHttpRequests()
                .antMatchers("/signup", "/login", "/check-username", "/check-email", "/verify-email", "/users/insert", "/css/**", "/js/**") // 접근 허용 경로
                .permitAll()  
                .anyRequest().authenticated() 
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 세션 조건부로 생성
            .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error")
                .permitAll()
            .and()
            .rememberMe()  
                .key("uniqueAndSecret")  
                .userDetailsService(userDetailsService)  
            .and()
            .logout()
                .logoutSuccessUrl("/login")
                .permitAll();

        return http.build();
    }
}
