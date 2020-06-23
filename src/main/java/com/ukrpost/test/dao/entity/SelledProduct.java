package com.ukrpost.test.dao.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "selled_product")
public class SelledProduct {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private int id;
	private int paymentId;
	private String name;
	private String description;
	private BigDecimal price;
	private BigDecimal discount;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SelledProduct [id=");
		builder.append(id);
		builder.append(", paymentId=");
		builder.append(paymentId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", price=");
		builder.append(price);
		builder.append(", discount=");
		builder.append(discount);
		builder.append("]");
		return builder.toString();
	}	
}
