package com.wallpad.project.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.wallpad.project.dto.EntryCarDTO;
import com.wallpad.project.dto.MaintenanceScheduleDTO;
import com.wallpad.project.dto.ParkingReserveDTO;
import com.wallpad.project.dto.RepairRequestDTO;
import com.wallpad.project.dto.ReserveStatesDTO;
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

//	@Value("${upload.dir}")
//	private String uploadDir;

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
	public String signUp(@ModelAttribute SignUpDTO signUpDTO, HttpSession session) {
		session.setAttribute("signUpDTO", signUpDTO);

		String token = authService.generateToken(signUpDTO.getEmail());
		authService.sendVerificationEmail(signUpDTO.getEmail(), token);

		return "이메일을 확인하여 인증 링크를 클릭하세요.";
	}

	@GetMapping("/verify-email")
	public RedirectView verifyEmail(@RequestParam String token, HttpSession session) {
		RedirectView redirectView = new RedirectView();

		if (authService.validateToken(token)) {

			// 이메일 추출
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

	@GetMapping("/api/schedules")
	public List<MaintenanceScheduleDTO> getSchedules() {
		List<MaintenanceScheduleDTO> schedules = apiService.maintenanceSchedules();
		return schedules;
	}

	@PostMapping("/api/repair")
	public void submitRequest(@RequestParam("majorCategory") String majorCategory,
			@RequestParam("middleCategory") String middleCategory, @RequestParam("lastCategory") String lastCategory,
			@RequestParam("request") String request,
			@RequestParam(value = "imageUpload[]", required = false) MultipartFile[] imageUploads,
			HttpServletRequest requestObj, HttpServletResponse response) throws IOException {

		// 1. saveid 쿠키 추출
		String savedId = null;
		Cookie[] cookies = requestObj.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("saveid".equals(cookie.getName())) {
					savedId = cookie.getValue();
					break;
				}
			}
		}
		
	    // 2. 동호수 조회
	    String apartmentNumber = null;
	    if (savedId != null) {
	        apartmentNumber = apiService.findApartmentNumberBySavedId(savedId);
	    }
		
	    // 3. 요청 DTO 생성 및 세팅
	    RepairRequestDTO repairRequestDTO = new RepairRequestDTO();
	    repairRequestDTO.setApartmentNumber(apartmentNumber);
	    repairRequestDTO.setMajorCategory(majorCategory);
	    repairRequestDTO.setMiddleCategory(middleCategory);
	    repairRequestDTO.setLastCategory(lastCategory);
	    repairRequestDTO.setRequest(request);
	    
	    // 4. 저장
		apiService.saveRepairRequest(repairRequestDTO, imageUploads);

		response.sendRedirect("/repair");
	}

	@PostMapping("/api/parking/reserve/states")
	public List<ReserveStatesDTO> reservation() {
		List<ReserveStatesDTO> reservationList = apiService.reserveStates();

		if (reservationList.isEmpty()) {
			System.out.println("예약 목록이 비어 있습니다.");
		} else {
			for (int i = 0; i < reservationList.size(); i++) {
				System.out.println("카 넘버는 :" + reservationList.get(i).getCarNumber());
				System.out.println("출입 기간은 :" + reservationList.get(i).getAllowedPeriod());
			}
		}

		return reservationList;
	}

	@PostMapping("/api/register/car")
	@ResponseBody
	public EntryCarDTO registerEntryCar(@RequestBody EntryCarDTO entryCarDTO) {
		EntryCarDTO result = apiService.registerEntryCar(entryCarDTO);

		if (result != null) {
			return result;
		} else {
			return null;
		}
	}

}