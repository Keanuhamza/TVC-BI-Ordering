package com.example.BIService.models;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class cOrder {
	
    private @Id @GeneratedValue Long id;
    private Long custID;
    private String custAddress;
    private Long custPhone;
	private String productName;
	private float prodPrice;
	private Integer quantity;
    
	//Constructors
	public cOrder() {}

	public cOrder(Long id, Long custID, 
	String custAddress, Long custPhone, 
	String productName, float prodPrice, Integer quantity) {
		this.id = id;
		this.custID = custID;
		this.custAddress = custAddress;
		this.custPhone = custPhone;
		this.productName = productName;
		this.prodPrice = prodPrice;
		this.quantity = quantity;
	}

	public cOrder(Long id, Long custID, String productName, Integer quantity) {
		this.id = id;
		this.custID = custID;
		this.productName = productName;
		this.quantity = quantity;

		prodPrice = 0f;
		custAddress = "null";
		custPhone = 0L;
	}

	public cOrder(Integer quantity) {
		this.id = -1L;
		this.custID = -1L;
		this.productName = "";
		this.quantity = quantity;

		prodPrice = 0f;
		custAddress = "null";
		custPhone = 0L;
	}

	//Setters and Getters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCustID() {
		return custID;
	}
	public void setCustID(Long custID) {
		this.custID = custID;
	}
	public String getCustAddress() {
		return custAddress;
	}
	public void setCustAddress(String custAddress) {
		this.custAddress = custAddress;
	}
	public Long getCustPhone() {
		return custPhone;
	}
	public void setCustPhone(Long custPhone) {
		this.custPhone = custPhone;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public float getProdPrice() {
		return prodPrice;
	}
	public void setProdPrice(float prodPrice) {
		this.prodPrice = prodPrice;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	//Overrides
	@Override
	public String toString() {
		return "Order{" +
				"id=" + id +
				", custID=" + custID +
				", custAddress='" + custAddress + '\'' +
				", custPhone=" + custPhone +
				", productName='" + productName + '\'' +
				", prodPrice=" + prodPrice +
				", quantity=" + quantity +
				'}';
	}
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		if (!super.equals(object)) return false;
		cOrder order = (cOrder) object;
		return Float.compare(order.prodPrice, prodPrice) == 0 &&
        quantity == order.quantity && 
        java.util.Objects.equals(id, order.id) && 
        java.util.Objects.equals(custID, order.custID) && 
        java.util.Objects.equals(custAddress, order.custAddress) && 
        java.util.Objects.equals(custPhone, order.custPhone) && 
        java.util.Objects.equals(productName, order.productName);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(super.hashCode(), id, custID, custAddress, custPhone, productName, prodPrice, quantity);
	}
}

