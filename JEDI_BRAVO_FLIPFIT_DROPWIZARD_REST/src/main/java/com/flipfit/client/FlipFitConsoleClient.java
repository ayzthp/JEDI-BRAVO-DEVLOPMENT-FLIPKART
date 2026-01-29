package com.flipfit.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * FlipFit Console Client Application
 * User-friendly console interface to interact with FlipFit REST API
 * 
 * @author JEDI-BRAVO
 * @version 1.0
 * @since 2026-01-29
 */
public class FlipFitConsoleClient {
    
    private static final String API_BASE_URL = "http://localhost:8080";
    private static final Scanner scanner = new Scanner(System.in);
    private static String currentUserId = null;
    private static String currentUserRole = null;
    private static String currentUserName = null;
    
    /**
     * Main method to start the console client
     */
    public static void main(String[] args) {
        showWelcomeScreen();
        mainMenu();
    }
    
    /**
     * Display welcome screen with ASCII art
     */
    private static void showWelcomeScreen() {
        clearScreen();
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                   ║");
        System.out.println("║   ███████╗██╗     ██╗██████╗ ███████╗██╗████████╗                ║");
        System.out.println("║   ██╔════╝██║     ██║██╔══██╗██╔════╝██║╚══██╔══╝                ║");
        System.out.println("║   █████╗  ██║     ██║██████╔╝█████╗  ██║   ██║                   ║");
        System.out.println("║   ██╔══╝  ██║     ██║██╔═══╝ ██╔══╝  ██║   ██║                   ║");
        System.out.println("║   ██║     ███████╗██║██║     ██║     ██║   ██║                   ║");
        System.out.println("║   ╚═╝     ╚══════╝╚═╝╚═╝     ╚═╝     ╚═╝   ╚═╝                   ║");
        System.out.println("║                                                                   ║");
        System.out.println("║           🏋️  Your Fitness Booking Companion  🏋️                  ║");
        System.out.println("║                                                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
        System.out.println();
        pauseForEffect();
    }
    
    /**
     * Main menu for the application
     */
    private static void mainMenu() {
        while (true) {
            clearScreen();
            printHeader("FLIPFIT MAIN MENU");
            
            // Show login status
            if (currentUserId != null) {
                System.out.println("╔═════════════════════════════════════════════════╗");
                System.out.println("║  🟢 Logged in as: " + currentUserName + " (" + currentUserRole + ")");
                System.out.println("╚═════════════════════════════════════════════════╝");
                System.out.println();
            }
            
            // Show different menus based on login status
            if (currentUserId == null) {
                // Not logged in - show only login and registration options
                System.out.println("┌─────────────────────────────────────────────────┐");
                System.out.println("│  1. 👤 Login                                    │");
                System.out.println("│  2. 📝 Register as Customer                     │");
                System.out.println("│  3. 🏢 Register as Gym Owner                    │");
                System.out.println("│  0. ❌ Exit                                      │");
                System.out.println("└─────────────────────────────────────────────────┘");
                System.out.print("\n👉 Enter your choice: ");
                
                int choice = getIntInput();
                
                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        registerCustomer();
                        break;
                    case 3:
                        registerGymOwner();
                        break;
                    case 0:
                        exitApplication();
                        return;
                    default:
                        showError("Invalid choice! Please try again.");
                }
            } else {
                // Logged in - show role-specific menu
                if ("CUSTOMER".equals(currentUserRole)) {
                    showCustomerMainMenu();
                } else if ("GYM_OWNER".equals(currentUserRole)) {
                    showOwnerMainMenu();
                } else if ("ADMIN".equals(currentUserRole)) {
                    showAdminMainMenu();
                }
            }
        }
    }
    
    /**
     * Main menu for logged-in customers
     */
    private static void showCustomerMainMenu() {
        System.out.println("┌─────────────────────────────────────────────────┐");
        System.out.println("│  1. 🏃 Customer Menu                            │");
        System.out.println("│  2. 🔑 Change Password                          │");
        System.out.println("│  3. 🚪 Logout                                   │");
        System.out.println("│  0. ❌ Exit                                      │");
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.print("\n👉 Enter your choice: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                customerMenu();
                break;
            case 2:
                changePassword();
                break;
            case 3:
                logout();
                break;
            case 0:
                exitApplication();
                return;
            default:
                showError("Invalid choice! Please try again.");
        }
    }
    
