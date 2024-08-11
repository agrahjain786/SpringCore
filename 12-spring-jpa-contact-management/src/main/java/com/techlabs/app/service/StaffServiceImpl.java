package com.techlabs.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.ContactDTO;
import com.techlabs.app.dto.ContactDetailsDTO;
import com.techlabs.app.dto.UserDTO;
import com.techlabs.app.dto.UserResponseDTO;
import com.techlabs.app.entity.Contact;
import com.techlabs.app.entity.ContactDetails;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.ContactException;
import com.techlabs.app.exception.UserException;
import com.techlabs.app.repository.ContactDetailsRepository;
import com.techlabs.app.repository.ContactRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.util.PagedResponse;

@Service
public class StaffServiceImpl implements StaffService{
	
	private ContactRepository contactRepository;
	
	private UserRepository userRepository;
	
	private ContactDetailsRepository contactDetailsRepository;

	public StaffServiceImpl(ContactRepository contactRepository, UserRepository userRepository, ContactDetailsRepository contactDetailsRepository) {
		super();
		this.contactRepository = contactRepository;
		this.userRepository = userRepository;
		this.contactDetailsRepository = contactDetailsRepository;
	}

	@Override
	public UserResponseDTO createContact(ContactDTO contactDTO) {
		
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
		
		Contact contact = convertContactDTOToEntity(contactDTO);
		contact.setUser(user);
		contact.setIsActive(true);
		user.getContacts().add(contact);
		User savedUser = userRepository.save(user);
        return convertUserEntityToUserDTO(savedUser);
	}


	@Override
	public PagedResponse<ContactDTO> getAllContacts(int page, int size, String sortBy, String direction) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
	    
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
		
		Page<Contact> pages = contactRepository.findByUser(user, pageable);
		List<Contact> allContacts = pages.getContent();
		List<ContactDTO> allContactsDTO = convertContactListToDTO(allContacts);
		
		return new PagedResponse<ContactDTO>(allContactsDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}


	@Override
	public ContactDTO getContactById(int id) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
	    
	    Contact contact = contactRepository.findById(id).orElseThrow(()->new ContactException("No contact found with this id"));
	    
	    if(!contact.getUser().equals(user)) {
	    	throw new UserException("This user does not have contact with this ID");
	    }
	    
