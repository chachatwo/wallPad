package com.wallpad.project.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

	@GetMapping("/login")
	public String Login(Model model) {
		return "login";
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
