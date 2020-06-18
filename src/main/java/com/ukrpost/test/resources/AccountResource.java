package com.ukrpost.test.resources;

import static java.math.BigDecimal.ZERO;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.ukrpost.test.dao.entity.Account;
import com.ukrpost.test.services.AccountService;

@Controller
@RequestMapping("/accounts")
public class AccountResource {
	
	@Autowired
	private AccountService accService;
	
	@PostMapping
	@ResponseBody
	public Account createAccount(@NotNull @RequestParam("userId") int userId) {
		return accService.createAccountForUser(userId);
	}
	
	@GetMapping
	@ResponseBody
	public Account getAccount(@NotNull @RequestParam("userId") int userId,
			@NotNull @RequestParam("userName") String userName) {
		
		return accService.findByUserIdAndName(userId, userName);
	}
	
	@GetMapping("/all")
	@ResponseBody
	public Iterable<Account> getAllAccounts() {
		return accService.findAll();
	}
	
	@PostMapping("/addMoney")
	@ResponseBody
	public Account addMoneyToAccount(@NotNull @RequestParam("userId") int userId,
			@NotEmpty @RequestParam("userName") String userName,
			@NotNull @RequestParam("money") BigDecimal money) {
		
		if (money == null || money.compareTo(ZERO) < 0) {
			throw new ResponseStatusException(BAD_REQUEST, "Money must be a positive value");
		}
		return accService.addMoneyToAccount(userId, userName, money);
	}
	
	@PutMapping("/{userId}")
	@ResponseBody
	public ResponseEntity<String> disableAccount(@PathVariable("userId") int userId) {
		accService.disableByUserId(userId);
		
		return ResponseEntity.ok("");
	}
	
	@DeleteMapping("/{userId}")
	@ResponseBody
	public ResponseEntity<String> deleteAccount(@PathVariable("userId") int userId) {
		accService.deleteByUserId(userId);
		
		return ResponseEntity.ok("");
	}
}
