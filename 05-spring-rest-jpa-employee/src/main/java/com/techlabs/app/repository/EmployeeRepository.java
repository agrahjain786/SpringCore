package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.techlabs.app.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

//	List<Employee> findByName(String name);
//
//	Employee findByEmail(String email);
//
//	List<Employee> findByActive(boolean isActive);
//
//	List<Employee> findByNameStartingWith(String nameStarts);
//
//	List<Employee> findBySalaryGreaterThanAndDesignation(double salary, String designation);
//
//	List<Employee> findBySalaryBetween(double starts, double ends);
//
//	List<Employee> findByActiveTrueAndSalaryGreaterThan(double salary);
//
//	int countByActive(boolean isActive);
//
//	int countByDesignation(String designation);
	
//	
//	@Query("select e from Employee e where e.name = ?1")
//    List<Employee> findByName(String name);
//
//    @Query("select e from Employee e where e.email = ?1")
//    Employee findByEmail(String email);
//
//    @Query("select e from Employee e where e.active= ?1")
//    List<Employee> findByActive(boolean isActive);
//
//    @Query("select e from Employee e where e.name like ?1%")
//    List<Employee> findByNameStartingWith(String nameStarts);
//
//    @Query("select e from Employee e where e.salary >= ?1 and e.designation = ?2")
//    List<Employee> findBySalaryGreaterThanAndDesignation(double salary, String designation);
//
//    @Query("select e from Employee e where e.salary between ?1 and ?2")
//    List<Employee> findBySalaryBetween(double starts, double ends);
//
//    @Query("select e from Employee e where e.active= true and e.salary > ?1")
//    List<Employee> findByActiveTrueAndSalaryGreaterThan(double salary);
//
//    @Query("select count(e) from Employee e where e.active= ?1")
//    int countByActive(boolean isActive);
//
//    @Query("select count(e) from Employee e where e.designation= ?1")
//    int countByDesignation(String designation);
	
	
	@Query(value = "select * from employee where name = ?1", nativeQuery = true)
    List<Employee> findByName(String name);

    @Query(value = "select * from employee where email = ?1", nativeQuery = true)
    Employee findByEmail(String email);

    @Query(value = "select * from employee where active= ?1", nativeQuery = true)
    List<Employee> findByActive(boolean isActive);

    @Query(value = "select * from employee where name like ?1%", nativeQuery = true)
    List<Employee> findByNameStartingWith(String nameStarts);

    @Query(value = "select * from employee where salary >= ?1 and designation = ?2", nativeQuery = true)
    List<Employee> findBySalaryGreaterThanAndDesignation(double salary, String designation);

    @Query(value = "select * from employee where salary between ?1 and ?2", nativeQuery = true)
    List<Employee> findBySalaryBetween(double starts, double ends);

    @Query(value = "select * from employee where active= true and salary > ?1", nativeQuery = true)
    List<Employee> findByActiveTrueAndSalaryGreaterThan(double salary);

    @Query(value = "select count(*) from employee where active= ?1", nativeQuery = true)
    int countByActive(boolean isActive);

    @Query(value = "select count(*) from employee where designation= ?1", nativeQuery = true)
    int countByDesignation(String designation);


}
