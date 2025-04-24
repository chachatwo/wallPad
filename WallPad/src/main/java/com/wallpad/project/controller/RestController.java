package com.wallpad.project.controller;

import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import com.wallpad.project.dto.*;
import com.wallpad.project.service.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.Jwts;

@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RestController
public class RestController {

	private final ApiService apiService;
	private final AuthService authService;
	private final NotificationService notificationService;

	@Value("${jwt.secret}")
	private String secretKey;

	@Operation(summary = "아이디 중복 확인", description = "입력된 아이디가 이미 존재하는지 확인합니다.")
	@GetMapping("/check-username")
	public String checkUsername(@Parameter(description = "확인할 아이디") @RequestParam String username) {
		boolean isAvailable = apiService.userNameCheck(username);
		return isAvailable ? "사용 가능한 아이디입니다." : "이미 사용 중인 아이디입니다.";
	}

	@Operation(summary = "이메일 중복 확인", description = "입력된 이메일이 이미 존재하는지 확인합니다.")
	@GetMapping("/check-email")
	public String checkEmail(@Parameter(description = "확인할 이메일") @RequestParam String email) {
		boolean isAvailable = apiService.emailCheck(email);
		return isAvailable ? "사용 가능한 이메일입니다." : "이미 사용 중인 이메일입니다.";
	}

	@Operation(summary = "회원가입 처리", description = "입력한 회원 정보를 세션에 저장하고 이메일 인증 링크를 발송합니다.")
	@PostMapping("/users/insert")
	public String signUp(@ModelAttribute SignUpDTO signUpDTO, HttpSession session) {
		session.setAttribute("signUpDTO", signUpDTO);
		String token = authService.generateToken(signUpDTO.getEmail());
		authService.sendVerificationEmail(signUpDTO.getEmail(), token);
		return "이메일을 확인하여 인증 링크를 클릭하세요.";
	}

	@Operation(summary = "이메일 인증 처리", description = "전달받은 이메일 토큰을 검증하고 회원가입을 완료합니다.")
	@GetMapping("/verify-email")
	public RedirectView verifyEmail(@Parameter(description = "이메일 인증 토큰") @RequestParam String token,
			HttpSession session) {
		RedirectView redirectView = new RedirectView();
		if (authService.validateToken(token)) {
			String email = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
			if (!authService.checkUserByEmail(email)) {
				SignUpDTO signUpDTO = (SignUpDTO) session.getAttribute("signUpDTO");
				if (signUpDTO != null && signUpDTO.getEmail().equals(email)) {
					apiService.saveUserData(signUpDTO);
					authService.updateEmailVerified(email);
					session.removeAttribute("signUpDTO");
					redirectView.setUrl("/login");
				} else {
					redirectView.setUrl("/error");
				}
			} else {
				redirectView.setUrl("/error");
			}
		} else {
			redirectView.setUrl("/error");
		}
		return redirectView;
	}

	@Operation(summary = "유지보수 일정 전체 조회", description = "전체 유지보수 일정을 조회합니다.")
	@GetMapping("/api/schedules")
	public List<MaintenanceScheduleDTO> getSchedules() {
		return apiService.maintenanceSchedules();
	}

	@Operation(summary = "아파트 차량 예약 상태 조회", description = "세션 사용자 기반으로 예약된 차량 상태를 조회합니다.")
	@PostMapping("/api/parking/reserve/states")
	public List<ReserveStatesDTO> reservation(HttpSession session) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String apartmentNumber = apiService.findApartmentNumberByUsername(username);
		return apiService.reserveStatesByApartment(apartmentNumber);
	}

	@Operation(summary = "입차 차량 등록", description = "차량번호 기반으로 예약 여부를 확인하고 입차 등록합니다.")
	@PostMapping("/api/register/car")
	public EntryCarDTO registerEntryCar(@RequestBody EntryCarDTO entryCarDTO) {
		EntryCarDTO reservationInfo = apiService.findReservedCar(entryCarDTO.getCarNumber());

		if (reservationInfo != null) {
			entryCarDTO.setApartmentNumber(reservationInfo.getApartmentNumber());
			apiService.insertEntryCar(entryCarDTO);

			NotificationDTO notification = new NotificationDTO();
			notification.setApartmentNumber(reservationInfo.getApartmentNumber());
			notification.setMessage(entryCarDTO.getCarNumber() + " 차량이 입차하였습니다.");
			notification.setRead(false); 
			notificationService.saveNotification(notification);

			return entryCarDTO;
		}

		return null;
	}

}