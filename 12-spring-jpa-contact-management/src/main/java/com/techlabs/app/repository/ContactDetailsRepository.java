package com.techlabs.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.Contact;
import com.techlabs.app.entity.ContactDetails;

public interface ContactDetailsRepository extends JpaRepository<ContactDetails, Integer> {

	Page<ContactDetails> findByContact(Contact contact, Pageable pageable);

}
