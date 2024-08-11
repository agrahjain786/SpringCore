package com.techlabs.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.dto.UserResponseDTO;
import com.techlabs.app.service.AdminService;
import com.techlabs.app.util.PagedResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class AdminController {
	
	private AdminService adminService;

	public AdminController(AdminService adminService) {
		super();
		this.adminService = adminService;
	}
	
	@PostMapping
	public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
		
		UserResponseDTO createdUser = adminService.createUser(userDTO);
		
		return new ResponseEntity<UserResponseDTO>(createdUser, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<PagedResponse<UserResponseDTO>> getAllUsers(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction){
		
		PagedResponse<UserResponseDTO> users = adminService.getAllUsers(page,size,sortBy,direction);
		
		return new ResponseEntity<PagedResponse<UserResponseDTO>>(users, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable(name = "id") int id) {
		UserResponseDTO user = adminService.getUserById(id);
		
		return new ResponseEntity<UserResponseDTO>(user, HttpStatus.OK);
	}

	
	@PutMapping("/{id}")
	public ResponseEntity<UserResponseDTO> updateUser(@PathVariable(name = "id") int id,@Valid @RequestBody UserDTO userDTO) {
		
		UserResponseDTO updatedUser = adminService.updateUser(id, userDTO);
		
		return new ResponseEntity<UserResponseDTO>(updatedUser, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable(name = "id") int id) {
		adminService.deleteUser(id);
		return new ResponseEntity<>("User Deleted Successfully", HttpStatus.OK);
	}

}
