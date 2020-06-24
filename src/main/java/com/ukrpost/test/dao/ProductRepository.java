package com.ukrpost.test.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ukrpost.test.dao.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>{
	
	@Query("select pro from Product pro where pro.isDeleted = false and pro.id = :id")
	Product findById(@Param("id") int id);
	
	@Query("select pro from Product pro where pro.isDeleted = false and pro.name = :name")
	List<Product> findByName(@Param("name") String name);
	
	@Query(value = "select pro.id pro_id, pro.name, pro.description, pro.price, pro.quantity, pro.category, pro.discount_id, "
			+ "dis.id dis_id, dis.percent, dis.description, dis.is_deleted "
			+ "from Product pro "
			+ "left join Discount dis on dis.id = discount_id and dis.is_deleted = false "
			+ "where discount_id not is null "
			+ "and pro.is_deleted = false and quantity > 0 and dis.percent > 0", nativeQuery = true)
	List<Product> findWithDiscount();
	
	@Query("select pro from Product pro where pro.isDeleted = false and quantity > 0 ")
	List<Product> findAvailable();
	
	@Modifying
	@Query(value = "update Product pro set pro.is_deleted = true where pro.id = :id", nativeQuery = true)
	void deleteById(@Param("id") int id);
	
	@Query(value = "select pro.id pro_id, pro.name, pro.description, pro.price, "
			+ "pro.quantity, pro.category, pro.discount_id, pro.is_deleted, "
			+ "dis.id dis_id, dis.percent, dis.description, dis.is_deleted "
			+ "from Product pro "
			+ "left join Discount dis on dis.id = discount_id and dis.is_deleted = false "
			+ "where pro.is_deleted = false and quantity > 0 and pro.id in :ids", nativeQuery = true)
	List<Product> findAvailableByListId(@Param("ids") List<Integer> ids);
}
