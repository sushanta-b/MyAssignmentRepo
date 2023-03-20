package com.thinktalentindia.CrudeProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thinktalentindia.CrudeProject.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	 
}