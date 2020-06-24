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
	public User userById(@PathVariable("userId") int userId) {
		return userService.findById(userId);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> delete(@PathVariable(name = "userId", required = true) int userId) {
		userService.delete(userId);
		return ResponseEntity.ok("");
	}
	
}
