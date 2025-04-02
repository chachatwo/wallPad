package com.wallpad.project.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class NoticeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private int id;
	private String title;	
	private String content;
	private LocalDateTime createdAt;
	private String createdBy;
	private String formattedCreatedAt;
}
