/**
 * 
 */
package com.flipfit.business;

/**
 * 
 */

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymSlot;
import com.flipfit.bean.GymUser;
import com.flipfit.enums.BookingStatus;
import com.flipfit.dao.BookingDAO;
import com.flipfit.dao.GymCentreDAO;
import com.flipfit.dao.GymCentreDAOImpl;

import java.util.Date;
import java.time.LocalTime;
import java.util.UUID;

public class FlipFitBookingService implements BookingService {
    
    // Dependencies as shown in the class diagram [cite: 61]
    private BookingDAO bookingDAO;
    private GymCentreDAO gymDAO;

    public FlipFitBookingService() {
        this.bookingDAO = new BookingDAO();
        this.gymDAO = new GymCentreDAOImpl();
    }

    /**
     * Business Logic: Finds available slot by time, generates ID, and saves booking.
     */
    @Override
    public String createBooking(String userId, String centreId, LocalTime startTime, LocalTime endTime, Date date) {
        
        // 1. Find the correct GymSlot using the provided times [cite: 63, 72, 92]
        GymSlot slot = gymDAO.getSlotByTime(centreId, startTime, endTime);

        // 2. Validate availability based on Slot logic [cite: 33]
        if (slot == null || !slot.isAvailable()) {
            System.out.println("Slot not found or no seats available.");
            return null;
        }

        // 3. Internal Rule: Check for overlapping bookings [cite: 69]
        checkOverlapAndRemove(userId, date, startTime);

        // 4. Instantiate the Booking bean and populate with system-generated data
        String generatedBookingId = UUID.randomUUID().toString();
        Booking newBooking = new Booking();
        
        newBooking.setBookingId(generatedBookingId); // System-allotted ID
        
        // Create GymUser wrapper for the userId
        GymUser user = new GymUser();
        user.setUserId(userId);
        newBooking.setGymUser(user);
        
        newBooking.setGymSlot(slot);
        newBooking.setDateAndTime(date.toString() + " " + startTime.toString());
        newBooking.setBookingStatus(BookingStatus.CONFIRMED); // 

        // 5. Update state: decrease seats and save to DB [cite: 34, 75]
        slot.decreaseAvailability();
        bookingDAO.save(newBooking);

        return generatedBookingId;
    }

    /**
     * Logic: Cancels existing record and re-runs createBooking for the new time[cite: 66, 65].
     */
    @Override
    public boolean modifyBooking(String userId, String oldBookingId, LocalTime startTime, LocalTime endTime, Date date) {
        if (oldBookingId != null) {
            cancelBooking(oldBookingId);
        }
        
        String newBookingId = createBooking(userId, "CENTRE_ID_VAR", startTime, endTime, date);
        return newBookingId != null;
    }

    /**
     * Updates status to CANCELLED[cite: 49, 78].
     */
    @Override
    public boolean cancelBooking(String bookingId) {
        bookingDAO.updateStatus(bookingId, BookingStatus.CANCELLED);
        // Logic to promote someone from Waitlist could be added here [cite: 71]
        return true;
    }

    private void checkOverlapAndRemove(String userId, Date date, LocalTime time) {
        // Simple overlap handling lives in BookingServiceImpl; left as a placeholder here.
    }
}
