package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.dto.CourseDTO;
import com.techlabs.app.dto.StudentDTO;
import com.techlabs.app.entity.Course;
import com.techlabs.app.entity.Student;


public interface StudentService {

	Course createCourse(Course course);

	List<CourseDTO> getAllCourses();

	CourseDTO getCourseById(Long id);

	void deleteCourse(Long id);

	Student createStudent(Student student);

	List<StudentDTO> getAllStudents();

	StudentDTO getStudentById(Long id);

	void deleteStudent(Long id);

	Student addCourseToStudent(Long studentId, Long courseId);

	Student removeCourseFromStudent(Long studentId, Long courseId);

}
