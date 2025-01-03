package com.wallpad.project.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.wallpad.project.mapper.ApiMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

	private final ApiMapper apiMapper; // ApiMapper 의존성 주입 (final로 선언되어 생성자 주입)

	@Autowired
	private JavaMailSender mailSender; // 이메일 발송을 위한 JavaMailSender

	@Value("${jwt.secret}") // application.properties에서 'jwt.secret' 값을 읽어옵니다.
	private String secretKey; // 비밀 키를 외부 설정에서 가져옵니다.

	// 이메일 전송 함수
	public void sendVerificationEmail(String toEmail, String token) {
		try {
			String subject = "이메일 인증";
			String text = "안녕하세요! 이메일 인증을 위해 아래 링크를 클릭하세요:\n" + "http://localhost:8080/verify-email?token=" + token;

			MimeMessageHelper message = new MimeMessageHelper(mailSender.createMimeMessage(), true);
			message.setFrom("wallpadtest@gmail.com"); // 발신 이메일
			message.setTo(toEmail); // 수신 이메일
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
				.setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1시간 만료
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

	public void updateEmailVerified(String email) {
		apiMapper.updateEmailVerified(email);
	}

}
