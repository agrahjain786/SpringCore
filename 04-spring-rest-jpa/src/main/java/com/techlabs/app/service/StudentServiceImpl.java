package com.techlabs.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.techlabs.app.entity.Student;
import com.techlabs.app.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService{
	

	private StudentRepository studentRepository;
	

	public StudentServiceImpl(StudentRepository studentDao) {
		super();
		this.studentRepository = studentDao;
	}

	@Override
	public List<Student> getAllStudents() {
		// TODO Auto-generated method stub
		return studentRepository.findAll();
	}

	@Override
	public Student getStudentById(int id) {
		// TODO Auto-generated method stub
		return studentRepository.findById(id).orElse(null);
	}

	@Override
	public Student save(Student student) {
		// TODO Auto-generated method stub
		return studentRepository.save(student);
	}

	@Override
	public Student saveAndUpdate(Student student) {
		// TODO Auto-generated method stub
		return studentRepository.save(student);
	}

	@Override
	public void deleteStudent(Student student) {
		// TODO Auto-generated method stub
		studentRepository.delete(student);
	}

}
