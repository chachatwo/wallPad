package com.wallpad.project.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveStatesDTO {
	private String carNumber;
	private Timestamp allowedPeriod;
}