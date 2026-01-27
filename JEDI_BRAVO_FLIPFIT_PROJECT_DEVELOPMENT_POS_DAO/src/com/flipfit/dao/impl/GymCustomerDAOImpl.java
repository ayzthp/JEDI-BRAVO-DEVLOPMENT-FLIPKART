package com.flipfit.dao.impl;

import com.flipfit.bean.GymUser;
import com.flipfit.constant.SQLConstants;
import com.flipfit.dao.GymCustomerDAO;
import com.flipfit.dao.GymUserDAO;
import com.flipfit.constants.DatabaseConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import java.time.LocalTime;
/**
 * Implementation of GymCustomerDAO interface
 * Handles all database operations for Customer management
 */
public class GymCustomerDAOImpl implements GymCustomerDAO {
    
    private GymUserDAO gymUserDAO;
    
    public GymCustomerDAOImpl() {
        this.gymUserDAO = new GymUserDAOImpl();
    }
    
    /**
     * Get database connection
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            DatabaseConstants.DB_URL,
            DatabaseConstants.DB_USER,
            DatabaseConstants.DB_PASSWORD
        );
    }
    
    @Override
    public boolean insertGymCustomer(String customerId, String userId, 
                                      Date dateOfBirth, String fitnessGoal) {
        String sql = "INSERT INTO GymCustomer (customer_id, user_id, date_of_birth, fitness_goal) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            pstmt.setString(2, userId);
            pstmt.setDate(3, dateOfBirth != null ? new java.sql.Date(dateOfBirth.getTime()) : null);
            pstmt.setString(4, fitnessGoal);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public GymUser getGymCustomerById(String customerId) {
        String sql = "SELECT user_id FROM GymCustomer WHERE customer_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String userId = rs.getString("user_id");
                return gymUserDAO.getUserById(userId);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public GymUser getGymCustomerByUserId(String userId) {
        String sql = "SELECT user_id FROM GymCustomer WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return gymUserDAO.getUserById(userId);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<GymUser> getAllGymCustomers() {
        List<GymUser> customers = new ArrayList<>();
        String sql = "SELECT user_id FROM GymCustomer";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String userId = rs.getString("user_id");
                GymUser user = gymUserDAO.getUserById(userId);
                if (user != null) {
                    customers.add(user);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    
    @Override
    public boolean updateGymCustomer(String customerId, Date dateOfBirth, String fitnessGoal) {
        String sql = "UPDATE GymCustomer SET date_of_birth = ?, fitness_goal = ? " +
                     "WHERE customer_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, dateOfBirth != null ? new java.sql.Date(dateOfBirth.getTime()) : null);
            pstmt.setString(2, fitnessGoal);
            pstmt.setString(3, customerId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean updateMembership(String customerId, Date membershipStartDate, 
                                     Date membershipEndDate, boolean isPremium) {
        String sql = "UPDATE GymCustomer SET membership_start_date = ?, " +
                     "membership_end_date = ?, is_premium = ? WHERE customer_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, membershipStartDate != null ? new java.sql.Date(membershipStartDate.getTime()) : null);
            pstmt.setDate(2, membershipEndDate != null ? new java.sql.Date(membershipEndDate.getTime()) : null);
            pstmt.setBoolean(3, isPremium);
            pstmt.setString(4, customerId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteGymCustomer(String customerId) {
        String sql = "DELETE FROM GymCustomer WHERE customer_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public int getCustomerBookingCount(String customerId) {
        String sql = "SELECT COUNT(*) FROM Booking WHERE customer_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<GymCenter> fetchGymCentersByCity(String city) {
        List<GymCenter> centers = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.FETCH_GYM_CENTERS_BY_CITY)) {

            pstmt.setString(1, city);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                GymCenter center = new GymCenter();
                center.setCenterId(rs.getString("centerId")); // Ensure col names match DB
                center.setCenterLocn(rs.getString("centerLocn"));
                center.setCenterCity(rs.getString("centerCity"));
                center.setOwnerId(rs.getString("ownerId"));
                centers.add(center);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return centers;
    }

    @Override
    public GymSlot getSlotDetails(String slotId) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.GET_SLOT_DETAILS)) {
            pstmt.setString(1, slotId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                GymSlot slot = new GymSlot();
                slot.setSlotId(rs.getString("slot_id"));
                // Need to convert SQL Time/String to LocalTime
                slot.setStartTime(LocalTime.parse(rs.getString("start_time")));
                slot.setAvailableSeats(rs.getInt("available_seats"));
                // Set other fields as needed
                return slot;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean decrementSlotSeats(String slotId) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.DECREMENT_SLOT_SEATS)) {
            pstmt.setString(1, slotId);
            return pstmt.executeUpdate() > 0; // Returns true only if row updated (seats were > 0)
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean incrementSlotSeats(String slotId) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.INCREMENT_SLOT_SEATS)) {
            pstmt.setString(1, slotId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean insertBooking(Booking booking) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.INSERT_BOOKING)) {

            pstmt.setString(1, booking.getBookingId());
            pstmt.setString(2, booking.getGymUser().getUserId());
            pstmt.setString(3, booking.getGymSlot().getSlotId());
            pstmt.setString(4, booking.getDateAndTime()); // Storing Date here
            pstmt.setString(5, booking.getBookingStatus().toString());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public String findConflictingBookingId(String userId, String date, String startTime) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.FIND_CONFLICTING_BOOKING)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, date);
            pstmt.setString(3, startTime);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("booking_id");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean updateBookingStatus(String bookingId, String status) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.UPDATE_BOOKING_STATUS)) {

            pstmt.setString(1, status);
            pstmt.setString(2, bookingId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public List<GymSlot> getSlotsByGymId(String gymId) {
        List<GymSlot> slots = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.GET_ALL_SLOTS_FOR_GYM)) {

            pstmt.setString(1, gymId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                GymSlot slot = new GymSlot();
                slot.setSlotId(rs.getString("slot_id"));
                slot.setStartTime(LocalTime.parse(rs.getString("start_time")));
                slot.setAvailableSeats(rs.getInt("available_seats"));
                slots.add(slot);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return slots;
    }
}
