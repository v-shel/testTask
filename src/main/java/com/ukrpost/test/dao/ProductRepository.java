package com.ukrpost.test.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ukrpost.test.dao.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>{
	
	@Query("select pro from Product pro where pro.isDeleted = false and pro.id = :id")
	Product findById(@Param("id") int id);
	
	@Query("select pro from Product pro where pro.isDeleted = false and pro.name = :name")
	Iterable<Product> findByName(@Param("name") String name);
	
	@Query(value = "select * from Product pro, Discount dis "
			+ "where pro.DISCOUNT_ID not is null and dis.IS_DELETED = false "
			+ "and pro.is_deleted = false and dis.amount > 0", nativeQuery = true)
	Iterable<Product> findWithDiscount();
	
	@Query("select pro from Product pro where pro.isDeleted = false")
	Iterable<Product> findAvailable();
	
	@Modifying
	@Query(value = "update Product pro set pro.is_deleted = true where pro.id = :id", nativeQuery = true)
	void deleteById(@Param("id") int id);
}
