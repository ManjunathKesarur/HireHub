package com.lancer.HireHub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Table(name = "user")
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty(message = "name is required")
	private String name;
	
	@NotEmpty(message = "email is required")
	@Email(message = "incorrect email ")
	private String email;
	
	
	@NotEmpty(message = "password is required")
	private String password;
	
	@NotEmpty(message = "phone number is required")
	private String phonenumber;
	
	@NotEmpty(message = "role is required")
	private String role;
	
}
	
