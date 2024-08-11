package com.techlabs.app.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
	private int id;
	
	@NotBlank(message = "First name is Mandatory")
	private String firstName;
	
	private String lastName;
	
	@NotBlank(message = "Username is Mandatory")
	private String username;
	
	@NotBlank(message = "Email is Mandatory")
	private String email;
	
	@NotBlank(message = "Password is Mandatory")
	private String password;
	
	@NotNull(message = "role cannot be null")
	private Boolean isAdmin;

	private Boolean isActive;
	
	private List<ContactDTO> contacts;
}