    /**
     * Main menu for logged-in gym owners
     */
    private static void showOwnerMainMenu() {
        System.out.println("┌─────────────────────────────────────────────────┐");
        System.out.println("│  1. 🏋️  Gym Owner Menu                          │");
        System.out.println("│  2. 🔑 Change Password                          │");
        System.out.println("│  3. 🚪 Logout                                   │");
        System.out.println("│  0. ❌ Exit                                      │");
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.print("\n👉 Enter your choice: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                gymOwnerMenu();
                break;
            case 2:
                changePassword();
                break;
            case 3:
                logout();
                break;
            case 0:
                exitApplication();
                return;
            default:
                showError("Invalid choice! Please try again.");
        }
    }
    
    /**
     * Main menu for logged-in admin
     */
    private static void showAdminMainMenu() {
        System.out.println("┌─────────────────────────────────────────────────┐");
        System.out.println("│  1. 👑 Admin Menu                               │");
        System.out.println("│  2. 🔑 Change Password                          │");
        System.out.println("│  3. 🚪 Logout                                   │");
        System.out.println("│  0. ❌ Exit                                      │");
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.print("\n👉 Enter your choice: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                adminMenu();
                break;
            case 2:
                changePassword();
                break;
            case 3:
                logout();
                break;
            case 0:
                exitApplication();
                return;
            default:
                showError("Invalid choice! Please try again.");
        }
    }
    
    /**
     * Login functionality with Date/Time API feature
     */
    private static void login() {
        clearScreen();
        printHeader("LOGIN");
        
        System.out.print("📧 Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("🔒 Enter Password: ");
        String password = scanner.nextLine();
        
        System.out.print("👥 Enter Role (CUSTOMER/GYM_OWNER/ADMIN): ");
        String role = scanner.nextLine().toUpperCase();
        
        String jsonPayload = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"role\":\"%s\"}", 
                                          email, password, role);
        
        showLoading("Authenticating");
        String response = sendPostRequest("/auth/login", jsonPayload);
        
        if (response != null && !response.contains("error")) {
            // Parse response to get user details and login time
            currentUserId = extractJsonValue(response, "userId");
            currentUserName = extractJsonValue(response, "username");
            currentUserRole = extractJsonValue(response, "role");
            String loginTime = extractJsonValue(response, "loginTime");
            String welcomeMsg = extractJsonValue(response, "welcomeMessage");
            
            showSuccess("Login Successful!");
            System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
            System.out.println("║  " + welcomeMsg);
            System.out.println("║  Login Time: " + loginTime);
            System.out.println("╚═══════════════════════════════════════════════════════════╝");
        } else {
            showError("Login Failed! " + extractJsonValue(response, "error"));
        }
        
        pause();
    }
    
    /**
     * Customer menu
     */
    private static void customerMenu() {
        // Check if user is logged in and has correct role
        if (currentUserId == null) {
            showError("Please login first!");
            pause();
            return;
        }
        
        if (!"CUSTOMER".equals(currentUserRole)) {
            showError("Access denied! This menu is only for customers.");
            pause();
            return;
        }
        
        while (true) {
            clearScreen();
            printHeader("CUSTOMER MENU");
            
            System.out.println("┌─────────────────────────────────────────────────┐");
            System.out.println("│  1. 🏢 View Gym Centers by City                │");
            System.out.println("│  2. ⏰ View Available Slots                     │");
            System.out.println("│  3. 📅 Book a Slot                              │");
            System.out.println("│  4. 📋 View My Bookings                         │");
            System.out.println("│  5. ❌ Cancel Booking                            │");
            System.out.println("│  6. 🔔 View Notifications                       │");
            System.out.println("│  7. 📝 Join Waitlist                            │");
            System.out.println("│  8. 🔍 Find Nearest Slot                        │");
            System.out.println("│  0. ⬅️  Back to Main Menu                       │");
            System.out.println("└─────────────────────────────────────────────────┘");
            System.out.print("\n👉 Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    viewGymCentersByCity();
                    break;
                case 2:
                    viewAvailableSlots();
                    break;
                case 3:
                    bookSlot();
                    break;
                case 4:
                    viewMyBookings();
                    break;
                case 5:
                    cancelBooking();
                    break;
                case 6:
                    viewNotifications();
                    break;
                case 7:
                    joinWaitlist();
                    break;
                case 8:
                    findNearestSlot();
                    break;
                case 0:
                    return;
                default:
                    showError("Invalid choice!");
            }
        }
    }
    
