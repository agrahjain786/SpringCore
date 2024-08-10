package com.techlabs.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.CourseDTO;
import com.techlabs.app.dto.StudentDTO;
import com.techlabs.app.entity.Course;
import com.techlabs.app.entity.Student;
import com.techlabs.app.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class StudentController {
	
	private StudentService studentService;

	public StudentController(StudentService studentService) {
		super();
		this.studentService = studentService;
	}
	
	@PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) {
        return new ResponseEntity<Course>(studentService.createCourse(course), HttpStatus.CREATED);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return new ResponseEntity<List<CourseDTO>>(studentService.getAllCourses(), HttpStatus.OK);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<CourseDTO>(studentService.getCourseById(id), HttpStatus.OK);
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable(name = "id") Long id) {
    	studentService.deleteCourse(id);
    	return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }
    
    
    
    @PostMapping("/students")
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        return new ResponseEntity<Student>(studentService.createStudent(student), HttpStatus.CREATED);
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return new ResponseEntity<List<StudentDTO>>(studentService.getAllStudents(), HttpStatus.OK);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<StudentDTO>(studentService.getStudentById(id), HttpStatus.OK);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable(name = "id") Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }
    
    
    @PostMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<Student> addCourseToStudent(@PathVariable(name = "studentId") Long studentId, @PathVariable(name = "courseId") Long courseId) {
        return new ResponseEntity<Student>(studentService.addCourseToStudent(studentId, courseId), HttpStatus.OK);
    }

    @DeleteMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<Student> removeCourseFromStudent(@PathVariable(name = "studentId") Long studentId, @PathVariable(name = "courseId") Long courseId) {
        return new ResponseEntity<Student>(studentService.removeCourseFromStudent(studentId, courseId), HttpStatus.OK);
    }
    
	

}
