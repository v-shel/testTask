package com.ukrpost.test.services;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ukrpost.test.dao.AccountRepository;
import com.ukrpost.test.dao.PaymentRepository;
import com.ukrpost.test.dao.SelledProductRepository;
import com.ukrpost.test.dao.entity.Account;
import com.ukrpost.test.dao.entity.Discount;
import com.ukrpost.test.dao.entity.Payment;
import com.ukrpost.test.dao.entity.Product;
import com.ukrpost.test.dao.entity.SelledProduct;

@Service
public class PaymentService {
	
	private PaymentRepository payRepo;
	private AccountRepository accRepo;
	private SelledProductRepository sellProdRepo;

	@Autowired
	public PaymentService(PaymentRepository payRepo, AccountRepository accRepo, SelledProductRepository sellProdRepo) {
		this.payRepo = payRepo;
		this.accRepo = accRepo;
		this.sellProdRepo = sellProdRepo;
	}

	public List<Payment> findByAccountId(int userId) {
		return payRepo.findByAccountId(userId);
	}
	
	public Payment createAndProcessPayment(List<Product> products, int userId) {
		Account account = ofNullable(accRepo.findByUserId(userId))
				.orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "This user can't by products"));
		
		List<Product> bestDisc = leftOnlyBestDiscount(products, 3); // Delete all discount from products except for 3 best discount.
		List<SelledProduct> selled = productToSelled(bestDisc);

		Payment payment = new Payment();
		payment.setAccount(account);
		payment.setAmountWithoutDiscount(getTotalSumWithoutDiscount(selled));
		payment.setAmountWithDiscount(getTotalSumWithDiscount(selled));
		payPayment(payment, account);
		
		payment = payRepo.save(payment);
		int payId = payment.getId();
		
		selled.forEach(p -> p.setPaymentId(payId));
		payment.setProducts((List<SelledProduct>) sellProdRepo.saveAll(selled));

		return payment;
	}
	
	private void payPayment(Payment payment, Account account) {
		if (account.getMoney().compareTo(payment.getAmountWithDiscount()) >= 0) {
			account.setMoney(account.getMoney().subtract(payment.getAmountWithDiscount()));
			payment.setCreatedDate(new Date());
		} else {
			throw new ResponseStatusException(BAD_REQUEST, "Not enough money to buy this products");
		}
	}
	
	private BigDecimal getTotalSumWithDiscount(List<SelledProduct> products) {
		BigDecimal total = ZERO;
		
		for (SelledProduct p : products) {
			
			if (p.getDiscount() != null && p.getDiscount().compareTo(ZERO) > 0) {
				total = total.add(decreasePriceByPercent(p.getPrice(), p.getDiscount()));
			} else {
				total = total.add(p.getPrice());
			}
		}
		return total;
	}
	
	private BigDecimal getTotalSumWithoutDiscount(List<SelledProduct> products) {
		return products.stream() 
				.map(SelledProduct::getPrice)
				.reduce(ZERO, BigDecimal::add)
				.setScale(2, HALF_EVEN);
	}

	private List<Product> leftOnlyBestDiscount(List<Product> products, int discountedProducts) {
		Map<Product, BigDecimal> bestDisc = new HashMap<>();
		
		products.stream() //Collect product as key and total sum of discount as value of each group of same product
			.filter(p -> p.getDiscount() != null && p.getDiscount().getAmount().compareTo(ZERO) > 0)
			.forEach(p -> { 
				if (bestDisc.containsKey(p)) {
					bestDisc.get(p).add(percentOfPrice(p.getPrice(), p.getDiscount().getAmount()));
				} else {
					BigDecimal sum = percentOfPrice(p.getPrice(), p.getDiscount().getAmount());
					bestDisc.put(p, sum);
				}
			});
		
		Map<Product, BigDecimal> filteredDisc = bestDisc.entrySet() 
				.stream()
				.sorted((a,b) -> b.getValue().compareTo(a.getValue()))
				.limit(discountedProducts)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		
		for (Entry<Product, BigDecimal> e : filteredDisc.entrySet()) {
			System.out.println(e.getValue());
		}
				
		if (filteredDisc.isEmpty()) { // If unique products with discount is zero, return all
			return products;

		} else if (filteredDisc.size() > discountedProducts) { // If unique products more than discountedProducts delete
			products.stream().forEach(p -> {
				if (!filteredDisc.containsKey(p)) {
					p.setDiscount(null);
				}
			});
			return products;

		} else { // If unique products with discount is equals to discountedProducts or less,
					// return all products
			return products;
		}
	}
	
	private BigDecimal percentOfPrice(BigDecimal price, BigDecimal percent) {
		return price.divide(BigDecimal.valueOf(100.00)).multiply(percent).setScale(2, HALF_EVEN);
	}
	
	private BigDecimal decreasePriceByPercent(BigDecimal price, BigDecimal percent) {
		BigDecimal percentValue = price.divide(BigDecimal.valueOf(100.00)).multiply(percent);
		return price.subtract(percentValue).setScale(2, HALF_EVEN);
	}

	private List<SelledProduct> productToSelled(List<Product> products) {
		List<SelledProduct> selled = new ArrayList<>();

		for (Product pro : products) {
			selled.add(productToSelled(pro));
		}
		return selled;
	}

	private SelledProduct productToSelled(Product product) {
		SelledProduct selled = new SelledProduct();

		selled.setName(product.getName());
		selled.setDescription(product.getDescription());
		selled.setPrice(product.getPrice());
		selled.setDiscount(ofNullable(product.getDiscount()).map(Discount::getAmount).orElse(ZERO));

		return selled;
	}
}
