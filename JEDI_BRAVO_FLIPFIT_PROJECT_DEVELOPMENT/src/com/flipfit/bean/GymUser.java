package com.flipfit.bean;

import com.flipfit.enums.Role;
import com.flipfit.helper.DataStore;

public class GymUser {
	private String userId;
	private String name;
	private String email;
	private String address;
	private String password;
	private Role role;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		DataStore.addNewEmail(email);
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
}
