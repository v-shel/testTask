package com.ukrpost.test.dao;

import org.springframework.data.repository.CrudRepository;

import com.ukrpost.test.dao.entity.Discount;

public interface DiscountRepository extends CrudRepository<Discount, Integer> {
	
	Discount findById(Discount discount);
	void delete(Discount discount);
}