    /**
     * Admin menu with Stream API features
     */
    private static void adminMenu() {
        // Check if user is logged in and has correct role
        if (currentUserId == null) {
            showError("Please login first!");
            pause();
            return;
        }
        
        if (!"ADMIN".equals(currentUserRole)) {
            showError("Access denied! This menu is only for administrators.");
            pause();
            return;
        }
        
        while (true) {
            clearScreen();
            printHeader("ADMIN MENU");
            
            System.out.println("┌─────────────────────────────────────────────────┐");
            System.out.println("│  1. ✅ View Approved Gym Owners (Stream API)    │");
            System.out.println("│  2. ⏳ View Pending Gym Owners (Stream API)     │");
            System.out.println("│  3. ✅ View Approved Gym Centers (Stream API)   │");
            System.out.println("│  4. ⏳ View Pending Gym Centers (Stream API)    │");
            System.out.println("│  5. ✓ Approve Gym Owner                         │");
            System.out.println("│  6. ✓ Approve Gym Center                        │");
            System.out.println("│  7. 📊 View System Statistics                   │");
            System.out.println("│  0. ⬅️  Back to Main Menu                       │");
            System.out.println("└─────────────────────────────────────────────────┘");
            System.out.print("\n👉 Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    viewApprovedOwners();
                    break;
                case 2:
                    viewPendingOwners();
                    break;
                case 3:
                    viewApprovedCenters();
                    break;
                case 4:
                    viewPendingCenters();
                    break;
                case 5:
                    approveGymOwner();
                    break;
                case 6:
                    approveGymCenter();
                    break;
                case 7:
                    viewStatistics();
                    break;
                case 0:
                    return;
                default:
                    showError("Invalid choice!");
            }
        }
    }
    
    /**
     * Gym Owner menu
     */
    private static void gymOwnerMenu() {
        // Check if user is logged in and has correct role
        if (currentUserId == null) {
            showError("Please login first!");
            pause();
            return;
        }
        
        if (!"GYM_OWNER".equals(currentUserRole)) {
            showError("Access denied! This menu is only for gym owners.");
            pause();
            return;
        }
        
        while (true) {
            clearScreen();
            printHeader("GYM OWNER MENU");
            
            System.out.println("┌─────────────────────────────────────────────────┐");
            System.out.println("│  1. 👤 View My Profile                          │");
            System.out.println("│  2. 📋 View All Gym Owners                      │");
            System.out.println("│  3. ⏳ View Pending Approvals                   │");
            System.out.println("│  0. ⬅️  Back to Main Menu                       │");
            System.out.println("└─────────────────────────────────────────────────┘");
            System.out.print("\n👉 Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    viewOwnerProfile();
                    break;
                case 2:
                    viewAllOwners();
                    break;
                case 3:
                    viewOwnerPendingApprovals();
                    break;
                case 0:
                    return;
                default:
                    showError("Invalid choice!");
            }
        }
    }
    
    // ==================== Customer Functions ====================
    
    private static void viewGymCentersByCity() {
        clearScreen();
        printHeader("VIEW GYM CENTERS BY CITY");
        
        System.out.print("🏙️  Enter City Name: ");
        String city = scanner.nextLine();
        
        showLoading("Fetching gym centers");
        String response = sendGetRequest("/customer/centers/city/" + city);
        
        if (response == null || response.equals("[]")) {
            showError("No gym centers found in " + city);
        } else {
            showSuccess("Gym Centers in " + city + ":");
            displaySelectableList(response, "gymId", "gymName", "gymAddress");
        }
        
        pause();
    }
    
