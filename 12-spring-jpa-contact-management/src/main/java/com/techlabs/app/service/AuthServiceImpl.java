package com.techlabs.app.service;

import com.techlabs.app.entity.User;
import com.techlabs.app.exception.ApiException;
import com.techlabs.app.exception.UserException;
import com.techlabs.app.dto.LoginDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;


    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDTO loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
        
        if (!user.getIsActive()) {
            throw new UserException("Your account is inactive. Please contact Admin to make it active.");
        }

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    public String register(UserDTO userDTO) {
        if(userRepository.existsByUsername(userDTO.getUsername())){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
        }

        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setIsAdmin(userDTO.getIsAdmin());
        System.out.println("Is Admin: " + userDTO.getIsAdmin());
        user.setIsActive(true);
        userRepository.save(user);

        return "User registered successfully!.";
    }
}
