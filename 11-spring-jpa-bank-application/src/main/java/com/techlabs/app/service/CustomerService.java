package com.techlabs.app.service;

import java.time.LocalDateTime;
import java.util.List;

import com.techlabs.app.dto.BalanceDTO;
import com.techlabs.app.dto.TransactionDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.dto.UserResponseDTO;
import com.techlabs.app.entity.Transaction;
import com.techlabs.app.util.PagedResponse;


public interface CustomerService {

	PagedResponse<TransactionDTO> getAllUsersTransactions(int page, int size, String sortBy, String direction);

	PagedResponse<TransactionDTO> getAllUsersTransactionsByDate(int page, int size, String sortBy, String direction, 
			LocalDateTime startDate, LocalDateTime endDate);

	PagedResponse<TransactionDTO> getAllUsersTransactionsByAccountNumber(int page, int size, String sortBy,
			String direction, int accountNumber);
	
	UserResponseDTO getUserProfile();

	UserResponseDTO getUserProfileUpdate(UserDTO userDTO);

	TransactionDTO generateTransaction(TransactionDTO transactionDTO);

	BalanceDTO getTotalAndIndividualBalance();

	void requestAccountActivation(int accountNumber);

	List<Transaction> getPassbook();

	List<Transaction> getPassbookBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

	
	
	

}
