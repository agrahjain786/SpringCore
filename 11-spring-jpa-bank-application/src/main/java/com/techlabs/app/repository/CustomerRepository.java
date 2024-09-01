package com.techlabs.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.techlabs.app.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	Page<Customer> findByActiveFalse(Pageable pageable);

	Page<Customer> findByActiveTrue(Pageable pageable);

	@Query("SELECT c FROM Customer c WHERE c.user.firstName LIKE :startWith%")
	Page<Customer> findByUserFirstNameStartingWith(String startWith, Pageable pageable);
	
	long countByActiveTrue();
    long countByActiveFalse();
    long count();

}
