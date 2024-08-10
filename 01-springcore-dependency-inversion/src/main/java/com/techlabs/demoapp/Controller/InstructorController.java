package com.techlabs.demoapp.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.demoapp.model.Instructor;

@RestController
public class InstructorController {
	
	private Instructor instructor;
	
//	public InstructorController(@Qualifier(value="javaInstructor")Instructor instructor) {
//		super();
//		this.instructor = instructor;
//	}
	
	public InstructorController(Instructor instructor) {
		super();
		this.instructor = instructor;
	}
	
	@GetMapping("/train")
    public String getMessage(){
        return instructor.getTrainingPlan();
    }

}
