package com.wallpad.project.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	private int id;
	private String username;
	private String password;
	private String name;
	private String gender;
	private String phone_num;
	private String email;
	private String resetToken;
	private LocalDateTime expireAt;
		
	}