    private static void viewAvailableSlots() {
        clearScreen();
        printHeader("VIEW AVAILABLE SLOTS");
        
        // Step 1: Ask for city
        System.out.print("🏙️  Enter City Name: ");
        String city = scanner.nextLine();
        
        showLoading("Fetching gym centers");
        String centersResponse = sendGetRequest("/customer/centers/city/" + city);
        
        if (centersResponse == null || centersResponse.equals("[]")) {
            showError("No gym centers found in " + city);
            pause();
            return;
        }
        
        // Step 2: Display centers and let user select
        showSuccess("Gym Centers in " + city + ":");
        String[] centerIds = displaySelectableList(centersResponse, "gymId", "gymName", "gymAddress");
        
        if (centerIds.length == 0) {
            pause();
            return;
        }
        
        System.out.print("\n👉 Select Center (Enter number): ");
        int centerChoice = getIntInput();
        
        if (centerChoice < 1 || centerChoice > centerIds.length) {
            showError("Invalid selection!");
            pause();
            return;
        }
        
        String selectedGymId = centerIds[centerChoice - 1];
        
        // Step 3: Show slots for selected center
        showLoading("Fetching available slots");
        String slotsResponse = sendGetRequest("/customer/slots/available/" + selectedGymId);
        
        if (slotsResponse != null && !slotsResponse.equals("[]")) {
            clearScreen();
            printHeader("AVAILABLE SLOTS");
            showSuccess("Slots at selected gym:");
            displaySlotsList(slotsResponse);
        } else {
            showError("No available slots found!");
        }
        
        pause();
    }
    
    private static void bookSlot() {
        clearScreen();
        printHeader("BOOK A SLOT");
        
        if (currentUserId == null) {
            showError("Please login first!");
            pause();
            return;
        }
        
        // Step 1: Ask for city
        System.out.print("🏙️  Enter City Name: ");
        String city = scanner.nextLine();
        
        showLoading("Fetching gym centers");
        String centersResponse = sendGetRequest("/customer/centers/city/" + city);
        
        if (centersResponse == null || centersResponse.equals("[]")) {
            showError("No gym centers found in " + city);
            pause();
            return;
        }
        
        // Step 2: Display centers and let user select
        clearScreen();
        printHeader("SELECT GYM CENTER");
        showSuccess("Gym Centers in " + city + ":");
        String[] centerIds = displaySelectableList(centersResponse, "gymId", "gymName", "gymAddress");
        
        if (centerIds.length == 0) {
            pause();
            return;
        }
        
        System.out.print("\n👉 Select Center (Enter number): ");
        int centerChoice = getIntInput();
        
        if (centerChoice < 1 || centerChoice > centerIds.length) {
            showError("Invalid selection!");
            pause();
            return;
        }
        
        String selectedGymId = centerIds[centerChoice - 1];
        
        // Step 3: Show available slots
        showLoading("Fetching available slots");
        String slotsResponse = sendGetRequest("/customer/slots/available/" + selectedGymId);
        
        if (slotsResponse == null || slotsResponse.equals("[]")) {
            showError("No available slots found!");
            pause();
            return;
        }
        
        // Step 4: Display slots and let user select
        clearScreen();
        printHeader("SELECT SLOT");
        showSuccess("Available Slots:");
        String[] slotIds = displaySlotsList(slotsResponse);
        
        if (slotIds.length == 0) {
            pause();
            return;
        }
        
        System.out.print("\n👉 Select Slot (Enter number): ");
        int slotChoice = getIntInput();
        
        if (slotChoice < 1 || slotChoice > slotIds.length) {
            showError("Invalid selection!");
            pause();
            return;
        }
        
        String selectedSlotId = slotIds[slotChoice - 1];
        
        // Step 5: Ask for booking date
        System.out.print("\n📅 Enter Booking Date (YYYY-MM-DD): ");
        String bookingDate = scanner.nextLine();
        
        // Step 6: Book the slot
        String jsonPayload = String.format("{\"customerId\":\"%s\",\"slotId\":\"%s\",\"bookingDate\":\"%s\"}", 
                                          currentUserId, selectedSlotId, bookingDate);
        
        showLoading("Booking slot");
        String response = sendPostRequest("/customer/booking/create", jsonPayload);
        
        if (response != null && !response.contains("error")) {
            clearScreen();
            showSuccess("🎉 Slot Booked Successfully!");
            System.out.println("╔═══════════════════════════════════════════════════╗");
            System.out.println("║  Booking ID: " + extractJsonValue(response, "bookingId"));
            System.out.println("║  Date: " + bookingDate);
            System.out.println("╚═══════════════════════════════════════════════════╝");
        } else {
            showError("Booking failed: " + extractJsonValue(response, "error"));
        }
        
        pause();
    }
    
