package com.wallpad.project.service;

import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wallpad.project.dto.SignUpDTO;
import com.wallpad.project.mapper.ApiMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiService {

	private final ApiMapper apiMapper;
	private final BCryptPasswordEncoder passwordEncoder;

	public int saveUserData(SignUpDTO signUpDTO) {

		String hashedPassword = passwordEncoder.encode(signUpDTO.getPassword());
		signUpDTO.setPassword(hashedPassword); 

		return apiMapper.saveUserData(signUpDTO);
	}

	public boolean userNameCheck(String username) {
		int count = apiMapper.userNameCheck(username);
		return count == 0; 
	}

	public boolean emailCheck(String email) {
		int count = apiMapper.emailCheck(email);
		return count == 0; 
	}

}