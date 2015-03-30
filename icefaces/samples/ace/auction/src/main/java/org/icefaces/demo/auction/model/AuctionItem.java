package org.icefaces.demo.auction.model;

import java.io.Serializable;
import java.util.Date;

import org.icefaces.demo.auction.converter.TimeLeftConverter;

public class AuctionItem implements Serializable {
	private static final long serialVersionUID = -8371157741959714824L;

	public enum Condition {
		NEW("New"),
		USED_EXCELLENT("Used (Excellent)"),
		USED_GOOD("Used (Good)"),
		USED_BAD("Used (Bad)"),
		DAMAGED("Damaged");
		
		private String value;
		public String getValue() { return value; }
		private Condition(String value) { this.value = value; }
		@Override
		public String toString() { return value; }
	}
	
	public enum Delivery {
		ONE_DAY("1 day"),
		THREE_DAYS("1-3 days"),
		ONE_WEEK("3-7 days"),
		ONE_TWO_WEEKS("1-2 weeks"),
		ONE_MONTH("up to 1 month");
		
		private String value;
		public String getValue() { return value; }
		private Delivery(String value) { this.value = value; }
		@Override
		public String toString() { return value; }
	}
	
	// Main info
	private String imagePath;
	private String name;
	private double price;
	private int bids = 0;
	private long expiryDate; // In milliseconds
	
	// Details
	private Double shippingCost;
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
			long expiryDateMil, Double shippingCost, String sellerName,
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
	public void increaseBids() {
		bids++;
	}
	public long getExpiryDate() {
		return expiryDate;
	}
	public String getTimeLeft() {
		return TimeLeftConverter.convertExpiryToTimeLeft(expiryDate);
	}
	public void setExpiryDate(long expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Double getShippingCost() {
		return shippingCost;
	}
	public void setShippingCost(Double shippingCost) {
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
	
	public boolean isExpired() {
		return new Date().after(new Date(expiryDate));
	}
}
