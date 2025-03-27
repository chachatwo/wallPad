package com.wallpad.project.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wallpad.project.dto.ResetTokenDTO;
import com.wallpad.project.dto.UserDTO;
import com.wallpad.project.mapper.ApiMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

	private final ApiMapper apiMapper;
	private final BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private JavaMailSender mailSender;

	@Value("${jwt.secret}")
	private String secretKey;

	public boolean authenticate(String username, String password) {
		UserDTO user = apiMapper.findUserByUsername(username);

		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			return true;
		}
		return false;
	}

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

	public String generateToken(String userEmail) {
		return Jwts.builder().setSubject(userEmail).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000))
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

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

	public String findUsernameByEmail(String email) {
		UserDTO user = apiMapper.findByEmail(email);
		return (user != null) ? user.getUsername() : null;
	}

	public boolean sendResetLink(String username, String email) {
		ResetTokenDTO resetTokenDTO = new ResetTokenDTO();
		resetTokenDTO.setUsername(username);
		resetTokenDTO.setEmail(email);

		UserDTO user = apiMapper.findByUsernameAndEmail(resetTokenDTO);
		if (user == null)
			return false;

		String token = UUID.randomUUID().toString();
		resetTokenDTO.setResetToken(token);
		resetTokenDTO.setExpireAt(LocalDateTime.now().plusMinutes(10));
		apiMapper.updateResetToken(resetTokenDTO);

		String link = "http://localhost:8080/reset-password?token=" + token;

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(email);
			helper.setSubject("[WallPad] 비밀번호 재설정 링크");

			String content = """
					    <p>안녕하세요.</p>
					    <p>아래 링크를 클릭해 비밀번호를 재설정하세요.</p>
					    <p><a href="%s">비밀번호 재설정하기</a></p>
					    <p style="color:gray;">※ 이 링크는 10분간 유효합니다.</p>
					""".formatted(link);

			helper.setText(content, true); 
			helper.setFrom("wallpadtest@gmail.com");

			mailSender.send(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public UserDTO findByResetToken(String token) {
		UserDTO user = apiMapper.findByResetToken(token);

		if (user == null || user.getExpireAt() == null) {
			return null;
		}

		LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
		if (user.getExpireAt().isBefore(now)) {
			return null;
		}

		return user;
	}

	public void resetPassword(int userId, String newPassword) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(userId);

		userDTO.setPassword(passwordEncoder.encode(newPassword));

		apiMapper.updatePassword(userDTO);
		apiMapper.clearResetToken(userId);
	}

}
