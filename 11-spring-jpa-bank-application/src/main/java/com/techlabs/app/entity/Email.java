package com.techlabs.app.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email")
public class Email {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="id")
	private int id;
	

	@Column(name ="recipient_id")
	private String recipient;
	
	@Column(name ="message")
    private String msgBody;
	
	@Column(name ="subject")
    private String subject;
	
//	@Column(name ="attachment")
//    private String attachment;
    
    

}
