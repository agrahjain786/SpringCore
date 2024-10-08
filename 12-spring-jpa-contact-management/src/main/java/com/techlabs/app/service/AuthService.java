package com.techlabs.app.service;

import com.techlabs.app.dto.LoginDTO;
import com.techlabs.app.dto.UserDTO;

public interface AuthService {
    String login(LoginDTO loginDto);

    String register(UserDTO UserDTO);
}
