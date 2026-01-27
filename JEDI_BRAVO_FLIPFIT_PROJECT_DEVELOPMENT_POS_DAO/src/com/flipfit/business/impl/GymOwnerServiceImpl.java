package com.flipfit.business.impl;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.business.GymOwnerService;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.impl.GymOwnerDAOImpl;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of GymOwnerService interface
 * Uses DAO layer for database operations
 */
public class GymOwnerServiceImpl implements GymOwnerService {

    private GymOwnerDAO ownerDAO;

    public GymOwnerServiceImpl() {
        this.ownerDAO = new GymOwnerDAOImpl();
    }

    @Override
    public String registerGymOwner(String userId, String panCard, String aadharCard, String gstNumber) {
        // Validate input
        if (userId == null || userId.isEmpty()) {
            System.out.println("User ID is required!");
            return null;
        }

        // Generate owner ID
        String ownerId = generateOwnerId();

        // Insert gym owner
        boolean success = ownerDAO.insertGymOwner(ownerId, userId, panCard, aadharCard, gstNumber);

        if (success) {
            System.out.println("Gym Owner registered successfully! Waiting for admin approval.");
            return ownerId;
        } else {
            System.out.println("Failed to register gym owner!");
            return null;
        }
    }

    @Override
    public GymOwner getGymOwnerById(String ownerId) {
        return ownerDAO.getGymOwnerById(ownerId);
    }

    @Override
    public GymOwner getGymOwnerByUserId(String userId) {
        return ownerDAO.getGymOwnerByUserId(userId);
    }

    @Override
    public List<GymOwner> getAllGymOwners() {
        return ownerDAO.getAllGymOwners();
    }

    @Override
    public List<GymOwner> getPendingApprovals() {
        return ownerDAO.getPendingGymOwners();
    }

    @Override
    public boolean updateGymOwner(String ownerId, String panCard, String aadharCard, String gstNumber) {
        // Validate owner exists
        GymOwner owner = ownerDAO.getGymOwnerById(ownerId);
        if (owner == null) {
            System.out.println("Gym Owner not found!");
            return false;
        }

        // Update owner
        return ownerDAO.updateGymOwner(ownerId, panCard, aadharCard, gstNumber);
    }

    /**
     * Generate unique owner ID
     * @return Generated owner ID
     */
    private String generateOwnerId() {
        return "OWN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public boolean addGymCenter(String gymId, String ownerId, String city, String address) {
        // 1. Validation
        if(gymId == null || ownerId == null) {
            System.out.println("Invalid Gym ID or Owner ID");
            return false;
        }

        // 2. Create Bean
        GymCenter center = new GymCenter();
        center.setCenterId(gymId);
        center.setOwnerId(ownerId);
        center.setCenterCity(city);
        center.setCenterLocn(address);
        // Default isApproved is false in Bean/DB

        // 3. Call DAO
        boolean success = ownerDAO.addGymCenter(center);

        if(success) {
            System.out.println("Gym Center Added Successfully! Waiting for Admin Approval.");
        } else {
            System.out.println("Failed to add Gym Center. ID might already exist.");
        }
        return success;
    }
}
