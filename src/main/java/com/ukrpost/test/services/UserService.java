package com.ukrpost.test.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ukrpost.test.dao.AccountRepository;
import com.ukrpost.test.dao.UserRepository;
import com.ukrpost.test.dao.entity.Account;
import com.ukrpost.test.dao.entity.User;

@Service
public class UserService {
	
	private UserRepository userRepo;
	private AccountRepository accRepo;
	
	@Autowired
	public UserService(UserRepository userRepo, AccountRepository accRepo) {
		this.userRepo = userRepo;
		this.accRepo = accRepo;
	}
	
	public User create(User user) {
		Account account = new Account();
		account.setUser(user);
		account.setMoney(BigDecimal.valueOf(0.0));
		accRepo.save(account);
		
		return userRepo.save(user);
	}
	
	public User update(User user) {
		return userRepo.save(user);
	}
	
	public User findById(int id) {
		return userRepo.findById(id);
	}
	
	public Iterable<User> findAll() {
		return userRepo.findAll();
	}
	
	public void delete(int userId) {
		userRepo.deleteById(userId);
	}
}
