package com.ukrpost.test.dao.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Payment {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private int id;
	
	
	@OneToMany
	@JoinColumn(name = "paymentId")
	private List<SelledProduct> products;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "accountId")
	private Account account;
	private BigDecimal amountWithDiscount;
	private BigDecimal amountWithoutDiscount;
	private String payStatus;
	
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
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
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
		builder.append(", payStatus=");
		builder.append(payStatus);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append(", isDeleted=");
		builder.append(isDeleted);
		builder.append("]");
		return builder.toString();
	}
}
