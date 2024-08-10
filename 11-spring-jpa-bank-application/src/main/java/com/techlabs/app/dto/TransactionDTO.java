package com.techlabs.app.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

	private int id;
	
	@NotNull(message = "Sender Account Number is Mandatory and should be valid")
	private int senderAccountNumber;
	
	@NotNull(message = "Receiver Account Number is Mandatory and should be valid")
	private int receiverAccountNumber;
	
	@NotBlank(message = "Transaction type is mandatory")
	private String transactionType;
	
	@NotNull(message = "Amount cannot be null")
	private double amount;
	
	
	private LocalDateTime transactionDate;
}
