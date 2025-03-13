package com.wallpad.project.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RepairRequestDTO {

	private int id;
    private String apartmentNumber;  
    private String majorCategory;       
    private String middleCategory;         
    private String lastCategory;              
    private String request;   
    private LocalDateTime requestTime;
}