    private static void viewMyBookings() {
        clearScreen();
        printHeader("MY BOOKINGS");
        
        System.out.print("👤 Enter Customer ID: ");
        String customerId = scanner.nextLine();
        
        showLoading("Fetching your bookings");
        String response = sendGetRequest("/customer/bookings/" + customerId);
        
        if (response != null) {
            showSuccess("Your Bookings:");
            displayJsonArray(response);
        }
        
        pause();
    }
    
    private static void cancelBooking() {
        clearScreen();
        printHeader("CANCEL BOOKING");
        
        System.out.print("🎫 Enter Booking ID: ");
        String bookingId = scanner.nextLine();
        
        showLoading("Cancelling booking");
        String response = sendDeleteRequest("/customer/booking/" + bookingId);
        
        if (response != null && !response.contains("error")) {
            showSuccess("Booking cancelled successfully!");
        } else {
            showError("Cancellation failed!");
        }
        
        pause();
    }
    
    private static void viewNotifications() {
        clearScreen();
        printHeader("NOTIFICATIONS");
        
        System.out.print("👤 Enter Customer ID: ");
        String customerId = scanner.nextLine();
        
        showLoading("Fetching notifications");
        String response = sendGetRequest("/customer/notifications/" + customerId);
        
        if (response != null) {
            showSuccess("Your Notifications:");
            displayJsonArray(response);
        }
        
        pause();
    }
    
    private static void joinWaitlist() {
        clearScreen();
        printHeader("JOIN WAITLIST");
        
        System.out.print("👤 Customer ID: ");
        String customerId = scanner.nextLine();
        
        System.out.print("🏢 Slot ID: ");
        String slotId = scanner.nextLine();
        
        System.out.print("📅 Requested Date (YYYY-MM-DD): ");
        String requestedDate = scanner.nextLine();
        
        String jsonPayload = String.format("{\"customerId\":\"%s\",\"slotId\":\"%s\",\"requestedDate\":\"%s\"}", 
                                          customerId, slotId, requestedDate);
        
        showLoading("Adding to waitlist");
        String response = sendPostRequest("/customer/waitlist/join", jsonPayload);
        
        if (response != null && !response.contains("error")) {
            showSuccess("Added to waitlist successfully!");
        } else {
            showError("Failed to join waitlist!");
        }
        
        pause();
    }
    
    private static void findNearestSlot() {
        clearScreen();
        printHeader("FIND NEAREST SLOT");
        
        System.out.print("🏢 Gym ID: ");
        String gymId = scanner.nextLine();
        
        System.out.print("⏰ Preferred Time (HH:MM): ");
        String preferredTime = scanner.nextLine();
        
        showLoading("Finding nearest slot");
        String response = sendGetRequest("/customer/slots/nearest/" + gymId + "/" + preferredTime);
        
        if (response != null) {
            showSuccess("Nearest Available Slot:");
            System.out.println(response);
        }
        
        pause();
    }
    
    // ==================== Admin Functions with Stream API ====================
    
    private static void viewApprovedOwners() {
        clearScreen();
        printHeader("APPROVED GYM OWNERS (Stream API Filter)");
        
        showLoading("Fetching approved gym owners using Stream API");
        String response = sendGetRequest("/admin/owners/approved");
        
        if (response != null) {
            showSuccess("Approved Gym Owners (Filtered by Stream API):");
            displayJsonArray(response);
        }
        
        pause();
    }
    
    private static void viewPendingOwners() {
        clearScreen();
        printHeader("PENDING GYM OWNERS (Stream API Filter)");
        
        showLoading("Fetching pending gym owners using Stream API");
        String response = sendGetRequest("/admin/owners/pending");
        
        if (response != null) {
            showSuccess("Pending Gym Owners (Filtered by Stream API):");
            displayJsonArray(response);
        }
        
        pause();
    }
    
