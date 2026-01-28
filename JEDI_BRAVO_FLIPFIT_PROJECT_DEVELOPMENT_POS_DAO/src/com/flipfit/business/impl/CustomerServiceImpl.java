package com.flipfit.business.impl;

import com.flipfit.bean.GymUser;
import com.flipfit.business.CustomerService;
import com.flipfit.dao.GymCustomerDAO;
import com.flipfit.dao.impl.GymCustomerDAOImpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import com.flipfit.bean.GymUser;
import com.flipfit.enums.BookingStatus;
import java.util.UUID;
import java.util.List;
/**
 * Implementation of CustomerService interface
 * Uses DAO layer for database operations
 */
public class CustomerServiceImpl implements CustomerService {
    
    private GymCustomerDAO customerDAO;
    
    public CustomerServiceImpl() {
        this.customerDAO = new GymCustomerDAOImpl();
    }
    
    @Override
    public String registerCustomer(String userId, Date dateOfBirth, String fitnessGoal) {
        // Validate input
        if (userId == null || userId.isEmpty()) {
            System.out.println("User ID is required!");
            return null;
        }
        
        // Generate customer ID
        String customerId = generateCustomerId();
        
        // Insert customer
        boolean success = customerDAO.insertGymCustomer(customerId, userId, dateOfBirth, fitnessGoal);
        
        if (success) {
            System.out.println("Customer registered successfully!");
            return customerId;
        } else {
            System.out.println("Failed to register customer!");
            return null;
        }
    }

    @Override
    public List<GymCenter> viewGymsByCity(String city) {
        return customerDAO.fetchGymCentersByCity(city);
    }

    @Override
    public boolean bookSlot(String userId, String slotId, String gymId, String date) {

        // 1. Get Slot Details
        GymSlot slot = customerDAO.getSlotDetails(slotId);
        if (slot == null) {
            System.out.println("Error: Slot not found!");
            return false;
        }

        // --- USE CASE 3: AUTO REMOVE OLD BOOKING ---
        // Check if user has a CONFIRMED booking at the same time (even in diff gym)
        String conflictingBookingId = customerDAO.findConflictingBookingId(userId, date, slot.getStartTime().toString());
        if (conflictingBookingId != null) {
            System.out.println("Info: Found conflicting booking (ID: " + conflictingBookingId + "). Auto-cancelling it...");
            // We need slotId of old booking to increment seats, assumes DAO can handle or we fetch it.
            // For simplicity, just marking Cancelled here. Ideally, fetch old slotId to increment.
            customerDAO.updateBookingStatus(conflictingBookingId, BookingStatus.CANCELLED.toString());
        }

        // --- USE CASE 4 & 2: CHECK CAPACITY & WAITLIST ---
        boolean isSeatAvailable = customerDAO.decrementSlotSeats(slotId);

        Booking booking = new Booking();
        booking.setBookingId("BKG" + UUID.randomUUID().toString().substring(0,8));

        // Construct User and Slot objects for Booking Bean
        GymUser user = new GymUser(); user.setUserId(userId);
        booking.setGymUser(user);
        booking.setGymSlot(slot);
        booking.setDateAndTime(date);

        if (isSeatAvailable) {
            // Success Path
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            boolean success = customerDAO.insertBooking(booking);
            if(success) System.out.println("Success: Slot Booked! Booking ID: " + booking.getBookingId());
            return success;
        } else {
            // --- USE CASE 2: WAITLIST & SUGGESTION ---
            System.out.println("Alert: Slot is Full! Adding you to Waitlist...");
            booking.setBookingStatus(BookingStatus.WAITLIST);
            customerDAO.insertBooking(booking); // Add to DB as Waitlist

            // Suggestion Logic: Nearest time slot in same gym on same date
            System.out.println("--- Suggestion ---");
            List<GymSlot> allSlots = customerDAO.getSlotsByGymId(gymId);

            boolean foundSuggestion = false;
            for (GymSlot s : allSlots) {
                // Check if slot is later than requested slot AND has seats
                if (s.getStartTime().isAfter(slot.getStartTime()) && s.getAvailableSeats() > 0) {
                    System.out.println("Nearest Available Slot: " + s.getStartTime() + " - " + s.getEndTime());
                    foundSuggestion = true;
                    break; // Only show the nearest one
                }
            }
            if (!foundSuggestion) {
                System.out.println("No other slots available today.");
            }
            return true; // Return true as Waitlist entry was successful
        }
    }

    @Override
    public boolean cancelBooking(String bookingId, String slotId) {
        // --- USE CASE 5: CANCEL BOOKING ---
        boolean statusUpdated = customerDAO.updateBookingStatus(bookingId, BookingStatus.CANCELLED.toString());
        if (statusUpdated) {
            // Free up the seat
            customerDAO.incrementSlotSeats(slotId);
            System.out.println("Booking Cancelled Successfully.");
            return true;
        }
        return false;
    }
    @Override
    public GymUser getCustomerById(String customerId) {
        return customerDAO.getGymCustomerById(customerId);
    }
    
    @Override
    public GymUser getCustomerByUserId(String userId) {
        return customerDAO.getGymCustomerByUserId(userId);
    }
    
    @Override
    public List<GymUser> getAllCustomers() {
        return customerDAO.getAllGymCustomers();
    }
    
    @Override
    public boolean updateCustomer(String customerId, Date dateOfBirth, String fitnessGoal) {
        // Validate customer exists
        GymUser customer = customerDAO.getGymCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found!");
            return false;
        }
        
        // Update customer
        return customerDAO.updateGymCustomer(customerId, dateOfBirth, fitnessGoal);
    }
    
    @Override
    public int getBookingCount(String customerId) {
        return customerDAO.getCustomerBookingCount(customerId);
    }
    
    /**
     * Generate unique customer ID
     * @return Generated customer ID
     */
    private String generateCustomerId() {
        return "CUS" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
