package com.wallpad.project.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
public class EntryCarDTO {
	private String carNumber;
	private Timestamp entryTime;
}

