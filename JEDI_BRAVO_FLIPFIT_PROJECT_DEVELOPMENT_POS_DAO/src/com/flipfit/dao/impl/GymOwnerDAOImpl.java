package com.flipfit.dao.impl;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.GymUser;
import com.flipfit.constant.SQLConstants;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.GymUserDAO;
import com.flipfit.constants.DatabaseConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of GymOwnerDAO interface
 * Handles all database operations for Gym Owner management
 */
public class GymOwnerDAOImpl implements GymOwnerDAO {
    
    private GymUserDAO gymUserDAO;
    
    public GymOwnerDAOImpl() {
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
    public boolean insertGymOwner(String ownerId, String userId, String panCard, 
                                   String aadharCard, String gstNumber) {
        String sql = "INSERT INTO GymOwner (owner_id, user_id, pan_card, aadhar_card, gst_number) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, ownerId);
            pstmt.setString(2, userId);
            pstmt.setString(3, panCard);
            pstmt.setString(4, aadharCard);
            pstmt.setString(5, gstNumber);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public GymOwner getGymOwnerById(String ownerId) {
        String sql = "SELECT * FROM GymOwner WHERE owner_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, ownerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToGymOwner(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public GymOwner getGymOwnerByUserId(String userId) {
        String sql = "SELECT * FROM GymOwner WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToGymOwner(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<GymOwner> getAllGymOwners() {
        List<GymOwner> owners = new ArrayList<>();
        String sql = "SELECT * FROM GymOwner";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                owners.add(mapResultSetToGymOwner(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owners;
    }
    
    @Override
    public List<GymOwner> getPendingGymOwners() {
        List<GymOwner> owners = new ArrayList<>();
        String sql = "SELECT * FROM GymOwner WHERE is_approved = FALSE";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                owners.add(mapResultSetToGymOwner(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owners;
    }
    
    @Override
    public boolean approveGymOwner(String ownerId) {
        String sql = "UPDATE GymOwner SET is_approved = TRUE, approval_date = CURRENT_TIMESTAMP " +
                     "WHERE owner_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, ownerId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean updateGymOwner(String ownerId, String panCard, 
                                   String aadharCard, String gstNumber) {
        String sql = "UPDATE GymOwner SET pan_card = ?, aadhar_card = ?, gst_number = ? " +
                     "WHERE owner_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, panCard);
            pstmt.setString(2, aadharCard);
            pstmt.setString(3, gstNumber);
            pstmt.setString(4, ownerId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteGymOwner(String ownerId) {
        String sql = "DELETE FROM GymOwner WHERE owner_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, ownerId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Helper method to map ResultSet to GymOwner object
     */
    private GymOwner mapResultSetToGymOwner(ResultSet rs) throws SQLException {
        GymOwner owner = new GymOwner();
        
        // Get user information
        String userId = rs.getString("user_id");
        GymUser user = gymUserDAO.getUserById(userId);
        owner.setUser(user);
        
        return owner;
    }

    @Override
    public boolean addGymCenter(GymCenter gymCenter) {
        // SQLConstants.INSERT_GYM_CENTER = "INSERT INTO GymCenter (gym_id, owner_id, gym_address, city, is_approved) VALUES (?, ?, ?, ?, FALSE)"

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLConstants.INSERT_GYM_CENTER)) {

            pstmt.setString(1, gymCenter.getCenterId());
            pstmt.setString(2, gymCenter.getOwnerId());
            pstmt.setString(3, gymCenter.getCenterLocn());
            pstmt.setString(4, gymCenter.getCenterCity());
            // is_approved is handled by SQL default or 'FALSE' in query

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
