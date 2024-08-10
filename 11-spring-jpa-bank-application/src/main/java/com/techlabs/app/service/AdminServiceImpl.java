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
import com.techlabs.app.dto.CustomerDTO;
import com.techlabs.app.dto.TransactionDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.dto.UserResponseDTO;
import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.ActivationRequest;
import com.techlabs.app.entity.Admin;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.Transaction;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.AccountException;
import com.techlabs.app.exception.UserException;
import com.techlabs.app.repository.AccountRepository;
import com.techlabs.app.repository.ActivationRequestRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.TransactionRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.util.PagedResponse;


@Service
public class AdminServiceImpl implements AdminService{
	
	private AccountRepository accountRepository;
	
	private TransactionRepository transactionRepository;
	
	private UserRepository userRepository;
	
	private RoleRepository roleRepository;
	
	private PasswordEncoder passwordEncoder;
	
	private EmailService emailService;
	
	private CustomerRepository customerRepository;
	
	private ActivationRequestRepository activationRequestRepository;
	
	private FileService fileService;

	public AdminServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository,
			UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, 
			EmailService emailService, CustomerRepository customerRepository, ActivationRequestRepository activationRequestRepository,
			FileService fileService) {
		super();
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
		this.customerRepository = customerRepository;
		this.activationRequestRepository = activationRequestRepository;
		this.fileService = fileService;
	}

	@Override
	public PagedResponse<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
		
		Page<User> pages = userRepository.findAll(pageable);
		List<User> allUsers = pages.getContent();
		List<UserResponseDTO> allUsersDTO = convertUserListEntityToDTO(allUsers);
		
		return new PagedResponse<UserResponseDTO>(allUsersDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}


	@Override
	public UserResponseDTO getUserById(int userId) {
		User user = userRepository.findById(userId).orElseThrow(()->new UserException("User Not Found!"));
		
		return convertUserEntityToDTO(user);
	}

	@Override
	public PagedResponse<UserResponseDTO> getAllUsersByFirstNameStartsWith(int page, int size, String sortBy, String direction,
			String startWith) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
		
		Page<User> pages = userRepository.findByFirstNameStartingWith(startWith, pageable);
		List<User> allUsers = pages.getContent();
		List<UserResponseDTO> allUsersDTO = convertUserListEntityToDTO(allUsers);
		
		return new PagedResponse<UserResponseDTO>(allUsersDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}

	@Override
	public PagedResponse<TransactionDTO> getAllTransactions(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
		
		Page<Transaction> pages = transactionRepository.findAll(pageable);
		List<Transaction> allTransactions = pages.getContent();
		List<TransactionDTO> allTransactionsDTO = convertTransactionListEntityToDTO(allTransactions);
		
		return new PagedResponse<TransactionDTO>(allTransactionsDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}

	

	@Override
	public PagedResponse<TransactionDTO> getAllTransactionsBetweenDate(int page, int size, String sortBy,
			String direction, LocalDateTime startDate, LocalDateTime endDate) {
		
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
		
		Page<Transaction> pages = transactionRepository.findByTransactionDateBetween(startDate, endDate, pageable);
		List<Transaction> allTransactions = pages.getContent();
		List<TransactionDTO> allTransactionsDTO = convertTransactionListEntityToDTO(allTransactions);
		
		return new PagedResponse<TransactionDTO>(allTransactionsDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}

	@Override
	public PagedResponse<TransactionDTO> getAllTransactionsByAccountNumber(int page, int size, String sortBy,
			String direction,int accountNumber) {
		Account account = accountRepository.findById(accountNumber).orElseThrow(()->new AccountException("Account Not Found!"));
		
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
		
		Page<Transaction> pages = transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber(accountNumber, accountNumber,pageable);
		List<Transaction> allTransactions = pages.getContent();
		List<TransactionDTO> allTransactionsDTO = convertTransactionListEntityToDTO(allTransactions);
		
		return new PagedResponse<TransactionDTO>(allTransactionsDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}

	@Override
	public UserResponseDTO createAdmin(UserDTO userDTO) {
		userDTO.setId(0);
		User user = convertUserDTOToEntity(userDTO);
		
		Admin admin = new Admin();
		admin.setUser(user);
		user.setAdmin(admin);
		userRepository.save(user);
		
		String subject = "Creation of Admin";
		String text = "Hey! \n\nProfile has been created as the role of Admin with \nID: "+user.getId()+"\n"
				+ "Username : "+user.getUsername()+"\nEmail: "+user.getEmail()+"\nFull Name: "+user.getFirstName()+" "+user.getLastName();
		
		emailService.sendSimpleMail(user.getEmail(),subject,text);
		
		return convertUserEntityToDTO(user);
	}

	


	@Override
	public UserResponseDTO createUserAccount(int userId, AccountDTO accountDTO) {
		if(accountDTO.getBalance() < 1000) throw new AccountException("Initial Balance of account must be greater than 1000");
		
		Customer customer = customerRepository.findById(userId).orElseThrow(()->new UserException("Customer Not Found!"));
		
		User user = customer.getUser();
		
		if(!customer.isActive()) {
			throw new UserException("This customer is inactive. Please contact Admin to make it active.");
		}
		
		Account account = convertDTOToAccountEntity(accountDTO);
		account.setActive(true);
		account.setCustomer(customer);
		Account savedAccount = accountRepository.save(account);
		customer.getAccounts().add(account);
		customer.setTotalBalance(customer.getTotalBalance()+account.getBalance());
//		customerRepository.save(customer);
		
		Transaction transaction = new Transaction();
		transaction.setAmount(savedAccount.getBalance());
		transaction.setSenderAccountNumber(savedAccount.getAccountNumber());
		transaction.setReceiverAccountNumber(savedAccount.getAccountNumber());
		transaction.setTransactionDate(LocalDateTime.now());
		transaction.setTransactionType("credit");
		
		transactionRepository.save(transaction);
		
		String subject = "Creation of Customer Bank Account";
		String text = "Hey! \n\nBank Account has been created for \nID: "+user.getId()+"\n"
				+ "Username : "+user.getUsername()+"\nEmail: "+user.getEmail()+"\nFull Name: "+user.getFirstName()+" "+user.getLastName();
		emailService.sendSimpleMail(user.getEmail(),subject,text);
		return convertUserEntityToDTO(user);
	}

	@Override
	public void deleteUser(int userId) {
		User user = userRepository.findById(userId).orElseThrow(()->new UserException("User Not Found!"));
		
		boolean hasUserRole = user.getRoles().stream()
	            .anyMatch(role -> role.getName().equals("ROLE_USER"));
		
		if (!hasUserRole) {
	        throw new UserException("Cannot delete the user beacuse user is admin");
	    }
		userRepository.delete(user);
		
	}

	@Override
	public void deleteUserAccount(int userId, int accountNumber) {
		Customer customer = customerRepository.findById(userId).orElseThrow(()->new UserException("Customer Not Found!"));
		
		User user = customer.getUser();
		
		Account account = accountRepository.findById(accountNumber).orElseThrow(()->new AccountException("Account Not Found!"));
		
		if (!customer.getAccounts().contains(account)) {
	        throw new AccountException("Account does not belong to the user!");
	    }
		customer.getAccounts().remove(account);
	    customerRepository.save(customer);
	}
	
	
	@Override
	public UserResponseDTO getAdminProfile() {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User admin = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	        .orElseThrow(() -> new UserException("Admin not found!"));
	    
	    UserResponseDTO adminDTO = convertUserEntityToDTO(admin);
	    return adminDTO;
	}
	

	@Override
	public UserResponseDTO updateAdminProfile(UserDTO userDTO) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User admin = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	        .orElseThrow(() -> new UserException("Admin not found!"));
	    
	    admin.setFirstName(userDTO.getFirstName());
	    admin.setLastName(userDTO.getLastName());
	    admin.setUsername(userDTO.getUsername());
	    admin.setEmail(userDTO.getEmail());
	    admin.setPassword(passwordEncoder.encode(userDTO.getPassword()));
	    userRepository.save(admin);
	    
	    UserResponseDTO adminResponseDTO = convertUserEntityToDTO(admin);
	    return adminResponseDTO;
	}
	
	
	@Override
	public PagedResponse<UserResponseDTO> getAllInactiveCustomers(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();

		Pageable pageable = (Pageable)PageRequest.of(page, size, sort);

		Page<Customer> pages = customerRepository.findByActiveFalse(pageable);
		List<Customer> allInactiveCustomers = pages.getContent();
		List<UserResponseDTO> inactiveCustomersDTO = allInactiveCustomers.stream()
				.map(customer -> convertUserEntityToDTO(customer.getUser())).collect(Collectors.toList());
		
		return new PagedResponse<UserResponseDTO>(inactiveCustomersDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}

	@Override
	public PagedResponse<UserResponseDTO> getAllActiveCustomers(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();

		Pageable pageable = (Pageable)PageRequest.of(page, size, sort);

		Page<Customer> pages = customerRepository.findByActiveTrue(pageable);
		List<Customer> allInactiveCustomers = pages.getContent();
		List<UserResponseDTO> inactiveCustomersDTO = allInactiveCustomers.stream()
				.map(customer -> convertUserEntityToDTO(customer.getUser())).collect(Collectors.toList());
		
		return new PagedResponse<UserResponseDTO>(inactiveCustomersDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}

	
	@Override
	public int makeActiveCustomersInactive() {
		LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
		
		List<Customer> customers = customerRepository.findAll();
		
		int deactivatedCount = 0;
		
		for (Customer customer : customers) {
			if (customer.isActive()) {
				boolean hasRecentTransaction = customer.getAccounts().stream().filter(Account::isActive)
						.anyMatch(account -> transactionRepository.existsBySenderAccountNumberOrReceiverAccountNumberAndTransactionDateAfter(
										account.getAccountNumber(), account.getAccountNumber(), oneYearAgo));

				if (!hasRecentTransaction) {
					double totalDeductedBalance = 0;
					for (Account account : customer.getAccounts()) {
						if (account.isActive()) {
							account.setActive(false);
							totalDeductedBalance += account.getBalance();
						}
					}
					customer.getAccounts().forEach(account -> account.setActive(false));
					customer.setActive(false);
					customer.setTotalBalance(customer.getTotalBalance() - totalDeductedBalance);
					customerRepository.save(customer);
					deactivatedCount++;
				}
			}

		}

	    return deactivatedCount;
	}

	@Override
	public int makeInactiveAccounts() {
		LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);

	    List<Customer> customers = customerRepository.findAll();

	    int inactiveAccountsCount = 0;

	    for (Customer customer : customers) {
	    	if(customer.isActive()) {
	    		for (Account account : customer.getAccounts()) {
	    			if(account.isActive()) {
	    				boolean hasRecentTransaction = transactionRepository.existsBySenderAccountNumberOrReceiverAccountNumberAndTransactionDateAfter(account.getAccountNumber(), account.getAccountNumber(),oneYearAgo);

			            if (!hasRecentTransaction && account.isActive()) {
			                account.setActive(false);
			                customer.setTotalBalance(customer.getTotalBalance() - account.getBalance());
			                accountRepository.save(account);
			                inactiveAccountsCount++;
			            }
	    			}
		            
		        }
	    	}
	        
	    }

	    return inactiveAccountsCount;
	}

	
	@Override
	public PagedResponse<AccountDTO> getAllActiveAccounts(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();

		Pageable pageable = (Pageable)PageRequest.of(page, size, sort);

		Page<Account> pages = accountRepository.findByActiveTrue(pageable);
		List<Account> allActiveAccounts = pages.getContent();
		List<AccountDTO> activeAccountsDTO = allActiveAccounts.stream()
				.map(account -> convertAccountToDTO(account)).collect(Collectors.toList());
		
		return new PagedResponse<AccountDTO>(activeAccountsDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}

	@Override
	public PagedResponse<AccountDTO> getAllInactiveAccounts(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();

		Pageable pageable = (Pageable)PageRequest.of(page, size, sort);

		Page<Account> pages = accountRepository.findByActiveFalse(pageable);
		List<Account> allActiveAccounts = pages.getContent();
		List<AccountDTO> activeAccountsDTO = allActiveAccounts.stream()
				.map(account -> convertAccountToDTO(account)).collect(Collectors.toList());
		
		return new PagedResponse<AccountDTO>(activeAccountsDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}

	@Override
	public void activateAccount(int accountNumber) {
		Account account = accountRepository.findById(accountNumber).orElseThrow(()->new AccountException("Account does not exists with this account number"));
		
		if(account.isActive()) {
			throw new AccountException("Account is already active");
		}
		
		if(!account.getCustomer().isActive()) {
			throw new AccountException("Make Customer active first to active this account");
		}

		account.setActive(true);
		account.getCustomer().setTotalBalance(account.getCustomer().getTotalBalance()+account.getBalance());
		accountRepository.save(account);
		
	}
	
	
	@Override
	public void activateCustomer(int customerId) {
		Customer customer = customerRepository.findById(customerId).orElseThrow(()->new AccountException("Customer does not exists with this Id"));
		
		if(customer.isActive()) {
			throw new AccountException("Account is already active");
		}
		
		customer.setActive(true);
		customerRepository.save(customer);
		
	}
	
	
	@Override
	public int activateAccountsFromRequests() {
	
		int countActivatedRequests = 0;
		List<ActivationRequest> pendingRequests = activationRequestRepository.findByStatusAndRequestType("Pending", "AccountActivation");
		for (ActivationRequest request : pendingRequests) {
			
			Account account = accountRepository.findById(request.getCustomerIdOrAccountNumber()).orElse(null);
			
			if(account == null) {
				request.setStatus("Done");
				throw new AccountException("No such Account Exist with the account Number "+ request.getCustomerIdOrAccountNumber());
			}
			
			if(account.isActive()) {
				request.setStatus("Done");
				throw new AccountException("Account already activated with the account Number "+ request.getCustomerIdOrAccountNumber());
			}
			
			if(!account.getCustomer().isActive()) {
				request.setStatus("Done");
				throw new AccountException("Customer with with the account Number "+ request.getCustomerIdOrAccountNumber()+" is inactive. First make Customer Active");
			}
			
			account.setActive(true);
			request.setStatus("Done");
			activationRequestRepository.save(request);
			account.getCustomer().setTotalBalance(account.getCustomer().getTotalBalance()+account.getBalance());
			accountRepository.save(account);
			countActivatedRequests++;
		}
		
		return countActivatedRequests;
	}

	
	@Override
	public int activateCustomersFromRequests() {
		int countActivatedRequests = 0;

	    List<ActivationRequest> pendingRequests = activationRequestRepository.findByStatusAndRequestType("Pending", "CustomerActivation");
	    for (ActivationRequest request : pendingRequests) {
			
			Customer customer = customerRepository.findById(request.getCustomerIdOrAccountNumber()).orElse(null);
			
			if(customer == null) {
				request.setStatus("Done");
				throw new UserException("No such Customer Exist with the Customer Id "+ request.getCustomerIdOrAccountNumber());
			}
			
			if(customer.isActive()) {
				request.setStatus("Done");
				throw new UserException("Customer already activated with the Customer Id "+ request.getCustomerIdOrAccountNumber());
			}
			
			request.setStatus("Done");
			activationRequestRepository.save(request);
			customer.setActive(true);
			customerRepository.save(customer);
			countActivatedRequests++;
		}
	    
	    return countActivatedRequests;
	    
	}

	@Override
	public List<byte[]> getFileContent(int customerId, int fileNumber) {
		
		Customer customer = customerRepository.findById(customerId).orElseThrow(()->new AccountException("Customer does not exists with this Id"));
		
		List<byte[]> allFiles = fileService.getFiles(customerId);
		
		if(allFiles.size() < fileNumber) {
			throw new UserException("User File does not exists");
		}
		return allFiles;
	}

	
	

	
	

	private List<UserResponseDTO> convertUserListEntityToDTO(List<User> allUsers) {
		return allUsers.stream()
		        .map(this::convertUserEntityToDTO)
		        .collect(Collectors.toList());
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

	
	private User convertUserDTOToEntity(UserDTO userDTO) {
		User user = new User();
	    user.setFirstName(userDTO.getFirstName());
	    user.setLastName(userDTO.getLastName());
	    user.setUsername(userDTO.getUsername());
	    user.setEmail(userDTO.getEmail());
	    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
	    Set<Role> roles = userDTO.getRoles().stream()
                .map(roleName -> roleRepository.findByName("ROLE_" + roleName).orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());
	    user.setRoles(roles);
	    return user;
	}

	private List<Account> convertDTOListToAccountEntity(List<AccountDTO> accountsDTO) {
		return accountsDTO.stream()
                .map(this::convertDTOToAccountEntity)
                .collect(Collectors.toList());
	}

	private Account convertDTOToAccountEntity(AccountDTO accountDTO) {
		Account account = new Account();
	    account.setAccountNumber(accountDTO.getAccountNumber());
	    account.setAccountType(accountDTO.getAccountType());
	    account.setBalance(accountDTO.getBalance());
	    account.setActive(accountDTO.isActive());
	    return account;
	}










	
}
