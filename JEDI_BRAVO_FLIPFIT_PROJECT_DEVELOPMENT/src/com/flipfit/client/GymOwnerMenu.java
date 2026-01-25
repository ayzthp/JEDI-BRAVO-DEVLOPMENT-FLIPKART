package com.flipfit.client;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import com.flipfit.bean.GymUser;
import com.flipfit.business.GymOwnerService;
import com.flipfit.helper.DataStore;

public class GymOwnerMenu {

	private final GymOwnerService gymOwnerService;
	private final GymUser loggedInUser;

	public GymOwnerMenu(GymOwnerService gymOwnerService, GymUser loggedInUser) {
		this.gymOwnerService = gymOwnerService;
		this.loggedInUser = loggedInUser;
	}

	public void displayMenu() {
		Scanner scanner = new Scanner(System.in);
		int choice;

		do {
			System.out.println("\n--- Gym Owner Menu ---");
			System.out.println("1. View my gyms");
			System.out.println("2. Add a new gym");
			System.out.println("3. View bookings for my gyms");
			System.out.println("4. Logout");
			System.out.print("Enter your choice: ");

			if (!scanner.hasNextInt()) {
				System.out.println("No more input detected. Returning to previous menu.");
				return;
			}
			choice = scanner.nextInt();

			switch (choice) {
			case 1:
				viewMyGyms();
				break;
			case 2:
				addGym(scanner);
				break;
			case 3:
				viewBookings();
				break;
			case 4:
				System.out.println("Logging out from Gym Owner Menu.");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		} while (choice != 4);
	}

	private void viewMyGyms() {
		List<GymCenter> centers = gymOwnerService.viewMyCenters(loggedInUser.getUserId());
		if (centers.isEmpty()) {
			System.out.println("You don't own any centers yet.");
			return;
		}
		for (GymCenter center : centers) {
			System.out.println(center.getCenterId() + " | " + center.getCenterLocn() + " | " + center.getCenterCity());
			for (GymSlot slot : center.getCenterSlot()) {
				System.out.println("   Slot " + slot.getSlotId() + " " + slot.getStartTime() + "-" + slot.getEndTime()
						+ " Seats:" + slot.getAvailableSeats() + "/" + slot.getTotalSeats());
			}
		}
	}

	private void addGym(Scanner scanner) {
		
		System.out.print("Enter center location: ");
		String loc = scanner.next();
		
		System.out.print("Enter city: ");
		String city = scanner.next();

		GymCenter center = new GymCenter();
		center.setCenterId(DataStore.nextCenterId());
		center.setCenterLocn(loc);
		center.setCenterCity(city);
		center.setOwnerId(loggedInUser.getUserId());
		center.setCenterSlot(new java.util.ArrayList<>());

		System.out.print("Add default 6 slots (y/n)? ");
		String choice = scanner.next();
		if ("y".equalsIgnoreCase(choice)) {
			center.getCenterSlot().addAll(defaultSlots());
		}
		gymOwnerService.addCenter(center);
		System.out.println("Center added with id: " + center.getCenterId());
	}

	private List<GymSlot> defaultSlots() {
		List<GymSlot> slots = new java.util.ArrayList<>();
		int seats = 10;
		slots.add(buildSlot("06:00", "07:00", seats));
		slots.add(buildSlot("07:00", "08:00", seats));
		slots.add(buildSlot("08:00", "09:00", seats));
		slots.add(buildSlot("18:00", "19:00", seats));
		slots.add(buildSlot("19:00", "20:00", seats));
		slots.add(buildSlot("20:00", "21:00", seats));
		return slots;
	}

	private GymSlot buildSlot(String start, String end, int seats) {
		GymSlot slot = new GymSlot();
		slot.setSlotId(DataStore.nextSlotId());
		slot.setStartTime(LocalTime.parse(start));
		slot.setEndTime(LocalTime.parse(end));
		slot.setTotalSeats(seats);
		slot.setAvailableSeats(seats);
		return slot;
	}

	private void viewBookings() {
		Collection<Booking> bookings = gymOwnerService.viewBookingsForOwner(loggedInUser.getUserId());
		if (bookings.isEmpty()) {
			System.out.println("No bookings yet for your centers.");
			return;
		}
		for (Booking booking : bookings) {
			System.out.println(booking.getBookingId() + " | " + booking.getGymUser().getUserId() + " | Slot:"
					+ booking.getGymSlot().getSlotId() + " | " + booking.getDateAndTime());
		}
	}
}
