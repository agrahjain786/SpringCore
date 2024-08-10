package com.techlabs.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.app.entity.Email;
import com.techlabs.app.repository.EmailRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService{
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	private EmailRepository emailRepository;
	
	@Value("${spring.mail.username}") 
	private String sender;
	
	public EmailServiceImpl(EmailRepository emailRepository) {
		super();
		this.emailRepository = emailRepository;
	}

	
	@Override
	public String sendMailWithAttachment(Email email) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		
		try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(email.getRecipient());
            mimeMessageHelper.setText(email.getMsgBody());
            mimeMessageHelper.setSubject(email.getSubject());
 
            FileSystemResource file = new FileSystemResource(new File(email.getAttachment()));
 
            mimeMessageHelper.addAttachment(file.getFilename(), file);
 
            javaMailSender.send(mimeMessage);
            emailRepository.save(email);
            return "Mail sent Successfully";
        }
        catch (MessagingException e) {
            return "Error while sending mail!!!";
        }
	}


	@Override
	public String uploadFile(MultipartFile file) {
		String directory_path = "src/main/java/com/techlabs/app/attachments/";
		
		if (file.isEmpty()) {
            return "Please select a file to upload.";
        }

        try {
            File directory = new File(directory_path);
            if (!directory.exists()) {
            	directory.mkdirs();
            }

            Path path = Paths.get(directory_path + file.getOriginalFilename());
            if (Files.exists(path)) {
                return "File already exists: " + file.getOriginalFilename();
            }
            Files.write(path, file.getBytes());
            
            return "File uploaded successfully: " + file.getOriginalFilename();
        } 
        catch (IOException e) {
        	
            return "Could not upload the file: " + e.getMessage();
        }
	}


	@Override
	public List<String> getAllFiles() {
		String directory_path = "src/main/java/com/techlabs/app/attachments/";
		File directory = new File(directory_path);
		if (!directory.exists() || !directory.isDirectory()) {
            return null;
        }

        String[] filenames = directory.list();
        if (filenames == null) {
            return null;
        }

        List<String> files = Arrays.asList(filenames);
        return files;
	}


	@Override
	public InputStreamResource downloadFile(String filename) {
		
		String directory_path = "src/main/java/com/techlabs/app/attachments/";
		
		try {
            Path path = Paths.get(directory_path + filename);
            if (!Files.exists(path)) {
                return null;
            }

            File file = path.toFile();
            return new InputStreamResource(new FileInputStream(file));

        } 
		catch (IOException e) {
			throw new RuntimeException("Error accessing file: " + filename);
        }
	}
	

	
	
	
}
