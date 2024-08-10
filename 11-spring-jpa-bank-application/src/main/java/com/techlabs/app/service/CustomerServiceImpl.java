package com.techlabs.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.AccountDTO;
import com.techlabs.app.dto.BalanceDTO;
import com.techlabs.app.dto.CustomerDTO;
import com.techlabs.app.dto.TransactionDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.dto.UserResponseDTO;
import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.ActivationRequest;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.Transaction;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.AccountException;
import com.techlabs.app.exception.UserException;
import com.techlabs.app.repository.AccountRepository;
import com.techlabs.app.repository.ActivationRequestRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.TransactionRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.util.PagedResponse;

@Service
public class CustomerServiceImpl implements CustomerService{
	
	private AccountRepository accountRepository;
	
	private TransactionRepository transactionRepository;
	
	private UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder;
	
	private CustomerRepository customerRepository;
	
	private ActivationRequestRepository activationRequestRepository;
	

	public CustomerServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository,
			UserRepository userRepository,PasswordEncoder passwordEncoder, CustomerRepository customerRepository,
			ActivationRequestRepository activationRequestRepository) {
		super();
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.activationRequestRepository = activationRequestRepository;
	}

	@Override
	public PagedResponse<TransactionDTO> getAllUsersTransactions(int page, int size, String sortBy, String direction) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
	    
	    Customer customer = user.getCustomer();
	    
	    List<Integer> accountNumbers = customer.getAccounts().stream()
	            .map(Account::getAccountNumber)
	            .collect(Collectors.toList());
	    
	    Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
		
		Page<Transaction> pages = transactionRepository.findBySenderAccountNumberInOrReceiverAccountNumberIn(accountNumbers, accountNumbers, pageable);
		List<Transaction> allUserTransactions = pages.getContent();
		List<TransactionDTO> allUserTransactionsDTO = convertTransactionListEntityToDTO(allUserTransactions);
		
		return new PagedResponse<TransactionDTO>(allUserTransactionsDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}

	@Override
	public PagedResponse<TransactionDTO> getAllUsersTransactionsByDate(int page, int size, String sortBy,
			String direction, LocalDateTime startDate, LocalDateTime endDate) {
		
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
	    
	    Customer customer = user.getCustomer();
	    
	    List<Integer> accountNumbers = customer.getAccounts().stream()
	            .map(Account::getAccountNumber)
	            .collect(Collectors.toList());
	    
	    Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
		
		Page<Transaction> pages = transactionRepository.findBySenderAccountNumberInOrReceiverAccountNumberInAndTransactionDateBetween(
	            accountNumbers, accountNumbers, startDate, endDate, pageable);
		List<Transaction> allUserTransactions = pages.getContent();
		List<TransactionDTO> allUserTransactionsDTO = convertTransactionListEntityToDTO(allUserTransactions);
		
		return new PagedResponse<TransactionDTO>(allUserTransactionsDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());

	}
	
	@Override
	public PagedResponse<TransactionDTO> getAllUsersTransactionsByAccountNumber(int page, int size, String sortBy,
			String direction, int accountNumber) {
		
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
	    
	    Customer customer = user.getCustomer();
	    
	    boolean hasAccount = customer.getAccounts().stream()
	            .anyMatch(account -> account.getAccountNumber() == accountNumber);
	    
	    if (!hasAccount) {
	        throw new AccountException("The logged-in user does not own the account with account number: " + accountNumber);
	    }
	    
	    Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
		
		Page<Transaction> pages = transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber(accountNumber, accountNumber, pageable);
		List<Transaction> allUserTransactions = pages.getContent();
		List<TransactionDTO> allUserTransactionsDTO = convertTransactionListEntityToDTO(allUserTransactions);
		
		return new PagedResponse<TransactionDTO>(allUserTransactionsDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());

	}

	@Override
	public UserResponseDTO getUserProfile() {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
	   
	    UserResponseDTO userDTO = convertUserEntityToDTO(user);
	    return userDTO;
	}

	@Override
	public UserResponseDTO getUserProfileUpdate(UserDTO userDTO) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));

	    user.setFirstName(userDTO.getFirstName());
	    user.setLastName(userDTO.getLastName());
	    user.setUsername(userDTO.getUsername());
	    user.setEmail(userDTO.getEmail());
	    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
	    userRepository.save(user);
	    
	    UserResponseDTO customerDTO = convertUserEntityToDTO(user);
	    return customerDTO;
	}

	@Override
	public TransactionDTO generateTransaction(TransactionDTO transactionDTO) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
	    
	    Account senderAccount = accountRepository.findById(transactionDTO.getSenderAccountNumber())
	            .orElseThrow(() -> new AccountException("Sender account not found"));
	    Account receiverAccount = accountRepository.findById(transactionDTO.getReceiverAccountNumber())
	            .orElseThrow(() -> new AccountException("Receiver account not found"));
	    
	    if(!receiverAccount.isActive()) {
	    	throw new AccountException("Receiver Account is not active");
	    }
	    
	    if (!senderAccount.getCustomer().getUser().equals(user)) {
	        throw new AccountException("Sender account does not belong to the authenticated user");
	    }
	    
	    if(!senderAccount.isActive()) {
	    	throw new AccountException("Your account is inactive. Please contact Admin to make it active.");
	    }
	    
	    boolean isSameAccount = senderAccount.equals(receiverAccount);
	    
	    double amount = transactionDTO.getAmount();
	    String transactionType = transactionDTO.getTransactionType().toLowerCase();
	    
	    if (isSameAccount && transactionType.equals("transfer")) {
	        throw new AccountException("Sender and Receiver cannot be the same for a transfer transaction.");
	    }
	    
	    if (transactionType.equals("debit") || transactionType.equals("transfer")) {
	    	if (senderAccount.getBalance() < amount) {
	            throw new AccountException("Insufficient funds in your account");
	        }
	    	senderAccount.setBalance(senderAccount.getBalance() - amount);
	    	senderAccount.getCustomer().setTotalBalance(senderAccount.getCustomer().getTotalBalance() - amount);
	    	accountRepository.save(senderAccount);
	    }
	    
	    if (transactionType.equals("credit") || transactionType.equals("transfer")) {
	    	receiverAccount.setBalance(receiverAccount.getBalance() + amount);
	    	receiverAccount.getCustomer().setTotalBalance(receiverAccount.getCustomer().getTotalBalance() + amount);
	    	accountRepository.save(receiverAccount);
	    }
	    
	    
	    Transaction transaction = new Transaction();
	    transaction.setSenderAccountNumber(transactionDTO.getSenderAccountNumber());
	    transaction.setReceiverAccountNumber(transactionDTO.getReceiverAccountNumber());
	    transaction.setTransactionType(transactionDTO.getTransactionType());
	    transaction.setAmount(transactionDTO.getAmount());
	    transaction.setTransactionDate(LocalDateTime.now());
	    
	    transactionRepository.save(transaction);
	    
	    return convertTransactionToDTO(transaction);
	}
	
	
	@Override
	public BalanceDTO getTotalAndIndividualBalance() {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
	    
	    Customer customer = user.getCustomer();
	    List<Account> accounts = customer.getAccounts();
	    double totalBalance = accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();
	    List<AccountDTO> accountDTOs = accounts.stream()
	            .map(account -> new AccountDTO(account.getAccountNumber(), account.getAccountType(), account.getBalance(), account.isActive()))
	            .toList();
	    BalanceDTO balance = new BalanceDTO(totalBalance, accountDTOs);
	    
	    return balance;
	}
	
	
	@Override
	public void requestAccountActivation(int accountNumber) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
	    
	    Account account = accountRepository.findById(accountNumber).orElseThrow(()->new AccountException("No Such Account exists with this account Number"));
		
	    if(!account.getCustomer().getUser().equals(user)) {
	    	throw new UserException("This account does not belongs to you");
	    }
	    
	    if (account.isActive()) {
	        throw new UserException("Account is already active.");
	    }
	    
	    ActivationRequest activationRequest = new ActivationRequest();
	    activationRequest.setCustomerIdOrAccountNumber(accountNumber);
	    activationRequest.setRequestDate(LocalDateTime.now());
	    activationRequest.setRequestType("AccountActivation");
	    activationRequest.setStatus("Pending");
	    
	    activationRequestRepository.save(activationRequest);
	}
	
	
	
	@Override
	public List<Transaction> getPassbook() {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
	    
	    Customer customer = user.getCustomer();
	    
	    List<Integer> accountNumbers = customer.getAccounts().stream()
	            .map(Account::getAccountNumber)
	            .collect(Collectors.toList());
	    
		List<Transaction> allUserTransactions = transactionRepository.findBySenderAccountNumberInOrReceiverAccountNumberIn(accountNumbers, accountNumbers);
		
		return allUserTransactions;
	}

	@Override
	public List<Transaction> getPassbookBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
	    
	    Customer customer = user.getCustomer();
	    
	    List<Integer> accountNumbers = customer.getAccounts().stream()
	            .map(Account::getAccountNumber)
	            .collect(Collectors.toList());
	    
	    List<Transaction> allUserTransactions = transactionRepository.findBySenderAccountNumberInOrReceiverAccountNumberInAndTransactionDateBetween(accountNumbers, accountNumbers, startDate, endDate);
	    
	    return allUserTransactions;
	}




	
	
