package com.wallpad.project.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.wallpad.project.dto.IdCheckDTO;
import com.wallpad.project.dto.SignUpDTO;
import com.wallpad.project.service.ApiService;
import com.wallpad.project.service.AuthService;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RestController
public class RestController {
	private final ApiService apiService;
	private final AuthService authService;

	@Value("${jwt.secret}") // application.properties에서 'jwt.secret' 값을 읽어옵니다.
	private String secretKey; // 비밀 키를 외부 설정에서 가져옵니다.

	@GetMapping("/check-username")
	public String checkUsername(@RequestParam String username) {
		boolean isAvailable = apiService.userNameCheck(username);

		if (isAvailable) {
			return "사용 가능한 아이디입니다.";
		} else {
			return "이미 사용 중인 아이디입니다.";
		}
	}

	@GetMapping("/check-email")
	public String checkEmail(@RequestParam String email) {
		boolean isAvailable = apiService.emailCheck(email);

		if (isAvailable) {
			return "사용 가능한 이메일입니다.";
		} else {
			return "이미 사용 중인 이메일입니다.";
		}
	}

	@PostMapping("/users/insert")
	public String signUp(@ModelAttribute SignUpDTO signUpDTO) {
		System.out.println(signUpDTO.getUsername());
		System.out.println(signUpDTO.getPassword());
		System.out.println(signUpDTO.getEmail());
		System.out.println(signUpDTO.getGender());
		System.out.println(signUpDTO.getPhone_num());
		System.out.println(signUpDTO.getName());

		apiService.saveUserData(signUpDTO);

		String token = authService.generateToken(signUpDTO.getEmail());
		authService.sendVerificationEmail(signUpDTO.getEmail(), token);

		return "이메일을 확인하여 인증 링크를 클릭하세요.";
	}

	@GetMapping("/verify-email")
	public RedirectView verifyEmail(@RequestParam String token) {
		RedirectView redirectView = new RedirectView();

		// 토큰 유효성 검증
		if (authService.validateToken(token)) {

			String email = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject(); // 이메일을
																												// subject로
																												// 설정한
																												// 부분

			authService.updateEmailVerified(email);
			// 이메일 인증 성공 후 로그인 페이지로 리다이렉트
			redirectView.setUrl("/login");
		} else {
			// 인증 실패 시, 오류 메시지를 반환하거나, 다시 인증을 요청하도록 안내
			redirectView.setUrl("/error"); // 잘못된 링크일 경우 오류 페이지로 이동
		}

		return redirectView;
	}
}
