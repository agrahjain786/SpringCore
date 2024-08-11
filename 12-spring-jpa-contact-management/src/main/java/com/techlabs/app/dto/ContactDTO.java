package com.techlabs.app.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ContactDTO {

	private int id;

	@NotBlank(message = "First Name of contact is Mandatory")
	private String firstName;
	
	private String lastName;
	
	private Boolean isActive;


	private List<ContactDetailsDTO> contactDetails;
}