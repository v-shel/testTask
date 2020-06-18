package com.ukrpost.test.services;

import static java.util.Optional.of;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ukrpost.test.dao.UserRepository;
import com.ukrpost.test.dao.entity.Account;
import com.ukrpost.test.dao.entity.User;

@Service
public class UserService {
	
	private UserRepository userRepo;
	private AccountService accService;
	
	@Autowired
	public UserService(UserRepository userRepo, AccountService accService) {
		this.userRepo = userRepo;
		this.accService = accService;
	}
	
	public User create(User user) {
		User created = userRepo.save(user);
		Account account = new Account();
		account.setUser(created);
		account.setMoney(BigDecimal.valueOf(0.0));
		accService.create(account);
		
		return created;
	}
	
	public User update(User user) {
		return userRepo.save(user);
	}
	
	public User findById(int id) {
		return of(userRepo.findById(id))
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
	}
	
	public Iterable<User> findAll() {
		return userRepo.findAll();
	}
	
	public void delete(int userId) {
		userRepo.deleteById(userId);
	}
}
