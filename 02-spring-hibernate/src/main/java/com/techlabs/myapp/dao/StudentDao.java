package com.techlabs.myapp.dao;

import java.util.List;

import com.techlabs.myapp.entity.Student;

public interface StudentDao {
	
	public void save(Student student);
	
	public List<Student> getAllStudents();

	public Student getStudentById(int i);

	public List<Student> getStudentByName(String firstName);

	public List<Student> getStudentByFirstAndLastName(String firstName, String lastName);

	public void updateStudent(Student student);

	public void deleteStudent(int i);

	public void updateWithoutMerge(Student student);

	public void deleteAllStudentsIdLessThanHalf(int i);

}
