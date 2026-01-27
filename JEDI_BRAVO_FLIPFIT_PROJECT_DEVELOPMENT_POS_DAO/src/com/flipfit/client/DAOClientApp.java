package com.flipfit.client;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymUser;
import com.flipfit.business.AdminService;
import com.flipfit.business.CustomerService;
import com.flipfit.business.GymOwnerService;
import com.flipfit.business.UserService;
import com.flipfit.business.impl.AdminServiceImpl;
import com.flipfit.business.impl.CustomerServiceImpl;
import com.flipfit.business.impl.GymOwnerServiceImpl;
import com.flipfit.business.impl.UserServiceImpl;
import com.flipfit.enums.Role;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Client application demonstrating FlipFit functionalities.
 * Connects UI (Console) to the Service Layer.
 */
public class DAOClientApp {

    // Replace Direct DAOs with Service Implementations
    private static UserService userService = new UserServiceImpl();
    private static CustomerService customerService = new CustomerServiceImpl();
    private static GymOwnerService gymOwnerService = new GymOwnerServiceImpl();
    private static AdminService adminService = new AdminServiceImpl();

    private static Scanner scanner = new Scanner(System.in);
    private static GymUser currentUser = null;

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("    Welcome to FlipFit Application");
        System.out.println("===========================================\n");

        boolean running = true;

