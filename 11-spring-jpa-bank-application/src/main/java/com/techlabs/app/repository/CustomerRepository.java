package com.techlabs.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	Page<Customer> findByActiveFalse(Pageable pageable);

	Page<Customer> findByActiveTrue(Pageable pageable);

}
