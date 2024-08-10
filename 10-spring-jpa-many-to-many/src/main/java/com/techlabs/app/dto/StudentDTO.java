package com.techlabs.app.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDTO {
	
	private Long id;
    private String name;
    private Set<CourseDTO> courses;
    
    
	public StudentDTO(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
    
    

}
