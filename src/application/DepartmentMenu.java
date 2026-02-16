package application;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.entities.Department;
import repository.DepartmentRepository;
import service.DepartmentService;
import util.InputUtils;

public class DepartmentMenu {

	Scanner sc;
	private Connection conn;
	private DepartmentRepository repository;
	private DepartmentService service;

	public DepartmentMenu(Scanner sc) {
		super();
		this.sc = sc;
		this.conn = DB.getConnection();
		this.repository = new DepartmentRepository(conn);
		this.service = new DepartmentService(repository);
	}

	public void start() {
		int option = 0;
		while (option != 6) {
			System.out.println("--- Department Menu ---");
			System.out.println("1 - Insert");
			System.out.println("2 - FindById");
			System.out.println("3 - FindAll");
			System.out.println("4 - Update");
			System.out.println("5 - Delete");
			System.out.println("6 - Return to Main Menu");
			option = InputUtils.readInt(sc, "Choose an option: ");

			switch (option) {
			case 1:
				System.out.println("Option selected: INSERT DEPARTMENT");
				insert();
				break;
			case 2:
				System.out.println("Option selected: FINDBYID DEPARTMENT");
				findById();
				break;
			case 3:
				System.out.println("Option selected: FINDALL DEPARTMENT");
				findAll();
				break;
			case 4:
				System.out.println("Option selected: UPDATE DEPARTMENT");
				update();
				break;
			case 5:
				System.out.println("Option selected: DELETE DEPARTMENT");
				delete();
				break;
			case 6:
				System.out.println("Returning to Main Menu...");
				System.out.println();
				break;
			default:
				System.out.println("Invalid option");
			}
		}
		DB.closeConnection();
	}

	private void insert() {
		// Insert Department
		String newName = InputUtils.readNonEmptyString(sc, "Enter the name of the new department: ");
		try {
			Department department = new Department(null, newName);
			service.insert(department);
			System.out.println("Department inserted successfully! New Id: " + department.getId());
			InputUtils.waitForEnter(sc);
		} catch (DbException e) {
			System.out.println("Database error: " + e.getMessage());
		}
	}

	private void update() {
		// Update Department
		int id = InputUtils.readInt(sc, "Enter deparment Id: ");
		String newName = InputUtils.readNonEmptyString(sc, "Enter new name for the department: ");
		try {
			Department department = new Department(id, newName);
			service.update(department);
			System.out.println("Department updated successfully.");
			InputUtils.waitForEnter(sc);
		} catch (DbException e) {
			System.out.println("Database error: " + e.getMessage());
		}
	}

	private void findAll() {
		// FindAll Department
		try {
			List<Department> departments = service.findAll();
			System.out.println("~~ Departments ~~");
			for (Department department : departments) {
				System.out.println(department.toString());
			}
			System.out.println();
			InputUtils.waitForEnter(sc);
		} catch (DbException e) {
			System.out.println("Database error: " + e.getMessage());
		}
	}

	public void findById() {
		// FindById Department
		int id = InputUtils.readInt(sc, "Enter deparment Id: ");
		try {
			Department department = service.findById(id);
			if (department != null) {
				System.out.println("~~ Department Found ~~");
				System.out.println(department.toString());
			} else {
				System.out.println("Department not found.");
			}
			InputUtils.waitForEnter(sc);
		} catch (DbException e) {
			System.out.println("Database error: " + e.getMessage());
		}
	}

	public void delete() {
		// Delete Department
		int id = InputUtils.readInt(sc, "Enter deparment Id: ");
		try {
			service.delete(id);
			System.out.println("Department deleted successfully.");
		} catch (DbIntegrityException e) {
			System.out.println("Cannot delete department: it has associated sellers.");

		} catch (DbException e) {
			System.out.println("Database error: " + e.getMessage());
		}
	}

}
