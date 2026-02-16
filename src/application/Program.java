package application;
import java.sql.Connection;
import java.util.Scanner;

import db.DB;
import util.InputUtils;

public class Program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		Connection conn = DB.getConnection();
		DepartmentMenu departmentMenu = new DepartmentMenu(sc, conn);
		SellerMenu sellerMenu = new SellerMenu(sc, conn);

		int option = 0;

		while (option != 3) {
			System.out.println("--- --- Main Menu --- ---");
			System.out.println("1 - Department");
			System.out.println("2 - Seller");
			System.out.println("3 - Close application");
			option = InputUtils.readInt(sc, "Choose an option: ");

			switch (option) {
			case 1:
				departmentMenu.start();
				break;
			case 2:
				sellerMenu.start();
				break;
			case 3:
				System.out.println("Closing application...");
				break;
			default:
				System.out.println("Invalid option");
			}
		}
		DB.closeConnection();
		sc.close();
		System.out.println("Application closed.");
	}
}
