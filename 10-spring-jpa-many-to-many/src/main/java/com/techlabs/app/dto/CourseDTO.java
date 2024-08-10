package com.techlabs.app.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDTO {
	
	private Long id;
    private String title;
    private Set<StudentDTO> students;
	public CourseDTO(Long id, String title) {
		super();
		this.id = id;
		this.title = title;
	}
    
    

}
