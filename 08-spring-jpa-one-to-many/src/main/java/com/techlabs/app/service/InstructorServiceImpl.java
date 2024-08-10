package com.techlabs.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.techlabs.app.entity.Course;
import com.techlabs.app.entity.Instructor;
import com.techlabs.app.repository.CourseRepository;
import com.techlabs.app.repository.InstructorRepository;

@Service
public class InstructorServiceImpl implements InstructorService{
	
	private InstructorRepository instructorRepository;
	
	private CourseRepository courseRepository;

	
	public InstructorServiceImpl(InstructorRepository instructorRepository, CourseRepository courseRepository) {
		super();
		this.instructorRepository = instructorRepository;
		this.courseRepository = courseRepository;
	}
	

	@Override
	public List<Instructor> findAllInstructors() {
		return instructorRepository.findAll();
	}

	@Override
	public Instructor saveInstructor(Instructor instructor) {
		return instructorRepository.save(instructor);
	}
	
	@Override
	public Course saveCourse(Course course) {
		// TODO Auto-generated method stub
		return courseRepository.save(course);
	}


	@Override
	public Instructor getInstructorById(int id) {
		return instructorRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteEmployee(Instructor instructor) {
		for(Course c: instructor.getCourses()) {
			c.setInstructor(null);
		}
		instructorRepository.delete(instructor);
		
	}


	@Override
	public Instructor assignInstructorToCourse(int instructorId, int courseId) {
		Instructor instructor = instructorRepository.findById(instructorId).orElse(null);
		if(instructor != null) {
			Course course = courseRepository.findById(courseId).orElse(null);
			if(course != null) {
				if(course.getInstructor() == null) {
					instructor.addCourse(course);
					instructorRepository.save(instructor);
					return instructor;
				}
			}
		}
		return null;
	}


	@Override
	public Course deassignInstructorToCourse(int courseId) {
		Course course = courseRepository.findById(courseId).orElse(null);
		if(course != null) {
			course.setInstructor(null);
			courseRepository.save(course);
			return course;
		}
		return null;
	}


	@Override
	public Instructor unassignSpecificInstructorToCourse(int instructorId, int courseId) {
		Instructor instructor = instructorRepository.findById(instructorId).orElse(null);
		if(instructor != null) {
			Course course = courseRepository.findById(courseId).orElse(null);
			if(course != null) {
				if(course.getInstructor() != null && (course.getInstructor().getId() == instructorId)) {
					instructor.removeCourse(course);
					instructorRepository.save(instructor);
					return instructor;
				}
			}
		}
		return null;
	}


	
}
