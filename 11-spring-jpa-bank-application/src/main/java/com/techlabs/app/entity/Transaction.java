package com.techlabs.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="id")
	private int id;
	
	@NotNull
	@Column(name ="sender_account_number")
	private int senderAccountNumber;
	
	@NotNull
	@Column(name ="receiver_account_number")
	private int receiverAccountNumber;
	
	@NotBlank
	@Column(name ="transaction_type")
	private String transactionType;
	
	@NotNull
	@Column(name ="amount")
	private double amount;
	
	@Column(name ="transaction_date")
	private LocalDateTime transactionDate;

}
