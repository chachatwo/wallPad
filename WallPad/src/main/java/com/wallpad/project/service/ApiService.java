package com.wallpad.project.service;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wallpad.project.dto.MaintenanceScheduleDTO;
import com.wallpad.project.dto.NoticeDTO;
import com.wallpad.project.dto.RepairImageDTO;
import com.wallpad.project.dto.RepairRequestDTO;
import com.wallpad.project.dto.SignUpDTO;
import com.wallpad.project.mapper.ApiMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiService {

	private final ApiMapper apiMapper;
	private final BCryptPasswordEncoder passwordEncoder;

	@Value("${upload.dir}")
	private String uploadDir;

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

	public List<MaintenanceScheduleDTO> maintenanceSchedules() {
		return apiMapper.maintenanceSchedules();
	}

	// 수리 요청을 DB에 저장하는 메소드
	public void saveRepairRequest(RepairRequestDTO repairRequestDTO, MultipartFile[] imageUploads) {
		// 수리 요청 정보 저장
		apiMapper.saveRepairRequest(repairRequestDTO);

		// 저장된 수리 요청 ID 조회
		int repairRequestId = apiMapper.getLastInsertedId();

		if (imageUploads != null) {
			for (MultipartFile imageUpload : imageUploads) {
				try {
					String imageName = UUID.randomUUID().toString().replace("-", "") + "_" + imageUpload.getOriginalFilename();
					String imagePath = "/images/" + imageName;

					File targetFile = new File(uploadDir + imageName);
					targetFile.getParentFile().mkdirs(); // 디렉토리 생성
					imageUpload.transferTo(targetFile);
					System.out.println("파일이 저장될 경로: " + targetFile.getAbsolutePath());

					RepairImageDTO repairImageDTO = new RepairImageDTO();
					repairImageDTO.setRepairRequestId(repairRequestId);
					repairImageDTO.setImagePath(imagePath);

					apiMapper.saveImage(repairImageDTO);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}