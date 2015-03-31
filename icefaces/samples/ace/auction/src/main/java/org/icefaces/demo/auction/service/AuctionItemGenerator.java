/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.demo.auction.service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.icefaces.demo.auction.model.AuctionItem;
import org.icefaces.demo.auction.test.TestFlags;

public class AuctionItemGenerator {
	private static final Random random = new SecureRandom();
	
	private static final int HOUR_IN_MILLISECONDS = 60 * 60 * 1000;
	
	public static AuctionItem makeItem() {
		AuctionItem toReturn = new AuctionItem();
		toReturn.setImagePath(generateImagePath());
		toReturn.setName(generateName());
		toReturn.setPrice(generatePrice());
		toReturn.setBids(generateBids());
		toReturn.setExpiryDate(generateExpiryDate());
		toReturn.setShippingCost(generateShippingCost());
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
	
	private static double generatePrice() {
		// Figure out if we're doing a cheap, normal, big, or huge size price
		switch (1+random.nextInt(4)) {
			case 1: return random.nextInt(10) + random.nextDouble() + 0.1; // Need to add 0.1 for the super rare 0 + 0.0 case
			case 2: return 5+random.nextInt(100) + random.nextDouble();
			case 3: return 100+random.nextInt(1000) + random.nextDouble();
			case 4: return 1000+random.nextInt(10000) + random.nextDouble();
		}
		return 1.0;
	}
	
	private static int generateBids() {
		if (random.nextBoolean()) {
			return random.nextInt(3);
		}
		return 0;
	}
	
	private static long generateExpiryDate() {
		if (!TestFlags.TEST_EXPIRY) {
			// First randomly choose if we're doing time beyond a day
			int min = HOUR_IN_MILLISECONDS; // Minimum of an hour away
			int cap = min;
			switch (1+random.nextInt(4)) {
				// Up to a week
				case 1: cap = HOUR_IN_MILLISECONDS * 24 * 6; break;
				// Up to 3 days
				case 2: cap = HOUR_IN_MILLISECONDS * 24 * 3; break;
				// Up to 1 day
				case 3: cap = HOUR_IN_MILLISECONDS * 24; break;
				// Random hours
				case 4: cap = HOUR_IN_MILLISECONDS * (1+random.nextInt(5)); break;
			}
			return (new Date().getTime()+min)+random.nextInt(cap);
		}
		else {
			long shortExpiry = new Date().getTime();
			shortExpiry += ((20+random.nextInt(30)) * 1000); // Expire in 20-50 seconds
			return shortExpiry;
		}
	}
	
	private static Double generateShippingCost() {
		if (random.nextBoolean()) {
			return (1+random.nextInt(20)) + random.nextDouble();
		}
		else {
			return 1+random.nextInt(20) + 0.0;
		}
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
