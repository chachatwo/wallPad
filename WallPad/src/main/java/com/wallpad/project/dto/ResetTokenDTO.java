package com.wallpad.project.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetTokenDTO {
	private String username;
	private String email;
	private String resetToken;
	private LocalDateTime expireAt;
}