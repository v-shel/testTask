package com.ukrpost.test.services;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ukrpost.test.dao.DiscountRepository;
import com.ukrpost.test.dao.ProductRepository;
import com.ukrpost.test.dao.entity.Discount;
import com.ukrpost.test.dao.entity.Product;

@Service
public class ProductService {

	private ProductRepository prodRepo;
	private DiscountRepository discoRepo;

	@Autowired
	public ProductService(ProductRepository prodRepo, DiscountRepository discoRepo) {
		super();
		this.prodRepo = prodRepo;
		this.discoRepo = discoRepo;
	}

	public Product create(Product product) {
		if (product.getDiscount() != null) {
			Discount dis = discoRepo.save(product.getDiscount());
			product.setDiscount(dis);
		}
		return prodRepo.save(product);
	}

	public Product findById(int productId) {
		return ofNullable(prodRepo.findById(productId))
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found"));
	}

	public Iterable<Product> findByName(String name) {
		return ofNullable(findByName(name))
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found"));
	}

	public Iterable<Product> findWithDiscount() {
		return prodRepo.findWithDiscount();
	}

	public Iterable<Product> findAvailable() {
		return prodRepo.findAvailable();
	}
	
	@Transactional
	public Product addDiscount(int productId, Discount discount) {
		Product prod = ofNullable(findById(productId))
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found"));

		if (discount.getAmount().compareTo(ZERO) < 0 || discount.getAmount().compareTo(valueOf(100)) > 0) {
			throw new ResponseStatusException(BAD_REQUEST, "Incorect discount value");
		}
		if (prod.getDiscount() != null && prod.getDiscount().getId() != 0) {
			discount.setId(prod.getDiscount().getId());
		}
		
		prod.setDiscount(discoRepo.save(discount));

		return prodRepo.save(prod);
	}

	@Transactional
	public void deleteById(int productId) {
		Product prod = findById(productId);
		
		if (prod.getDiscount() != null || prod.getDiscount().getId() != 0) {
			discoRepo.deleteById(prod.getDiscount().getId());
		}
		prodRepo.deleteById(productId);
	}

}
