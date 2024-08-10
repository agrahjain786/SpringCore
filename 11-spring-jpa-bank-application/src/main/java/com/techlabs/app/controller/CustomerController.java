package com.techlabs.app.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.TransactionDTO;
import com.techlabs.app.dto.BalanceDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.dto.UserResponseDTO;
import com.techlabs.app.entity.Transaction;
import com.techlabs.app.service.CustomerService;
import com.techlabs.app.service.TransactionPdfExporter;
import com.techlabs.app.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	private CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}
	
	@Operation(summary = "By Customer: Get the passbook of the Particular Customer")
	@GetMapping("/transactions")
	public ResponseEntity<PagedResponse<TransactionDTO>> getAllUsersTransactions(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction){
		
		PagedResponse<TransactionDTO> transactions = customerService.getAllUsersTransactions(page,size,sortBy,direction);
		
		return new ResponseEntity<PagedResponse<TransactionDTO>>(transactions, HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Customer: Get the passbook of the Particular Customer between Start date and end date")
	@GetMapping("/transactions/date")
	public ResponseEntity<PagedResponse<TransactionDTO>> getAllUsersTransactionsByDate(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction,
			@RequestParam(name = "startDate") LocalDateTime startDate,
			@RequestParam(name = "endDate") LocalDateTime endDate){
		
		PagedResponse<TransactionDTO> transactions = customerService.getAllUsersTransactionsByDate(page,size,sortBy,direction,startDate,endDate);
		
		return new ResponseEntity<PagedResponse<TransactionDTO>>(transactions, HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Customer: Get the passbook of the Particular Customer of their respective particular Account")
	@GetMapping("/transactions/{accNumber}")
	public ResponseEntity<PagedResponse<TransactionDTO>> getAllUsersTransactionsByAccountNumber(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction,
			@PathVariable(name = "accNumber") int accountNumber){
		
		PagedResponse<TransactionDTO> transactions = customerService.getAllUsersTransactionsByAccountNumber(page,size,sortBy,direction,accountNumber);
		
		return new ResponseEntity<PagedResponse<TransactionDTO>>(transactions, HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Customer: Get the Customer Profile")
	@GetMapping
	public ResponseEntity<UserResponseDTO> getUserProfile(){
		UserResponseDTO user = customerService.getUserProfile();
		
		return new ResponseEntity<UserResponseDTO>(user, HttpStatus.OK);
		
	}
	
	@Operation(summary = "By Customer: Update the Customer Profile")
	@PutMapping
	public ResponseEntity<UserResponseDTO> getUserProfileUpdate(@Valid @RequestBody UserDTO userDTO){
		UserResponseDTO updatedUser = customerService.getUserProfileUpdate(userDTO);
		
		return new ResponseEntity<UserResponseDTO>(updatedUser, HttpStatus.CREATED);
		
	}
	
	@Operation(summary = "By Customer: Make the transaction(credit,debit,transfer) to their own/another customer accounts")
	@PostMapping("/transaction/new")
	public ResponseEntity<TransactionDTO> generateTransaction(@Valid @RequestBody TransactionDTO transactionDTO){
		TransactionDTO transaction = customerService.generateTransaction(transactionDTO);
		
		return new ResponseEntity<TransactionDTO>(transaction, HttpStatus.CREATED);
		
	}
	
	@Operation(summary = "By Customer: Get Total and individual balances of the Account")
	@GetMapping("/balance")
	public ResponseEntity<BalanceDTO> getTotalAndIndividualBalance(){
		BalanceDTO balances = customerService.getTotalAndIndividualBalance();
		
		return new ResponseEntity<BalanceDTO>(balances, HttpStatus.CREATED);
		
	}
	
	@Operation(summary = "By Customer: Make the request to the admin to activate their particular account")
	@PostMapping("/request-activation/{accountNumber}")
	public ResponseEntity<String> requestCustomerActivation(@PathVariable(name = "accountNumber") int accountNumber) {
		customerService.requestAccountActivation(accountNumber);

		return new ResponseEntity<String>("Your request to activate the customer's account has been sent to the admin.",HttpStatus.OK);
	}
	
	@Operation(summary = "By Customer: Download the Passbook in pdf format")
	@GetMapping("/passbook/download")
	public ResponseEntity<String> exportPassbookToPDF(HttpServletResponse response) {

		response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
	    
        List<Transaction> transactions = customerService.getPassbook();
        
        TransactionPdfExporter exporter = new TransactionPdfExporter(transactions);
        exporter.export(response);

	    return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@Operation(summary = "By Customer: Download the Passbook between start date and end date in pdf format")
	@GetMapping("/passbook/date/download")
	public ResponseEntity<String> exportPassbookBetweenDatesToPDF(HttpServletResponse response,
			@RequestParam(name = "startDate") LocalDateTime startDate,
			@RequestParam(name = "endDate") LocalDateTime endDate) {

		response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
	    
        List<Transaction> transactions = customerService.getPassbookBetweenDates(startDate,endDate);
        
        TransactionPdfExporter exporter = new TransactionPdfExporter(transactions);
        exporter.export(response);

	    return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
}
