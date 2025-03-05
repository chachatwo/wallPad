package com.wallpad.project.service;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wallpad.project.dto.EntryCarDTO;
import com.wallpad.project.dto.MaintenanceScheduleDTO;
import com.wallpad.project.dto.NoticeDTO;
import com.wallpad.project.dto.ParkingReserveDTO;
import com.wallpad.project.dto.RepairImageDTO;
import com.wallpad.project.dto.RepairRequestDTO;
import com.wallpad.project.dto.ReserveStatesDTO;
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

	public List<NoticeDTO> findRecentNotices() {
		List<NoticeDTO> notices = apiMapper.findRecentNotices();

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

	public void saveRepairRequest(RepairRequestDTO repairRequestDTO, MultipartFile[] imageUploads) {
		apiMapper.saveRepairRequest(repairRequestDTO);

		int repairRequestId = apiMapper.getLastInsertedId();

		if (imageUploads != null) {
			for (MultipartFile imageUpload : imageUploads) {
				try {
					String imageName = UUID.randomUUID().toString().replace("-", "") + "_"
							+ imageUpload.getOriginalFilename();
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
	
	public List<RepairRequestDTO> findRepairRequest() {
		return apiMapper.findRepairRequest();
	}

	public ParkingReserveDTO findByCarNumber(String carNumber) {
		return apiMapper.findByCarNumber(carNumber);
	}

	public int saveParkingReserve(ParkingReserveDTO parkingReserveDTO) {
		return apiMapper.saveParkingReserve(parkingReserveDTO);
	}

	public int updateParkingReserve(ParkingReserveDTO parkingReserveDTO) {
		return apiMapper.updateParkingReserve(parkingReserveDTO);
	}

	public List<ReserveStatesDTO> reserveStates() {
		return apiMapper.reserveStates();
	}

	public EntryCarDTO registerEntryCar(EntryCarDTO entryCarDTO) {
		EntryCarDTO existingEntryCar = apiMapper.findCarNumber(entryCarDTO.getCarNumber());

		if (existingEntryCar != null) {
			apiMapper.insertEntryCar(entryCarDTO);
			return entryCarDTO;
		}
		return null;
	}

	public List<EntryCarDTO> parkingStates() {
		return apiMapper.parkingStates();
	}

}