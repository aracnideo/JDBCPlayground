package application;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.entities.Department;
import model.entities.Seller;
import repository.DepartmentRepository;
import repository.SellerRepository;
import service.DepartmentService;
import service.SellerService;
import util.InputUtils;

public class SellerMenu {

	Scanner sc;
	private Connection conn;
	private SellerRepository repository;
	private SellerService service;
	private DepartmentRepository departmentRepository;
	private DepartmentService departmentService;

	public SellerMenu(Scanner sc) {
		super();
		this.sc = sc;
		this.conn = DB.getConnection();
		this.repository = new SellerRepository(conn);
		this.service = new SellerService(repository);
		this.departmentRepository = new DepartmentRepository(conn);
		this.departmentService = new DepartmentService(departmentRepository);
	}

	public void start() {
		int option = 0;
		while (option != 6) {
			System.out.println("--- Seller Menu ---");
			System.out.println("1 - Insert");
			System.out.println("2 - FindById");
			System.out.println("3 - FindAll");
			System.out.println("4 - Update");
			System.out.println("5 - Delete");
			System.out.println("6 - Return to Main Menu");
			option = InputUtils.readInt(sc, "Choose an option: ");

			switch (option) {
			case 1:
				System.out.println("Option selected: INSERT SELLER");
				insert();
				break;
			case 2:
				System.out.println("Option selected: FINDBYID SELLER");
				findById();
				break;
			case 3:
				System.out.println("Option selected: FINDALL SELLER");
				findAll();
				break;
			case 4:
				System.out.println("Option selected: UPDATE SELLER");
				update();
				break;
			case 5:
				System.out.println("Option selected: DELETE SELLER");
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
		// Insert Seller
		try {
			String name = InputUtils.readNonEmptyString(sc, "Enter the name of the new seller: ");
			String email = InputUtils.readNonEmptyString(sc, "Enter the email of the new seller: ");
			LocalDate birthDate = null;
			boolean validBirthDate = false;
			while (!validBirthDate) {
				System.out.println("Enter the birthdate of the new seller: (Format: yyyy-MM-dd)");
				String birthDayString = sc.nextLine();
				try {
					birthDate = LocalDate.parse(birthDayString);
					validBirthDate = true;
				} catch (Exception e) {
					System.out.println("Invalid date format. Use yyyy-MM-dd.");
				}
			}
			double baseSalary = InputUtils.readDouble(sc, "Enter the base salary of the new seller: ");
			Department department = null;
			while (department == null) {
				int departmentId = InputUtils.readInt(sc, "Enter the department ID of the new seller: ");
				department = departmentService.findById(departmentId);
				if (department == null) {
					System.out.println("Department Id not found. Please try again.");
				}
			}
			Seller seller = new Seller(name, email, birthDate, baseSalary, department);
			service.insert(seller);
			System.out.println("Seller inserted successfully! New Id: " + seller.getId());
			InputUtils.waitForEnter(sc);
		} catch (DbException e) {
			System.out.println("Database error: " + e.getMessage());
		} 
	}

	private void findAll() {
		// FindAll Seller
		try {
			List<Seller> sellers = service.findAll();
			System.out.println("~~ Sellers ~~");
			for (Seller seller : sellers) {
				System.out.println(seller.toString());
			}
			System.out.println();
			InputUtils.waitForEnter(sc);
		} catch (DbException e) {
			System.out.println("Database error: " + e.getMessage());
		} 
	}

	public void findById() {
		// FindById Seller
		int id = InputUtils.readInt(sc, "Enter seller Id: ");
		try {
			Seller seller = service.findById(id);
			if (seller != null) {
				System.out.println("~~ Seller Found ~~");
				System.out.println(seller.toString());
			} else {
				System.out.println("Seller not found.");
			}
			InputUtils.waitForEnter(sc);
		} catch (DbException e) {
			System.out.println("Database error: " + e.getMessage());
		} 
	}

	public void delete() {
		// Delete Seller
		int id = InputUtils.readInt(sc, "Enter seller Id: ");
		try {
			service.delete(id);
			System.out.println("Seller deleted successfully.");
		} catch (DbIntegrityException e) {
			System.out.println("Integrity error: " + e.getMessage());
		} catch (DbException e) {
			System.out.println("Database error: " + e.getMessage());
		} 
	}

	public void update() {
		// Update Seller
		int id = InputUtils.readInt(sc, "Enter seller Id: ");
		try {
			Seller seller = service.findById(id);
			if (seller == null) {
				System.out.println("Seller not found.");
				return;
			}
			System.out.println("Current data:");
			System.out.println(seller);
			System.out.println("\nWhat do you want to update?");
			System.out.println("1 - Name");
			System.out.println("2 - Email");
			System.out.println("3 - Birth Date");
			System.out.println("4 - Base Salary");
			System.out.println("5 - Department");
			int option = InputUtils.readInt(sc, "Choose an option: ");
			switch (option) {
			case 1:
				String name = InputUtils.readNonEmptyString(sc, "Enter new name: ");
				seller.setName(name);
				break;
			case 2:
				String email = InputUtils.readNonEmptyString(sc, "Enter new email: ");
				seller.setEmail(email);
				break;
			case 3:
				LocalDate birthDate = InputUtils.readDate(sc, "Enter new birth date (yyyy-MM-dd): ");
				seller.setBirthDate(birthDate);
				break;
			case 4:
				double salary = InputUtils.readDouble(sc, "Enter new base salary: ");
				seller.setBaseSalary(salary);
				break;
			case 5:
				int departmentId = InputUtils.readInt(sc, "Enter new department Id: ");
				Department department = departmentService.findById(departmentId);
				if (department == null) {
					System.out.println("Department not found.");
					return;
				}
				seller.setDepartment(department);
				break;
			}
			service.update(seller);
			Seller updatedSeller = service.findById(seller.getId());
			System.out.println("Seller updated successfully.");
			System.out.println(updatedSeller);
			InputUtils.waitForEnter(sc);
		} catch (DbException e) {
			System.out.println("Database error: " + e.getMessage());
		} 
	}
}
