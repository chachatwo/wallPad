package com.wallpad.project.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SignUpDTO {

	private String username;
	private String password;	
	private String name;
	private String gender;
	private String phone_num;
	private String email;
	
}
