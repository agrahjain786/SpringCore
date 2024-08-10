package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.Student;

public interface StudentRepository extends JpaRepository<Student,Integer>{

}
