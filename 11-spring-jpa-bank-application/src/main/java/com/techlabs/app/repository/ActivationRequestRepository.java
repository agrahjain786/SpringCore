package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techlabs.app.entity.ActivationRequest;

public interface ActivationRequestRepository extends JpaRepository<ActivationRequest, Integer> {

	List<ActivationRequest> findByStatusAndRequestType(String string, String string2);
	
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM ActivationRequest a WHERE (a.customerIdOrAccountNumber = :customerIdOrAccountNumber) AND a.status = :status")
    boolean existsByCustomerIdOrAccountNumberAndStatus( @Param("customerIdOrAccountNumber") int customerIdOrAccountNumber,@Param("status") String status);
    
	long countByStatusAndRequestType(String status, String requestType);
    long countByStatus(String status);

}
