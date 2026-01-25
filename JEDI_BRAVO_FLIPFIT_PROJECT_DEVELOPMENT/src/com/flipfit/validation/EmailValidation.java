package com.flipfit.validation;

import java.util.Set;

public class EmailValidation {
	public static boolean validateEmail(String email) {
		return email.endsWith("@gmail.com");
	}
	
	public static boolean checkUnique(Set<String> emails, String email) {
		if (emails.contains(email)){
			System.out.println("A user with this email already exists. Try again with diffrent email.");
			return false;
		}
		return true;
	}
}
