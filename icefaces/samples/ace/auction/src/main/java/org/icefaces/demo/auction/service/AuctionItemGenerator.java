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
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.icefaces.demo.auction.model.AuctionItem;
import org.icefaces.demo.auction.test.TestFlags;
import org.icefaces.demo.auction.util.ListData;

public class AuctionItemGenerator {
	private static final Random random = new SecureRandom();
	
	private static final int HOUR_IN_MILLISECONDS = 60 * 60 * 1000;
	
	public static AuctionItem makeItem() {
		AuctionItem toReturn = new AuctionItem();
		toReturn.setName(generateName());
		toReturn.setPrice(generatePrice());
		toReturn.setExpiryDate(generateExpiryDate());
		toReturn.setShippingCost(generateShippingCost());
		toReturn.setSellerName(generateSellerName());
		toReturn.setSellerLocation(generateSellerLocation());
		toReturn.setDescription(generateDescription(toReturn.getName()));
		toReturn.setEstimatedDelivery(generateEstimatedDelivery());
		toReturn.setCondition(generateCondition());
		
		// In half the cases generate some initial bids
		// We need to actually place/record these bids for historical purposes, as compared to just setting bid count directly
		if (random.nextBoolean()) {
			generateFakeBids(toReturn);
		}
		
		return toReturn;
	}
	
	public static double makeSmallBid(AuctionItem toBid) {
		return toBid.getPrice()+AuctionItem.DEFAULT_BID_INCREMENT+random.nextInt((int)AuctionItem.SMALL_BID_INCREMENT)+random.nextDouble();
	}
	
	public static double makeBid(AuctionItem toBid) {
		return toBid.getPrice()+AuctionItem.DEFAULT_BID_INCREMENT+random.nextInt((int)AuctionItem.MAX_BID_INCREASE/2)+random.nextDouble();
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
		String toReturn = ListData.FIRST_NAMES[random.nextInt(ListData.FIRST_NAMES.length)];
		
		// Don't need a last name for everyone
		if (random.nextInt(10) > 4) {
			toReturn += " " + ListData.LAST_NAMES[random.nextInt(ListData.LAST_NAMES.length)];
		}
		return toReturn;
	}
	
	private static String generateSellerLocation() {
		return ListData.LOCATIONS[random.nextInt(ListData.LOCATIONS.length)];
	}
	
	private static String generateDescription(String name) {
		// TODO Generate a realistic description based on name
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
	
	private static AuctionItem generateFakeBids(AuctionItem toBid) {
		int bidsToPlace = random.nextInt(11);
		
		if (bidsToPlace > 0) {
			// We'll place one bid an hour for the past X hours, up to the current hour
			Calendar cal = Calendar.getInstance();
			// Subtract a number of hours equal to the bids we're going to place, plus one
			// Also randomize the minutes and seconds to get realistic looking data
			cal.add(Calendar.HOUR_OF_DAY, (bidsToPlace+1) * -1);
			cal.add(Calendar.MINUTE, (10+random.nextInt(20)) * -1);
			cal.add(Calendar.SECOND, random.nextInt(50) * -1);
			
			for (int i = 0; i < bidsToPlace; i++) {
				toBid.placeBid(cal.getTime(), makeSmallBid(toBid));
				
				// Each iteration we'll add another hour, random minutes, and subtract random seconds
				cal.add(Calendar.HOUR_OF_DAY, 1);
				cal.add(Calendar.MINUTE, random.nextInt(30));
				cal.add(Calendar.SECOND, random.nextInt(50) * -1);
			}
		}
		
		return toBid;
	}
}
