package com.wallpad.project.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wallpad.project.dto.IdCheckDTO;
import com.wallpad.project.dto.SignUpDTO;

@Mapper
public interface ApiMapper {

	int saveUserData(SignUpDTO signUpDTO);
	
	void updateEmailVerified(String email);
	
	int userNameCheck(String username);
	
	int emailCheck(String email);
	
	
	
	
	
	
}
