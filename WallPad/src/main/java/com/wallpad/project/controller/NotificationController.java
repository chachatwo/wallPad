package com.wallpad.project.controller;

import com.wallpad.project.dto.NotificationDTO;
import com.wallpad.project.service.ApiService;
import com.wallpad.project.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;
	private final ApiService apiService;

	@Operation(summary = "읽지 않은 알림 목록", description = "현재 사용자의 읽지 않은 알림 목록을 반환합니다.")
	@GetMapping("/api/notifications/unread")
	public List<NotificationDTO> getUnreadNotifications() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String apartmentNumber = apiService.findApartmentNumberByUsername(username);
		return notificationService.getUnreadNotifications(apartmentNumber);
	}

	@Operation(summary = "읽지 않은 알림 개수", description = "현재 사용자의 읽지 않은 알림의 개수를 반환합니다.")
	@GetMapping("/api/notifications/unread-count")
	public int getUnreadCount() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String apartmentNumber = apiService.findApartmentNumberByUsername(username);
		return notificationService.countUnread(apartmentNumber);
	}

	@Operation(summary = "알림 전체 읽음 처리", description = "현재 사용자에게 전송된 알림을 모두 읽음 처리합니다.")
	@PostMapping("/api/notifications/read")
	public void markAllAsRead() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String apartmentNumber = apiService.findApartmentNumberByUsername(username);
		notificationService.markAllAsRead(apartmentNumber);
	}

	@Operation(summary = "최신 알림 조회", description = "읽은 알림 중 최신 5개를 반환합니다.")
	@GetMapping("/api/notifications/latest")
	@ResponseBody
	public List<NotificationDTO> getLatestNotifications() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String apartmentNumber = apiService.findApartmentNumberByUsername(username);
		return notificationService.getLatestNotifications(apartmentNumber);
	}

}
