package com.lancer.HireHub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {

	    @NotEmpty(message = "Email is required")
	    @Email(message = "Invalid email")
	    private String email;

	    @NotEmpty(message = "Password is required")
	    private String password;

	    @NotEmpty(message = "Phone number is required")
	    private String phonenumber;

	    @NotEmpty(message = "Role is required")
	    private String role;
}
