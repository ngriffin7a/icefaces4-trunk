package org.icefaces.demo.auction.model;

import java.io.Serializable;

public class AuctionItem implements Serializable {
	private static final long serialVersionUID = -396846143165017478L;
	
	public enum Condition {
		NEW_IN_PACKAGE, USED_EXCELLENT, USED_GOOD, USED_BAD, DAMAGED
	}
	
	public enum Delivery {
		ONE_DAY, THREE_DAYS, ONE_WEEK, ONE_TWO_WEEKS, ONE_MONTH
	}
	
	// Main info
	private String imagePath;
	private String name;
	private double price;
	private int bids = 0;
	private long expiryDate; // In milliseconds
	
	// Details
	private Integer shippingCost;
	private String sellerName;
	private String sellerLocation;
	private String description;
	private Delivery estimatedDelivery;
	private Condition condition;
	
	public AuctionItem() {
	}
	
	public AuctionItem(String imagePath, String name, double price, int bids, long expiryDateMil) {
		this(imagePath, name, price, bids, expiryDateMil, null, null, null, null, null, null);
	}
	
	public AuctionItem(String imagePath, String name, double price, int bids,
			long expiryDateMil, Integer shippingCost, String sellerName,
			String sellerLocation, String description,
			Delivery estimatedDelivery, Condition condition) {
		this.imagePath = imagePath;
		this.name = name;
		this.price = price;
		this.bids = bids;
		this.expiryDate = expiryDateMil;
		this.shippingCost = shippingCost;
		this.sellerName = sellerName;
		this.sellerLocation = sellerLocation;
		this.description = description;
		this.estimatedDelivery = estimatedDelivery;
		this.condition = condition;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getBids() {
		return bids;
	}
	public void setBids(int bids) {
		this.bids = bids;
	}
	public long getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(long expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Integer getShippingCost() {
		return shippingCost;
	}
	public void setShippingCost(Integer shippingCost) {
		this.shippingCost = shippingCost;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getSellerLocation() {
		return sellerLocation;
	}
	public void setSellerLocation(String sellerLocation) {
		this.sellerLocation = sellerLocation;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Delivery getEstimatedDelivery() {
		return estimatedDelivery;
	}
	public void setEstimatedDelivery(Delivery estimatedDelivery) {
		this.estimatedDelivery = estimatedDelivery;
	}
	public Condition getCondition() {
		return condition;
	}
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
}
