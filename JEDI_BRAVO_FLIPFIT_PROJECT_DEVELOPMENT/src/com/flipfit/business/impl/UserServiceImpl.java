package com.flipfit.business.impl;

import com.flipfit.bean.GymUser;
import com.flipfit.business.UserService;
import com.flipfit.enums.Role;
import com.flipfit.helper.DataStore;
import com.flipfit.validation.EmailValidation;

public class UserServiceImpl implements UserService {

	@Override
	public GymUser login(String userId, String password, Role role) {
		GymUser user = DataStore.getUser(userId);
		if (user != null && role == user.getRole() && password.equals(user.getPassword())) {
			return user;
		}
		return null;
	}

	@Override
	public GymUser register(String name, String email, String password, Role role) {
		GymUser user = new GymUser();
		user.setUserId(DataStore.nextUserId());
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setRole(role);
		DataStore.addUser(user);
		return user;
	}

	@Override
	public boolean changePassword(String userId, String oldPassword, String newPassword) {
		GymUser user = DataStore.getUser(userId);
		if (user == null || !oldPassword.equals(user.getPassword())) {
			return false;
		}
		user.setPassword(newPassword);
		return true;
	}
}
// 
