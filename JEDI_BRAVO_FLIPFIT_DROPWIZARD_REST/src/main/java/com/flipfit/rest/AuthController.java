package com.flipfit.rest;

import com.flipfit.bean.GymUser;
import com.flipfit.business.CustomerService;
import com.flipfit.business.GymOwnerService;
import com.flipfit.business.UserService;
import com.flipfit.business.impl.CustomerServiceImpl;
import com.flipfit.business.impl.GymOwnerServiceImpl;
import com.flipfit.business.impl.UserServiceImpl;
import com.flipfit.enums.Role;
import com.flipfit.exception.InvalidCredentialsException;
import com.flipfit.exception.RegistrationFailedException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for authentication operations.
 * Handles user login, registration, and password management.
 * 
 * @author JEDI-BRAVO
 * @version 1.0
 * @since 2026-01-28
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {
    
    private final UserService userService;
    private final CustomerService customerService;
    private final GymOwnerService gymOwnerService;
    
    /**
     * Constructor initializing service instances.
     */
    public AuthController() {
        this.userService = new UserServiceImpl();
        this.customerService = new CustomerServiceImpl();
        this.gymOwnerService = new GymOwnerServiceImpl();
    }
    
    /**
     * Login endpoint for all user types.
     * Returns user details with current login timestamp using Java Date/Time API.
     * 
     * @param credentials Map containing email, password, and role
     * @return Response with user details and login timestamp
     */
    @POST
    @Path("/login")
    public Response login(Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            String roleStr = credentials.getOrDefault("role", "CUSTOMER");
            Role role = Role.valueOf(roleStr.toUpperCase());
            
            GymUser user = userService.login(email, password, role);
            
            // Use Java Date/Time API to get current timestamp
            LocalDateTime loginTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' hh:mm:ss a");
            String formattedLoginTime = loginTime.format(formatter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("userId", user.getUserId());
            response.put("username", user.getName());
            response.put("email", user.getEmail());
            response.put("role", user.getRole().toString());
            response.put("address", user.getAddress());
            response.put("loginTime", formattedLoginTime);
            response.put("welcomeMessage", "Welcome " + user.getName() + "!");
            
            if (user == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid credentials");
                return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
            }
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Login failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Registration endpoint for gym customers.
     * 
     * @param registrationData Map containing user registration details
     * @return Response with registration status
     */
    @POST
    @Path("/register/customer")
    public Response registerCustomer(Map<String, String> registrationData) {
        try {
            String name = registrationData.get("name");
            String email = registrationData.get("email");
            String password = registrationData.get("password");
            String address = registrationData.get("address");
            
            // First register as user
            GymUser user = userService.register(name, email, password, address, Role.CUSTOMER);
            
            if (user == null) {
                throw new RegistrationFailedException("User registration failed");
            }
            
            // Then register as customer (additional customer-specific data can be added later)
            // For now, just using basic customer registration
            String customerId = customerService.registerCustomer(user.getUserId(), null, "General Fitness");
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Customer registration successful");
            response.put("userId", user.getUserId());
            response.put("customerId", customerId);
            
            return Response.status(Response.Status.CREATED).entity(response).build();
            
        } catch (RegistrationFailedException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Registration endpoint for gym owners.
     * 
     * @param registrationData Map containing owner registration details
     * @return Response with registration status
     */
    @POST
    @Path("/register/owner")
    public Response registerGymOwner(Map<String, String> registrationData) {
        try {
            String name = registrationData.get("name");
            String email = registrationData.get("email");
            String password = registrationData.get("password");
            String address = registrationData.get("address");
            String panCard = registrationData.get("panCard");
            String aadharCard = registrationData.get("aadharCard");
            String gstNumber = registrationData.get("gstNumber");
            
            // First register as user
            GymUser user = userService.register(name, email, password, address, Role.GYM_OWNER);
            
            if (user == null) {
                throw new RegistrationFailedException("User registration failed");
            }
            
            // Then register as gym owner with business details
            String ownerId = gymOwnerService.registerGymOwner(user.getUserId(), panCard, aadharCard, gstNumber);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Gym owner registration successful. Pending admin approval.");
            response.put("userId", user.getUserId());
            response.put("ownerId", ownerId);
            
            return Response.status(Response.Status.CREATED).entity(response).build();
            
        } catch (RegistrationFailedException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
    
    /**
     * Change password endpoint for authenticated users.
     * 
     * @param passwordData Map containing userId, oldPassword, and newPassword
     * @return Response with password change status
     */
    @PUT
    @Path("/password/change")
    public Response changePassword(Map<String, String> passwordData) {
        try {
            String userId = passwordData.get("userId");
            String oldPassword = passwordData.get("oldPassword");
            String newPassword = passwordData.get("newPassword");
            
            userService.changePassword(userId, oldPassword, newPassword);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Password changed successfully");
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Password change failed: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
    }
}
