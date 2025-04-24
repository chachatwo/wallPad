package com.wallpad.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
	private int id;
	private String apartmentNumber;
	private String message;
	@JsonProperty("isRead")
	private boolean isRead;
	private String createdAt;
}
