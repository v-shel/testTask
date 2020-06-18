package com.ukrpost.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ukrpost.test.dao.UserRepository;
import com.ukrpost.test.dao.entity.User;

@SpringBootApplication
public class TestTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestTaskApplication.class, args);
	}
	
	@Bean
	CommandLineRunner runner(UserRepository userRepo) {
		return args -> {
			User user = new User();
			user.setName("Name");
			user.setEmail("main@name.com");
			
			User usr = userRepo.save(user);
			System.out.println(usr);
		};
	}

}
