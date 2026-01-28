package com.flipfit.business;

import com.flipfit.bean.GymOwner;
import java.util.List;

/**
 * Service interface for Gym Owner operations
 * Provides business logic for gym owner management
 */
public interface GymOwnerService {
    /**
     * Register a new gym owner
     * @param userId User ID associated with the owner
     * @param panCard PAN card number
     * @param aadharCard Aadhar card number
     * @param gstNumber GST number
     * @return Gym Owner ID if successful, null otherwise
     */
    String registerGymOwner(String userId, String panCard, String aadharCard, String gstNumber);
    
    /**
     * Get gym owner by owner ID
     * @param ownerId Owner ID
     * @return GymOwner object if found, null otherwise
     */
    GymOwner getGymOwnerById(String ownerId);
    
    /**
     * Get gym owner by user ID
     * @param userId User ID
     * @return GymOwner object if found, null otherwise
     */
    GymOwner getGymOwnerByUserId(String userId);
    
    /**
     * Get all gym owners
     * @return List of all gym owners
     */
    List<GymOwner> getAllGymOwners();
    
    /**
     * Get pending gym owner approvals
     * @return List of pending gym owners
     */
    List<GymOwner> getPendingApprovals();
    
    /**
     * Update gym owner information
     * @param ownerId Owner ID
     * @param panCard Updated PAN card
     * @param aadharCard Updated Aadhar card
     * @param gstNumber Updated GST number
     * @return true if successful, false otherwise
     */
    boolean updateGymOwner(String ownerId, String panCard, String aadharCard, String gstNumber);
    boolean addGymCenter(String gymId, String ownerId, String city, String address);
}
