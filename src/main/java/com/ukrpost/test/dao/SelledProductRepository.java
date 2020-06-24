package com.ukrpost.test.dao;

import org.springframework.data.repository.CrudRepository;

import com.ukrpost.test.dao.entity.SelledProduct;

public interface SelledProductRepository extends CrudRepository<SelledProduct, Integer>{
	
	
}
