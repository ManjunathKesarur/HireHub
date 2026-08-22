package com.lancer.HireHub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Job {

    @Id
    @GeneratedValue(strategy =   GenerationType.IDENTITY)
    private Integer id;
    
    @NotEmpty(message = "field is mandatory")
    private String title;
    
    @NotEmpty(message = "field is mandatory")
    private String description;

    @NotEmpty(message = "field is mandatory")
    private String location;

    @NotEmpty(message = "field is mandatory")
    private String company;
    
    @NotNull(message = "field is mandatory")
    private Double salary;

    @NotEmpty(message = "field is mandatory")
    private String jobType;
}
