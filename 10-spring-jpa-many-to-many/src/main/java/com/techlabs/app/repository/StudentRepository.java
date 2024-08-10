package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{

	
	@EntityGraph(attributePaths = {"courses"})
    List<Student> findAll();
}
