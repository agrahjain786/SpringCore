package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.entity.Course;
import com.techlabs.app.entity.Instructor;

public interface InstructorService {

	List<Instructor> findAllInstructors();

	Instructor saveInstructor(Instructor instructor);
	
	Course saveCourse(Course course);

	Instructor getInstructorById(int id);

	void deleteEmployee(Instructor instructor);

	Instructor assignInstructorToCourse(int instructorId, int courseId);

	Course deassignInstructorToCourse(int courseId);

	Instructor unassignSpecificInstructorToCourse(int instructorId, int courseId);

	

}
