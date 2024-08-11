package com.techlabs.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.ContactDTO;
import com.techlabs.app.dto.ContactDetailsDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.dto.UserResponseDTO;
import com.techlabs.app.entity.Contact;
import com.techlabs.app.entity.ContactDetails;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.UserException;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.util.PagedResponse;

@Service
public class AdminServiceImpl implements AdminService{
	
	
	private UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder;

	public AdminServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserResponseDTO createUser(UserDTO userDTO) {
		userDTO.setId(0);
		User user = convertUserDTOToUserEntity(userDTO);
		user.setIsActive(true);
		User savedUser = userRepository.save(user);
		return convertUserEntityToUserDTO(savedUser);
	}



	@Override
	public PagedResponse<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
		
		Page<User> pages = userRepository.findAll(pageable);
		List<User> allUsers = pages.getContent();
		List<UserResponseDTO> allUsersDTO = convertUserListToDTO(allUsers);
		
		return new PagedResponse<UserResponseDTO>(allUsersDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}



	@Override
	public UserResponseDTO getUserById(int id) {
		
		User user = userRepository.findById(id).orElseThrow(()->new UserException("User not found!!"));
		
		return convertUserEntityToUserDTO(user);
	}

	@Override
	public UserResponseDTO updateUser(int id, UserDTO userDTO) {
		User user = userRepository.findById(id).orElseThrow(()->new UserException("User not found!!"));
		
		if(!user.getIsActive()) {
			throw new UserException("This user is not active. Make sure to activate this user");
		}
		
		user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setIsAdmin(userDTO.getIsAdmin());
        user.setIsActive(userDTO.getIsActive());
        
        userRepository.save(user);
        
        return convertUserEntityToUserDTO(user);
	}

	@Override
	public void deleteUser(int id) {
		User user = userRepository.findById(id).orElseThrow(()->new UserException("User not found!!"));
		
		userRepository.delete(user);
		
	}
	
	private List<UserResponseDTO> convertUserListToDTO(List<User> allUsers) {
		return allUsers.stream()
				   .map(this::convertUserEntityToUserDTO)
				   .collect(Collectors.toList());
	}

	
	private UserResponseDTO convertUserEntityToUserDTO(User user) {
		UserResponseDTO userDTO = new UserResponseDTO();
		userDTO.setId(user.getId());
	    userDTO.setFirstName(user.getFirstName());
	    userDTO.setLastName(user.getLastName());
	    userDTO.setUsername(user.getUsername());
	    userDTO.setEmail(user.getEmail());
	    userDTO.setIsAdmin(user.getIsAdmin());
	    userDTO.setIsActive(user.getIsActive());
	    
	    if (user.getContacts() != null && !user.getContacts().isEmpty()) {
	        List<ContactDTO> contactDTOs = user.getContacts()
	                                            .stream()
	                                            .map(this::convertContactToContactDTO)
	                                            .toList();
	        userDTO.setContacts(contactDTOs);
	    }

	    return userDTO;
		
	}
	
	

	private ContactDTO convertContactToContactDTO(Contact contact) {
		ContactDTO contactDTO = new ContactDTO();
		contactDTO.setId(contact.getId());
	    contactDTO.setFirstName(contact.getFirstName());
	    contactDTO.setLastName(contact.getLastName());
	    contactDTO.setIsActive(contact.getIsActive());
	    
	    if (contact.getContactDetails() != null && !contact.getContactDetails().isEmpty()) {
	        List<ContactDetailsDTO> contactDetailsDTOs = contact.getContactDetails()
	                                                            .stream()
	                                                            .map(this::convertContactDetailsToContactDetailsDTO)
	                                                            .toList();
	        contactDTO.setContactDetails(contactDetailsDTOs);
	    }

	    return contactDTO;
	}

	
	private ContactDetailsDTO convertContactDetailsToContactDetailsDTO(ContactDetails contactDetails) {
	    ContactDetailsDTO contactDetailsDTO = new ContactDetailsDTO();
	    contactDetailsDTO.setId(contactDetails.getId());
	    contactDetailsDTO.setType(contactDetails.getType());
	    contactDetailsDTO.setValue(contactDetails.getValue());

	    return contactDetailsDTO;
	}
	
	
	private User convertUserDTOToUserEntity(UserDTO userDTO) {
		User user = new User();
		
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setIsAdmin(userDTO.getIsAdmin());
        user.setIsActive(userDTO.getIsActive());
        
        return user;
	}

	
}
