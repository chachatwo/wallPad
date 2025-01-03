package com.wallpad.project.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.wallpad.project.dto.IdCheckDTO;
import com.wallpad.project.dto.SignUpDTO;
import com.wallpad.project.mapper.ApiMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiService {

	private final ApiMapper apiMapper;

	
	
	
	public int saveUserData(SignUpDTO signUpDTO) {
		return apiMapper.saveUserData(signUpDTO);
	}

	
    public boolean userNameCheck(String username) {
        int count = apiMapper.userNameCheck(username);
        return count == 0; // 0이면 사용 가능한 아이디
    }

    public boolean emailCheck(String email) {
        int count = apiMapper.emailCheck(email);
        return count == 0; // 0이면 사용 가능한 이메일
    }
	
	
	
	
	
}