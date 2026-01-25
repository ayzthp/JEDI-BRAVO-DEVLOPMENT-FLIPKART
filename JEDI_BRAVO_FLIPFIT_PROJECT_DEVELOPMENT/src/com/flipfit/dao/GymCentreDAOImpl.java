
package com.flipfit.dao;

/**
 * 
 */


import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GymCentreDAOImpl implements GymCentreDAO {
    // In a real app, this would be a database connection. 
    // Here we use an in-memory list for demonstration.
    private List<GymCenter> centres = new ArrayList<>();

    @Override
    public void save(GymCenter centre) {
        centres.add(centre);
    }

    @Override
    public List<GymCenter> findByCity(String city) {
        return centres.stream()
                .filter(c -> c.getCenterCity().equalsIgnoreCase(city))
                .toList();
    }

    @Override
    public GymSlot getSlotByTime(String centreId, LocalTime startTime, LocalTime endTime) {
        // Logic to find a centre and then search its slots for a time match [cite: 92]
        return centres.stream()
                .filter(c -> c.getCenterId().equals(centreId))
                .flatMap(c -> c.getCenterSlot().stream())
                .filter(s -> s.getStartTime().equals(startTime) && s.getEndTime().equals(endTime))
                .findFirst()
                .orElse(null);
    }
}