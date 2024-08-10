package com.techlabs.app.service;

import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.app.entity.Email;

public interface EmailService {
	
	String sendMailWithAttachment(Email email);
	
	String uploadFile(MultipartFile file);

	List<String> getAllFiles();

	InputStreamResource downloadFile(String filename);

}
