package com.flipfit.client;

import java.util.Scanner;

import com.flipfit.bean.GymUser;
import com.flipfit.business.AdminService;
import com.flipfit.business.BookingService;
import com.flipfit.business.CustomerService;
import com.flipfit.business.GymOwnerService;
import com.flipfit.business.UserService;
import com.flipfit.business.impl.AdminServiceImpl;
import com.flipfit.business.impl.BookingServiceImpl;
import com.flipfit.business.impl.CustomerServiceImpl;
import com.flipfit.business.impl.GymOwnerServiceImpl;
import com.flipfit.business.impl.UserServiceImpl;
import com.flipfit.enums.Role;
import com.flipfit.helper.DataStore;
import com.flipfit.validation.EmailValidation;

public class Main {

	private static final UserService userService = new UserServiceImpl();
	private static final BookingService bookingService = new BookingServiceImpl();
	private static final CustomerService customerService = new CustomerServiceImpl(bookingService);
	private static final GymOwnerService gymOwnerService = new GymOwnerServiceImpl();
	private static final AdminService adminService = new AdminServiceImpl();

	static boolean validate= false;
	
    public static void main(String[] args) {
        System.out.println("Welcome to Flipfit Application!");

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Login");
            System.out.println("2. Registration of the GymCustomer");
            System.out.println("3. Registration of the GymOwner");
            System.out.println("4. Change Password");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

			if (!scanner.hasNextInt()) {
				System.out.println("No more input detected. Exiting.");
				break;
			}
			choice = scanner.nextInt();

            switch (choice) {
                case 1:
				login(scanner);
                    break;
                case 2:
				registerCustomer(scanner);
                    break;
                case 3:
				registerOwner(scanner);
                    break;
                case 4:
				changePassword(scanner);
                    break;
                case 5:
                    System.out.println("exited!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }

    private static void login(Scanner scanner) {
        System.out.println("\n--- Login ---");
		System.out.println("Choose your role first:");
		System.out.println("1. GYMOwner");
		System.out.println("2. GymCustomer");
		System.out.println("3. GymAdmin");
		System.out.print("Enter role choice: ");
		if (!scanner.hasNextInt()) {
			System.out.println("No input. Returning to main menu.");
			return;
		}
		int roleChoice = scanner.nextInt();
		Role role = mapRole(roleChoice);

		System.out.print("Enter UserId: ");
		if (!scanner.hasNext()) {
			System.out.println("No input. Returning to main menu.");
			return;
		}
		String userId = scanner.next();
		System.out.print("Enter Password: ");
		if (!scanner.hasNext()) {
			System.out.println("No input. Returning to main menu.");
			return;
		}
		String password = scanner.next();

		GymUser loggedIn = userService.login(userId, password, role);
		if (loggedIn == null) {
			System.out.println("Invalid credentials.");
			return;
		}

		switch (role) {
		case GYM_OWNER:
			GymOwnerMenu gymOwnerMenu = new GymOwnerMenu(gymOwnerService, loggedIn);
			gymOwnerMenu.displayMenu();
			break;
		case CUSTOMER:
			CustomerMenu customerMenu = new CustomerMenu(customerService, loggedIn);
			customerMenu.displayMenu();
			break;
		case ADMIN:
			AdminMenu adminMenu = new AdminMenu(adminService);
			adminMenu.displayMenu();
			break;
		default:
			System.out.println("Unsupported role.");
        }
    }

	private static void registerCustomer(Scanner scanner) {
		
		System.out.print("Enter name: ");
		String name = scanner.next();

		String email;
		validate = true;
		do {
			if(!validate) System.out.println("Email Validation Failed. Try again..");
			System.out.print("Enter email(Ends with \" @gmail.com \" ): ");
			email = scanner.next();
			validate = EmailValidation.validateEmail(email);
			validate &= EmailValidation.checkUnique(DataStore.getAllEmails(), email);
		} while (!validate );
		
		System.out.print("Choose password: ");
		String password = scanner.next();
		
		GymUser user = userService.register(name, email, password, Role.CUSTOMER);
		System.out.println("Registered Customer with user id: " + user.getUserId());
	}

	private static void registerOwner(Scanner scanner) {
		System.out.print("Enter name: ");
		String name = scanner.next();
		
		String email;
		validate = true;
		do {
			if(!validate) System.out.println("Email Validation Failed. Try again..");
			System.out.print("Enter email(Ends with \" @gmail.com \" ): ");
			email = scanner.next();
			validate = EmailValidation.validateEmail(email);
			validate &= EmailValidation.checkUnique(DataStore.getAllEmails(), email);
		} while (!validate );
		
		System.out.print("Choose password: ");
		String password = scanner.next();
		
		GymUser user = userService.register(name, email, password, Role.GYM_OWNER);
		System.out.println("Registered Gym Owner with user id: " + user.getUserId());
	}

	private static void changePassword(Scanner scanner) {
		System.out.print("Enter your user id: ");
		if (!scanner.hasNext()) {
			System.out.println("No input. Returning to main menu.");
			return;
		}
		String userId = scanner.next();
		System.out.print("Enter old password: ");
		if (!scanner.hasNext()) {
			System.out.println("No input. Returning to main menu.");
			return;
		}
		String oldPwd = scanner.next();
		System.out.print("Enter new password: ");
		if (!scanner.hasNext()) {
			System.out.println("No input. Returning to main menu.");
			return;
		}
		String newPwd = scanner.next();
		boolean success = userService.changePassword(userId, oldPwd, newPwd);
		System.out.println(success ? "Password updated." : "Password update failed.");
	}

	private static Role mapRole(int choice) {
		switch (choice) {
		case 1:
			return Role.GYM_OWNER;
		case 2:
			return Role.CUSTOMER;
		case 3:
			return Role.ADMIN;
		default:
			return Role.CUSTOMER;
		}
	}
}
