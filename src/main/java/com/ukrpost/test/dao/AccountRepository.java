package com.ukrpost.test.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ukrpost.test.dao.entity.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {
	
	@Query("select acc, u from Account acc, User u "
			+ "where acc.isDeleted = false and acc.isActive = true and u.id = :userId")
	Account findByUserId(@Param("userId") int userId);
	
	@Query("select acc, u from Account acc, User u "
			+ "where acc.isDeleted = false and acc.isActive = true and u.id = :userId and u.name = :name")
	Account findByUserIdAndName(@Param("userId") int userId, @Param("name") String name);
	
	@Modifying
	@Query(value = "update Account acc set acc.is_deleted = true, acc.is_active = false where user_id = :id", nativeQuery = true)
	void deleteByUserId(@Param("id") int id);

}
