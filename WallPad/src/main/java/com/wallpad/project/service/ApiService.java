package com.wallpad.project.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wallpad.project.dto.NoticeDTO;
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
	
    public List<NoticeDTO> findAllNotices() {
        List<NoticeDTO> notices = apiMapper.findAllNotices();
        
        // 날짜 포맷 처리
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (NoticeDTO notice : notices) {
            String formattedDate = notice.getCreatedAt().format(formatter);
            notice.setFormattedCreatedAt(formattedDate);
        }

        return notices;
    }
}