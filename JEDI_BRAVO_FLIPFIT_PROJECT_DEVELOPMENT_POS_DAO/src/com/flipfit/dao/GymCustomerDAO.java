package com.flipfit.dao;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import com.flipfit.bean.GymUser;
import java.util.List;
import java.util.Date;

/**
 * DAO interface for GymCustomer operations
 * Defines CRUD operations for Customer management
 */
public interface GymCustomerDAO {

    List<GymCenter> fetchGymCentersByCity(String city);

    // Use Case 2 & 4: Slot Operations
    GymSlot getSlotDetails(String slotId);
    boolean decrementSlotSeats(String slotId); // Returns false if full
    boolean incrementSlotSeats(String slotId);

    // Use Case 2: Booking
    boolean insertBooking(Booking booking);

    // Use Case 3: Conflict Check
    String findConflictingBookingId(String userId, String date, String startTime);

    // Use Case 5: Cancellation
    boolean updateBookingStatus(String bookingId, String status);

    // Use Case 2 (Suggestion): Get all slots
    List<GymSlot> getSlotsByGymId(String gymId);
    /**
     * Insert a new gym customer into the database
     * @param customerId Customer ID
     * @param userId User ID associated with the customer
     * @param dateOfBirth Date of birth
     * @param fitnessGoal Fitness goal description
     * @return true if insertion is successful, false otherwise
     */
    boolean insertGymCustomer(String customerId, String userId, Date dateOfBirth, String fitnessGoal);
    
    /**
     * Retrieve a gym customer by customer ID
     * @param customerId Customer ID to search for
     * @return GymUser object if found, null otherwise
     */
    GymUser getGymCustomerById(String customerId);
    
    /**
     * Retrieve a gym customer by user ID
     * @param userId User ID to search for
     * @return GymUser object if found, null otherwise
     */
    GymUser getGymCustomerByUserId(String userId);
    
    /**
     * Retrieve all gym customers from the database
     * @return List of all GymUser objects representing customers
     */
    List<GymUser> getAllGymCustomers();
    
    /**
     * Update gym customer information
     * @param customerId Customer ID
     * @param dateOfBirth Updated date of birth
     * @param fitnessGoal Updated fitness goal
     * @return true if update is successful, false otherwise
     */
    boolean updateGymCustomer(String customerId, Date dateOfBirth, String fitnessGoal);
    
    /**
     * Update customer membership details
     * @param customerId Customer ID
     * @param membershipStartDate Membership start date
     * @param membershipEndDate Membership end date
     * @param isPremium Premium membership flag
     * @return true if update is successful, false otherwise
     */
    boolean updateMembership(String customerId, Date membershipStartDate, Date membershipEndDate, boolean isPremium);
    
    /**
     * Delete a gym customer by customer ID
     * @param customerId Customer ID to delete
     * @return true if deletion is successful, false otherwise
     */
    boolean deleteGymCustomer(String customerId);
    
    /**
     * Get customer's booking count
     * @param customerId Customer ID
     * @return Number of bookings made by the customer
     */
    int getCustomerBookingCount(String customerId);
}
