/*
 * Copyright 2004-2015 ICEsoft Technologies Canada Corp.
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

package org.icefaces.demo.auction.bid.util;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.icefaces.demo.auction.bid.model.AuctionImage;
import org.icefaces.demo.auction.bid.model.AuctionItem;
import org.icefaces.demo.auction.test.TestFlags;
import org.icefaces.demo.auction.util.ListData;

public class AuctionItemGenerator {
	private static final Random random = new SecureRandom();
	
	private static final int HOUR_IN_MILLISECONDS = 60 * 60 * 1000;
	private static final int UNIQUE_NAME_MAX_CHECKS = 5;
	
	private static int uniqueCountSuffix = random.nextInt(100);
	
	public static AuctionItem makeItem() {
		AuctionItem toReturn = new AuctionItem();
		toReturn.setName(generateName());
		toReturn.setImageName(generateImageName(toReturn.getName()));
		toReturn.setPrice(generatePrice());
		toReturn.setExpiryDate(generateExpiryDate());
		toReturn.setShippingCost(generateShippingCost());
		toReturn.setSellerName(generatePersonName());
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
	
	public static AuctionItem makeUniqueItem(List<AuctionItem> toCheck) {
		AuctionItem toReturn = makeItem();
		
		if ((toCheck == null) || (toCheck.isEmpty())) {
			return toReturn;
		}
		
		return checkUniqueName(toReturn, toCheck, 0);
	}
	
	private static AuctionItem checkUniqueName(AuctionItem item, List<AuctionItem> toCheck, int iteration) {
		boolean nameChanged = false;
		for (AuctionItem loopCheck : toCheck) {
			// If we have a matching name regenerate one
			if (loopCheck.getName().equalsIgnoreCase(item.getName())) {
				item.setName(generateName()); // Regenerate a new name
				
				// Also need to regenerate the description since it might use the name
				// Similarly we regenerate the image name for the same reason
				// Note we use the base name without any unique count suffix, to make it more readable
				item.setDescription(generateDescription(item.getName()));
				item.setImageName(generateImageName(item.getName()));
				
				// It's possible to reach our max unique name checks
				// This would happen if our requested auction list size is bigger than ListData.ITEMS
				// In such a case we want to append a random unique string and just return to stop recursively checking
				if (iteration > UNIQUE_NAME_MAX_CHECKS) {
					uniqueCountSuffix++;
					item.setName(item.getName() + " #" + uniqueCountSuffix);
					
					return item;
				}
				
				nameChanged = true;
				break;
			}
		}
		
		if (nameChanged) {
			// If our name changed we need to recursively recheck it
			return checkUniqueName(item, toCheck, iteration+1);
		}
		
		return item;
	}
	
	public static double makeSmallBid(AuctionItem toBid) {
		return toBid.getPrice()+AuctionItem.DEFAULT_BID_INCREMENT+random.nextInt((int)AuctionItem.SMALL_BID_INCREMENT)+random.nextDouble();
	}
	
	public static double makeBid(AuctionItem toBid) {
		return toBid.getPrice()+AuctionItem.DEFAULT_BID_INCREMENT+random.nextInt((int)AuctionItem.MAX_BID_INCREASE/2)+random.nextDouble();
	}
	
	public static String generateName() {
		return ListData.ITEMS[random.nextInt(ListData.ITEMS.length)];
	}
	
	public static String generateImageName(String name) {
		// Need to use a static approach here instead of a bean lookup to be threadsafe
		return AuctionImage.staticConvertNameToImageName(name);
	}
	
	public static double generatePrice() {
		// Figure out if we're doing a cheap, normal, big, or huge size price
		switch (1+random.nextInt(6)) {
			case 1: case 2: return random.nextInt(10) + random.nextDouble() + 0.1; // Need to add 0.1 for the super rare 0 + 0.0 case
			case 3: case 4: return 5+random.nextInt(100) + random.nextDouble();
			case 5: return 100+random.nextInt(1000) + random.nextDouble();
			case 6: return 1000+random.nextInt(10000) + random.nextDouble();
		}
		return 1.0;
	}
	
	public static long generateExpiryDate() {
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
				case 4: cap = HOUR_IN_MILLISECONDS * 24; break;
				// Random hours
				case 5: cap = HOUR_IN_MILLISECONDS * (1+random.nextInt(5)); break;
			}
			return (new Date().getTime()+min)+random.nextInt(cap);
		}
		else {
			long shortExpiry = new Date().getTime();
			shortExpiry += ((20+random.nextInt(30)) * 1000); // Expire in 20-50 seconds
			return shortExpiry;
		}
	}
	
	public static Double generateShippingCost() {
		if (random.nextBoolean()) {
			return (1+random.nextInt(20)) + random.nextDouble();
		}
		else {
			return 1+random.nextInt(20) + 0.0;
		}
	}
	
	public static String generatePersonName() {
		String toReturn = ListData.FIRST_NAMES[random.nextInt(ListData.FIRST_NAMES.length)];
		
		// Don't need a last name for everyone
		if (random.nextInt(10) > 4) {
			toReturn += " " + ListData.LAST_NAMES[random.nextInt(ListData.LAST_NAMES.length)];
		}
		return toReturn;
	}
	
	public static String generateSellerLocation() {
		return ListData.LOCATIONS[random.nextInt(ListData.LOCATIONS.length)];
	}
	
	public static String generateDescription(String name) {
		// Start with an opening line
		String toReturn = "";
		switch (random.nextInt(7)) {
			case 0: toReturn = "Bid now on this"; break;
			case 1: toReturn = "This"; break;
			case 2: toReturn = "Hurry and bid on this"; break;
			case 3: toReturn = "Great opportunity to own this"; break;
			case 4: toReturn = "Rare chance to purchase this"; break;
			case 5: toReturn = "Interesting listing on this"; break;
			case 6: toReturn = "Quickly put some money on this"; break;
		}
		
		// Then add an adjective to help describe the item
		// tiny, small, big, huge, cool, neat, fun, unique, historical, funky, pretty, basic, fancy, red, blue, green, orange
		toReturn += " ";
		switch (random.nextInt(18)) {
			case 0: toReturn += "tiny"; break;
			case 1: toReturn += "small"; break;
			case 2: toReturn += "big"; break;
			case 3: toReturn += "huge"; break;
			case 4: toReturn += "cool"; break;
			case 5: toReturn += "neat"; break;
			case 6: toReturn += "fun"; break;
			case 7: toReturn += "unique"; break;
			case 8: toReturn += "historical"; break;
			case 9: toReturn += "funky"; break;
			case 10: toReturn += "pretty"; break;
			case 11: toReturn += "basic"; break;
			case 12: toReturn += "fancy"; break;
			case 13: toReturn += "red"; break;
			case 14: toReturn += "blue"; break;
			case 15: toReturn += "green"; break;
			case 16: toReturn += "orange"; break;
			case 17: toReturn = toReturn.trim(); break;
		}
		
		// Have a chance to add the item name, otherwise go generic
		if (random.nextInt(10) < 3) {
			toReturn += " item";
		}
		else {
			toReturn += " " + name;
		}
		
		// End with a final sentence
		switch (random.nextInt(9)) {
			case 0: toReturn += ", highly sought after"; break;
			case 1: toReturn += ", lots of fun"; break;
			case 2: toReturn += ", interesting and unique"; break;
			case 3: toReturn += ", barely any left"; break;
			case 4: toReturn += ", many for sale"; break;
			case 5: toReturn += ", free local shipping"; break;
			case 6: toReturn += ", once in a lifetime chance"; break;
			case 7: toReturn += ", tell your friends"; break;
			case 8: toReturn += "."; break;
		}
		
		return toReturn;
	}
	
	public static AuctionItem.Delivery generateEstimatedDelivery() {
		AuctionItem.Delivery available[] = AuctionItem.Delivery.values();
		return available[random.nextInt(available.length)];
	}
	
	public static AuctionItem.Condition generateCondition() {
		AuctionItem.Condition available[] = AuctionItem.Condition.values();
		return available[random.nextInt(available.length)];
	}
	
	public static AuctionItem generateFakeBids(AuctionItem toBid) {
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
