package com.techlabs.app.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.techlabs.app.dao.StudentDao;
import com.techlabs.app.entity.Student;
import com.techlabs.app.exception.StudentErrorResponse;
import com.techlabs.app.exception.StudentExceptionHandler;
import com.techlabs.app.exception.StudentNotFoundException;
import com.techlabs.app.service.StudentService;

@RestController
public class StudentController {

	private StudentService studentService;

	public StudentController(StudentService studentService) {
		super();
		this.studentService = studentService;
	}
	
	
	@GetMapping("/hello")
	public String test_func() {
		return "hello";
	}
	
	@GetMapping("/students")
	public ResponseEntity<List<Student>> getAllStudents() {
		
		List<Student> students = studentService.getAllStudents();
		
		return new ResponseEntity<List<Student>>(students,HttpStatus.OK);
	}
	
	@GetMapping("/students/{sid}")
	public ResponseEntity<Student> getStudentById(@PathVariable(name="sid")int id) {
		
		Student student = studentService.getStudentById(id);
		
		if(student == null) {
			throw new StudentNotFoundException("Student with id " + id+ " is not available");
		}
		return new ResponseEntity<Student>(student,HttpStatus.OK);
	}
	
	
	@PostMapping("/students")
	public ResponseEntity<Student> addStudent(@RequestBody Student student) {
		Student addedStudent = studentService.save(student);
		return new ResponseEntity<Student>(addedStudent,HttpStatus.CREATED);
	}
	
	
	@PutMapping("/students")
	public ResponseEntity<Student> saveAndUpdate(@RequestBody Student student) {
		Student student1 = studentService.getStudentById(student.getId());
		if(student1 == null) {
			throw new RuntimeException("Student with id " + student.getId() + " is not found");
			
		}
		Student addedStudent = studentService.saveAndUpdate(student);
		return new ResponseEntity<Student>(addedStudent,HttpStatus.OK);
	}
	
	
	@DeleteMapping("/students/{sid}")
	public ResponseEntity<HttpStatus> deleteStudent(@PathVariable(name="sid")int id) {
		Student student = studentService.getStudentById(id);
		if (student == null) {
			throw new StudentNotFoundException("Student with id " + id + " is not available");
            
        } 
		studentService.deleteStudent(student);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
	
//	@ExceptionHandler
//	public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException ex){
//		StudentErrorResponse error = new StudentErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
//		
//		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//	}
//	
//	@ExceptionHandler
//	public ResponseEntity<StudentErrorResponse> handleException(Exception ex){
//		StudentErrorResponse error = new StudentErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
//		
//		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//	}

}
