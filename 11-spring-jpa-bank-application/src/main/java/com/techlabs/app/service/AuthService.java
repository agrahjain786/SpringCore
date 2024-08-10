package com.techlabs.app.service;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.app.dto.LoginDTO;
import com.techlabs.app.dto.UserDTO;

public interface AuthService {
    String login(LoginDTO loginDto);

    String register(UserDTO userDTO, MultipartFile file1, MultipartFile file2);

	void requestCustomerActivation(int customerId);
}


//Changes done