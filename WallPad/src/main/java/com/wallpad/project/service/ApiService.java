package com.wallpad.project.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.channels.Channels;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.wallpad.project.dto.EntryCarDTO;
import com.wallpad.project.dto.MaintenanceScheduleDTO;
import com.wallpad.project.dto.NoticeDTO;
import com.wallpad.project.dto.ParkingReserveDTO;
import com.wallpad.project.dto.RepairImageDTO;
import com.wallpad.project.dto.RepairRequestDTO;
import com.wallpad.project.dto.ReserveStatesDTO;
import com.wallpad.project.dto.SignUpDTO;
import com.wallpad.project.dto.UserDTO;
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

	@Cacheable(value = "notices")
	public List<NoticeDTO> findAllNotices() {
		List<NoticeDTO> notices = apiMapper.findAllNotices();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		for (NoticeDTO notice : notices) {
			String formattedDate = notice.getCreatedAt().format(formatter);
			notice.setFormattedCreatedAt(formattedDate);
		}

		return notices;
	}

	public List<NoticeDTO> findRecentNotices() {
		List<NoticeDTO> notices = apiMapper.findRecentNotices();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		for (NoticeDTO notice : notices) {
			String formattedDate = notice.getCreatedAt().format(formatter);
			notice.setFormattedCreatedAt(formattedDate);
		}

		return notices;
	}

	@Cacheable(value = "maintenanceSchedules")
	public List<MaintenanceScheduleDTO> maintenanceSchedules() {
		return apiMapper.maintenanceSchedules();
	}

	public void saveRepairRequest(RepairRequestDTO repairRequestDTO, MultipartFile[] imageUploads) {
		apiMapper.saveRepairRequest(repairRequestDTO);
		int repairRequestId = apiMapper.getLastInsertedId();

		if (imageUploads != null && imageUploads.length > 0) {
			try {
				String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
				String bucketName = System.getenv("GOOGLE_CLOUD_STORAGE_BUCKET");

				GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath))
						.createScoped(Arrays.asList("https://www.googleapis.com/auth/devstorage.read_write"));
				Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

				if (storage == null) {
					System.out.println("Google Cloud Storage 인증 실패");
					return;
				}

				for (MultipartFile imageUpload : imageUploads) {
					if (!imageUpload.isEmpty()) {
						String imageName = UUID.randomUUID().toString().replace("-", "") + "_"
								+ imageUpload.getOriginalFilename();
						String imagePath = "images/" + imageName;

						BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, imagePath)
								.setContentType(imageUpload.getContentType()).build();

						Blob blob = storage.create(blobInfo, imageUpload.getInputStream());

						String imageUrl = "https://storage.googleapis.com/" + bucketName + "/" + imagePath;
						System.out.println("GCS에 파일이 저장됨: " + imageUrl);

						RepairImageDTO repairImageDTO = new RepairImageDTO();
						repairImageDTO.setRepairRequestId(repairRequestId);
						repairImageDTO.setImagePath(imageUrl);
						apiMapper.saveImage(repairImageDTO);
					}
				}
			} catch (IOException e) {
				System.out.println("파일 업로드 중 오류 발생: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public String findApartmentNumberByUsername(String username) {
		return apiMapper.findApartmentNumberByUsername(username);
	}

	public List<RepairRequestDTO> findRepairRequestByApartment(String apartmentNumber) {
		return apiMapper.findRepairRequestByApartment(apartmentNumber);
	}

	public ParkingReserveDTO findByCarNumber(ParkingReserveDTO parkingReserveDTO) {
		return apiMapper.findByCarNumber(parkingReserveDTO);
	}

	public int saveParkingReserve(ParkingReserveDTO parkingReserveDTO) {
		return apiMapper.saveParkingReserve(parkingReserveDTO);
	}

	public int updateParkingReserve(ParkingReserveDTO parkingReserveDTO) {
		return apiMapper.updateParkingReserve(parkingReserveDTO);
	}

	public List<ReserveStatesDTO> reserveStatesByApartment(String apartmentNumber) {
		return apiMapper.reserveStatesByApartment(apartmentNumber);
	}

	public EntryCarDTO findReservedCar(String carNumber) {
		return apiMapper.findReservedCar(carNumber);
	}

	public void insertEntryCar(EntryCarDTO entryCarDTO) {
		apiMapper.insertEntryCar(entryCarDTO);
	}

	public List<EntryCarDTO> parkingStatesByApartment(String apartmentNumber) {
		return apiMapper.parkingStatesByApartment(apartmentNumber);
	}

}