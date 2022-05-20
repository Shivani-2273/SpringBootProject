package com.usermanagement.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.usermanagement.DAO.UserDAO;
import com.usermanagement.model.Address;
import com.usermanagement.model.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDAO userDao;

	@Override
	public void saveUser(User user) {
		List<Integer> addressIdList = new ArrayList<Integer>();
		try {
			for (Address address : user.getAddress()) {
				address.setUser(user);
			}
			if (user.getUserId() != 0) {
				for (Address address : user.getAddress()) {
					addressIdList.add(address.getAddressId());
				}
				userDao.deleteAddressById(addressIdList);
			}	
			user.setAdmin(true);
			userDao.save(user);
	

	} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public User userLogin(User user) {
		User userObj = userDao.findByEmailAndPassword(user.getEmail(), user.getPassword());
		return userObj;

	}

	@Override
	public List<User> getAllUser() {
		List<User> userList = userDao.findAll();
		return userList;
	}

	@Override
	public User getUserAddress(int userId) {
		User addressList = userDao.getUserByUserId(userId);
		return addressList;
	}

	@Override
	public void deleteUser(int userId) {
		userDao.deleteById(userId);
	}

	@Override
	public User displayUser(int userId) {
		User userData = userDao.getUserByUserId(userId);
		return userData;
	}

	@Override
	public boolean checkEmail(String email) {
		List<User> emails = userDao.findByEmail(email);
		if (!emails.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void resetPassword(User user) {
		userDao.updatePassword(user.getEmail(), user.getPassword());
	}

	@Override
	public void generateCSV(Date startDate, Date endDate) throws FileNotFoundException {
		File file = new File("/home/root-tr-013/Desktop/CSV_FILE/Login.csv");
		Writer w = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8").newEncoder());
		PrintWriter fw = new PrintWriter(w);
		fw.append("First Name");
		fw.append(',');
		fw.append("Last Name");
		fw.append(',');
		fw.append("Date/Time");
		fw.append('\n');
		try {
			List<User> userList=userDao.getCSV(startDate,endDate);
			for (User userData : userList) {
				fw.append(userData.getFirstName());
				fw.append(',');
				fw.append(userData.getLastName());
				fw.append(',');
				LocalDateTime date = userData.getUpdatedAt();
				fw.append(date.toString());
				fw.append('\n');
			}
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
