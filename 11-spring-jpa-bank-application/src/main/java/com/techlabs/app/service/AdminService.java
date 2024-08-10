package com.techlabs.app.service;

import java.time.LocalDateTime;
import java.util.List;

import com.techlabs.app.dto.AccountDTO;
import com.techlabs.app.dto.TransactionDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.dto.UserResponseDTO;
import com.techlabs.app.util.PagedResponse;



public interface AdminService {

	PagedResponse<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String direction);

	UserResponseDTO getUserById(int userId);

	PagedResponse<UserResponseDTO> getAllUsersByFirstNameStartsWith(int page,int size, String sortBy, String direction, 
			String startWith);

	PagedResponse<TransactionDTO> getAllTransactions(int page, int size, String sortBy, String direction);

	PagedResponse<TransactionDTO> getAllTransactionsBetweenDate(int page, int size, String sortBy, String direction,
			LocalDateTime startDate, LocalDateTime endDate);

	PagedResponse<TransactionDTO> getAllTransactionsByAccountNumber(int page, int size, String sortBy,
			String direction, int accountNumber);

	UserResponseDTO createAdmin(UserDTO userDTO);

	UserResponseDTO createUserAccount(int userId, AccountDTO accountDTO);

	void deleteUser(int userId);

	void deleteUserAccount(int userId, int accountNumber);

	UserResponseDTO updateAdminProfile(UserDTO userDTO);

	UserResponseDTO getAdminProfile();

	PagedResponse<UserResponseDTO> getAllInactiveCustomers(int page, int size, String sortBy, String direction);

	PagedResponse<UserResponseDTO> getAllActiveCustomers(int page, int size, String sortBy, String direction);

	int makeActiveCustomersInactive();

	int makeInactiveAccounts();

	PagedResponse<AccountDTO> getAllActiveAccounts(int page, int size, String sortBy, String direction);

	PagedResponse<AccountDTO> getAllInactiveAccounts(int page, int size, String sortBy, String direction);

	void activateAccount(int accountNumber);

	void activateCustomer(int customerId);

	int activateAccountsFromRequests();

	int activateCustomersFromRequests();

	List<byte[]> getFileContent(int customerId, int fileNumber);
	
	

}
