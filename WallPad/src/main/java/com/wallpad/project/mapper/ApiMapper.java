package com.wallpad.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wallpad.project.dto.MaintenanceScheduleDTO;
import com.wallpad.project.dto.NoticeDTO;
import com.wallpad.project.dto.ParkingReserveDTO;
import com.wallpad.project.dto.RepairImageDTO;
import com.wallpad.project.dto.RepairRequestDTO;
import com.wallpad.project.dto.ReserveStatesDTO;
import com.wallpad.project.dto.SignUpDTO;
import com.wallpad.project.dto.UserDTO;

@Mapper
public interface ApiMapper {

	int saveUserData(SignUpDTO signUpDTO);

	void updateEmailVerified(String email);

	int userNameCheck(String username);

	int emailCheck(String email);

	UserDTO findByUsername(String username);

	List<NoticeDTO> findAllNotices();
	
	List<MaintenanceScheduleDTO> maintenanceSchedules();
	
	void saveRepairRequest(RepairRequestDTO repairRequestDTO);
	
	int getLastInsertedId();
	
	void saveImage(RepairImageDTO repairImageDTO);
	
	
	ParkingReserveDTO findByCarNumber(String carNumber);
	
	int saveParkingReserve(ParkingReserveDTO parkingReserveDTO);
	
	int updateParkingReserve(ParkingReserveDTO parkingReserveDTO);
	
	List<ReserveStatesDTO> reserveStates();
	
	
	
}
