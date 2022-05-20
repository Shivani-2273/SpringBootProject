package com.usermanagement.service;

import java.io.FileNotFoundException;
import java.sql.Date;
import java.util.List;

import com.usermanagement.model.User;

public interface UserService {

	void saveUser(User user);
	
	User userLogin(User user);
	
	List<User> getAllUser();
	
	User getUserAddress(int userId);
	
	void deleteUser(int userId);
	
	User displayUser(int userId);
		
	boolean checkEmail(String email);
	
	void resetPassword(User user);

	void generateCSV(Date startDate, Date endDate) throws FileNotFoundException;
	
}
