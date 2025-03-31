package com.wallpad.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wallpad.project.dto.EntryCarDTO;
import com.wallpad.project.dto.MaintenanceScheduleDTO;
import com.wallpad.project.dto.NoticeDTO;
import com.wallpad.project.dto.ParkingReserveDTO;
import com.wallpad.project.dto.RepairImageDTO;
import com.wallpad.project.dto.RepairRequestDTO;
import com.wallpad.project.dto.ReserveStatesDTO;
import com.wallpad.project.dto.ResetTokenDTO;
import com.wallpad.project.dto.SignUpDTO;
import com.wallpad.project.dto.UserDTO;

@Mapper
public interface ApiMapper {

	int checkUserByEmail(String email);

	int saveUserData(SignUpDTO signUpDTO);

	void updateEmailVerified(String email);

	int userNameCheck(String username);

	int emailCheck(String email);

	UserDTO findByUsername(String username);

	UserDTO findUserByUsername(String username);

	UserDTO findByEmail(String username);

	UserDTO findByUsernameAndEmail(ResetTokenDTO resetTokenDTO);

	void updateResetToken(ResetTokenDTO resetTokenDTO);

	UserDTO findByResetToken(String token);

	void updatePassword(UserDTO userDTO);

	void clearResetToken(int userId);

	List<NoticeDTO> findAllNotices();

	List<NoticeDTO> findRecentNotices();

	List<MaintenanceScheduleDTO> maintenanceSchedules();

	String findApartmentNumberByUsername(String username);

	void saveRepairRequest(RepairRequestDTO repairRequestDTO);

	List<RepairRequestDTO> findRepairRequest();

	int getLastInsertedId();

	void saveImage(RepairImageDTO repairImageDTO);

	ParkingReserveDTO findByCarNumber(ParkingReserveDTO parkingReserveDTO);

	int saveParkingReserve(ParkingReserveDTO parkingReserveDTO);

	int updateParkingReserve(ParkingReserveDTO parkingReserveDTO);

	List<ReserveStatesDTO> reserveStates();

	List<ReserveStatesDTO> reserveStatesByApartment(String apartmentNumber);

	EntryCarDTO findReservedCar(String carNumber);

	void insertEntryCar(EntryCarDTO entryCarDTO);

	List<EntryCarDTO> parkingStatesByApartment(String apartmentNumber);
}
