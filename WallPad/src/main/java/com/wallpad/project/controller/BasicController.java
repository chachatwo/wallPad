package com.wallpad.project.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wallpad.project.dto.MaintenanceScheduleDTO;
import com.wallpad.project.dto.NoticeDTO;
import com.wallpad.project.dto.ParkingReserveDTO;
import com.wallpad.project.service.ApiService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BasicController {
	private final ApiService apiService;

	@GetMapping("/signup")
	public String signup(Model model) {
		return "signup";
	}

	@GetMapping("/")
	public String dashboard(Model model) {

		return "dashboard";
	}

	@GetMapping("/login")
	public String Login(Model model) {
		return "login";
	}

	@GetMapping("/dashboard")
	public String dashboard2(Model model) {

		return "dashboard";
	}

	@GetMapping("/notices")
	public String getNotices(Model model) {
		List<NoticeDTO> notices = apiService.findAllNotices();
		model.addAttribute("notices", notices);

		return "notices";
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

		System.out.println("차번호 :" + carNumber);
		System.out.println("출입기간 :" + allowedPeriod);

		LocalDateTime currentDateTime = LocalDateTime.now();

		LocalDateTime reserveDateTime = currentDateTime.plusDays(allowedPeriod);

		// 타임존 서울 기준으로 변경
		ZonedDateTime zonedDateTime = reserveDateTime.atZone(ZoneId.of("Asia/Seoul"));

		Timestamp reserveTimestamp = Timestamp.from(zonedDateTime.toInstant());

		ParkingReserveDTO parkingReserveDTO = new ParkingReserveDTO();
		parkingReserveDTO.setCarNumber(carNumber);
		parkingReserveDTO.setAllowedPeriod(reserveTimestamp);

		System.out.println("dto 저장 카넘버:" + parkingReserveDTO.getCarNumber());
		System.out.println("dto 저장 일시:" + parkingReserveDTO.getAllowedPeriod());

		apiService.saveParkingReserve(parkingReserveDTO);

		redirectAttributes.addFlashAttribute("message", "예약이 완료되었습니다.");

		return "redirect:/parking";
	}

	@GetMapping("/parking")
	public String parking(Model model) {
		if (model.containsAttribute("message")) {
			String message = (String) model.getAttribute("message");
			model.addAttribute("message", message);
		}
		return "parking";

	}
}
