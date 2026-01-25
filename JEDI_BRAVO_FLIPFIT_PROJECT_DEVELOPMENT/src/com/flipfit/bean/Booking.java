package com.flipfit.bean;
//added a commment

import com.flipfit.enums.BookingStatus;

public class Booking {
	private String BookingId ;
	private GymUser gymUser ;
	private GymSlot gymSlot ;
	private String dateAndTime ;
	private BookingStatus bookingStatus;
	
	public String getBookingId() {
		return BookingId;
	}
	public void setBookingId(String bookingId) {
		BookingId = bookingId;
	}
	public GymUser getGymUser() {
		return gymUser;
	}
	public void setGymUser(GymUser gymUser) {
		this.gymUser = gymUser;
	}
	public GymSlot getGymSlot() {
		return gymSlot;
		
	}
	public void setGymSlot(GymSlot gymSlot) {
		this.gymSlot = gymSlot;
	}
	public String getDateAndTime() {
		return dateAndTime;
	}
	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
}
