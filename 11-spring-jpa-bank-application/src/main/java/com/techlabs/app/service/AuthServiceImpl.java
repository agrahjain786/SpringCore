package com.techlabs.app.service;

import com.techlabs.app.entity.ActivationRequest;
import com.techlabs.app.entity.Admin;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.ApiException;
import com.techlabs.app.exception.UserException;
import com.techlabs.app.dto.JWTAuthResponse;
import com.techlabs.app.dto.LoginDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.repository.ActivationRequestRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.security.JwtTokenProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    
    @Value("${app.jwt-secret}")
    private String secretKey;

    @Override
    public JWTAuthResponse login(LoginDTO loginDto) {
    	try {
	        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
	                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
	
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        
	        String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
		            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
		    
		    
	        
	        if (user.getCustomer() != null && !user.getCustomer().isActive()) {
	            throw new UserException("Your account is inactive. Please contact Admin to make it active.");
	        }
	        
	        String token = jwtTokenProvider.generateToken(authentication);
	        
	        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
	        jwtAuthResponse.setAccessToken(token);
	        jwtAuthResponse.setId(user.getId());
	        if(user.getAdmin() != null) jwtAuthResponse.setRole("Admin");
	        if(user.getCustomer() != null) jwtAuthResponse.setRole("Customer");
	
	        return jwtAuthResponse;
        }
    	catch (BadCredentialsException e) {
            throw new UserException("Invalid username or password.");
        } 
    	catch (UserException e) {
            throw new UserException(e.getMessage());
        } 
    	catch (Exception e) {
            throw new UserException("Login failed! Please try again later");
        }
    }

    @Override
    @Transactional
    public String register(UserDTO userDTO, MultipartFile file1, MultipartFile file2){
    	
    	try {

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
	            throw new ApiException(HttpStatus.BAD_REQUEST, "User registration failed due to file upload error: " + e.getMessage());
	        }
	
	        return "User registered successfully!.";
    	}
    	catch(RuntimeException e){
    		throw new UserException(e.getMessage());
    	}
    	catch(Exception e) {
    		throw new UserException("An Unexpected error Occurred! Please try again later");
    	}
    }


	@Override
	public boolean isAdmin(String token, int userId) {
		Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
		Integer tokenUserId = claims.get("id", Integer.class);
	    if (tokenUserId == null || !tokenUserId.equals(userId)) {
	        return false;
	    }
		List<Map<String, Object>> roles = claims.get("roles", List.class);
        System.out.println("Roles: " + roles);
        return roles != null && roles.stream()
                .map(role -> (String) role.get("name"))
                .anyMatch(roleName -> "ROLE_ADMIN".equals(roleName));
	}

	@Override
	public boolean isCustomer(String token, int userId) {
		Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
		List<Map<String, Object>> roles = claims.get("roles", List.class);
		Integer tokenUserId = claims.get("id", Integer.class);
	    if (tokenUserId == null || !tokenUserId.equals(userId)) {
	        return false;
	    }
        System.out.println("Roles: " + roles);
        return roles != null && roles.stream()
                .map(role -> (String) role.get("name"))
                .anyMatch(roleName -> "ROLE_USER".equals(roleName));
	}

	@Override
	public void requestCustomerActivation(int customerId, LoginDTO loginDto) {
		try {
	        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
	                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
	
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        
	        String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
		            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
		    
		    if(user.getCustomer() == null) {
		    	throw new UserException("Invalid username or password to make the request");
		    }
	        
	        if (user.getCustomer() != null && user.getCustomer().isActive()) {
	            throw new UserException("Customer with this email and password are already active");
	        }
	        
	        Customer customer = customerRepository.findById(customerId).orElseThrow(()->new UserException("No such customer with this customer Id"));
			
	        if(customer.getUser() != user) {
	        	throw new UserException("This customerId does not belongs to this user");
	        }
	        
	        boolean pendingRequestExists = activationRequestRepository.existsByCustomerIdOrAccountNumberAndStatus(customerId, "Pending");
		    if (pendingRequestExists) {
		        throw new UserException("An activation request for this account is already made and its status is still Pending. Wait for some time to activate this account");
		    }
			
			ActivationRequest activationRequest = new ActivationRequest();
			activationRequest.setCustomerIdOrAccountNumber(customerId);
			activationRequest.setRequestDate(LocalDateTime.now());
			activationRequest.setRequestType("CustomerActivation");
			activationRequest.setStatus("Pending");
			
			activationRequestRepository.save(activationRequest);
			
			
        }
		catch (BadCredentialsException e) {
            throw new UserException("Invalid username or password.");
        } 
    	catch (UserException e) {
            throw new UserException(e.getMessage());
        } 
    	catch (Exception e) {
            throw new UserException("Error Occurred! Please try again later");
        }
		
	}
}


// Changes done
