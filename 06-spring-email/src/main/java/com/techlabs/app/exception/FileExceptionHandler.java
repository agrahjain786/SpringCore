package com.techlabs.app.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FileExceptionHandler {
	
	@ExceptionHandler
	public ResponseEntity<FileErrorResponse> handleException(FileNotFoundException exception){
		FileErrorResponse error = new FileErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(), LocalDateTime.now());
		
		return new ResponseEntity<FileErrorResponse>(error, HttpStatus.OK);
		
	}
	
	@ExceptionHandler
	public ResponseEntity<FileErrorResponse> handleException(Exception ex){
		FileErrorResponse error = new FileErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

}