//	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//  UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	
	
	private List<TransactionDTO> convertTransactionListEntityToDTO(List<Transaction> allTransactions) {
		return allTransactions.stream()
	            .map(this::convertTransactionToDTO)
	            .collect(Collectors.toList());
	}

	private TransactionDTO convertTransactionToDTO(Transaction transaction) {
		TransactionDTO transactionDTO = new TransactionDTO();
	    transactionDTO.setId(transaction.getId());
	    transactionDTO.setSenderAccountNumber(transaction.getSenderAccountNumber());
	    transactionDTO.setReceiverAccountNumber(transaction.getReceiverAccountNumber());
	    transactionDTO.setTransactionType(transaction.getTransactionType());
	    transactionDTO.setAmount(transaction.getAmount());
	    transactionDTO.setTransactionDate(transaction.getTransactionDate());
	    
	    return transactionDTO;
	}

	private UserResponseDTO convertUserEntityToDTO(User user) {
		UserResponseDTO userDTO = new UserResponseDTO();
	    userDTO.setId(user.getId());
	    userDTO.setFirstName(user.getFirstName());
	    userDTO.setLastName(user.getLastName());
	    userDTO.setUsername(user.getUsername());
	    userDTO.setEmail(user.getEmail());
	    Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
	    userDTO.setRoles(roles);
	    if (user.getCustomer() != null) {
	        CustomerDTO customerDTO = convertCustomerEntityToDTO(user.getCustomer());
	        userDTO.setCustomer(customerDTO);
	    }
		return userDTO;
	}
	
	private CustomerDTO convertCustomerEntityToDTO(Customer customer) {
	    CustomerDTO customerDTO = new CustomerDTO();
	    customerDTO.setId(customer.getId());
	    customerDTO.setActive(customer.isActive());
	    customerDTO.setTotalBalance(customer.getTotalBalance());
	    
	    List<AccountDTO> accountDTOs = customer.getAccounts().stream()
	    		.map(this::convertAccountToDTO)
	            .collect(Collectors.toList());
	    customerDTO.setAccounts(accountDTOs);
	    
	    return customerDTO;
	}

	private AccountDTO convertAccountToDTO(Account account) {
		AccountDTO accountDTO = new AccountDTO();
	    accountDTO.setAccountNumber(account.getAccountNumber());
	    accountDTO.setAccountType(account.getAccountType());
	    accountDTO.setBalance(account.getBalance());
	    accountDTO.setActive(account.isActive());
	    return accountDTO;
	}





}
