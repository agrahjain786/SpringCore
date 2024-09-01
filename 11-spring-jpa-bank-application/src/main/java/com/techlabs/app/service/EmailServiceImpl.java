package com.techlabs.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.techlabs.app.entity.Email;
import com.techlabs.app.exception.ApiException;
import com.techlabs.app.repository.EmailRepository;

@Service
public class EmailServiceImpl implements EmailService{
	
	@Autowired 
	private JavaMailSender javaMailSender;
	
	private EmailRepository emailRepository;

	 
    public EmailServiceImpl(JavaMailSender javaMailSender, EmailRepository emailRepository) {
		super();
		this.javaMailSender = javaMailSender;
		this.emailRepository = emailRepository;
	}

	@Value("${spring.mail.username}")
    private String sender;

	@Override
	public void sendSimpleMail(String recipientEmail, String subject, String text)  throws Exception{

		try {
	        SimpleMailMessage mailMessage = new SimpleMailMessage();
	        mailMessage.setFrom(sender);
	        mailMessage.setTo(recipientEmail);
	        mailMessage.setText(text);
	        mailMessage.setSubject(subject);
	        javaMailSender.send(mailMessage);
	        
	        Email email = new Email();
	        email.setRecipient(recipientEmail);
	        email.setMsgBody(text);
	        email.setSubject(subject);
	        emailRepository.save(email);
	    } 
		catch (MailException e) {
	        throw new Exception("Error occurred while sending email");
	    } 
		catch (Exception e) {
	        throw new Exception("Error occurred while saving email record");
	    }
	}

}
