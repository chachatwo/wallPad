package com.wallpad.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallpad.project.dto.NotificationDTO;
import com.wallpad.project.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {

	private final NotificationMapper notificationMapper;
	private final SimpMessagingTemplate messagingTemplate;

	public List<NotificationDTO> getUnreadNotifications(String apartmentNumber) {
		return notificationMapper.selectUnreadNotifications(apartmentNumber);
	}

	public int countUnread(String apartmentNumber) {
		return notificationMapper.countUnreadNotifications(apartmentNumber);
	}

	public void markAllAsRead(String apartmentNumber) {
		notificationMapper.updateAllReadStatus(apartmentNumber);
	}

	public void saveNotification(NotificationDTO dto) {
		notificationMapper.insertNotification(dto);
		messagingTemplate.convertAndSendToUser(dto.getApartmentNumber(), "/queue/alerts", dto);
	}

	public List<NotificationDTO> getLatestNotifications(String apartmentNumber) {
		return notificationMapper.selectLatestNotifications(apartmentNumber);
	}

}
