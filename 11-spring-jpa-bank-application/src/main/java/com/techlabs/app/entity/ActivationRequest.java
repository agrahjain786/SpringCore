package com.techlabs.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "activation_request")
public class ActivationRequest {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

	@NotNull
    @Column(name = "id_or_number", nullable = false)
    private int customerIdOrAccountNumber;

	@NotBlank
    @Column(name = "request_type", nullable = false)
    private String requestType;

	@NotNull
    @Column(nullable = false)
    private LocalDateTime requestDate;

    @NotBlank
    @Column(nullable = false)
    private String status;

}
