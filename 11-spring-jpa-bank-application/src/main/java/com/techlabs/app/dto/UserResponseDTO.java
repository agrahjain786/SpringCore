package com.techlabs.app.dto;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserResponseDTO {
	
	private int id;
	
	@NotBlank(message = "First name is Mandatory")
	private String firstName;
	
	private String lastName;
	
	@NotBlank(message = "Username is Mandatory")
	private String username;
	
	@NotBlank(message = "Email is Mandatory")
	private String email;
	
	@NotEmpty(message = "At least one role is Mandatory")
    private Set<String> roles;
	
	private CustomerDTO customer;
	
	private AdminDTO admin;

}
