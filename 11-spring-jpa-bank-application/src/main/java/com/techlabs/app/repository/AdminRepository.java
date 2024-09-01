package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
	
	long count();

}
