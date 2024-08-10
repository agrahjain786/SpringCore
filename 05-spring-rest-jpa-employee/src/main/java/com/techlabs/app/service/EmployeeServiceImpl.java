package com.techlabs.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.EmployeeDTO;
import com.techlabs.app.entity.Employee;
import com.techlabs.app.repository.EmployeeRepository;
import com.techlabs.app.util.PagedResponse;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	private EmployeeRepository employeeRepository;
	

	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		super();
		this.employeeRepository = employeeRepository;
	}
	
	@Override
	public PagedResponse<EmployeeDTO> getAllEmployees(int page, int size, String sortBy, String direction) {
		
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
		
		Page<Employee> pages = employeeRepository.findAll(pageable);
		List<Employee> allEmployees = pages.getContent();
		List<EmployeeDTO> allEmployeesDTO = convertListToDTO(allEmployees);
		
		return new PagedResponse<EmployeeDTO>(allEmployeesDTO,pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
		
	}




//	@Override
//	public List<Employee> getAllEmployees(int page, int size) {
//		
//		Pageable pageable = (Pageable) PageRequest.of(page, size);
//		
//		
//		
//		Page<Employee> page1 = employeeRepository.findAll(pageable);
//		
//		return page1.getContent();
//		
//	}


	@Override
	public Employee getEmployeeById(int id) {
		return employeeRepository.findById(id).orElse(null);
		
	}


	@Override
	public EmployeeDTO save(EmployeeDTO employeeDTO) {
		Employee employee = convertToEntity(employeeDTO);
	    employeeRepository.save(employee);
	    EmployeeDTO employeeResponseDTO = convertToDTOObject(employee);
	    return employeeResponseDTO;
	}


	@Override
	public void deleteEmployee(Employee employee) {
		employeeRepository.delete(employee);
		
	}


	@Override
	public List<Employee> getAllEmployeesByName(String name) {
		return employeeRepository.findByName(name);
	}


	@Override
	public Employee getEmployeesByEmail(String email) {
		return employeeRepository.findByEmail(email);
	}


	@Override
	public List<Employee> getAllEmployeesByActiveness(boolean isActive) {
		return employeeRepository.findByActive(isActive);
	}


	@Override
	public List<Employee> getAllEmployeesStartsWith(String nameStarts) {
		// TODO Auto-generated method stub
		return employeeRepository.findByNameStartingWith(nameStarts);
	}


	@Override
	public List<Employee> getEmployeesBySalaryAndDesignation(double salary, String designation) {
		// TODO Auto-generated method stub
		return employeeRepository.findBySalaryGreaterThanAndDesignation(salary, designation);
	}


	@Override
	public List<Employee> getEmployeesBySalaryBetween(double starts, double ends) {
		// TODO Auto-generated method stub
		return employeeRepository.findBySalaryBetween(starts, ends);
	}


	@Override
	public List<Employee> getActiveEmployeesBySalary(double salary) {
		// TODO Auto-generated method stub
		return employeeRepository.findByActiveTrueAndSalaryGreaterThan(salary);
	}


	@Override
	public int getCountOfActiveEmployees(boolean isActive) {
		return employeeRepository.countByActive(isActive);
	}


	@Override
	public int getCountOfEmployeesByDesignation(String designation) {
		// TODO Auto-generated method stub
		return employeeRepository.countByDesignation(designation);
	}
	
	
	private Employee convertToEntity(EmployeeDTO employeeDTO) {
	    Employee employee = new Employee();
	    employee.setId(employeeDTO.getId());
	    employee.setName(employeeDTO.getName());
	    employee.setEmail(employeeDTO.getEmail());
	    employee.setDesignation(employeeDTO.getDesignation());
	    employee.setSalary(employeeDTO.getSalary());
	    employee.setActive(employeeDTO.getActive());
	    return employee;
	}
	
	private EmployeeDTO convertToDTOObject(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setDesignation(employee.getDesignation());
        employeeDTO.setSalary(employee.getSalary());
        employeeDTO.setActive(employee.getActive());
        return employeeDTO;
    }
	
	private List<EmployeeDTO> convertListToDTO(List<Employee> allEmployees) {
		List<EmployeeDTO> employees = new ArrayList<>();
		for(Employee e:allEmployees) {
			employees.add(convertToDTOObject(e));
		}
		return employees;
	}

}
