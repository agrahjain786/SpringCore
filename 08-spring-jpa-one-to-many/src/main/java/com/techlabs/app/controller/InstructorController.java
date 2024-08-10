package com.techlabs.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.entity.Course;
import com.techlabs.app.entity.Instructor;
import com.techlabs.app.service.InstructorService;


@RestController
@RequestMapping("/api/instructor")
public class InstructorController {
	
	private InstructorService instructorService;

	public InstructorController(InstructorService instructorService) {
		super();
		this.instructorService = instructorService;
	}
	
	@GetMapping
	public List<Instructor> getAllInstructors(){
		return instructorService.findAllInstructors();
	}
	
	
	@PostMapping
	public Instructor addInstructor(@RequestBody Instructor instructor) {
		instructor.setId(0);
		return instructorService.saveInstructor(instructor);
	}
	
	@PostMapping("/course/add")
	public Course addCourse(@RequestBody Course course) {
		course.setId(0);
		return instructorService.saveCourse(course);
	}
	
	@GetMapping("/{id}")
	public Instructor getInstructorById(@PathVariable(name="id")int id){
		return instructorService.getInstructorById(id);
		
	}
	
	@PutMapping
	public Instructor updateInstructor(@RequestBody Instructor instructor) {
		Instructor existedInstructor = instructorService.getInstructorById(instructor.getId());
		if(existedInstructor != null) {
			return instructorService.saveInstructor(instructor);
		}
		return null;
	}
	
	@DeleteMapping("/{id}")
	public void deleteInstructor(@PathVariable(name="id")int id) {
		Instructor existedInstructor = instructorService.getInstructorById(id);
		if(existedInstructor != null) {
			instructorService.deleteEmployee(existedInstructor);
		}
	}
	
	@PutMapping("/{insId}/course/{courseId}")
	public Instructor assignInstructorToCourse(@PathVariable(name="insId")int instructorId, @PathVariable(name="courseId")int courseId) {
		return instructorService.assignInstructorToCourse(instructorId, courseId);
		
	}
	
	@PutMapping("/{insId}/particular/course/{courseId}")
	public Instructor unassignSpecificInstructorToCourse(@PathVariable(name="insId")int instructorId, @PathVariable(name="courseId")int courseId) {
		return instructorService.unassignSpecificInstructorToCourse(instructorId, courseId);
		
	}
	
	@PutMapping("/course/{courseId}")
	public Course deassignInstructorToCourse(@PathVariable(name="courseId")int courseId) {
		return instructorService.deassignInstructorToCourse(courseId);
		
	}

}
