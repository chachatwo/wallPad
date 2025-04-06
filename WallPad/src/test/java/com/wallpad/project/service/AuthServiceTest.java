package com.wallpad.project.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;


import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private JavaMailSender javaMailSender;

	@InjectMocks
	private AuthService authService;

	@Test
	void 이메일_전송_테스트() {
		String email = "dnjscjf0104@naver.com";
		String token = "testtesttest";

		MimeMessage message = mock(MimeMessage.class);
		when(javaMailSender.createMimeMessage()).thenReturn(message);

		authService.sendVerificationEmail(email, token);

		verify(javaMailSender, times(1)).send(message);
	}
}
