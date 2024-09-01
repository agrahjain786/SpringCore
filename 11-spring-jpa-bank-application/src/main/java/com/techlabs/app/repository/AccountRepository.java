package com.techlabs.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.Customer;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

	Page<Account> findByActiveTrue(Pageable pageable);

	Page<Account> findByActiveFalse(Pageable pageable);

	Page<Account> findByCustomer(Customer customer, Pageable pageable);
	
	long countByActiveTrue();
    long countByActiveFalse();
    long count();


}
