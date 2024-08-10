package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.ActivationRequest;

public interface ActivationRequestRepository extends JpaRepository<ActivationRequest, Integer> {

	List<ActivationRequest> findByStatusAndRequestType(String string, String string2);

}
