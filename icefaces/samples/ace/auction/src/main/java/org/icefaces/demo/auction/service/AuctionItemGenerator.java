package org.icefaces.demo.auction.service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.icefaces.demo.auction.model.AuctionItem;

public class AuctionItemGenerator {
	private static final Random random = new SecureRandom();
	
	public static AuctionItem makeItem() {
		AuctionItem toReturn = new AuctionItem();
		toReturn.setImagePath(generateImagePath());
		toReturn.setName(generateName());
		toReturn.setPrice(10+random.nextInt(5000));
		if (random.nextBoolean()) {
			toReturn.setBids(random.nextInt(3));
		}
		toReturn.setExpiryDate((new Date().getTime()+3600000)+random.nextInt(604800000)); // Random from base date plus an hour up to a week away
		toReturn.setShippingCost(1+random.nextInt(20));
		toReturn.setSellerName(generateSellerName());
		toReturn.setSellerLocation(generateSellerLocation());
		toReturn.setDescription(generateDescription(toReturn.getName(), toReturn.getImagePath()));
		toReturn.setEstimatedDelivery(generateEstimatedDelivery());
		toReturn.setCondition(generateCondition());
		return toReturn;
	}
	
	private static String generateImagePath() {
		// TODO Pick from a preset list generated from image files in directory
		return "none.png";
	}
	
	private static String generateName() {
		// TODO Generate a realistic sounding item name
		return "Bucket";
	}
	
	private static String generateSellerName() {
		// TODO Generate a human sounding first/last name
		return "Joe Smith";
	}
	
	private static String generateSellerLocation() {
		// TODO Pick from a preset list of countries, pulled from a common class (also used in dropdowns when adding an item)
		return "Ireland";
	}
	
	private static String generateDescription(String name, String imagePath) {
		// TODO Generate a realistic description based on name and image path
		return "Great item";
	}
	
	private static AuctionItem.Delivery generateEstimatedDelivery() {
		AuctionItem.Delivery available[] = AuctionItem.Delivery.values();
		return available[random.nextInt(available.length)];
	}
	
	private static AuctionItem.Condition generateCondition() {
		AuctionItem.Condition available[] = AuctionItem.Condition.values();
		return available[random.nextInt(available.length)];		
	}
}
