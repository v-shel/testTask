package com.ukrpost.test.dao;

import org.springframework.data.repository.CrudRepository;

import com.ukrpost.test.dao.entity.User;

public interface UserRepository extends CrudRepository<User, Integer>{
	
	User findById(int id);
	boolean existsByEmail(String email);
}
