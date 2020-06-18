package com.ukrpost.test.dao;

import org.springframework.data.repository.CrudRepository;

import com.ukrpost.test.dao.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>{
	
	Product findById(Product product);
	void delete(Product product);
}
