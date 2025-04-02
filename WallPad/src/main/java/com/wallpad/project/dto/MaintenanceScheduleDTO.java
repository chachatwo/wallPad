package com.wallpad.project.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MaintenanceScheduleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate maintenanceDate;  
    private LocalTime startTime;       
    private LocalTime endTime;         
    private String title;              
    private String description;       
    private String status;  
}
