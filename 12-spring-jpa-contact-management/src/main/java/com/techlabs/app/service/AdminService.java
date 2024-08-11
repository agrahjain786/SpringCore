package com.techlabs.app.service;

import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.dto.UserResponseDTO;
import com.techlabs.app.util.PagedResponse;


public interface AdminService {

	UserResponseDTO createUser(UserDTO userDTO);

	PagedResponse<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String direction);

	UserResponseDTO getUserById(int id);

	UserResponseDTO updateUser(int id,UserDTO userDTO);

	void deleteUser(int id);

}
