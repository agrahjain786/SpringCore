package com.techlabs.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.entity.Employee;
import com.techlabs.app.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	
	private EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		super();
		this.employeeService = employeeService;
	}
	
	@GetMapping
	public List<Employee> getAllEmployees(){
		return employeeService.findAllEmployees();
	}
	
	
	@PostMapping
	public Employee addEmployee(@RequestBody Employee employee) {
		employee.setId(0);
		return employeeService.saveEmployee(employee);
	}
	
	@GetMapping("/{eid}")
	public Employee getEmployeeById(@PathVariable(name="eid")int id){
		return employeeService.getEmployeeById(id);
		
	}
	
	@PutMapping
	public Employee updateEmployee(@RequestBody Employee employee) {
		Employee existedEmployee = employeeService.getEmployeeById(employee.getId());
		if(existedEmployee != null) {
			return employeeService.saveEmployee(employee);
		}
		return null;
	}
	
	@DeleteMapping("/{eid}")
	public void deleteEmployee(@PathVariable(name="eid")int id) {
		Employee existedEmployee = employeeService.getEmployeeById(id);
		if(existedEmployee != null) {
			employeeService.deleteEmployee(existedEmployee);
		}
	}
	
//	@GetMapping("/address/{id}")
//	public void deleteEmployee(@PathVariable(name="id")int id) {
//		return employee
//	}
	
}
