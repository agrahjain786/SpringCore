package com.techlabs.app.controller;

import com.techlabs.app.dto.JWTAuthResponse;
import com.techlabs.app.dto.LoginDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Build Login REST API
    @Operation(summary = "By Anyone: Login the user or admin if have registered and if active")
    @PostMapping(value = {"/login"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDTO loginDto){
    	JWTAuthResponse jwtAuthResponse = authService.login(loginDto);
        System.out.println(loginDto);

        String token = jwtAuthResponse.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);


        JWTAuthResponse responseBody = new JWTAuthResponse();
        responseBody.setTokenType(jwtAuthResponse.getTokenType());
        responseBody.setRole(jwtAuthResponse.getRole());
        responseBody.setId(jwtAuthResponse.getId());

        return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
    }

    // Build Register REST API
    @Operation(summary = "By Anyone: Register for the role of customer and upload the files for verification")
    @PostMapping(value = {"/register"})
    public ResponseEntity<String> register(@ModelAttribute UserDTO userDto, @RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2){
        String response = authService.register(userDto, file1, file2);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    
    @Operation(summary = "By Anyone: Make the Request to the admin to make the customer active")
    @PostMapping("/request-activation/{customerId}")
	public ResponseEntity<String> requestCustomerActivation(@PathVariable(name = "customerId") int customerId, @RequestBody LoginDTO loginDto) {
		authService.requestCustomerActivation(customerId, loginDto);

		return new ResponseEntity<String>("Your request to activate the customer has been sent to the admin.",HttpStatus.OK);
	}
    
    
    @Operation(summary = "By Anyone: Verify Admin")
    @GetMapping(value = {"/verify/admin/{userId}"})
    public ResponseEntity<Boolean> isAdmin(@PathVariable(name = "userId")int userId, @RequestHeader("Authorization") String token){
    	System.out.println("Received Token: " + token);
    	
    	if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
    	
    	boolean adminOrNot = authService.isAdmin(token , userId);
        System.out.println(adminOrNot);

        return ResponseEntity.ok(adminOrNot);
    }
    
    
    @Operation(summary = "By Anyone: Verify Customer")
    @GetMapping(value = {"/verify/customer/{userId}"})
    public ResponseEntity<Boolean> isCustomer(@PathVariable(name = "userId")int userId, @RequestHeader("Authorization") String token){
    	System.out.println("Received Token: " + token);
    	
    	if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
    	boolean customerOrNot = authService.isCustomer(token, userId);
        System.out.println(customerOrNot);

        return ResponseEntity.ok(customerOrNot);
    }

	
    
}

//Changes done
