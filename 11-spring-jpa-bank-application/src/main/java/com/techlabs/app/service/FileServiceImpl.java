package com.techlabs.app.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.app.exception.UserException;

@Service
public class FileServiceImpl implements FileService{

	@Override
	public void uploadFile(MultipartFile file, int userId) {
		String directory_path = "src/main/java/com/techlabs/app/attachments/";
		
		if (file.isEmpty()) {
            throw new UserException("Please select a file to upload.");
        }

        try {
        	directory_path = directory_path + userId + "/";
            File directory = new File(directory_path);
            if (!directory.exists()) {
            	directory.mkdirs();
            }

            Path path = Paths.get(directory_path + file.getOriginalFilename());
            if (Files.exists(path)) {
            	throw new UserException("File already exists: " + file.getOriginalFilename());
            }
            Files.write(path, file.getBytes());
        } 
        catch(UserException e) {
        	throw new UserException(e.getMessage());
        }
        catch (IOException e) {
        	
            throw new UserException("Could not upload the file! Error Occurred. Please try again later");
        }
		
	}

	@Override
	public List<byte[]> getFiles(int customerId) {
		String directory_path = "src/main/java/com/techlabs/app/attachments/"+customerId+"/";
		
		try {
		
			File directory = new File(directory_path);
			
			if (!directory.exists() || !directory.isDirectory()) {
	            throw new UserException("Directory does not exist for customerId: " + customerId);
	        }
	
	        List<byte[]> fileContents = new ArrayList<>();
	        
	        // List all files in the directory and read them
	        File[] files = directory.listFiles(File::isFile);
	        if(files == null) {
	        	throw new UserException("No files Uploaded!!");
	        }
			for (File file : files) {
				try {
					byte[] content = Files.readAllBytes(file.toPath());
					fileContents.add(content);
				} catch (IOException e) {
					throw new UserException("Error reading file: " + file.getName());
				}
			}
	
			return fileContents;
		}
		catch (UserException e){
			throw new UserException(e.getMessage());
		}
		catch(Exception e) {
			throw new UserException("Unexpected Error occurred");
		}
	}

}
