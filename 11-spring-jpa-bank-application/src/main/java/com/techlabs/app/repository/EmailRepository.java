package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, Integer>{



}
