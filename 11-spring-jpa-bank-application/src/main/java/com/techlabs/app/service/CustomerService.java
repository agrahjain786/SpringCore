package com.techlabs.app.service;

import java.time.LocalDateTime;
import java.util.List;

import com.techlabs.app.dto.AccountDTO;
import com.techlabs.app.dto.TransactionDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.dto.UserResponseDTO;
import com.techlabs.app.entity.Transaction;
import com.techlabs.app.util.BalancePagedResponse;
import com.techlabs.app.util.PagedResponse;


public interface CustomerService {

	PagedResponse<TransactionDTO> getAllUsersTransactions(int page, int size, String sortBy, String direction); //done done

	PagedResponse<TransactionDTO> getAllUsersTransactionsByDate(int page, int size, String sortBy, String direction, 
			LocalDateTime startDate, LocalDateTime endDate); //done done

	PagedResponse<TransactionDTO> getAllUsersTransactionsByAccountNumber(int page, int size, String sortBy,
			String direction, int accountNumber); //done done
	
	UserResponseDTO getUserProfile(); //done done

	UserResponseDTO getUserProfileUpdate(UserDTO userDTO); //done done

	TransactionDTO generateTransaction(TransactionDTO transactionDTO); //done done

	List<AccountDTO> getAllAccounts(); //done done
	
	BalancePagedResponse<AccountDTO> getTotalAndIndividualBalance(int page, int size, String sortBy, String direction); //done done

	void requestAccountActivation(int accountNumber); //done done

	List<Transaction> getPassbook(); //done done

	List<Transaction> getPassbookBetweenDates(LocalDateTime startDate, LocalDateTime endDate); //done done

	List<Transaction> getPassbookByAccountNumber(int accountNumber); // done done


	

	
	
	

}
