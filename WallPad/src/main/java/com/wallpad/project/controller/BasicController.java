package com.wallpad.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.wallpad.project.dto.MaintenanceScheduleDTO;
import com.wallpad.project.dto.NoticeDTO;
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
		System.out.println(schedules.get(0).getTitle());
		System.out.println(schedules.get(1).getTitle());
		System.out.println(schedules.get(0).getStartTime());
		System.out.println(schedules.get(1).getStartTime());
		model.addAttribute("schedules", schedules);

		return "schedule";
	}

}
