package com.techlabs.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

	Page<Account> findByActiveTrue(Pageable pageable);

	Page<Account> findByActiveFalse(Pageable pageable);

}
