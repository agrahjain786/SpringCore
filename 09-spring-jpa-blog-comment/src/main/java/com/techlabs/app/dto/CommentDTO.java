package com.techlabs.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CommentDTO {
	
    private int id;
	
	@NotBlank(message = "Comment Description is mandatory")
    private String description;


}
