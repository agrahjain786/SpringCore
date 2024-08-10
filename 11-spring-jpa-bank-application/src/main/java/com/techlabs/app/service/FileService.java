package com.techlabs.app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	void uploadFile(MultipartFile file1, int userId);

	List<byte[]> getFiles(int customerId);
	
	

}
