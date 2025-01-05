package com.wallpad.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.wallpad.project.dto.SignUpDTO;
import com.wallpad.project.service.ApiService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BasicController {

	@GetMapping("/signup")
	public String signup() {

		return "signup.html";
	}

	@GetMapping("/")
	public String dashboard() {

		return "dashboard.html";
	}

	@GetMapping("/login")
	public String login() {

		return "login.html";
	}

	@GetMapping("/dashboard")
	public String dashboard2() {

		return "dashboard.html";
	}
	
	@GetMapping("/notices")
	public String notices() {

		return "notices.html";
	}
}
