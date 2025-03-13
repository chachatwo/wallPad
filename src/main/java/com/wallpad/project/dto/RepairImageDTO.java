package com.wallpad.project.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RepairImageDTO {

    private int id;  
    private int repairRequestId;  
    private String imagePath; 
}
