package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Instructor;


@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Integer> {

}
