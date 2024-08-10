package com.techlabs.app.controller;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.app.entity.Email;
import com.techlabs.app.exception.FileNotFoundException;
import com.techlabs.app.service.EmailService;



@RestController
public class EmailController {
	
	private EmailService emailService;

	
	public EmailController(EmailService emailService) {
		super();
		this.emailService = emailService;
	}
	
	
	@PostMapping("/send/mail")
    public ResponseEntity<String> sendMailWithAttachment(@RequestBody Email email) {
 
        String status = emailService.sendMailWithAttachment(email);
        
        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
	
	
	
	@PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		
		String status = emailService.uploadFile(file);
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
	
	
	
	@GetMapping("/files")
    public ResponseEntity<List<String>> listFiles() {
		
		List<String> files = emailService.getAllFiles();
		if(files == null) {
			throw new FileNotFoundException("No files found");
		}
		
		return new ResponseEntity<List<String>>(files, HttpStatus.OK);
	}
	
	
	
	@GetMapping(value ="/download/{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable(name="filename") String filename) {
		
		InputStreamResource resource = emailService.downloadFile(filename);

		if (resource == null) {
			throw new FileNotFoundException("No such file exists");
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

}