    private static void viewApprovedCenters() {
        clearScreen();
        printHeader("APPROVED GYM CENTERS (Stream API Filter)");
        
        showLoading("Fetching approved gym centers using Stream API");
        String response = sendGetRequest("/admin/centers/approved");
        
        if (response != null) {
            showSuccess("Approved Gym Centers (Filtered by Stream API):");
            displayJsonArray(response);
        }
        
        pause();
    }
    
    private static void viewPendingCenters() {
        clearScreen();
        printHeader("PENDING GYM CENTERS (Stream API Filter)");
        
        showLoading("Fetching pending gym centers using Stream API");
        String response = sendGetRequest("/admin/centers/pending");
        
        if (response != null) {
            showSuccess("Pending Gym Centers (Filtered by Stream API):");
            displayJsonArray(response);
        }
        
        pause();
    }
    
    private static void approveGymOwner() {
        clearScreen();
        printHeader("APPROVE GYM OWNER");
        
        System.out.print("👤 Enter Owner ID: ");
        String ownerId = scanner.nextLine();
        
        showLoading("Approving gym owner");
        String response = sendPutRequest("/admin/owner/approve/" + ownerId, "{}");
        
        if (response != null && !response.contains("error")) {
            showSuccess("Gym owner approved successfully!");
        } else {
            showError("Approval failed!");
        }
        
        pause();
    }
    
    private static void approveGymCenter() {
        clearScreen();
        printHeader("APPROVE GYM CENTER");
        
        System.out.print("🏢 Enter Gym ID: ");
        String gymId = scanner.nextLine();
        
        showLoading("Approving gym center");
        String response = sendPutRequest("/admin/center/approve/" + gymId, "{}");
        
        if (response != null && !response.contains("error")) {
            showSuccess("Gym center approved successfully!");
        } else {
            showError("Approval failed!");
        }
        
        pause();
    }
    
    private static void viewStatistics() {
        clearScreen();
        printHeader("SYSTEM STATISTICS");
        
        showLoading("Fetching statistics");
        String response = sendGetRequest("/admin/statistics");
        
        if (response != null) {
            showSuccess("System Statistics:");
            System.out.println(response);
        }
        
        pause();
    }
    
    // ==================== Owner Functions ====================
    
    private static void viewOwnerProfile() {
        clearScreen();
        printHeader("GYM OWNER PROFILE");
        
        System.out.print("👤 Enter Owner ID: ");
        String ownerId = scanner.nextLine();
        
        showLoading("Fetching profile");
        String response = sendGetRequest("/owner/profile/" + ownerId);
        
        if (response != null) {
            showSuccess("Owner Profile:");
            System.out.println(response);
        }
        
        pause();
    }
    
    private static void viewAllOwners() {
        clearScreen();
        printHeader("ALL GYM OWNERS");
        
        showLoading("Fetching all gym owners");
        String response = sendGetRequest("/owner/all");
        
        if (response != null) {
            showSuccess("All Gym Owners:");
            displayJsonArray(response);
        }
        
        pause();
    }
    
    private static void viewOwnerPendingApprovals() {
        clearScreen();
        printHeader("PENDING APPROVALS");
        
        showLoading("Fetching pending approvals");
        String response = sendGetRequest("/owner/pending");
        
        if (response != null) {
            showSuccess("Pending Gym Owners:");
            displayJsonArray(response);
        }
        
        pause();
    }
    
    // ==================== Registration & Auth Functions ====================
    
    private static void registerCustomer() {
        clearScreen();
        printHeader("REGISTER AS CUSTOMER");
        
        System.out.print("👤 Name: ");
        String name = scanner.nextLine();
        
        System.out.print("📧 Email: ");
        String email = scanner.nextLine();
        
        System.out.print("🔒 Password: ");
        String password = scanner.nextLine();
        
        System.out.print("🏠 Address: ");
        String address = scanner.nextLine();
        
        String jsonPayload = String.format("{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"address\":\"%s\"}", 
                                          name, email, password, address);
        
        showLoading("Registering");
        String response = sendPostRequest("/auth/register/customer", jsonPayload);
        
        if (response != null && !response.contains("error")) {
            showSuccess("Registration successful!");
            System.out.println(response);
        } else {
            showError("Registration failed: " + extractJsonValue(response, "error"));
        }
        
        pause();
    }
    
