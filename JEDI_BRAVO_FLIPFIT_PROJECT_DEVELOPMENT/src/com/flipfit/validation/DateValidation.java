package com.flipfit.validation;

public class DateValidation {
	public static boolean validateDate(String date) {
		String[] d = date.split("-");
		int[] dateS = {Integer.parseInt(d[0]),Integer.parseInt(d[1]),Integer.parseInt(d[2])};
		int[] days = {31,28,31,30,31,30,31,31,30,31,30,31};
		if(dateS[0] >2026 && dateS[0] < 2100 ) {
			// checkforleapYear
			if(dateS[1] ==2  && dateS[0] % 4 ==0 ) {
				return dateS[2]< 30 && dateS[1] >0;
			}
			else if( dateS[1] < 13 ) return  dateS[2] < days[dateS[1] -1]; 
		}
		return false;
	}
}
