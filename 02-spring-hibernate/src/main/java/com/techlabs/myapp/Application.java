package com.techlabs.myapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.techlabs.myapp.dao.StudentDao;
import com.techlabs.myapp.entity.Student;

@SpringBootApplication
public class Application implements CommandLineRunner{
	
	@Autowired
	private StudentDao studentDao;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Hello World!");
//		addStudent();
//		getAllStudents();
//		getStudentById();
//		getStudentByName();
//		getStudentByFirstAndLastName();
//		updateStudent();
//		deleteStudent();
//		updatewithoutMerge();
		deleteAllStudentsIdLessThanHalf();
	
	}

	private void deleteAllStudentsIdLessThanHalf() {
		// TODO Auto-generated method stub
		studentDao.deleteAllStudentsIdLessThanHalf(5);
	}

	private void updatewithoutMerge() {
		// TODO Auto-generated method stub
		Student student = new Student(7,"Virag","Jain","v@email");
		studentDao.updateWithoutMerge(student);
	}

	private void deleteStudent() {
		// TODO Auto-generated method stub
		studentDao.deleteStudent(3);
	}

	private void updateStudent() {
		// TODO Auto-generated method stub
		Student student = new Student(7,"Virag","abhi","v@gmail");
		studentDao.updateStudent(student);
	}

	private void getStudentByFirstAndLastName() {
		// TODO Auto-generated method stub
		List<Student> students = studentDao.getStudentByFirstAndLastName("Agrah", "Jain");
		for(Student s: students) {
			System.out.println(s);
		}
	}

	private void getStudentByName() {
		// TODO Auto-generated method stub
		List<Student> students = studentDao.getStudentByName("Agrah");
		for(Student s: students) {
			System.out.println(s);
		}
		
	}

	private void getStudentById() {
		// TODO Auto-generated method stub
		Student student = studentDao.getStudentById(7);
		if(student != null) System.out.println(student);
		else System.out.println("Student not found");
		
	}

	private void addStudent() {
		// TODO Auto-generated method stub
		Student student = new Student("Agrah", "V", "av@email");
		studentDao.save(student);
		
	}
	
	private void getAllStudents() {
		List<Student> students = studentDao.getAllStudents();
		for(Student s: students) {
			System.out.println(s);
		}
		
	}

}