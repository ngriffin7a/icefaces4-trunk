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

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.icefaces.demo.auction.message.GlobalMessageBean;
import org.icefaces.demo.auction.model.AuctionItem;
import org.icefaces.demo.auction.push.AuctionWatcher;
import org.icefaces.demo.auction.test.TestFlags;

@ManagedBean(name=AuctionService.BEAN_NAME,eager=true)
@ApplicationScoped
public class AuctionService implements Serializable {
	public static final String BEAN_NAME = "auctionService";
	private static final Logger log = Logger.getLogger(AuctionService.class.getName());
	
	public static final int MINIMUM_ITEMS = TestFlags.TEST_MANY_ITEMS ? 5000 : 10;

	private AuctionWatcher renderer = AuctionWatcher.getInstance();
	private List<AuctionItem> auctions = new Vector<AuctionItem>(MINIMUM_ITEMS);
	
	@ManagedProperty(value="#{" + GlobalMessageBean.BEAN_NAME + "}")
	private GlobalMessageBean globalMessage;
	
	@PostConstruct
	public void setupAuction() {
		log.info("Test Flag status...expiry [" + TestFlags.TEST_EXPIRY
				+ "], bid robot [" + TestFlags.TEST_BIDROBOT
				+ "], manual push [" + TestFlags.TEST_MANUAL_PUSH
				+ "], many items [" + TestFlags.TEST_MANY_ITEMS + "].");
		log.info("Starting up AuctionService, generating " + MINIMUM_ITEMS + " auction items.");
		
		for (int i = 0; i < MINIMUM_ITEMS; i++) {
			addAuction(AuctionItemGenerator.makeUniqueItem(auctions), true);
		}
		
		renderer.start(this);
	}
	
	@PreDestroy
	public void cleanupAuction() {
		// Note as of Tomcat 7 the PreDestroy doesn't seem to be called properly for ApplicationScoped beans
		// So although we ideally want to stop the IntervalPushRenderer here, instead we have to use a
		//  ServletContextListener to reliably do so. See util.ContextListener for details.
		renderer.stop();
	}
	
	public void checkAuctionExpiry() {
		// Start adding items to get above our minimum as needed
		if (auctions.size() < MINIMUM_ITEMS) {
			addAuction(AuctionItemGenerator.makeUniqueItem(auctions));
		}
		
		for (AuctionItem currentItem : auctions) {
			if (currentItem.isExpired()) {
				deleteAuction(currentItem);
				break;
			}
		}
	}
	
	public boolean isUniqueItemName(String name) {
		if ((name != null) && (!name.isEmpty())) {
			for (AuctionItem loopCheck : auctions) {
				if (name.equalsIgnoreCase(loopCheck.getName())) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void addAuction(AuctionItem toAdd) {
		addAuction(toAdd, false);
	}
	
	public void addAuction(AuctionItem toAdd, boolean silent) {
		auctions.add(toAdd);
		sortAuctions();
		
		if (!silent) {
			if (globalMessage != null) {
				globalMessage.setLastUpdated(toAdd);
				
				globalMessage.addMessage("Listed a new auction sold by " + toAdd.getSellerName() + " for item '" + toAdd.getName() + "' added for " + NumberFormat.getCurrencyInstance().format(toAdd.getPrice()) + " ending in " +
						toAdd.getTimeLeft() + ".");
			}
			
			AuctionWatcher.getInstance().manualPush();
		}
	}
	
	public void deleteAuction(AuctionItem toRemove) {
		if (auctions.remove(toRemove)) {
			sortAuctions();
			
			if (globalMessage != null) {
				if (toRemove.getBids() > 0) {
					globalMessage.addMessage("Auction won for item '" + toRemove.getName() + "' with " + toRemove.getBids() + " bids and a winning price of " +
							NumberFormat.getCurrencyInstance().format(toRemove.getPrice()) + ".");
				}
				else {
					globalMessage.addMessage("Auction expired for item '" + toRemove.getName() + "' with no bids and a final listed price of " +
							NumberFormat.getCurrencyInstance().format(toRemove.getPrice()) + ".");
				}
			}
			
			AuctionWatcher.getInstance().manualPush();
		}
	}
	
	public boolean placeBid(AuctionItem toUpdate, double newBid) {
		return placeBid(toUpdate, null, newBid);
	}
	
	public boolean placeBid(AuctionItem toUpdate, String bidUsername, double newBid) {
		if (newBid > toUpdate.getPrice()) {
			// Handle the new bid
			double oldPrice = toUpdate.getPrice();
			toUpdate.placeBid(newBid);
			
			if (globalMessage != null) {
				globalMessage.setLastUpdated(toUpdate);
				
				// Customize our message with a username if we have one available
				String messageOpener = "New bid";
				if ((bidUsername != null) && (!bidUsername.isEmpty())) {
					messageOpener += " by " + bidUsername;
				}
				globalMessage.addMessage(messageOpener + " on item '" + toUpdate.getName() + "' increasing the price from " +
						NumberFormat.getCurrencyInstance().format(oldPrice) + " to " + NumberFormat.getCurrencyInstance().format(newBid) +
						" (" + NumberFormat.getCurrencyInstance().format(newBid - oldPrice) + " bid, " + toUpdate.getBids() + " bids total).");
			}
			
			AuctionWatcher.getInstance().manualPush();
			
			return true;
		}
		return false;
	}
	
	public void sortAuctions() {
		Collections.sort(auctions);
	}
	
	public int getAuctionsSize() {
		return auctions != null ? auctions.size() : 0;
	}
	
	public List<AuctionItem> getAuctions() {
		return auctions;
	}

	public void setAuctions(List<AuctionItem> auctions) {
		this.auctions = auctions;
	}
	
	public GlobalMessageBean getGlobalMessage() {
		return globalMessage;
	}

	public void setGlobalMessage(GlobalMessageBean globalMessage) {
		this.globalMessage = globalMessage;
	}
}
