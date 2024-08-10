package com.techlabs.myapp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.techlabs.myapp.entity.Student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;


@Repository
public class StudentDaoImpl implements StudentDao{
	
	
	EntityManager entityManager;
	

	public StudentDaoImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}



	@Override
	@Transactional
	public void save(Student student) {
		// TODO Auto-generated method stub
		this.entityManager.persist(student);
		
	}
	
	
	public List<Student> getAllStudents(){
		TypedQuery<Student> query = entityManager.createQuery("Select s from Student s", Student.class);
		List<Student> students = query.getResultList();
		return students;
	}



	@Override
	public Student getStudentById(int id) {
		Student student = entityManager.find(Student.class, id);
		return student;
	}



	@Override
	public List<Student> getStudentByName(String firstName) {
		TypedQuery<Student> query = entityManager.createQuery("Select s from Student s where firstName = ?1", Student.class);
		query.setParameter(1, firstName);
		List<Student> students = query.getResultList();
		return students;
	}



	@Override
	public List<Student> getStudentByFirstAndLastName(String firstName, String lastName) {
		TypedQuery<Student> query = entityManager.createQuery("Select s from Student s where firstName =:first and lastName=:last", Student.class);
		query.setParameter("first", firstName);
		query.setParameter("last", lastName);
		List<Student> students = query.getResultList();
		return students;
	}



	@Override
	@Transactional
	public void updateStudent(Student student) {
		// TODO Auto-generated method stub
		Student student2 = entityManager.find(Student.class, student.getId());
		if(student2 != null) {
			entityManager.merge(student);
		}
		else System.out.println("No student find with this id");
		
	}



	@Override
	@Transactional
	public void deleteStudent(int id) {
		// TODO Auto-generated method stub
		Student student = entityManager.find(Student.class, id);
		if(student != null) {
			entityManager.remove(student);
		}
		else System.out.println("No such student to remove with id "+ id);
		
	}



	@Override
	@Transactional
	public void updateWithoutMerge(Student student) {
		// TODO Auto-generated method stub
		Student student2 = entityManager.find(Student.class, student.getId());
		if(student2 != null) {
//			entityManager.remove(student2);
//			entityManager.persist(student);
			Query query = entityManager.createQuery( "update Student s set s.firstName = ?1, s.lastName = ?2, s.email = ?3 where s.id = ?4");
			query.setParameter(1, student.getFirstName());
			query.setParameter(2, student.getLastName());
			query.setParameter(3, student.getEmail());
			query.setParameter(4, student.getId());
			int res= query.executeUpdate();
			System.out.println(res);
		}
		else System.out.println("No student find with this id");
		
	}



	@Override
	@Transactional
	public void deleteAllStudentsIdLessThanHalf(int id) {
		// TODO Auto-generated method stub
		Query query = entityManager.createQuery( "delete from Student s where s.id < ?1");
		query.setParameter(1, id);
		int res = query.executeUpdate();
		System.out.println(res);
	}

}
