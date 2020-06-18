package com.ukrpost.test.dao;

import org.springframework.data.repository.CrudRepository;

import com.ukrpost.test.dao.entity.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {
	
	Account findById(Account account);
	void delete(Account account);
}
