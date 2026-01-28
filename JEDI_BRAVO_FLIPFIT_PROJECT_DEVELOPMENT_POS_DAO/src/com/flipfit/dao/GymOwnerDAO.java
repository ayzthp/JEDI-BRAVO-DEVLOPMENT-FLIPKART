package com.flipfit.dao;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import java.util.List;

/**
 * DAO interface for GymOwner operations
 * Defines CRUD operations for Gym Owner management
 */
public interface GymOwnerDAO {
    
    /**
     * Insert a new gym owner into the database
     * @param ownerId Owner ID
     * @param userId User ID associated with the owner
     * @param panCard PAN card number
     * @param aadharCard Aadhar card number
     * @param gstNumber GST number
     * @return true if insertion is successful, false otherwise
     */
    boolean insertGymOwner(String ownerId, String userId, String panCard, String aadharCard, String gstNumber);
    
    /**
     * Retrieve a gym owner by owner ID
     * @param ownerId Owner ID to search for
     * @return GymOwner object if found, null otherwise
     */
    GymOwner getGymOwnerById(String ownerId);
    
    /**
     * Retrieve a gym owner by user ID
     * @param userId User ID to search for
     * @return GymOwner object if found, null otherwise
     */
    GymOwner getGymOwnerByUserId(String userId);
    
    /**
     * Retrieve all gym owners from the database
     * @return List of all GymOwner objects
     */
    List<GymOwner> getAllGymOwners();
    
    /**
     * Retrieve all pending approval gym owners
     * @return List of GymOwner objects waiting for approval
     */
    List<GymOwner> getPendingGymOwners();
    
    /**
     * Approve a gym owner
     * @param ownerId Owner ID to approve
     * @return true if approval is successful, false otherwise
     */
    boolean approveGymOwner(String ownerId);
    
    /**
     * Update gym owner information
     * @param ownerId Owner ID
     * @param panCard Updated PAN card number
     * @param aadharCard Updated Aadhar card number
     * @param gstNumber Updated GST number
     * @return true if update is successful, false otherwise
     */
    boolean updateGymOwner(String ownerId, String panCard, String aadharCard, String gstNumber);
    
    /**
     * Delete a gym owner by owner ID
     * @param ownerId Owner ID to delete
     * @return true if deletion is successful, false otherwise
     */
    boolean deleteGymOwner(String ownerId);

    boolean addGymCenter(GymCenter gymCenter);
}
