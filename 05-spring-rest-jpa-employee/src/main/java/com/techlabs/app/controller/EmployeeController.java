package com.techlabs.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.EmployeeDTO;
import com.techlabs.app.entity.Employee;
import com.techlabs.app.exception.EmployeeNotFoundException;
import com.techlabs.app.service.EmployeeService;
import com.techlabs.app.util.PagedResponse;

import jakarta.validation.Valid;


@RestController
public class EmployeeController {
	
	private EmployeeService employeeService;
	

	public EmployeeController(EmployeeService employeeService) {
		super();
		this.employeeService = employeeService;
	}
	
	
	@GetMapping("/employees")
	public ResponseEntity<PagedResponse<EmployeeDTO>> getAllEmployees( 
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "size", defaultValue = "5") int size,
		@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
		@RequestParam(name = "direction", defaultValue = "asc") String direction){
		PagedResponse<EmployeeDTO> employees = employeeService.getAllEmployees(page,size,sortBy,direction);
		
		return new ResponseEntity<PagedResponse<EmployeeDTO>>(employees, HttpStatus.OK);
	}
	
	
	@GetMapping("/employees/{eid}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(name="eid")int id) {
		Employee employee = employeeService.getEmployeeById(id);
		
		if(employee == null) {
			throw new EmployeeNotFoundException("No such employee with this id "+ id);
		}
		
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}
	
	
	@PostMapping("/employees")
	public ResponseEntity<EmployeeDTO> addEmployee(@Valid @RequestBody EmployeeDTO employeeDTO){
		employeeDTO.setId(0);
		EmployeeDTO addedEmployee = employeeService.save(employeeDTO);
		
		return new ResponseEntity<EmployeeDTO>(addedEmployee, HttpStatus.CREATED);
		
	}
	
	@PutMapping("/employees")
	public ResponseEntity<EmployeeDTO> updateEmployee(@Valid @RequestBody EmployeeDTO employeeDTO){
		Employee dbEmployee = employeeService.getEmployeeById(employeeDTO.getId());
		
		if(dbEmployee == null) {
			throw new EmployeeNotFoundException("No such employee with this id "+ employeeDTO.getId());
		}
		
		EmployeeDTO updatedEmployee = employeeService.save(employeeDTO);
		
		return new ResponseEntity<EmployeeDTO>(updatedEmployee, HttpStatus.OK);
		
	}
	
	@DeleteMapping("/employees/{eid}")
	public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable(name="eid")int id){
		Employee dbEmployee = employeeService.getEmployeeById(id);
		
		if(dbEmployee == null) {
			throw new EmployeeNotFoundException("No such employee with this id "+ id);
		}
		
		employeeService.deleteEmployee(dbEmployee);
		
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		
	}
	
	@GetMapping("/employees/name/{ename}")
	public ResponseEntity<List<Employee>> getAllEmployeesByName(@PathVariable(name="ename")String name){
		List<Employee> employees = employeeService.getAllEmployeesByName(name);
		
		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}
	
	@GetMapping("/employees/email/{eemail}")
	public ResponseEntity<Employee> getEmployeesByEmail(@PathVariable(name="eemail")String email){
		Employee employee = employeeService.getEmployeesByEmail(email);
		
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}
	
	
	@GetMapping("/employees/active/{eactive}")
	public ResponseEntity<List<Employee>> getAllEmployeesByActive(@PathVariable(name="eactive")boolean isActive){
		List<Employee> employees = employeeService.getAllEmployeesByActiveness(isActive);
		
		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}
	
	@GetMapping("/employees/nameStarts/{enamestarts}")
	public ResponseEntity<List<Employee>> getAllEmployeesStartsWith(@PathVariable(name="enamestarts")String nameStarts){
		List<Employee> employees = employeeService.getAllEmployeesStartsWith(nameStarts);
		
		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}
	
	@GetMapping("/employees/salary/designation/{esalary}/{edesignation}")
	public ResponseEntity<List<Employee>> getEmployeesBySalaryAndDesignation(
            @PathVariable(name = "esalary") double salary, @PathVariable(name = "edesignation") String designation){
		List<Employee> employees = employeeService.getEmployeesBySalaryAndDesignation(salary, designation);
		
		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}
	
	@GetMapping("/employees/salary/{starts}/{ends}")
    public ResponseEntity<List<Employee>> getEmployeesBySalaryBetween(@PathVariable(name = "starts") double starts,
            @PathVariable(name = "ends") double ends) {
        List<Employee> employees = employeeService.getEmployeesBySalaryBetween(starts, ends);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
	
	
	@GetMapping("/employees/active/salary/{esalary}")
    public ResponseEntity<List<Employee>> getActiveEmployeesBySalary(@PathVariable(name = "esalary") double salary) {
        List<Employee> employees = employeeService.getActiveEmployeesBySalary(salary);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
	
	
	@GetMapping("/employees/active/count/{eactive}")
    public ResponseEntity<Integer> getCountOfActiveEmployees(@PathVariable(name="eactive")boolean isActive) {
        int count = employeeService.getCountOfActiveEmployees(isActive);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
	
	
	@GetMapping("/employees/designation/count/{designation}")
    public ResponseEntity<Integer> getCountOfEmployeesByDepartment(@PathVariable(name = "designation") String designation) {
        int count = employeeService.getCountOfEmployeesByDesignation(designation);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
	
}
