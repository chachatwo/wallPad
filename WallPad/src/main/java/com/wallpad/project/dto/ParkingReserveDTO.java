package com.wallpad.project.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingReserveDTO {
	private String carNumber;
	private Timestamp allowedPeriod;
}