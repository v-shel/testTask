package com.ukrpost.test.services;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ukrpost.test.dao.UserRepository;
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
		accService.createAccountForUser(created.getId());
		return created;
	}

	public User findById(int userId) {
		return ofNullable(userRepo.findById(userId))
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
	}
	
	@Transactional
	public void delete(int userId) {
		User user = ofNullable(userRepo.findById(userId))
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
		accService.deleteByUserId(userId);
		userRepo.deleteById(user.getId());
	}
}
