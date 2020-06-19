package com.ukrpost.test.dao;

import org.springframework.data.repository.CrudRepository;

import com.ukrpost.test.dao.entity.Payment;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {
	
	Payment findById(Payment payment);
	void delete(Payment payment);
}
