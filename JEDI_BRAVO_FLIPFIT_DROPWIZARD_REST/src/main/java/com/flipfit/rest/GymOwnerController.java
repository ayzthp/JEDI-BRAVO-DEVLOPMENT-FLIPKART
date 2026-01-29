package com.flipfit.rest;

import com.flipfit.bean.GymOwner;
import com.flipfit.business.GymOwnerService;
import com.flipfit.business.impl.GymOwnerServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for gym owner operations.
 * Handles owner profile viewing and updates.
 * Uses for-each loops for iterations as per Java 8+ standards.
 * 
 * @author JEDI-BRAVO
 * @version 1.0
 * @since 2026-01-28
 */
@Path("/owner")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GymOwnerController {
    
    private final GymOwnerService gymOwnerService;
    
    /**
     * Constructor initializing gym owner service.
     */
    public GymOwnerController() {
        this.gymOwnerService = new GymOwnerServiceImpl();
    }
    
    /**
     * Get gym owner profile and approval status.
     * 
     * @param ownerId The owner ID
     * @return Response with owner details
     */
    @GET
    @Path("/profile/{ownerId}")
    public Response getOwnerProfile(@PathParam("ownerId") String ownerId) {
        try {
            GymOwner owner = gymOwnerService.getGymOwnerById(ownerId);
            
            if (owner != null) {
                return Response.ok(owner).build();
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Owner not found");
                return Response.status(Response.Status.NOT_FOUND).entity(error).build();
            }
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch owner profile: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Get all gym owners.
     * 
     * @return Response with list of all gym owners
     */
    @GET
    @Path("/all")
    public Response getAllOwners() {
        try {
            List<GymOwner> owners = gymOwnerService.getAllGymOwners();
            return Response.ok(owners).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch gym owners: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Get pending approval gym owners.
     * 
     * @return Response with list of pending gym owners
     */
    @GET
    @Path("/pending")
    public Response getPendingApprovals() {
        try {
            List<GymOwner> pending = gymOwnerService.getPendingApprovals();
            return Response.ok(pending).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch pending approvals: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Update gym owner information.
     * 
     * @param ownerId The owner ID
     * @param ownerData Map containing updated owner details
     * @return Response with update status
     */
    @PUT
    @Path("/update/{ownerId}")
    public Response updateOwner(@PathParam("ownerId") String ownerId, 
                                Map<String, String> ownerData) {
        try {
            String panCard = ownerData.get("panCard");
            String aadharCard = ownerData.get("aadharCard");
            String gstNumber = ownerData.get("gstNumber");
            
            boolean success = gymOwnerService.updateGymOwner(ownerId, panCard, aadharCard, gstNumber);
            
            if (success) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Gym owner updated successfully");
                response.put("ownerId", ownerId);
                return Response.ok(response).build();
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Failed to update gym owner");
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update gym owner: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
}
