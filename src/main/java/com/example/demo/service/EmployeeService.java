package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.employeeRepository;


@Service
public class EmployeeService {
	
	
	@Autowired
	private employeeRepository repo;
	
	
	public List<Employee>  getData() {
		
		List<Employee> list=repo.findAll();
		
		return list;
		
		
		
	}

}
