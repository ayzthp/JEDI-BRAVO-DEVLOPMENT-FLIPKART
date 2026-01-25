package com.flipfit.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.flipfit.bean.Booking;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import com.flipfit.bean.GymUser;
import com.flipfit.business.CustomerService;
import com.flipfit.helper.DataStore;
import com.flipfit.validation.DateValidation;

public class CustomerMenu {

	private final CustomerService customerService;
	private final GymUser loggedInUser;

	public CustomerMenu(CustomerService customerService, GymUser loggedInUser) {
		this.customerService = customerService;
		this.loggedInUser = loggedInUser;
	}

	public void displayMenu() {
		Scanner scanner = new Scanner(System.in);
		int choice;

		do {
			System.out.println("\n--- Customer Menu ---");
			System.out.println("1. View gyms in a city");
			System.out.println("2. Book a slot");
			System.out.println("3. View my bookings");
			System.out.println("4. Cancel a booking");
			System.out.println("5. Logout");
			System.out.print("Enter your choice: ");

			if (!scanner.hasNextInt()) {
				System.out.println("No more input detected. Returning to previous menu.");
				return;
			}
			choice = scanner.nextInt();

			switch (choice) {
			case 1:
				viewGyms(scanner);
				break;
			case 2:
				bookSlot(scanner);
				break;
			case 3:
				viewMyBookings();
				break;
			case 4:
				cancelBooking(scanner);
				break;
			case 5:
				System.out.println("Logging out from Customer Menu.");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		} while (choice != 5);
	}

	private void viewGyms(Scanner scanner) {
		System.out.print("Enter city: ");
		String city = scanner.next();
		List<GymCenter> centers = customerService.viewCentersByCity(city);
		if (centers.isEmpty()) {
			System.out.println("No centers found for city: " + city);
			return;
		}
		for (GymCenter center : centers) {
			System.out.println(center.getCenterId() + " | " + center.getCenterLocn());
			for (GymSlot slot : center.getCenterSlot()) {
				System.out.println("   " + slot.getSlotId() + " " + slot.getStartTime() + "-" + slot.getEndTime()
						+ " Seats:" + slot.getAvailableSeats() + "/" + slot.getTotalSeats());
			}
		}
	}

	private void bookSlot(Scanner scanner) {
		
		System.out.print("Enter center id: ");
		String centerId = scanner.next();
		
		boolean validate= true;
		String dateInput;
		do {
			if(!validate) System.out.println("Date validation failed. Try again");
			System.out.print("Enter date (yyyy-MM-dd): ");
			dateInput= scanner.next();
			DateValidation.validateDate(dateInput);
		
		}while(!validate);
		
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateInput);
			System.out.println("Available slots for center " + centerId + " on " + dateInput + ":");
			GymSlot chosen = null;
			for (GymSlot slot : customerService.viewSlotsForCenter(centerId)) {
				int remaining = remainingSeats(slot, date);
				System.out.println(slot.getSlotId() + " | " + slot.getStartTime() + "-" + slot.getEndTime()
						+ " | Seats left: " + remaining);
			}
			System.out.print("Enter slot id to book: ");
			String slotId = scanner.next();
			for (GymSlot slot : customerService.viewSlotsForCenter(centerId)) {
				if (slot.getSlotId().equals(slotId)) {
					chosen = slot;
					break;
				}
			}
			if (chosen == null) {
				System.out.println("Invalid slot id.");
				return;
			}

			String bookingId = customerService.bookSlot(loggedInUser.getUserId(), centerId, chosen.getStartTime(),
					chosen.getEndTime(), date);
			if (bookingId != null) {
				System.out.println("Booking confirmed with id: " + bookingId);
			} else {
				System.out.println("Booking failed.");
			}
		} catch (ParseException pe) {
			System.out.println("Invalid date format.");
		}
	}

	private void viewMyBookings() {
		List<Booking> bookings = customerService.viewBookings(loggedInUser.getUserId());
		if (bookings.isEmpty()) {
			System.out.println("No bookings yet.");
			return;
		}
		for (Booking booking : bookings) {
			System.out.println(booking.getBookingId() + " | Center Slot:" + booking.getGymSlot().getSlotId() + " | "
					+ booking.getDateAndTime() + " | Status:" + booking.getBookingStatus());
		}
	}

	private void cancelBooking(Scanner scanner) {
		System.out.print("Enter booking id to cancel: ");
		String id = scanner.next();
		boolean result = customerService.cancelBooking(id);
		System.out.println(result ? "Booking cancelled." : "Booking not found.");
	}

	private int remainingSeats(GymSlot slot, Date date) {
		String datePrefix = new java.sql.Date(date.getTime()).toLocalDate().toString();
		long booked = DataStore.getAllBookings().stream()
				.filter(b -> b.getGymSlot() != null && slot.getSlotId().equals(b.getGymSlot().getSlotId()))
				.filter(b -> b.getDateAndTime() != null && b.getDateAndTime().startsWith(datePrefix)).count();
		return slot.getTotalSeats() - (int) booked;
	}
}
