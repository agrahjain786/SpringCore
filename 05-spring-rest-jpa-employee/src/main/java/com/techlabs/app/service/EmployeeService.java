package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.dto.EmployeeDTO;
import com.techlabs.app.entity.Employee;
import com.techlabs.app.util.PagedResponse;


public interface EmployeeService {

	public PagedResponse<EmployeeDTO> getAllEmployees(int page, int size, String sortBy, String direction);

	public Employee getEmployeeById(int id);

	public EmployeeDTO save(EmployeeDTO employeeDTO);

	public void deleteEmployee(Employee employee);

	public List<Employee> getAllEmployeesByName(String name);

	public Employee getEmployeesByEmail(String email);

	public List<Employee> getAllEmployeesByActiveness(boolean isActive);

	public List<Employee> getAllEmployeesStartsWith(String nameStarts);

	public List<Employee> getEmployeesBySalaryAndDesignation(double salary, String department);

	public List<Employee> getEmployeesBySalaryBetween(double starts, double ends);

	public List<Employee> getActiveEmployeesBySalary(double salary);

	public int getCountOfActiveEmployees(boolean isActive);

	public int getCountOfEmployeesByDesignation(String designation);


}
