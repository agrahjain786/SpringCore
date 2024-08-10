package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.entity.Student;

public interface StudentService {
	
	
	public List<Student> getAllStudents();

	public Student getStudentById(int id);

	public Student save(Student student);

	public Student saveAndUpdate(Student student);

	public void deleteStudent(Student student);

}
