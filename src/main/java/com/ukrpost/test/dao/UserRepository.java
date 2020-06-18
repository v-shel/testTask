package com.ukrpost.test.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ukrpost.test.dao.entity.User;

public interface UserRepository extends CrudRepository<User, Integer>{
	
	@Query("select u from User u where u.id = :id and u.isDeleted = false")
	User findById(@Param("id") int id);
	
	@Modifying
	@Query("update User u set u.isDeleted = true where u.id = id")
	void deleteById(@Param("id") int id);
}
