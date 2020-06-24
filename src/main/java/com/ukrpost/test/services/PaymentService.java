package com.ukrpost.test.services;

import static com.ukrpost.test.enums.PaymentStatus.FAILD;
import static com.ukrpost.test.enums.PaymentStatus.PAYD;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

	public List<Payment> findByUserId(int userId) {
		return payRepo.findByAccountUserId(userId);
	}
	
	public List<Payment> findByPayStatus(String payStatus, int userId) {
		return payRepo.findByPayStatusAndAccountUserId(payStatus, userId);
	}
	
	@Transactional
	public Payment createAndProcessPayment(List<Product> products, int userId) {
		Account account = ofNullable(accRepo.findByUserId(userId))
				.orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "This user can't by the products"));
		
		List<Product> bestDisc = leftOnlyBestDiscount(products, 3); // Delete all discount from products except for 3 best discount.
		List<SelledProduct> selled = productToSelled(bestDisc);

		Payment payment = new Payment();
		payment.setAccount(account);
		payment.setAmountWithoutDiscount(getTotalSumWithoutDiscount(selled));
		payment.setAmountWithDiscount(getTotalSumWithDiscount(selled));
		
		payPayment(payment, account);
		savePayment(payment, selled);
		return payment;
	}
	
	private void savePayment(Payment payment, List<SelledProduct> products) {
		payment = payRepo.save(payment);
		int payId = payment.getId();
		
		products.forEach(p -> p.setPaymentId(payId));
		payment.setProducts((List<SelledProduct>) sellProdRepo.saveAll(products));
	}
	
	private BigDecimal getTotalSumWithDiscount(List<SelledProduct> products) {
		BigDecimal total = ZERO;
		for (SelledProduct p : products) {
			if (p.getDiscount() != null && p.getDiscount().compareTo(ZERO) > 0) {
				BigDecimal sumByQuantity = p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity()));
				total = total.add(decreaseValueByPercent(sumByQuantity, p.getDiscount()));
			} else {
				total = total.add(p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())));
			}
		}
		return total;
	}
	
	private BigDecimal getTotalSumWithoutDiscount(List<SelledProduct> products) {
		return products.stream()
				.map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity()))) //Price * quantity
				.reduce(ZERO, BigDecimal::add)
				.setScale(2, HALF_EVEN);
	}

	private List<Product> leftOnlyBestDiscount(List<Product> products, int discountedProducts) {
		//Find count of best discounts value equals to discountedProducts and have max sum of product quantity
		Map<Integer, BigDecimal> bestDisc = products.stream()
			.filter(p -> p.getDiscount() != null && p.getDiscount().getPercent().compareTo(ZERO) > 0)
			.collect(toMap(Product::getId, this::totalDiscountMoney, BigDecimal::add))
			.entrySet()
			.stream()
			.sorted((a, b) -> b.getValue().compareTo(a.getValue()))
			.limit(discountedProducts)
			.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		
		products.stream().filter(p -> !bestDisc.containsKey(p.getId())).forEach( p -> p.setDiscount(null));
		return products;
	}
	
	private BigDecimal totalDiscountMoney(Product prod) {
		return prod.getPrice()
				.multiply(BigDecimal.valueOf(prod.getQuantity()))
				.divide(BigDecimal.valueOf(100.00))
				.multiply(prod.getDiscount().getPercent()).setScale(2, HALF_EVEN);
	}
	
	private BigDecimal decreaseValueByPercent(BigDecimal price, BigDecimal percent) {
		BigDecimal percentValue = price.divide(BigDecimal.valueOf(100.00)).multiply(percent);
		return price.subtract(percentValue).setScale(2, HALF_EVEN);
	}
	
	private void payPayment(Payment payment, Account account) {
		if (account.getMoney().compareTo(payment.getAmountWithDiscount()) >= 0) {
			account.setMoney(account.getMoney().subtract(payment.getAmountWithDiscount()));
			payment.setPayStatus(PAYD.toString());
			payment.setCreatedDate(new Date());
		} else {
			payment.setPayStatus(FAILD.toString());
			payment.setCreatedDate(new Date());
		}
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
		selled.setDiscount(ofNullable(product.getDiscount()).map(Discount::getPercent).orElse(ZERO));
		selled.setQuantity(product.getQuantity());
		return selled;
	}
}
