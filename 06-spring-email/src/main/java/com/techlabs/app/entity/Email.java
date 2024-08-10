package com.techlabs.app.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
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
	
	@Column(name ="attachment")
    private String attachment;
    
    
    
	public Email(String recipient, String msgBody, String subject, String attachment) {
		super();
		this.recipient = recipient;
		this.msgBody = msgBody;
		this.subject = subject;
		this.attachment = attachment;
	}

	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getRecipient() {
		return recipient;
	}
	
	
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	
	public String getMsgBody() {
		return msgBody;
	}
	
	
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
	
	
	public String getSubject() {
		return subject;
	}
	
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
	public String getAttachment() {
		return attachment;
	}
	
	
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
    
    

}
