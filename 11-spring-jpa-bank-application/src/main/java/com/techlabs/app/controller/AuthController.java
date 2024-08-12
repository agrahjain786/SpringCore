package com.techlabs.app.controller;

import com.techlabs.app.dto.JWTAuthResponse;
import com.techlabs.app.dto.LoginDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        String token = authService.login(loginDto);
        System.out.println(loginDto);
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
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
	public ResponseEntity<String> requestCustomerActivation(@PathVariable(name = "customerId") int customerId) {
		authService.requestCustomerActivation(customerId);

		return new ResponseEntity<String>("Your request to activate the customer has been sent to the admin.",HttpStatus.OK);
	}

	
    
}

//Changes done
