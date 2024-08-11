package com.techlabs.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDetailsDTO {
	
    private int id;

	
	@NotBlank(message = "Contact type is mandatory")
    private String type;
	
	@NotBlank(message = "Value is mandatory")
    private String value;


}
