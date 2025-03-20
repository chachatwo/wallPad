package com.wallpad.project.service;

import java.util.Date;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wallpad.project.mapper.ApiMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

	private final ApiMapper apiMapper;
	@Autowired
	private JavaMailSender mailSender;

	@Value("${jwt.secret}")
	private String secretKey;

	// 이메일 전송 함수
	public void sendVerificationEmail(String toEmail, String token) {
		try {
			String subject = "이메일 인증";
			String text = "안녕하세요! 이메일 인증을 위해 아래 링크를 클릭하세요:\n" + "http://localhost:8080/verify-email?token=" + token;

			MimeMessageHelper message = new MimeMessageHelper(mailSender.createMimeMessage(), true);
			message.setFrom("wallpadtest@gmail.com");
			message.setTo(toEmail);
			message.setSubject(subject);
			message.setText(text);

			mailSender.send(message.getMimeMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// JWT 토큰 생성 함수
	public String generateToken(String userEmail) {
		return Jwts.builder().setSubject(userEmail).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000))
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	// 토큰 검증 함수
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean checkUserByEmail(String email) {
		int count = apiMapper.checkUserByEmail(email);
		return count > 0;
	}

	public void updateEmailVerified(String email) {
		apiMapper.updateEmailVerified(email);
	}

}