    private static void registerGymOwner() {
        clearScreen();
        printHeader("REGISTER AS GYM OWNER");
        
        System.out.print("👤 Name: ");
        String name = scanner.nextLine();
        
        System.out.print("📧 Email: ");
        String email = scanner.nextLine();
        
        System.out.print("🔒 Password: ");
        String password = scanner.nextLine();
        
        System.out.print("🏠 Address: ");
        String address = scanner.nextLine();
        
        System.out.print("💳 PAN Card: ");
        String panCard = scanner.nextLine();
        
        System.out.print("🪪 Aadhar Card: ");
        String aadharCard = scanner.nextLine();
        
        System.out.print("🏢 GST Number: ");
        String gstNumber = scanner.nextLine();
        
        String jsonPayload = String.format(
            "{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"address\":\"%s\"," +
            "\"panCard\":\"%s\",\"aadharCard\":\"%s\",\"gstNumber\":\"%s\"}", 
            name, email, password, address, panCard, aadharCard, gstNumber);
        
        showLoading("Registering");
        String response = sendPostRequest("/auth/register/owner", jsonPayload);
        
        if (response != null && !response.contains("error")) {
            showSuccess("Registration successful! Pending admin approval.");
            System.out.println(response);
        } else {
            showError("Registration failed: " + extractJsonValue(response, "error"));
        }
        
        pause();
    }
    
    private static void changePassword() {
        clearScreen();
        printHeader("CHANGE PASSWORD");
        
        if (currentUserId == null) {
            showError("Please login first!");
            pause();
            return;
        }
        
        System.out.print("🔒 Old Password: ");
        String oldPassword = scanner.nextLine();
        
        System.out.print("🔐 New Password: ");
        String newPassword = scanner.nextLine();
        
        String jsonPayload = String.format("{\"userId\":\"%s\",\"oldPassword\":\"%s\",\"newPassword\":\"%s\"}", 
                                          currentUserId, oldPassword, newPassword);
        
        showLoading("Changing password");
        String response = sendPutRequest("/auth/password/change", jsonPayload);
        
        if (response != null && !response.contains("error")) {
            showSuccess("Password changed successfully!");
        } else {
            showError("Password change failed!");
        }
        
        pause();
    }
    
    private static void logout() {
        currentUserId = null;
        currentUserRole = null;
        currentUserName = null;
        showSuccess("Logged out successfully!");
        pause();
    }
    
    private static void exitApplication() {
        clearScreen();
        System.out.println("\n");
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                               ║");
        System.out.println("║            Thank you for using FlipFit! 🏋️                    ║");
        System.out.println("║                Stay Fit, Stay Healthy! 💪                     ║");
        System.out.println("║                                                               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.exit(0);
    }
    
    // ==================== HTTP Request Methods ====================
    
    private static String sendGetRequest(String endpoint) {
        try {
            URL url = new URL(API_BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return response.toString();
            }
        } catch (Exception e) {
            showError("Connection error: " + e.getMessage());
        }
        return null;
    }
    
    private static String sendPostRequest(String endpoint, String jsonPayload) {
        try {
            URL url = new URL(API_BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            showError("Connection error: " + e.getMessage());
        }
        return null;
    }
    
    private static String sendPutRequest(String endpoint, String jsonPayload) {
        try {
            URL url = new URL(API_BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            showError("Connection error: " + e.getMessage());
        }
        return null;
    }
    
    private static String sendDeleteRequest(String endpoint) {
        try {
            URL url = new URL(API_BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return response.toString();
            }
        } catch (Exception e) {
            showError("Connection error: " + e.getMessage());
        }
        return null;
    }
    
    // ==================== Utility Methods ====================
    
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private static void printHeader(String title) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║  " + centerText(title, 59) + "  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text + " ".repeat(Math.max(0, padding));
    }
    
    private static void showSuccess(String message) {
        System.out.println("\n✅ " + message + "\n");
    }
    
    private static void showError(String message) {
        System.out.println("\n❌ " + message + "\n");
    }
    
    private static void showLoading(String message) {
        System.out.print("\n⏳ " + message);
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(300);
                System.out.print(".");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }
    
    private static void pause() {
        System.out.print("\n🔄 Press Enter to continue...");
        scanner.nextLine();
    }
    
    private static void pauseForEffect() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("❌ Invalid input! Please enter a number: ");
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return input;
    }
    
    private static String extractJsonValue(String json, String key) {
        if (json == null) return "";
        
        String searchKey = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) return "";
        
        startIndex += searchKey.length();
        int endIndex = json.indexOf("\"", startIndex);
        if (endIndex == -1) return "";
        
        return json.substring(startIndex, endIndex);
    }
    
