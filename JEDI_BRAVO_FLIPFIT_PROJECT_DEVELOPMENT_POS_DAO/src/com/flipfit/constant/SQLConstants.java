package com.flipfit.constant;

// TODO: Auto-generated Javadoc
/**
 * The Class SQLConstants.
 * Contains all SQL query constants for the FlipFit application
 *
 * @author JEDI-BRAVO
 * @ClassName SQLConstants
 */
public class SQLConstants {
    
    /**
     * Private constructor to prevent instantiation.
     */
    private SQLConstants() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }
    
    // ========== USER QUERIES ==========
    public static final String FETCH_GYM_CENTERS_BY_CITY =
            "SELECT * FROM GymCenter WHERE city = ?";

    // [CORRECTED] Assumes table 'GymSlot' has columns: slot_id, start_time, available_seats
    public static final String GET_SLOT_DETAILS =
            "SELECT * FROM GymSlot WHERE slot_id = ?";

    public static final String DECREMENT_SLOT_SEATS =
            "UPDATE GymSlot SET available_seats = available_seats - 1 WHERE slot_id = ? AND available_seats > 0";

    public static final String INCREMENT_SLOT_SEATS =
            "UPDATE GymSlot SET available_seats = available_seats + 1 WHERE slot_id = ?";


    // [CORRECTED] Added 'gym_id' column assumption for GymSlot table
    public static final String GET_ALL_SLOTS_FOR_GYM =
            "SELECT * FROM GymSlot WHERE gym_id = ? ORDER BY start_time";

    /** The constant for inserting a new user. */
    public static final String INSERT_USER = 
        "INSERT INTO User (user_id, name, email, password, address, phone_number, role_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    /** The constant for selecting user by ID. */
    public static final String SELECT_USER_BY_ID = 
        "SELECT u.*, r.role_name FROM User u JOIN Role r ON u.role_id = r.role_id WHERE u.user_id = ?";
    
    /** The constant for selecting user by email. */
    public static final String SELECT_USER_BY_EMAIL = 
        "SELECT u.*, r.role_name FROM User u JOIN Role r ON u.role_id = r.role_id WHERE u.email = ?";
    
    /** The constant for selecting all users. */
    public static final String SELECT_ALL_USERS = 
        "SELECT u.*, r.role_name FROM User u JOIN Role r ON u.role_id = r.role_id";
    
    /** The constant for updating user. */
    public static final String UPDATE_USER = 
        "UPDATE User SET name = ?, email = ?, password = ?, address = ?, role_id = ? WHERE user_id = ?";
    
    /** The constant for deleting user. */
    public static final String DELETE_USER = 
        "DELETE FROM User WHERE user_id = ?";
    
    /** The constant for authenticating user. */
    public static final String AUTHENTICATE_USER = 
        "SELECT u.*, r.role_name FROM User u JOIN Role r ON u.role_id = r.role_id WHERE u.email = ? AND u.password = ? AND u.is_active = TRUE";
    
    /** The constant for checking email existence. */
    public static final String CHECK_EMAIL_EXISTS = 
        "SELECT COUNT(*) FROM User WHERE email = ?";
    
    // ========== GYM OWNER QUERIES ==========
    
    /** The constant for inserting gym owner. */
    public static final String INSERT_GYM_OWNER = 
        "INSERT INTO GymOwner (owner_id, user_id, pan_card, aadhar_card, gst_number) VALUES (?, ?, ?, ?, ?)";
    
    /** The constant for selecting gym owner by ID. */
    public static final String SELECT_GYM_OWNER_BY_ID = 
        "SELECT * FROM GymOwner WHERE owner_id = ?";
    
    /** The constant for selecting gym owner by user ID. */
    public static final String SELECT_GYM_OWNER_BY_USER_ID = 
        "SELECT * FROM GymOwner WHERE user_id = ?";
    
    /** The constant for selecting all gym owners. */
    public static final String SELECT_ALL_GYM_OWNERS = 
        "SELECT * FROM GymOwner";
    
    /** The constant for selecting pending gym owners. */
    public static final String SELECT_PENDING_GYM_OWNERS = 
        "SELECT * FROM GymOwner WHERE is_approved = FALSE";
    
    /** The constant for approving gym owner. */
    public static final String APPROVE_GYM_OWNER = 
        "UPDATE GymOwner SET is_approved = TRUE, approval_date = CURRENT_TIMESTAMP WHERE owner_id = ?";
    
    /** The constant for updating gym owner. */
    public static final String UPDATE_GYM_OWNER = 
        "UPDATE GymOwner SET pan_card = ?, aadhar_card = ?, gst_number = ? WHERE owner_id = ?";
    
    /** The constant for deleting gym owner. */
    public static final String DELETE_GYM_OWNER = 
        "DELETE FROM GymOwner WHERE owner_id = ?";
    
    // ========== GYM CUSTOMER QUERIES ==========
    
    /** The constant for inserting gym customer. */
    public static final String INSERT_GYM_CUSTOMER = 
        "INSERT INTO GymCustomer (customer_id, user_id, date_of_birth, fitness_goal) VALUES (?, ?, ?, ?)";
    
    /** The constant for selecting gym customer by ID. */
    public static final String SELECT_GYM_CUSTOMER_BY_ID = 
        "SELECT user_id FROM GymCustomer WHERE customer_id = ?";
    
    /** The constant for selecting gym customer by user ID. */
    public static final String SELECT_GYM_CUSTOMER_BY_USER_ID = 
        "SELECT user_id FROM GymCustomer WHERE user_id = ?";
    
    /** The constant for selecting all gym customers. */
    public static final String SELECT_ALL_GYM_CUSTOMERS = 
        "SELECT user_id FROM GymCustomer";
    
    /** The constant for updating gym customer. */
    public static final String UPDATE_GYM_CUSTOMER = 
        "UPDATE GymCustomer SET date_of_birth = ?, fitness_goal = ? WHERE customer_id = ?";
    
    /** The constant for updating membership. */
    public static final String UPDATE_MEMBERSHIP = 
        "UPDATE GymCustomer SET membership_start_date = ?, membership_end_date = ?, is_premium = ? WHERE customer_id = ?";
    
    /** The constant for deleting gym customer. */
    public static final String DELETE_GYM_CUSTOMER = 
        "DELETE FROM GymCustomer WHERE customer_id = ?";
    
    /** The constant for getting customer booking count. */
    public static final String GET_CUSTOMER_BOOKING_COUNT = 
        "SELECT COUNT(*) FROM Booking WHERE customer_id = ?";
    
    // ========== ADMIN QUERIES ==========
    
    /** The constant for selecting pending gym center approvals. */
    public static final String SELECT_PENDING_GYM_CENTERS = 
        "SELECT * FROM GymCenter WHERE is_approved = FALSE";
    
    /** The constant for approving gym center. */
    public static final String APPROVE_GYM_CENTER = 
        "UPDATE GymCenter SET is_approved = TRUE, approval_date = CURRENT_TIMESTAMP WHERE gym_id = ?";
    
    /** The constant for rejecting gym owner. */
    public static final String REJECT_GYM_OWNER = 
        "UPDATE Registration SET status = 'REJECTED', reviewed_date = CURRENT_TIMESTAMP, remarks = ? WHERE user_id = (SELECT user_id FROM GymOwner WHERE owner_id = ?)";
    
    /** The constant for rejecting gym center. */
    public static final String REJECT_GYM_CENTER = 
        "UPDATE GymCenter SET is_approved = FALSE WHERE gym_id = ?";
    
    /** The constant for selecting all gym centers. */
    public static final String SELECT_ALL_GYM_CENTERS = 
        "SELECT * FROM GymCenter";
    
    /** The constant for getting total users count. */
    public static final String COUNT_TOTAL_USERS = 
        "SELECT COUNT(*) as count FROM User";
    
    /** The constant for getting total gym owners count. */
    public static final String COUNT_TOTAL_GYM_OWNERS = 
        "SELECT COUNT(*) as count FROM GymOwner";
    
    /** The constant for getting total customers count. */
    public static final String COUNT_TOTAL_CUSTOMERS = 
        "SELECT COUNT(*) as count FROM GymCustomer";
    
    /** The constant for getting total bookings count. */
    public static final String COUNT_TOTAL_BOOKINGS = 
        "SELECT COUNT(*) as count FROM Booking";
    
    /** The constant for getting total gym centers count. */
    public static final String COUNT_TOTAL_GYM_CENTERS = 
        "SELECT COUNT(*) as count FROM GymCenter";

    public static final String INSERT_GYM_CENTER =
            "INSERT INTO GymCenter (gym_id, owner_id, gym_address, city, is_approved) VALUES (?, ?, ?, ?, FALSE)";

    // [Optional] If you need to check if ID exists
    public static final String CHECK_GYM_EXISTS = "SELECT COUNT(*) FROM GymCenter WHERE gym_id = ?";
    public static final String INSERT_BOOKING =
            "INSERT INTO Booking (booking_id, user_id, slot_id, date, booking_status) VALUES (?, ?, ?, ?, ?)";

    // 2. CONFLICT CHECK: Changed 'b.status' to 'b.booking_status'
    public static final String FIND_CONFLICTING_BOOKING =
            "SELECT b.booking_id FROM Booking b " +
                    "JOIN GymSlot s ON b.slot_id = s.slot_id " +
                    "WHERE b.user_id = ? AND b.date = ? AND b.booking_status = 'CONFIRMED' AND s.start_time = ?";

    // 3. CANCEL/UPDATE: Changed 'status' to 'booking_status'
    public static final String UPDATE_BOOKING_STATUS =
            "UPDATE Booking SET booking_status = ? WHERE booking_id = ?";
}
