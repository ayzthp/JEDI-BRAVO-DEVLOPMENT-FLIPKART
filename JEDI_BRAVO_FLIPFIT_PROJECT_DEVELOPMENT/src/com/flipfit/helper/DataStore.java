package com.flipfit.helper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymSlot;
import com.flipfit.bean.GymUser;
import com.flipfit.bean.Booking;
import com.flipfit.enums.Role;
import com.flipfit.enums.BookingStatus;

/**
 * In-memory data store backed by Java collections.
 * This avoids any DB dependency while keeping the code modular.
 */
public final class DataStore {

	private static final Map<String, GymUser> USERS = new HashMap<>();
	private static final Map<String, GymCenter> CENTERS = new HashMap<>();
	private static final Map<String, Booking> BOOKINGS = new HashMap<>();
	private static final Map<String, List<Booking>> WAITLIST = new HashMap<>();

	private static final AtomicInteger CENTER_SEQ = new AtomicInteger(100);
	private static final AtomicInteger SLOT_SEQ = new AtomicInteger(200);
	private static final AtomicInteger BOOKING_SEQ = new AtomicInteger(300);
	private static final AtomicInteger USER_SEQ = new AtomicInteger(400);

	private static final Set<String> emails = new HashSet<>();
	
	static {
		seedUsers();
		seedCenters();
	}

	public static Set<String> getAllEmails() {
		return emails;
	}
	public static void addNewEmail(String email) {
		 emails.add(email);
	}
	

	private DataStore() {
	}

	// ---------- Seeding ----------
	private static void seedUsers() {
		GymUser admin = new GymUser();
		admin.setUserId("admin");
		admin.setName("Platform Admin");
		admin.setEmail("admin@flipfit.com");
		
		admin.setPassword("admin");
		admin.setRole(Role.ADMIN);
		USERS.put(admin.getUserId(), admin);

		GymUser owner = new GymUser();
		owner.setUserId("owner1");
		owner.setName("Bellandur Owner");
		owner.setEmail("owner1@flipfit.com");
		owner.setPassword("password");
		owner.setRole(Role.GYM_OWNER);
		USERS.put(owner.getUserId(), owner);

		GymUser customer = new GymUser();
		customer.setUserId("cust1");
		customer.setName("Karan Customer");
		customer.setEmail("cust1@flipfit.com");
		customer.setPassword("password");
		customer.setRole(Role.CUSTOMER);
		
		USERS.put(customer.getUserId(), customer);
	}

	private static void seedCenters() {
		GymCenter bellandur = new GymCenter();
		bellandur.setCenterId("C1");
		bellandur.setCenterCity("Bangalore");
		bellandur.setCenterLocn("Bellandur");
		bellandur.setOwnerId("owner1");
		bellandur.setCenterSlot(defaultSlots());
		CENTERS.put(bellandur.getCenterId(), bellandur);

		GymCenter koramangala = new GymCenter();
		koramangala.setCenterId("C2");
		koramangala.setCenterCity("Bangalore");
		koramangala.setCenterLocn("Koramangala");
		koramangala.setOwnerId("owner1");
		koramangala.setCenterSlot(defaultSlots());
		CENTERS.put(koramangala.getCenterId(), koramangala);
	}

	private static List<GymSlot> defaultSlots() {
		List<GymSlot> slots = new ArrayList<>();
		int seats = 10;
		slots.add(buildSlot("06:00", "07:00", 1));
		slots.add(buildSlot("07:00", "08:00", seats));
		slots.add(buildSlot("08:00", "09:00", seats));
		slots.add(buildSlot("18:00", "19:00", seats));
		slots.add(buildSlot("19:00", "20:00", seats));
		slots.add(buildSlot("20:00", "21:00", seats));
		return slots;
	}

	private static GymSlot buildSlot(String start, String end, int seats) {
		GymSlot slot = new GymSlot();
		slot.setSlotId("S" + SLOT_SEQ.getAndIncrement());
		slot.setStartTime(LocalTime.parse(start));
		slot.setEndTime(LocalTime.parse(end));
		slot.setTotalSeats(seats);
		slot.setAvailableSeats(seats);
		return slot;
	}

	// ---------- Accessors ----------
	public static Collection<GymUser> getAllUsers() {
		return USERS.values();
	}

	public static GymUser getUser(String userId) {
		return USERS.get(userId);
	}

	public static void addUser(GymUser user) {
		USERS.put(user.getUserId(), user);
	}

	public static Collection<GymCenter> getAllCenters() {
		return CENTERS.values();
	}

	public static GymCenter getCenter(String centerId) {
		return CENTERS.get(centerId);
	}

	public static void addCenter(GymCenter center) {
		CENTERS.put(center.getCenterId(), center);
	}

	public static Collection<Booking> getAllBookings() {
		return BOOKINGS.values();
	}

	public static Booking getBooking(String bookingId) {
		return BOOKINGS.get(bookingId);
	}

	public static void saveBooking(Booking booking) {
		BOOKINGS.put(booking.getBookingId(), booking);
	}

	public static Booking removeBooking(String bookingId) {
		return BOOKINGS.remove(bookingId);
	}

	public static void addToWaitlist(String slotId, Booking booking) {
		WAITLIST.computeIfAbsent(slotId, k -> new ArrayList<>()).add(booking);
	}

	public static List<Booking> getWaitlist(String slotId) {
		return WAITLIST.getOrDefault(slotId, List.of());
	}

	public static Booking popWaitlist(String slotId) {
		List<Booking> list = WAITLIST.get(slotId);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.remove(0);
	}

	// ---------- Id generation ----------
	public static String nextCenterId() {
		return "C" + CENTER_SEQ.getAndIncrement();
	}

	public static String nextSlotId() {
		return "S" + SLOT_SEQ.getAndIncrement();
	}

	public static String nextBookingId() {
		return "B" + BOOKING_SEQ.getAndIncrement();
	}

	public static String nextUserId() {
		return "U" + USER_SEQ.getAndIncrement();
	}

	// ---------- Utility helpers ----------
	public static String buildDateKey(LocalDate date, LocalTime startTime) {
		return date.toString() + " " + startTime.toString();
	}
}

