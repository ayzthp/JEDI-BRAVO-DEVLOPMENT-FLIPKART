package com.flipfit.validation;

public class TimeValidation {
	public static boolean validateTime(String time) {
		String t[] = time.split(":");
		int hour = Integer.parseInt(t[0]);
		int min = Integer.parseInt(t[1]);
		return hour>=0 & hour <23 && min >=0 && min< 60;
	}
}
