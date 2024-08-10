package com.techlabs.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
	
	private int accountNumber;
	
	@NotBlank(message = "Account Type cannot be null")
	private String accountType;
	
	private double balance;
	
	private boolean active;

}
