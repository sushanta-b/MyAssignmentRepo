package com.thinktalentindia.CrudeProject.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thinktalentindia.CrudeProject.model.Employee;
import com.thinktalentindia.CrudeProject.repository.EmployeeRepository;


@Component
public class EmployeeBo {


	public static final String ADD = "Successfully Added.";
	public static final String UPDATE = "Successfully Updated.";
	public static final String FAILED = "Failed.";
	public static final String DELETE = "Deleted.";
	public static final String NO_IDS = "could not found the employee with id-";
	
	@Autowired(required = true)
	private EmployeeRepository empRepo;

	public Map<String, Object> addUpdate(Employee p) {
		Map<String, Object> rsMap = new HashMap<String, Object>();
		rsMap.put("message", FAILED);
		try {
			if(p.getId() == null) {
				rsMap.put("message", ADD);
			} else {
				rsMap.put("message", UPDATE);
			}
			empRepo.save(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rsMap;
	}
	
	public List<Employee> findAll(){
		List<Employee> emps = null;
		try {
			emps = empRepo.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return emps;
	}
	
	public Employee findById(Long id) {
		Employee em = new Employee();
		try {
			Optional<Employee> emp = empRepo.findById(id);
			em = getEmpFromOPEmp(id,emp);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return em;
	}

	private Employee getEmpFromOPEmp(Long id, Optional<Employee> emp) throws Exception {
		Employee em;
		if(!emp.isPresent()) {
			throw new Exception(NO_IDS+id);
		}else {
			em = emp.get();
		}
		return em;
	}
	
	public Map<String, Object> remove(String ids) {
		Map<String, Object> rsMap = new HashMap<String, Object>();
		rsMap.put("message", FAILED);

		try {
			if (ids != null && ids.length() > 0) {
				String idArr[] = ids.split(",");
				if(idArr.length>0) {
					rsMap.put("message", DELETE);
				} else {
					rsMap.put("message", NO_IDS);
				}
				for (String id : idArr) {
					Long idl = Long.parseLong(id);
					Optional<Employee> eOp = empRepo.findById(idl);
					Employee e = getEmpFromOPEmp(idl, eOp);
					empRepo.delete(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rsMap;
	}
}