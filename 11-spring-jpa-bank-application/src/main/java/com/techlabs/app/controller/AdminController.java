package com.techlabs.app.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.AccountDTO;
import com.techlabs.app.dto.SystemCounts;
import com.techlabs.app.dto.TransactionDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.dto.UserResponseDTO;
import com.techlabs.app.exception.UserException;
import com.techlabs.app.service.AdminService;
import com.techlabs.app.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	private AdminService adminService;
	
	
	public AdminController(AdminService adminService) {
		super();
		this.adminService = adminService;
	}

	@Operation(summary = "By Admin: Get All the Users")
	@GetMapping("/users")
	public ResponseEntity<PagedResponse<UserResponseDTO>> getAllUsers(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction){
		
		PagedResponse<UserResponseDTO> users = adminService.getAllUsers(page,size,sortBy,direction);
		
		return new ResponseEntity<PagedResponse<UserResponseDTO>>(users, HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Admin: Get User by ID")
	@GetMapping("/users/{id}")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable(name="id")int userId){
		UserResponseDTO user = adminService.getUserById(userId);
		
		return new ResponseEntity<UserResponseDTO>(user, HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Admin: Get All Users By Characters their First Name with")
	@GetMapping("/users/starts-with")
	public ResponseEntity<PagedResponse<UserResponseDTO>> getAllUsersByFirstNameStartsWith(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction,
			@RequestParam(name = "startsWith") String startWith){
		
		PagedResponse<UserResponseDTO> users = adminService.getAllUsersByFirstNameStartsWith(page,size,sortBy,direction,startWith);
		
		return new ResponseEntity<PagedResponse<UserResponseDTO>>(users, HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Admin: Get All the customers")
	@GetMapping("/customers")
	public ResponseEntity<PagedResponse<UserResponseDTO>> getAllCustomers(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction){
		
		PagedResponse<UserResponseDTO> customers = adminService.getAllCustomers(page,size,sortBy,direction);
		
		return new ResponseEntity<PagedResponse<UserResponseDTO>>(customers, HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Admin: Get Customer by ID")
	@GetMapping("/customer/{id}")
	public ResponseEntity<UserResponseDTO> getCustomerById(@PathVariable(name="id")int userId){
		UserResponseDTO user = adminService.getCustomerById(userId);
		
		return new ResponseEntity<UserResponseDTO>(user, HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Admin: Get All Customers By Characters their First Name with")
	@GetMapping("/customers/starts-with")
	public ResponseEntity<PagedResponse<UserResponseDTO>> getAllCustomersByFirstNameStartsWith(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction,
			@RequestParam(name = "startsWith") String startWith){
		
		PagedResponse<UserResponseDTO> users = adminService.getAllCustomersByFirstNameStartsWith(page,size,sortBy,direction,startWith);
		
		return new ResponseEntity<PagedResponse<UserResponseDTO>>(users, HttpStatus.OK);
		
	}
	
	
	@Operation(summary = "By Admin: Get All the Transactions")
	@GetMapping("/transaction")
	public ResponseEntity<PagedResponse<TransactionDTO>> getAllTransactions(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction){
		
		PagedResponse<TransactionDTO> transactions = adminService.getAllTransactions(page,size,sortBy,direction);
		
		return new ResponseEntity<PagedResponse<TransactionDTO>>(transactions, HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Admin: Get All the Transactions between Start date and End Date")
	@GetMapping("/transaction/date")
	public ResponseEntity<PagedResponse<TransactionDTO>> getAllTransactionsBetweenDate(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction,
			@RequestParam(name = "startDate") LocalDateTime startDate,
			@RequestParam(name = "endDate") LocalDateTime endDate){
		
		PagedResponse<TransactionDTO> transactions = adminService.getAllTransactionsBetweenDate(page,size,sortBy,direction,startDate,endDate);
		
		return new ResponseEntity<PagedResponse<TransactionDTO>>(transactions, HttpStatus.OK);
		
	}
	
	
	@Operation(summary = "By Admin: Get All the Transactions of Particular Account Number")
	@GetMapping("/transaction/account/{accNumber}")
	public ResponseEntity<PagedResponse<TransactionDTO>> getAllTransactionsByUserId(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction,
			@PathVariable(name="accNumber")int accountNumber){
		
		PagedResponse<TransactionDTO> transactions = adminService.getAllTransactionsByAccountNumber(page,size,sortBy,direction,accountNumber);
		
		return new ResponseEntity<PagedResponse<TransactionDTO>>(transactions, HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Admin: Create Another Admin and email is send to the admin about the creation for the role of admin")
	@PostMapping("/user-admin")
	public ResponseEntity<UserResponseDTO> createAdmin(@Valid @RequestBody UserDTO UserDTO){
		UserResponseDTO addedUser = adminService.createAdmin(UserDTO);
		
		return new ResponseEntity<UserResponseDTO>(addedUser, HttpStatus.CREATED);
		
	}
	
	@Operation(summary = "By Admin: Create the account for Existing Customer and email will sent to the customer about the account creation")
	@PostMapping("/user/{id}")
	public ResponseEntity<UserResponseDTO> createUserAccount(@PathVariable(name="id")int userId, @Valid @RequestBody AccountDTO accountDTO){
		UserResponseDTO addedUserWithAccount = adminService.createUserAccount(userId, accountDTO);
		
		return new ResponseEntity<UserResponseDTO>(addedUserWithAccount, HttpStatus.CREATED);
		
	}
	
	@Operation(summary = "By Admin: Delete user")
	@DeleteMapping("/user/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable(name="id")int userId){
		adminService.deleteUser(userId);
		
		return new ResponseEntity<String>("Deleted Successfully", HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Admin: Delete Any Particular Account of the Customer")
	@DeleteMapping("/user/{id}/account/{accNumber}")
	public ResponseEntity<String> deleteUserAccount(@PathVariable(name="id")int userId, @PathVariable(name="accNumber")int accountNumber){
		adminService.deleteUserAccount(userId, accountNumber);
		
		return new ResponseEntity<String>("Deleted User Account Successfully", HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Admin: Get the admin profile")
	@GetMapping
	public ResponseEntity<UserResponseDTO> getAdminProfile(){
		
		UserResponseDTO admin = adminService.getAdminProfile();
		
		return new ResponseEntity<UserResponseDTO>(admin, HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Admin: Update the Admin Profile")
	@PutMapping
	public ResponseEntity<UserResponseDTO> updateAdminProfile(@Valid @RequestBody UserDTO UserDTO){
		UserResponseDTO updatedUser = adminService.updateAdminProfile(UserDTO);
		
		return new ResponseEntity<UserResponseDTO>(updatedUser, HttpStatus.OK);
		
	}
	
	
	@Operation(summary = "By Admin: Get All Inactive Customers")
	@GetMapping("/users/inactive")
	public ResponseEntity<PagedResponse<UserResponseDTO>> getAllInactiveCustomers(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction){
		
		PagedResponse<UserResponseDTO> users = adminService.getAllInactiveCustomers(page,size,sortBy,direction);
		
		return new ResponseEntity<PagedResponse<UserResponseDTO>>(users, HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Admin: Get All Active Customers")
	@GetMapping("/users/active")
	public ResponseEntity<PagedResponse<UserResponseDTO>> getAllActiveCustomers(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction){
		
		PagedResponse<UserResponseDTO> users = adminService.getAllActiveCustomers(page,size,sortBy,direction);
		
		return new ResponseEntity<PagedResponse<UserResponseDTO>>(users, HttpStatus.OK);
		
	}
	
	
	@Operation(summary = "By Admin: Make All the customers inactive who are not active from last one year")
	@PutMapping("/users/inactive")
	public ResponseEntity<String> makeActiveCustomersInactive(){
		
		int countOfConversion = adminService.makeActiveCustomersInactive();
		
		return new ResponseEntity<String>("Total "+ countOfConversion + " customers were inactive from last one year. There status successfully set to inactive.", HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Admin: Make All the accounts inactive who are not active from last one year")
	@PutMapping("/user/inactive/accounts")
	public ResponseEntity<String> makeInactiveAccounts() {

	    int countOfInactiveAccounts = adminService.makeInactiveAccounts();

	    return new ResponseEntity<String>("Total " + countOfInactiveAccounts + " accounts were inactive for the last year. There status successfully set to inactive.", HttpStatus.OK);
	}
	
	@Operation(summary = "By Admin: Get All the Active accounts")
	@GetMapping("/accounts/active")
	public ResponseEntity<PagedResponse<AccountDTO>> getAllActiveAccounts(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "accountNumber") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		
		PagedResponse<AccountDTO> activeAccounts = adminService.getAllActiveAccounts(page,size,sortBy,direction);
	    return new ResponseEntity<PagedResponse<AccountDTO>>(activeAccounts, HttpStatus.OK);
	}
	
	@Operation(summary = "By Admin: Get All the Inactive accounts")
	@GetMapping("/accounts/inactive")
	public ResponseEntity<PagedResponse<AccountDTO>> getAllInActiveAccounts(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "accountNumber") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		
		PagedResponse<AccountDTO> activeAccounts = adminService.getAllInactiveAccounts(page,size,sortBy,direction);
	    return new ResponseEntity<PagedResponse<AccountDTO>>(activeAccounts, HttpStatus.OK);
	}
	
	@Operation(summary = "By Admin: Make the Particular Acccount Activate")
	@PutMapping("/accounts/{accNumber}/activate")
	public ResponseEntity<String> activateAccount(@PathVariable(name = "accNumber") int accountNumber) {
		adminService.activateAccount(accountNumber);
		
		return new ResponseEntity<>("Account with account number " + accountNumber + " has been successfully activated.",HttpStatus.OK);
	}
	
	@Operation(summary = "By Admin: Make the Particular Customer Activate")
	@PutMapping("/users/{customerId}/activate")
	public ResponseEntity<String> activateCustomer(@PathVariable(name = "customerId") int customerId) {
		adminService.activateCustomer(customerId);
		
		return new ResponseEntity<>("Customer with ID " + customerId + " has been successfully activated.",HttpStatus.OK);
	}
	
	@Operation(summary = "By Admin: Make the All the accounts Activate whose owners have made request")
	@PutMapping("/activate/requests/accounts")
	public ResponseEntity<String> activateAccountsFromRequests() {
		int countOfActivateAccounts = adminService.activateAccountsFromRequests();
		
		return new ResponseEntity<String>("Total " + countOfActivateAccounts + " accounts made active as per the request made.",HttpStatus.OK);
	}
	
	
	@Operation(summary = "By Admin: Make the All the customers Activate who have made request")
	@PutMapping("/activate/requests/customer")
	public ResponseEntity<String> activateCustomersFromRequests() {
		int countOfActivateCustomers = adminService.activateCustomersFromRequests();
		
		return new ResponseEntity<String>("Total " + countOfActivateCustomers + " customers made active as per the request made.",HttpStatus.OK);
	}
	
	@Operation(summary = "By Admin: Get the uploaded Files of the Customers")
	@GetMapping("/files/user/{userId}/{fileNumber}")
	public ResponseEntity<Object> getUploadedFilesOfUsers(@PathVariable(name = "userId") int customerId,@PathVariable(name = "fileNumber") int fileNumber) {
		List<byte[]> fileContent = adminService.getFileContent(customerId, fileNumber);
		
		byte[] content = fileContent.get(fileNumber-1);
		System.out.println("image returned");
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(content);
	}
	
	
	
	@Operation(summary = "By Admin: Make the All the customers Activate who have made request")
	@GetMapping("/counts")
	public ResponseEntity<SystemCounts> wholeSystemStats() {
		SystemCounts counts = adminService.wholeSystemStats();
		
		return new ResponseEntity<SystemCounts>(counts,HttpStatus.OK);
	}
	
	
	
}
