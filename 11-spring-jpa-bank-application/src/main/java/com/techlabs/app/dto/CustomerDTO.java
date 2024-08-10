package com.techlabs.app.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
	
	private int id;
	
	@NotNull(message = "Active Status cannot empty")
	private boolean active;
	
	private double totalBalance;
	
	private List<AccountDTO> accounts;

}