    private static void displayJsonArray(String jsonArray) {
        if (jsonArray == null || jsonArray.equals("[]")) {
            System.out.println("   📭 No data found");
            return;
        }
        
        // Simple pretty print for JSON array
        String formatted = jsonArray.replace("[{", "[\n  {")
                                     .replace("},{", "},\n  {")
                                     .replace("}]", "}\n]")
                                     .replace(",\"", ",\n    \"");
        
        System.out.println(formatted);
    }
    
    /**
     * Display a numbered list of items and return their IDs
     * 
     * @param jsonArray JSON array string
     * @param idField Field name for ID
     * @param nameField Field name for display name
     * @param addressField Field name for address
     * @return Array of IDs in order
     */
    private static String[] displaySelectableList(String jsonArray, String idField, 
                                                   String nameField, String addressField) {
        if (jsonArray == null || jsonArray.equals("[]")) {
            System.out.println("   📭 No data found");
            return new String[0];
        }
        
        // Split by objects
        String[] items = jsonArray.split("\\},\\{");
        String[] ids = new String[items.length];
        
        System.out.println("\n┌─────────────────────────────────────────────────────────────────┐");
        
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            
            // Extract values
            String id = extractJsonValue("{" + item + "}", idField);
            String name = extractJsonValue("{" + item + "}", nameField);
            String address = extractJsonValue("{" + item + "}", addressField);
            
            ids[i] = id;
            
            // Display numbered item
            System.out.printf("│ %2d. %-20s │ %-35s │%n", (i + 1), name, address);
        }
        
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
        
        return ids;
    }
    
    /**
     * Display slots with serial numbers
     * 
     * @param slotsJson JSON array of slots
     * @return Array of slot IDs
     */
    private static String[] displaySlotsList(String slotsJson) {
        if (slotsJson == null || slotsJson.equals("[]")) {
            System.out.println("   📭 No slots available");
            return new String[0];
        }
        
        // Split by objects
        String[] slots = slotsJson.split("\\},\\{");
        String[] slotIds = new String[slots.length];
        
        System.out.println("\n┌──────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ No. │  Start Time  │   End Time   │ Seats │  Price   │  Status    │");
        System.out.println("├──────────────────────────────────────────────────────────────────────┤");
        
        for (int i = 0; i < slots.length; i++) {
            String slot = slots[i];
            
            // Extract values
            String slotId = extractJsonValue("{" + slot + "}", "slotId");
            String startTime = extractJsonValue("{" + slot + "}", "startTime");
            String endTime = extractJsonValue("{" + slot + "}", "endTime");
            String totalSeats = extractJsonValue("{" + slot + "}", "totalSeats");
            String availSeats = extractJsonValue("{" + slot + "}", "availableSeats");
            String price = extractJsonValue("{" + slot + "}", "price");
            
            slotIds[i] = slotId;
            
            // Format seats info
            String seatsInfo = availSeats + "/" + totalSeats;
            String status = Integer.parseInt(availSeats) > 0 ? "✅ Available" : "❌ Full";
            
            // Display numbered slot
            System.out.printf("│ %3d │  %10s  │  %10s  │ %5s │ ₹%-7s │ %-10s │%n", 
                            (i + 1), startTime, endTime, seatsInfo, price, status);
        }
        
        System.out.println("└──────────────────────────────────────────────────────────────────────┘");
        
        return slotIds;
    }
}