	    return convertContactEntityToDTO(contact);
	}

	@Override
	public ContactDTO updateContact(int id, ContactDTO contactDTO) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
	    
	    Contact contact = contactRepository.findById(id)
	            .orElseThrow(() -> new ContactException("No contact found with this id"));

	    if (!contact.getUser().equals(user)) {
	        throw new UserException("This user does not have a contact with this ID");
	    }
	    
	    contact.setFirstName(contactDTO.getFirstName());
	    contact.setLastName(contactDTO.getLastName());
	    contact.setIsActive(contactDTO.getIsActive());
	  
	    Contact updatedContact = contactRepository.save(contact);

	    return convertContactEntityToDTO(updatedContact);
	}

	@Override
	public void deleteContact(int id) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));

	    Contact contact = contactRepository.findById(id)
	            .orElseThrow(() -> new ContactException("No contact found with this id"));

	    if (!contact.getUser().equals(user)) {
	        throw new UserException("This user does not have a contact with this ID");
	    }

	    contactRepository.delete(contact);
		
	}

	@Override
	public ContactDTO createContactDetail(int contactId, ContactDetailsDTO contactDetailDTO) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));

	    Contact contact = contactRepository.findById(contactId)
	            .orElseThrow(() -> new ContactException("No contact found with this id"));
	  

	    if (!contact.getUser().equals(user)) {
	        throw new UserException("This user does not have a contact with this ID");
	    }
	    
	    if(!contact.getIsActive()) {
	    	throw new UserException("This contact is inactive. Make suer to activate the contact");
	    }
	    
	    ContactDetails contactDetails = convertContactDetailsDTOToEntity(contactDetailDTO);
	    contactDetails.setContact(contact);
	    contact.getContactDetails().add(contactDetails);
	    Contact updatedContact = contactRepository.save(contact);

	    return convertContactEntityToDTO(updatedContact);
	}


	@Override
	public PagedResponse<ContactDetailsDTO> getAllContactDetails(int contactId, int page, int size, String sortBy,
			String direction) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
	    
	    Contact contact = contactRepository.findById(contactId)
	            .orElseThrow(() -> new ContactException("No contact found with this id"));

	    if (!contact.getUser().equals(user)) {
	        throw new UserException("This user does not have a contact with this ID");
	    }
	    
	    Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
		
		Page<ContactDetails> pages = contactDetailsRepository.findByContact(contact, pageable);
		List<ContactDetails> allContactDetails = pages.getContent();
		List<ContactDetailsDTO> allContactDetailsDTO = convertContactDetailsListToDTO(allContactDetails);
		
		return new PagedResponse<ContactDetailsDTO>(allContactDetailsDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}


	@Override
	public ContactDetailsDTO getContactDetailById(int contactId,int id) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
	    
	    ContactDetails contactDetails = contactDetailsRepository.findById(id).orElseThrow(()->new ContactException("No such Contact details found with this id"));
	    
	    Contact contact = contactRepository.findById(contactId)
	            .orElseThrow(() -> new ContactException("No contact found with this id"));
	 
	    
	    if (!contact.getUser().equals(user)) {
	        throw new UserException("This user does not have a contact with this ID");
	    }
	    
	    if(!contactDetails.getContact().equals(contact)) {
	    	throw new ContactException("This contact has not this contact details id");
	    }
	    
	    return convertContactDetailsEntityToDTO(contactDetails);
	}



	@Override
	public ContactDTO updateContactDetail(int contactId, int id, ContactDetailsDTO contactDetailDTO) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));
	    
	    ContactDetails contactDetails = contactDetailsRepository.findById(id).orElseThrow(()->new ContactException("No such Contact details found with this id"));
	    
	    Contact contact = contactRepository.findById(contactId)
	            .orElseThrow(() -> new ContactException("No contact found with this id"));
	 
	    
	    if (!contact.getUser().equals(user)) {
	        throw new UserException("This user does not have a contact with this ID");
	    }
	    
	    if(!contactDetails.getContact().equals(contact)) {
	    	throw new ContactException("This contact has not this contact details id");
	    }
	    
	    if(!contact.getIsActive()) {
	    	throw new UserException("This contact is inactive. Make sure to activate the contact");
	    }
	    
	    
	    contactDetails.setType(contactDetailDTO.getType());
	    contactDetails.setValue(contactDetailDTO.getValue());
	    
	    ContactDetails updatedContactDetail = contactDetailsRepository.save(contactDetails);
	    
	    return convertContactEntityToDTO(updatedContactDetail.getContact());
	}

	@Override
	public void deleteContactDetail(int contactId,int id) {
		String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	            .orElseThrow(() -> new UserException("User not found with username or email: " + usernameOrEmail));

	    ContactDetails contactDetails = contactDetailsRepository.findById(id).orElseThrow(()->new ContactException("No such Contact details found with this id"));
	    
	    Contact contact = contactRepository.findById(contactId)
	            .orElseThrow(() -> new ContactException("No contact found with this id"));
	 
	    
	    if (!contact.getUser().equals(user)) {
	        throw new UserException("This user does not have a contact with this ID");
	    }
	    
	    if(!contactDetails.getContact().equals(contact)) {
	    	throw new ContactException("This contact has not this contact details id");
	    }
	    contactDetailsRepository.delete(contactDetails);
		
	}
	

	private List<ContactDetailsDTO> convertContactDetailsListToDTO(List<ContactDetails> allContactDetails) {
		return allContactDetails.stream()
		        .map(this::convertContactDetailsEntityToDTO)
		        .collect(Collectors.toList());
	}
	

	private ContactDetails convertContactDetailsDTOToEntity(ContactDetailsDTO contactDetailDTO) {
		ContactDetails contactDetails = new ContactDetails();
	    contactDetails.setId(contactDetailDTO.getId());
	    contactDetails.setType(contactDetailDTO.getType());
	    contactDetails.setValue(contactDetailDTO.getValue());
	    return contactDetails;
	}
	
	private ContactDetailsDTO convertContactDetailsEntityToDTO(ContactDetails contactDetails) {
		ContactDetailsDTO contactDetailsDTO = new ContactDetailsDTO();
	    contactDetailsDTO.setId(contactDetails.getId());
	    contactDetailsDTO.setType(contactDetails.getType());
	    contactDetailsDTO.setValue(contactDetails.getValue());
	    return contactDetailsDTO;
	}
	

	private List<ContactDTO> convertContactListToDTO(List<Contact> allContacts) {
		return allContacts.stream()
                .map(this::convertContactEntityToDTO)
                .collect(Collectors.toList());
	}

	private ContactDTO convertContactEntityToDTO(Contact contact) {
		 ContactDTO contactDTO = new ContactDTO();
		    contactDTO.setId(contact.getId());
		    contactDTO.setFirstName(contact.getFirstName());
		    contactDTO.setLastName(contact.getLastName());
		    contactDTO.setIsActive(contact.getIsActive());
		    
		    if (contact.getContactDetails() != null) {
		        List<ContactDetailsDTO> contactDetailsDTOList = contact.getContactDetails().stream()
		            .map(this::convertContactDetailsEntityToDTO)
		            .collect(Collectors.toList());
		        contactDTO.setContactDetails(contactDetailsDTOList);
		    }
		    
		    return contactDTO;
	    
	}

	private Contact convertContactDTOToEntity(ContactDTO contactDTO) {
		Contact contact = new Contact();
	    contact.setId(contactDTO.getId());
	    contact.setFirstName(contactDTO.getFirstName());
	    contact.setLastName(contactDTO.getLastName());
	    contact.setIsActive(contactDTO.getIsActive());
	    
	    if (contactDTO.getContactDetails() != null) {
	        List<ContactDetails> contactDetailsList = contactDTO.getContactDetails().stream()
	            .map(this::convertContactDetailsDTOToEntity)
	            .collect(Collectors.toList());
	        contact.setContactDetails(contactDetailsList);
	    }
	    
	    return contact;
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
	                                            .map(this::convertContactEntityToDTO)
	                                            .toList();
	        userDTO.setContacts(contactDTOs);
	    }

	    return userDTO;
		
	}

}
