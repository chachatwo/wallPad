package com.wallpad.project.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wallpad.project.dto.EntryCarDTO;
import com.wallpad.project.dto.MaintenanceScheduleDTO;
import com.wallpad.project.dto.NoticeDTO;
import com.wallpad.project.dto.ParkingReserveDTO;
import com.wallpad.project.dto.RepairRequestDTO;
import com.wallpad.project.dto.ReserveStatesDTO;
import com.wallpad.project.dto.UserDTO;
import com.wallpad.project.service.ApiService;
import com.wallpad.project.service.AuthService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BasicController {
	private final ApiService apiService;
	private final AuthService authService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/signup")
	public String signup(Model model) {
		return "signup";
	}

	@GetMapping("/login")
	public String loginPage(HttpServletRequest request, Model model) {
		String savedId = null;

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("saveid".equals(cookie.getName())) {
					savedId = cookie.getValue();
					break;
				}
			}
		}

		model.addAttribute("savedId", savedId);
		model.addAttribute("rememberMeChecked", savedId != null);

		return "login";
	}

	@PostMapping("/login")
	public String login(HttpServletResponse response, @RequestParam String username, @RequestParam String password,
			@RequestParam(required = false) boolean rememberMe, HttpSession session) {

		boolean isValid = authService.authenticate(username, password);

		if (isValid) {
			session.setAttribute("username", username);

			if (rememberMe) {
				Cookie rememberCookie = new Cookie("saveid", username);
				rememberCookie.setMaxAge(30 * 24 * 60 * 60);
				rememberCookie.setPath("/");
				rememberCookie.setHttpOnly(true);
				rememberCookie.setSecure(false);
				response.addCookie(rememberCookie);
			}

			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // 사용자 권한 부여

			Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			return "redirect:/dashboard";
		} else {
			System.out.println("로그인 실패: 아이디 또는 비밀번호 오류");
			return "redirect:/login?error";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpServletResponse response, HttpSession session) {
		Cookie saveidCookie = new Cookie("saveid", null);
		saveidCookie.setMaxAge(0);
		saveidCookie.setPath("/");
		saveidCookie.setHttpOnly(true);
		saveidCookie.setSecure(false);
		saveidCookie.setDomain("localhost");
		response.addCookie(saveidCookie);

		session.invalidate();

		SecurityContextHolder.clearContext();

		return "redirect:/login";
	}

	@GetMapping("/find-id")
	public String showFindIdPage() {
		return "find-id";
	}

	@PostMapping("/find-id")
	public String findId(@RequestParam("email") String email, Model model) {
		String username = authService.findUsernameByEmail(email);
		if (username != null) {
			model.addAttribute("foundUsername", username);
		} else {
			model.addAttribute("error", "해당 이메일로 등록된 아이디가 없습니다.");
		}
		return "find-id-result";
	}

	@GetMapping("/find-password")
	public String showFindForm() {
		return "find-password";
	}

	@PostMapping("/find-password")
	public String sendResetLink(@RequestParam String username, @RequestParam String email, Model model) {
		boolean sent = authService.sendResetLink(username, email);
		if (sent) {
			model.addAttribute("message", "비밀번호 재설정 링크가 이메일로 전송되었습니다.");
		} else {
			model.addAttribute("error", "일치하는 계정이 없습니다.");
		}
		return "find-password-result";
	}

	@GetMapping("/reset-password")
	public String showResetForm(@RequestParam String token, Model model) {
		UserDTO user = authService.findByResetToken(token);
		if (user == null) {
			model.addAttribute("error", "링크가 유효하지 않거나 만료되었습니다.");
			return "reset-password-error";
		}
		model.addAttribute("token", token);
		return "reset-password";
	}

	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam String token, @RequestParam String newPassword, Model model) {
		UserDTO user = authService.findByResetToken(token);
		if (user == null) {
			model.addAttribute("error", "링크가 유효하지 않거나 만료되었습니다.");
			return "reset-password-error";
		}
		authService.resetPassword(user.getId(), newPassword);
		return "redirect:/login";
	}

	@GetMapping("/notices")
	public String getNotices(Model model) {
		List<NoticeDTO> notices = apiService.findAllNotices();
		model.addAttribute("notices", notices);

		return "notices";
	}

	@GetMapping("/dashboard")
	public String getdashboard(Model model) {
		List<NoticeDTO> notices = apiService.findRecentNotices();

		if (notices.size() > 3) {
			notices = notices.subList(0, 3);
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		for (NoticeDTO notice : notices) {
			String formattedDate = notice.getCreatedAt().format(formatter);
			notice.setFormattedCreatedAt(formattedDate);
		}

		model.addAttribute("notices", notices);

		List<MaintenanceScheduleDTO> schedules = apiService.maintenanceSchedules();

		if (schedules.size() > 3) {
			schedules = schedules.subList(0, 3);
		}

		model.addAttribute("schedules", schedules);

		List<RepairRequestDTO> repair = apiService.findRepairRequest();

		if (repair.size() > 3) {
			repair = repair.subList(0, 3);
		}

		model.addAttribute("repair", repair);

		List<ReserveStatesDTO> reserve = apiService.reserveStates();

		if (reserve.size() > 3) {
			reserve = reserve.subList(0, 3);
		}

		model.addAttribute("reserve", reserve);

		return "dashboard";
	}

	@GetMapping("/schedule")
	public String schedule(Model model) {

		List<MaintenanceScheduleDTO> schedules = apiService.maintenanceSchedules();
		model.addAttribute("schedules", schedules);

		return "schedule";
	}

	@GetMapping("/repair")
	public String repair(Model model) {

		return "repair";
	}

	@PostMapping("/api/parking/reserve")
	public String parkingReserve(@RequestParam("carNumber") String carNumber,
			@RequestParam("allowedPeriod") int allowedPeriod, RedirectAttributes redirectAttributes) {

		System.out.println("차량번호 : " + carNumber);
		System.out.println("출입기간 : " + allowedPeriod);

		LocalDateTime currentDateTime = LocalDateTime.now();

		LocalDateTime reserveDateTime = currentDateTime.plusDays(allowedPeriod);

		ZonedDateTime zonedDateTime = reserveDateTime.atZone(ZoneId.of("Asia/Seoul"));
		Timestamp reserveTimestamp = Timestamp.from(zonedDateTime.toInstant());

		ParkingReserveDTO parkingReserveDTO = new ParkingReserveDTO();
		parkingReserveDTO.setCarNumber(carNumber);
		parkingReserveDTO.setAllowedPeriod(reserveTimestamp);

		ParkingReserveDTO existingReservation = apiService.findByCarNumber(carNumber);

		if (existingReservation != null) {
			existingReservation.setAllowedPeriod(reserveTimestamp);
			apiService.updateParkingReserve(existingReservation);
			redirectAttributes.addFlashAttribute("message", "차량의 출입기간이 갱신되었습니다.");
		} else {
			System.out.println("새 예약을 추가합니다.");
			apiService.saveParkingReserve(parkingReserveDTO);
			redirectAttributes.addFlashAttribute("message", "차량의 예약이 완료되었습니다.");
		}

		return "redirect:/parking";
	}

	@GetMapping("/parking")
	public String parking(Model model) {
		if (model.containsAttribute("message")) {
			String message = (String) model.getAttribute("message");
			model.addAttribute("message", message);
		}

		List<EntryCarDTO> parkingList = apiService.parkingStates();

		model.addAttribute("parking", parkingList);

		return "parking";
	}

	@GetMapping("/entryCarTest")
	public String entryCarTest(Model model) {
		return "entryCarTest";
	}

}
