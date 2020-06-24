package com.ukrpost.test.services;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ukrpost.test.dao.DiscountRepository;
import com.ukrpost.test.dao.ProductRepository;
import com.ukrpost.test.dao.entity.Discount;
import com.ukrpost.test.dao.entity.Payment;
import com.ukrpost.test.dao.entity.Product;

@Service
public class ProductService {

	private static final String PRODUCT_NOT_FOUND_MSG = "Product not found";
	private ProductRepository prodRepo;
	private DiscountRepository discoRepo;
	private PaymentService payService;

	@Autowired
	public ProductService(ProductRepository prodRepo, DiscountRepository discoRepo, PaymentService payService) {
		this.prodRepo = prodRepo;
		this.discoRepo = discoRepo;
		this.payService = payService;
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
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, PRODUCT_NOT_FOUND_MSG));
	}

	public Iterable<Product> findByName(String name) {
		return prodRepo.findByName(name);
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
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, PRODUCT_NOT_FOUND_MSG));

		if (discount.getPercent().compareTo(ZERO) < 0 || discount.getPercent().compareTo(valueOf(100)) > 0) {
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
	
	public Payment buyProducts(Collection<Product> products, int userId) {
		if (products.isEmpty()) {
			throw new ResponseStatusException(BAD_REQUEST, "Can't checkout without products");
		}
		// Get available products from DB
		List<Product> available = prodRepo.findAvailableByListId(products.stream()
				.map(Product::getId)
				.collect(toList()));
		
		validProducts(available, (List<Product>) products);
		
		Payment payment = payService.createAndProcessPayment((List<Product>) products, userId);
		
		if (!payment.getPayStatus().equals("FAILD")) {
			/*Update products quantities*/
			products.forEach(p -> {
				int quantity = available.get(available.indexOf(p)).getQuantity();
				available.get(available.indexOf(p)).setQuantity(quantity - p.getQuantity());
			});
			prodRepo.saveAll(available);
		}
		
		return payment;
	}
	
	private void validProducts(List<Product> available, List<Product> ordered) {
		/*Checking the availability of the required number of purchased products*/
		List<Product> failed = ordered.stream()
			.filter(p -> !available.contains(p) || available.get(available.indexOf(p)).getQuantity() < p.getQuantity())
			.collect(toList());
		
		if (!failed.isEmpty()) { // If available products less than purchase, throw error
			throw new ResponseStatusException(BAD_REQUEST, "Products not available: " + failed);
		} 
	}
}
