package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtils {

	public static int readInt(Scanner sc, String message) {
		while (true) {
			try {
				System.out.print(message);
				return Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Invalid number. Please try again.");
			}
		}
	}

	public static double readDouble(Scanner sc, String message) {
		while (true) {
			try {
				System.out.print(message);
				return Double.parseDouble(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Invalid number. Please try again.");
			}
		}
	}

	public static String readNonEmptyString(Scanner sc, String message) {
		while (true) {
			System.out.print(message);
			String input = sc.nextLine().trim();
			if (!input.isEmpty()) {
				return input;
			}
			System.out.println("Input cannot be empty.");
		}
	}

	public static LocalDate readDate(Scanner sc, String message) {
		while (true) {
			try {
				System.out.print(message);
				String input = sc.nextLine();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				return LocalDate.parse(input, formatter);
			} catch (DateTimeParseException e) {
				System.out.println("Invalid date format. Use yyyy-MM-dd.");
			}
		}
	}

	public static void waitForEnter(Scanner sc) {
		System.out.println("Press Enter to continue...");
		sc.nextLine();
	}
}
