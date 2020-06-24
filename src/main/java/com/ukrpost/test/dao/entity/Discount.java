package com.ukrpost.test.dao.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Discount {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private int id;
	
	@NotNull
	@Min(0)
	private BigDecimal percent;
	private String description;
	private boolean isDeleted;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public BigDecimal getPercent() {
		return percent;
	}
	
	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		builder.append("Discount [id=");
		builder.append(id);
		builder.append(", percent=");
		builder.append(percent);
		builder.append(", description=");
		builder.append(description);
		builder.append(", isDeleted=");
		builder.append(isDeleted);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(percent,  description);
	}

	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		
		if (!(that instanceof Discount)) {
			return false;
		}
		
		Discount other = (Discount) that;
		return Objects.equals(percent, other.percent)
				&& Objects.equals(description, other.description);
	}
}
