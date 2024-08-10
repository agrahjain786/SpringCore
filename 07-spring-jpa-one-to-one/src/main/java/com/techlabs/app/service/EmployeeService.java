package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.entity.Employee;

public interface EmployeeService {
	
	
	public Employee saveEmployee(Employee employee);

	public List<Employee> findAllEmployees();

	public Employee getEmployeeById(int id);

	public void deleteEmployee(Employee employee);
	
	

}
