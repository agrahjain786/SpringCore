package com.techlabs.app.dto;

import java.time.LocalDateTime;
import java.util.List;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class BlogDTO {
	
	
    private int id;
	
	@NotBlank(message = "Title is mandatory")
	@Size(max = 100, message = "Title size should be less than 100 characters")
    private String title;
    
	@NotBlank(message = "Category is mandatory")
	@Size(max = 255, message = "Category size should be less than 255 characters")
    private String category;
    
	@NotBlank(message = "Data is mandatory")
    private String data;
    
    private LocalDateTime publishedDate;
    
    private boolean published;
	

    private List<@Valid CommentDTO> comments;

}
