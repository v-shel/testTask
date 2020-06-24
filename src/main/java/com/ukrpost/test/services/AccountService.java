package com.ukrpost.test.services;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ukrpost.test.dao.AccountRepository;
import com.ukrpost.test.dao.UserRepository;
import com.ukrpost.test.dao.entity.Account;
import com.ukrpost.test.dao.entity.User;

@Service
public class AccountService {

	private static final String ACC_NOT_FOUND_MSG = "Account not found";
	private AccountRepository accRepo;
	private UserRepository userRepo;

	@Autowired
	public AccountService(AccountRepository accRepo, UserRepository userRepo) {
		this.accRepo = accRepo;
		this.userRepo = userRepo;
	}

	public Account createAccountForUser(@NotNull int userId) {
		User user = ofNullable(userRepo.findById(userId))
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
		
		Account account = new Account();
		account.setUser(user);
		account.setIsActive(true);
		account.setMoney(BigDecimal.valueOf(0.0));
		return accRepo.save(account);
	}
	
	public Account findByUserId(int userId) {
		return ofNullable(accRepo.findByUserId(userId))
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, ACC_NOT_FOUND_MSG));
	}

	public Account findByUserIdAndName(int userId, String name) {
		return ofNullable(accRepo.findByUserIdAndName(userId, name))
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, ACC_NOT_FOUND_MSG));
	}

	public Account addMoneyToAccount(int userId, String userName, BigDecimal money) {
		Account acc = accRepo.findByUserIdAndName(userId, userName);

		if (acc == null || acc.getId() == 0) {
			throw new ResponseStatusException(NOT_FOUND, ACC_NOT_FOUND_MSG);
		}

		acc.setMoney(acc.getMoney().add(money));
		return accRepo.save(acc);
	}

	public Iterable<Account> findAll() {
		return accRepo.findAll();
	}
	
	public void disableByUserId(int userId) {
		Account acc = findByUserId(userId);
		acc.setIsActive(false);
		accRepo.save(acc);
	}
	
	@Transactional
	public void deleteByUserId(int userId) {
		ofNullable(accRepo.findByUserId(userId)).ifPresent(a -> accRepo.deleteByUserId(a.getId()));
	}
}
