package com.techlabs.app.service;

import com.techlabs.app.entity.ActivationRequest;
import com.techlabs.app.entity.Admin;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.ApiException;
import com.techlabs.app.exception.UserException;
import com.techlabs.app.dto.LoginDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.repository.ActivationRequestRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private CustomerRepository customerRepository;
    private ActivationRequestRepository activationRequestRepository;
    private FileService fileService;


    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           CustomerRepository customerRepository,
                           ActivationRequestRepository activationRequestRepository,
                           FileService fileService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerRepository = customerRepository;
        this.activationRequestRepository = activationRequestRepository;
        this.fileService = fileService;
    }

    @Override
    public String login(LoginDTO loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
        
//        User user = (User) authentication.getPrincipal();
        
        if (user.getCustomer() != null && !user.getCustomer().isActive()) {
            throw new UserException("Your account is inactive. Please contact Admin to make it active.");
        }

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    @Transactional
    public String register(UserDTO userDTO, MultipartFile file1, MultipartFile file2) {

        // add check for username exists in database
        if(userRepository.existsByUsername(userDTO.getUsername())){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
        }

        // add check for email exists in database
        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

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
        
        if (userDTO.getRoles().contains("USER")) {
            Customer customer = new Customer();
            customer.setActive(true);
            customer.setTotalBalance(0.0);
            customer.setUser(user);
            user.setCustomer(customer);
        } else if (userDTO.getRoles().contains("ADMIN")) {
            Admin admin = new Admin();
            admin.setUser(user);
            user.setAdmin(admin);
        }
        
        userRepository.save(user);
        
        try {
            fileService.uploadFile(file1, user.getId());
            fileService.uploadFile(file2, user.getId());
            
        } 
        catch (UserException e) {
        	
            userRepository.delete(user);
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "User registration failed due to file upload error: " + e.getMessage());
        }

        return "User registered successfully!.";
    }

	@Override
	public void requestCustomerActivation(int customerId) {
		Customer customer = customerRepository.findById(customerId).orElseThrow(()->new UserException("No such customer with this customer Id"));
		
		if (customer.isActive()) {
            throw new UserException("Customer is already active.");
        }
		
		ActivationRequest activationRequest = new ActivationRequest();
		activationRequest.setCustomerIdOrAccountNumber(customerId);
		activationRequest.setRequestDate(LocalDateTime.now());
		activationRequest.setRequestType("CustomerActivation");
		activationRequest.setStatus("Pending");
		
		activationRequestRepository.save(activationRequest);
		
	}
}


// Changes done
