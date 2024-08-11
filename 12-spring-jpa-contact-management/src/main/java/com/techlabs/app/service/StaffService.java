package com.techlabs.app.service;

import com.techlabs.app.dto.ContactDTO;
import com.techlabs.app.dto.ContactDetailsDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.dto.UserResponseDTO;
import com.techlabs.app.util.PagedResponse;


public interface StaffService {

	UserResponseDTO createContact(ContactDTO contactDTO);

	PagedResponse<ContactDTO> getAllContacts(int page, int size, String sortBy, String direction);

	ContactDTO getContactById(int id);

	ContactDTO updateContact(int id,ContactDTO contactDTO);

	void deleteContact(int id);

	ContactDTO createContactDetail(int contactId,ContactDetailsDTO contactDetailDTO);

	PagedResponse<ContactDetailsDTO> getAllContactDetails(int contactId, int page, int size, String sortBy,
			String direction);

	ContactDetailsDTO getContactDetailById(int contactId, int id);

	ContactDTO updateContactDetail(int contactId, int id,ContactDetailsDTO contactDetailDTO);

	void deleteContactDetail(int contactId, int id);

}
