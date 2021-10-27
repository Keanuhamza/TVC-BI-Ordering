package com.example.BIService.models;

import javax.persistence.*;

public class cProductTotal {

	private String productName;
	private Long total;

	public cProductTotal() {
	}


	public cProductTotal(String productName, Long total) {
		this.productName = productName;
		this.total = total;
	}

	@Override
	public String toString() {
		return "{" +
			" productName='" + getProductName() + "'" +
			", total='" + getTotal() + "'" +
			"}";
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getTotal() {
		return this.total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	


}

