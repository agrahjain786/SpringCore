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

import com.techlabs.app.dto.ContactDTO;
import com.techlabs.app.dto.ContactDetailsDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.dto.UserResponseDTO;
import com.techlabs.app.service.StaffService;
import com.techlabs.app.util.PagedResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contacts")
public class StaffController {
	
	private StaffService staffService;

	public StaffController(StaffService staffService) {
		super();
		this.staffService = staffService;
	}
	
	
	@PostMapping
	public ResponseEntity<UserResponseDTO> createContact(@Valid @RequestBody ContactDTO contactDTO) {
		UserResponseDTO user = staffService.createContact(contactDTO);
		return new ResponseEntity<UserResponseDTO>(user, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<PagedResponse<ContactDTO>> getAllContacts(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {

		PagedResponse<ContactDTO> contacts = staffService.getAllContacts(page, size, sortBy, direction);
		return new ResponseEntity<PagedResponse<ContactDTO>>(contacts, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ContactDTO> getContactById(@PathVariable(name = "id") int id) {
		
		ContactDTO contact = staffService.getContactById(id);
		
		return new ResponseEntity<ContactDTO>(contact, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ContactDTO> updateContact(@PathVariable(name = "id") int id,
			@Valid @RequestBody ContactDTO contactDTO) {
		
		ContactDTO updatedContact = staffService.updateContact(id, contactDTO);
		
		return new ResponseEntity<ContactDTO>(updatedContact, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteContact(@PathVariable(name = "id") int id) {
		
		staffService.deleteContact(id);
		return new ResponseEntity<>("Contact Deleted Successfully", HttpStatus.OK);
	}
	
	

	@PostMapping("/{contactId}/details")
	public ResponseEntity<ContactDTO> createContactDetail(@PathVariable(name = "contactId") int contactId,
			@Valid @RequestBody ContactDetailsDTO contactDetailDTO) {
		
		ContactDTO createdDetail = staffService.createContactDetail(contactId, contactDetailDTO);
		
		return new ResponseEntity<>(createdDetail, HttpStatus.CREATED);
	}

	@GetMapping("/{contactId}/details")
	public ResponseEntity<PagedResponse<ContactDetailsDTO>> getAllContactDetails(
			@PathVariable(name = "contactId") int contactId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {

		PagedResponse<ContactDetailsDTO> details = staffService.getAllContactDetails(contactId, page, size, sortBy, direction);
		return new ResponseEntity<PagedResponse<ContactDetailsDTO>>(details, HttpStatus.OK);
	}

	@GetMapping("/{contactId}/contact-details/{id}")
	public ResponseEntity<ContactDetailsDTO> getContactDetailById(@PathVariable(name = "contactId") int contactId,@PathVariable(name = "id") int id) {
		
		ContactDetailsDTO detail = staffService.getContactDetailById(contactId, id);
		
		return new ResponseEntity<ContactDetailsDTO>(detail, HttpStatus.OK);
	}

	@PutMapping("/{contactId}/contact-details/{id}")
	public ResponseEntity<ContactDTO> updateContactDetail(@PathVariable(name = "contactId") int contactId, @PathVariable(name = "id") int id,
			@Valid @RequestBody ContactDetailsDTO contactDetailDTO) {
		
		ContactDTO updatedDetail = staffService.updateContactDetail(contactId, id, contactDetailDTO);
		
		return new ResponseEntity<ContactDTO>(updatedDetail, HttpStatus.OK);
	}

	@DeleteMapping("/{contactId}/contact-details/{id}")
	public ResponseEntity<String> deleteContactDetail(@PathVariable(name = "contactId") int contactId, @PathVariable(name = "id") int id) {
		
		staffService.deleteContactDetail(contactId, id);
		return new ResponseEntity<String>("Contact Detail Deleted Successfully", HttpStatus.OK);
	}
	

}
