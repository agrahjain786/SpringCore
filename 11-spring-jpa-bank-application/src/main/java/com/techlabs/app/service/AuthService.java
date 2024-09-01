package com.techlabs.app.service;

import org.springframework.web.multipart.MultipartFile;

import com.techlabs.app.dto.JWTAuthResponse;
import com.techlabs.app.dto.LoginDTO;
import com.techlabs.app.dto.UserDTO;

public interface AuthService {
	
	JWTAuthResponse login(LoginDTO loginDto); //done done

    String register(UserDTO userDTO, MultipartFile file1, MultipartFile file2); //done done

	boolean isAdmin(String token, int userId); //done done

	boolean isCustomer(String token, int userId); //done done

	void requestCustomerActivation(int customerId, LoginDTO loginDto); //done done
}


//Changes done