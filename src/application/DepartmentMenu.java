package application;

import java.sql.Connection;
import java.util.Scanner;

import db.DB;
import model.entities.Department;
import repository.DepartmentRepository;
import service.DepartmentService;

public class DepartmentMenu {
	
	Scanner sc;

	public DepartmentMenu(Scanner sc) {
		super();
		this.sc = sc;
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
			System.out.println("Choose an option:");
			option = sc.nextInt();

			switch (option) {
			case 1:
				insert();
				break;
			case 2:
				System.out.println("FindById em andamento");
				break;
			case 3:
				System.out.println("FindAll em andamento");
				break;
			case 4:
				update();
				break;
			case 5: 
				System.out.println("Deleção em andamento");
				break;
			case 6: 
				System.out.println("Returning to Main Menu...");
				break;
			default:
				System.out.println("Invalid option");
			}
		}
	}

	private void insert() {
		// Insert Department
		System.out.println("Enter the name of the new Department: ");
		sc.nextLine();
		String newName = sc.nextLine();
		
		Connection conn = null;
		try {
			conn = DB.getConnection();
			DepartmentRepository departmentRepository = new DepartmentRepository(conn);
			DepartmentService departmentService = new DepartmentService(departmentRepository);

			Department department = new Department(null, newName);
			departmentService.insert(department);
			System.out.println("Inserted! New Id: " + department.getId());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeConnection();
		}
	}
	
	private void update() {
		//Update Department
		System.out.println("Enter Deparment Id: ");
		int id = sc.nextInt();
		sc.nextLine();
		System.out.println("Enter new name: ");
		String newName = sc.nextLine();
		
		Connection conn = null;
		try {
			conn = DB.getConnection();
			DepartmentRepository departmentRepository = new DepartmentRepository(conn);
			DepartmentService departmentService = new DepartmentService(departmentRepository);

			Department department = new Department(id, newName);
			departmentService.update(department);
			System.out.println("Updated completed");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeConnection();
		}
	}

}
