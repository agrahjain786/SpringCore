package com.techlabs.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
	
	@NotBlank(message = "Username or Email is Mandatory")
	private String usernameOrEmail;
	
	@NotBlank(message = "Password is Mandatory")
	private String password;
	
}
