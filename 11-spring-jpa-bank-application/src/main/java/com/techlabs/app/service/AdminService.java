package com.techlabs.app.service;

import java.time.LocalDateTime;
import java.util.List;

import com.techlabs.app.dto.AccountDTO;
import com.techlabs.app.dto.SystemCounts;
import com.techlabs.app.dto.TransactionDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.dto.UserResponseDTO;
import com.techlabs.app.util.PagedResponse;



public interface AdminService {

	PagedResponse<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String direction); //done done

	UserResponseDTO getUserById(int userId); // done done

	PagedResponse<UserResponseDTO> getAllUsersByFirstNameStartsWith(int page,int size, String sortBy, String direction, 
			String startWith); // done done

	PagedResponse<TransactionDTO> getAllTransactions(int page, int size, String sortBy, String direction); //done done

	PagedResponse<TransactionDTO> getAllTransactionsBetweenDate(int page, int size, String sortBy, String direction,
			LocalDateTime startDate, LocalDateTime endDate); //done done

	PagedResponse<TransactionDTO> getAllTransactionsByAccountNumber(int page, int size, String sortBy,
			String direction, int accountNumber); //done done

	UserResponseDTO createAdmin(UserDTO userDTO); //done done

	UserResponseDTO createUserAccount(int userId, AccountDTO accountDTO); //done done

	void deleteUser(int userId);

	void deleteUserAccount(int userId, int accountNumber);

	UserResponseDTO updateAdminProfile(UserDTO userDTO); //done done

	UserResponseDTO getAdminProfile(); //done done

	PagedResponse<UserResponseDTO> getAllInactiveCustomers(int page, int size, String sortBy, String direction); //done done

	PagedResponse<UserResponseDTO> getAllActiveCustomers(int page, int size, String sortBy, String direction); //done done

	int makeActiveCustomersInactive(); //done done

	int makeInactiveAccounts(); //done done

	PagedResponse<AccountDTO> getAllActiveAccounts(int page, int size, String sortBy, String direction); //done done

	PagedResponse<AccountDTO> getAllInactiveAccounts(int page, int size, String sortBy, String direction); //done done

	void activateAccount(int accountNumber); //done done

	void activateCustomer(int customerId); //done done

	int activateAccountsFromRequests(); //done done

	int activateCustomersFromRequests(); //done done

	List<byte[]> getFileContent(int customerId, int fileNumber); //done done

	PagedResponse<UserResponseDTO> getAllCustomers(int page, int size, String sortBy, String direction); //done done

	UserResponseDTO getCustomerById(int userId); // done done 

	PagedResponse<UserResponseDTO> getAllCustomersByFirstNameStartsWith(int page, int size, String sortBy,
			String direction, String startWith); //done done

	SystemCounts wholeSystemStats(); //done done

	
	

}
