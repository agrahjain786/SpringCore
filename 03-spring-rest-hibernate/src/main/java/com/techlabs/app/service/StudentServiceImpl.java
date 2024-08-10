package com.techlabs.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.techlabs.app.dao.StudentDao;
import com.techlabs.app.entity.Student;

@Service
public class StudentServiceImpl implements StudentService{
	
	private StudentDao studentDao;
	

	public StudentServiceImpl(StudentDao studentDao) {
		super();
		this.studentDao = studentDao;
	}

	@Override
	public List<Student> getAllStudents() {
		// TODO Auto-generated method stub
		return studentDao.getAllStudents();
	}

	@Override
	public Student getStudentById(int id) {
		// TODO Auto-generated method stub
		return studentDao.getStudentById(id);
	}

	@Override
	public Student save(Student student) {
		// TODO Auto-generated method stub
		return studentDao.save(student);
	}

	@Override
	public Student saveAndUpdate(Student student) {
		// TODO Auto-generated method stub
		return studentDao.saveAndUpdate(student);
	}

	@Override
	public void deleteStudent(Student student) {
		// TODO Auto-generated method stub
		studentDao.deleteStudent(student);
	}

}
