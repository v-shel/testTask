package com.ukrpost.test.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ukrpost.test.dao.entity.Payment;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {
	
	Payment findById(Payment payment);
	
	void delete(Payment payment);
	
	List<Payment> findByAccountId(int accountId);
}
