package com.lancer.HireHub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JobClosingDto {

    @NotBlank(message = "Description is required")
	private String title;
	
	 
	public JobClosingDto(String title, String company) {
		super();
		this.title = title;
		this.company = company;
	}

	@NotBlank(message = "Description is required")
	private String company;

}
