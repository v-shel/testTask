package com.ukrpost.test.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ukrpost.test.dao.entity.User;
import com.ukrpost.test.services.UserService;

import javassist.NotFoundException;

@Controller
@RequestMapping("/users")
public class UserResource {

	@Autowired
	private UserService userService;

	@PostMapping
	@ResponseBody
	public User create(@RequestBody User user) {
		return userService.create(user);
	}

	@GetMapping("/{userId}")
	@ResponseBody
	public User userById(@PathVariable("userId") int userId) throws NotFoundException {
		return userService.findById(userId);
	}

	@GetMapping
	@ResponseBody
	public Iterable<User> findAllUsers() {
		return userService.findAll();
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> delete(@PathVariable("userId") int userId) {
		
		userService.delete(userId);
		
		return ResponseEntity.ok("");
	}
	
}
