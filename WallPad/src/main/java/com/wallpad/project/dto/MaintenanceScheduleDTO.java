package com.wallpad.project.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MaintenanceScheduleDTO {

    private LocalDate maintenanceDate;  
    private LocalTime startTime;       
    private LocalTime endTime;         
    private String title;              
    private String description;       
    private String status;  
}
