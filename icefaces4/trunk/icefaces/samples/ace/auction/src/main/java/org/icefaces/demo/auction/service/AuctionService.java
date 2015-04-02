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

@ManagedBean(name=AuctionService.BEAN_NAME,eager=true)
@ApplicationScoped
public class AuctionService implements Serializable {
	public static final String BEAN_NAME = "auctionService";
	private static final Logger log = Logger.getLogger(AuctionService.class.getName());
	
	public static final int MINIMUM_ITEMS = 10;

	private AuctionWatcher renderer = AuctionWatcher.getInstance();
	private List<AuctionItem> auctions = new Vector<AuctionItem>(MINIMUM_ITEMS);
	
	@ManagedProperty(value="#{" + GlobalMessageBean.BEAN_NAME + "}")
	private GlobalMessageBean globalMessage;
	
	@PostConstruct
	public void setupAuction() {
		log.info("Starting up AuctionService, generating " + MINIMUM_ITEMS + " auction items.");
		for (int i = 0; i <= MINIMUM_ITEMS; i++) {
			addAuction(AuctionItemGenerator.makeItem(), false);
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
		if (auctions.size() <= MINIMUM_ITEMS) {
			addAuction(AuctionItemGenerator.makeItem());
		}
		
		for (AuctionItem currentItem : auctions) {
			if (currentItem.isExpired()) {
				deleteAuction(currentItem);
				break;
			}
		}
	}
	
	public void addAuction(AuctionItem toAdd) {
		addAuction(toAdd, true);
	}
	
	public void addAuction(AuctionItem toAdd, boolean log) {
		auctions.add(toAdd);
		sortAuctions();
		
		if (log) {
			if (globalMessage != null) {
				globalMessage.setLastUpdated(toAdd);
				
				globalMessage.addMessage("Listed a new auction for item '" + toAdd.getName() + "' added for " + NumberFormat.getCurrencyInstance().format(toAdd.getPrice()) + " ending in " +
						toAdd.getTimeLeft() + ".");
			}
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
		}
	}
	
	public boolean placeBid(AuctionItem toUpdate, double newBid) {
		if (newBid > toUpdate.getPrice()) {
			// Handle the new bid
			double oldPrice = toUpdate.getPrice();
			toUpdate.placeBid(newBid);
			
			if (globalMessage != null) {
				globalMessage.setLastUpdated(toUpdate);
				
				// TODO Use the Settings name here (and other logging places)
				globalMessage.addMessage("New bid (" + toUpdate.getBids() + " total) on item '" + toUpdate.getName() + "' increasing the price from " +
						NumberFormat.getCurrencyInstance().format(oldPrice) + " to " + NumberFormat.getCurrencyInstance().format(newBid) + ".");
			}
			
			return true;
		}
		return false;
	}
	
	public void sortAuctions() {
		Collections.sort(auctions);
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
