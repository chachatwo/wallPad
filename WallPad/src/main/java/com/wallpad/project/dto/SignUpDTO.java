package com.wallpad.project.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SignUpDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String name;
	private String gender;
	private String apartmentNumber;
	private String phone_num;
	private String email;

}
