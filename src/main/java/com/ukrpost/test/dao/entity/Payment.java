package com.ukrpost.test.dao.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Payment {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private int id;
	
	@ManyToMany
	@JoinColumn(name = "payment_id")
	private List<SelledProduct> products;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;
	private BigDecimal amountWithDiscount;
	private BigDecimal amountWithoutDiscount;
	
	@JsonFormat(pattern = "dd:MM:yyyy hh:mm:ss")
	private Date createdDate;
	private boolean isDeleted;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<SelledProduct> getProducts() {
		return products;
	}
	public void setProducts(List<SelledProduct> products) {
		this.products = products;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public BigDecimal getAmountWithDiscount() {
		return amountWithDiscount;
	}
	public void setAmountWithDiscount(BigDecimal amountWithDiscount) {
		this.amountWithDiscount = amountWithDiscount;
	}
	public BigDecimal getAmountWithoutDiscount() {
		return amountWithoutDiscount;
	}
	public void setAmountWithoutDiscount(BigDecimal amountWithoutDiscount) {
		this.amountWithoutDiscount = amountWithoutDiscount;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Payment [id=");
		builder.append(id);
		builder.append(", products=");
		builder.append(products);
		builder.append(", account=");
		builder.append(account);
		builder.append(", amountWithDiscount=");
		builder.append(amountWithDiscount);
		builder.append(", amountWithoutDiscount=");
		builder.append(amountWithoutDiscount);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append(", isDeleted=");
		builder.append(isDeleted);
		builder.append("]");
		return builder.toString();
	}
}
