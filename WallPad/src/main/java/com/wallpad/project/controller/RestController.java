package com.wallpad.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.wallpad.project.dto.MaintenanceScheduleDTO;
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

	@Value("${jwt.secret}")
	private String secretKey;

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

			String email = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();

			authService.updateEmailVerified(email);
			redirectView.setUrl("/login");
		} else {
			redirectView.setUrl("/error");
		}

		return redirectView;
	}

	@GetMapping("/api/schedules")
	public List<MaintenanceScheduleDTO> getSchedules() {
		List<MaintenanceScheduleDTO> schedules = apiService.maintenanceSchedules();
		return schedules;
	}

}
