package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymUser;
import java.util.Date;
import java.util.List;

/**
 * Service interface for Customer operations
 * Provides business logic for customer management
 */
public interface CustomerService {
    /**
     * Register a new customer
     * @param userId User ID associated with the customer
     * @param dateOfBirth Date of birth
     * @param fitnessGoal Fitness goal
     * @return Customer ID if successful, null otherwise
     */
    String registerCustomer(String userId, Date dateOfBirth, String fitnessGoal);
    
    /**
     * Get customer by customer ID
     * @param customerId Customer ID
     * @return GymUser object if found, null otherwise
     */
    GymUser getCustomerById(String customerId);
    
    /**
     * Get customer by user ID
     * @param userId User ID
     * @return GymUser object if found, null otherwise
     */
    GymUser getCustomerByUserId(String userId);
    
    /**
     * Get all customers
     * @return List of all customers
     */
    List<GymUser> getAllCustomers();
    
    /**
     * Update customer information
     * @param customerId Customer ID
     * @param dateOfBirth Updated date of birth
     * @param fitnessGoal Updated fitness goal
     * @return true if successful, false otherwise
     */
    boolean updateCustomer(String customerId, Date dateOfBirth, String fitnessGoal);
    
    /**
     * Get customer's booking count
     * @param customerId Customer ID
     * @return Number of bookings
     */
    int getBookingCount(String customerId);
    List<GymCenter> viewGymsByCity(String city);

    /**
     * Handles booking logic:
     * 1. Auto-removes old bookings (Use Case 3)
     * 2. Checks capacity (Use Case 4)
     * 3. Suggests nearest slot if full (Use Case 2)
     */
    boolean bookSlot(String userId, String slotId, String gymId, String date);

    /**
     * Cancel a booking
     */
    boolean cancelBooking(String bookingId, String slotId);
}