        while (running) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Customer Login");
            System.out.println("2. Gym Owner Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Register New User");
            System.out.println("5. Exit");
            System.out.println("================================");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    login(Role.CUSTOMER);
                    break;
                case 2:
                    login(Role.GYM_OWNER);
                    break;
                case 3:
                    login(Role.ADMIN);
                    break;
                case 4:
                    registerUser();
                    break;
                case 5:
                    running = false;
                    System.out.println("\n✓ Thank you for using FlipFit!");
                    break;
                default:
                    System.out.println("✗ Invalid choice!");
            }
        }

        scanner.close();
    }

    /**
     * Generic Login Method for all roles
     */
    private static void login(Role role) {
        System.out.println("\n========== " + role + " LOGIN ==========");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        // Use UserService to login
        currentUser = userService.login(email, password, role);

        if (currentUser != null) {
            System.out.println("✓ Login successful! Welcome " + currentUser.getName());

            // Route to specific menu based on Role
            switch (role) {
                case CUSTOMER:
                    customerMenu();
                    break;
                case GYM_OWNER:
                    gymOwnerMenu();
                    break;
                case ADMIN:
                    adminMenu();
                    break;
            }
        } else {
            System.out.println("✗ Invalid credentials or role mismatch!");
        }
        currentUser = null; // Logout after session ends
    }

    /**
     * Customer Menu - Updated with Booking Options
     */
    private static void customerMenu() {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n========== CUSTOMER MENU ==========");
            System.out.println("1. View Profile");
            System.out.println("2. View Gyms (All or by City)");
            System.out.println("3. Book a Slot");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Logout");
            System.out.println("===================================");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayUser(currentUser);
                    break;
                case 2:
                    handleViewGyms();
                    break;
                case 3:
                    handleBooking();
                    break;
                case 4:
                    handleCancellation();
                    break;
                case 5:
                    loggedIn = false;
                    System.out.println("✓ Logged out successfully!");
                    break;
                default:
                    System.out.println("✗ Invalid choice!");
            }
        }
    }

    /**
     * Gym Owner Menu - Updated with Add Gym Option
     */
    private static void gymOwnerMenu() {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n========== GYM OWNER MENU ==========");
            System.out.println("1. View Profile");
            System.out.println("2. Add Gym Center");
            System.out.println("3. Logout");
            System.out.println("====================================");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayUser(currentUser);
                    break;
                case 2:
                    handleAddGymCenter();
                    break;
                case 3:
                    loggedIn = false;
                    System.out.println("✓ Logged out successfully!");
                    break;
                default:
                    System.out.println("✗ Invalid choice!");
            }
        }
    }

    /**
     * Admin Menu - Updated with Approval Logic
     */
    private static void adminMenu() {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("1. View Statistics");
            System.out.println("2. Approve Gym Centers");
            System.out.println("3. Approve Gym Owners");
            System.out.println("4. Logout");
            System.out.println("================================");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewSystemStatistics();
                    break;
                case 2:
                    handleApproveGymCenters();
                    break;
                case 3:
                    handleApproveGymOwners();
                    break;
                case 4:
                    loggedIn = false;
                    System.out.println("✓ Logged out successfully!");
                    break;
                default:
                    System.out.println("✗ Invalid choice!");
            }
        }
    }

    // ================= HELPER METHODS =================

    private static void registerUser() {
        System.out.println("\n========== USER REGISTRATION ==========");
        System.out.print("Enter Name: "); String name = scanner.nextLine();
        System.out.print("Enter Email: "); String email = scanner.nextLine();
        System.out.print("Enter Password: "); String password = scanner.nextLine();
        System.out.print("Enter Address: "); String address = scanner.nextLine();

        System.out.println("1. Customer | 2. Gym Owner");
        int roleChoice = scanner.nextInt(); scanner.nextLine();
        Role role = (roleChoice == 2) ? Role.GYM_OWNER : Role.CUSTOMER;

        GymUser newUser = userService.register(name, email, password, address, role);
        if (newUser != null) {
            System.out.println("✓ Registration successful! User ID: " + newUser.getUserId());
        }
    }

    // --- CUSTOMER HELPERS ---

    private static void handleViewGyms() {
        System.out.println("View All Gyms in a city");

        List<GymCenter> gyms;

        System.out.print("Enter City: ");
        String city = scanner.nextLine();
        gyms = customerService.viewGymsByCity(city);

        if (gyms.isEmpty()) {
            System.out.println("No gyms found.");
        } else {
            System.out.printf("%-10s %-20s %-15s%n", "ID", "Name", "City");
            System.out.println("---------------------------------------------");
            for (GymCenter g : gyms) {
                System.out.printf("%-10s %-20s %-15s%n", g.getCenterId(), g.getCenterLocn(), g.getCenterCity());
            }
        }
    }

    private static void handleBooking() {
        System.out.println("\n--- Book a Slot ---");
        System.out.print("Enter Gym ID: "); String gymId = scanner.nextLine();
        System.out.print("Enter Slot ID: "); String slotId = scanner.nextLine();
        System.out.print("Enter Date (YYYY-MM-DD): "); String date = scanner.nextLine();

        boolean success = customerService.bookSlot(currentUser.getUserId(), slotId, gymId, date);
        // Success/Failure message is handled inside Service via print statements as per previous code
    }

    private static void handleCancellation() {
        System.out.println("\n--- Cancel Booking ---");
        System.out.print("Enter Booking ID: "); String bId = scanner.nextLine();
        System.out.print("Enter Slot ID (to free up seat): "); String sId = scanner.nextLine();

        customerService.cancelBooking(bId, sId);
    }

    // --- GYM OWNER HELPERS ---

    private static void handleAddGymCenter() {
        System.out.println("\n--- Add Gym Center ---");
        System.out.print("Enter Gym ID: "); String gymId = scanner.nextLine();
        System.out.print("Enter City: "); String city = scanner.nextLine();
        System.out.print("Enter Location/Address: "); String loc = scanner.nextLine();

        // Assuming Owner ID is same as User ID for now, or derived
        String ownerId = currentUser.getUserId();

        gymOwnerService.addGymCenter(gymId, ownerId, city, loc);
    }

    // --- ADMIN HELPERS ---

    private static void handleApproveGymCenters() {
        List<GymCenter> pending = adminService.viewPendingGymCenters();
        if(pending.isEmpty()) {
            System.out.println("No pending gym centers.");
            return;
        }
        for(GymCenter c : pending) {
            System.out.println("Pending ID: " + c.getCenterId() + " | City: " + c.getCenterCity());
        }
        System.out.print("Enter Gym ID to Approve: ");
        String id = scanner.nextLine();
        adminService.approveGymCenter(id);
    }

    private static void handleApproveGymOwners() {
        // Similar logic for Owner Approval
        var pending = adminService.viewPendingGymOwners();
        if(pending.isEmpty()){
            System.out.println("No pending owners.");
            return;
        }
        // ... Display logic ...
        System.out.print("Enter Owner ID to Approve: ");
        String id = scanner.nextLine();
        adminService.approveGymOwner(id);
    }

    private static void viewSystemStatistics() {
        Map<String, Integer> stats = adminService.getSystemStatistics();
        System.out.println("\n--- System Stats ---");
        stats.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    private static void displayUser(GymUser user) {
        System.out.println("User: " + user.getName() + " | Role: " + user.getRole());
    }
}