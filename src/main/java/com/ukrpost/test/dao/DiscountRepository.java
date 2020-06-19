package com.ukrpost.test.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ukrpost.test.dao.entity.Discount;

public interface DiscountRepository extends CrudRepository<Discount, Integer> {
	
	Discount findById(Discount discount);
	
	@Modifying
	@Query(value = "update Discount dis set dis.is_deleted = true where dis.id = :id", nativeQuery = true)
	void deleteById(@Param("id") int id);
}
