package com.lancer.HireHub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobDto {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Company is required")
    private String company;

    @NotNull(message = "Salary is required")
    private Double salary;

    @NotBlank(message = "Job type is required")
    private String jobType;
}
