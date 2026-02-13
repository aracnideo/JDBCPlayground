package service;

import model.entities.Department;
import repository.DepartmentRepository;

public class DepartmentService {
	
	private DepartmentRepository repository;

	public DepartmentService(DepartmentRepository repository) {
		super();
		this.repository = repository;
	}
	
	public void insert(Department department) {
		if (department == null) {
			throw new IllegalArgumentException("Department cannot be null.");
		}
		
		if (department.getName() == null || department.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("Department name cannot be empty.");
		}
		repository.insert(department);
	}
	
	public void update(Department department) {
		repository.update(department);
	}

}
