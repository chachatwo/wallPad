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

	private final ApiService apiService;
	
	@GetMapping("/signup")
	public String signup() {

		return "signup.html";
	}

	@GetMapping("/")
	public String main() {

		return "main.html";
	}

	@GetMapping("/login")
	public String login() {

		return "login.html";
	}

	@GetMapping("/main")
	public String main2() {

		return "main.html";
	}

	@GetMapping("/logout")
	public String logout() {

		return "/";
	}
}



//	@PostMapping("/user/login") 
//	public String loginCheck(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpSession session) {
//		
//		String id = httpServletRequest.getParameter("id");
//		String pw = httpServletRequest.getParameter("pw");
//		String rememberId = httpServletRequest.getParameter("remember");
//		
//		if(id.equals("test") && pw.equals("test")) {
//			
//			session.setAttribute("userId", id);
//			
//			if(rememberId != null) {
//				if(rememberId.equals("true")) {
//					Cookie cookie = new Cookie("id", id);
//					cookie.setMaxAge(3600);
//					httpServletResponse.addCookie(cookie);
////					httpServletResponse.setHeader("Set-Cookie", "id=" + id + "; Max-age=3600;");
//				}
//				else {
//					Cookie cookie = new Cookie("id", id);
//					cookie.setMaxAge(0);
//					httpServletResponse.addCookie(cookie);
//				}
//			}
//
//			
//			return "redirect:/main";
//		}
//		return "redirect:/";
//	}
