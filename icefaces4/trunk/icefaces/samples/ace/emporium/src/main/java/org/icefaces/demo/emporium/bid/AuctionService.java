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

package org.icefaces.demo.emporium.bid;

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

import org.icefaces.application.ProductInfo;
import org.icefaces.demo.emporium.bid.model.AuctionItem;
import org.icefaces.demo.emporium.bid.util.AuctionItemGenerator;
import org.icefaces.demo.emporium.message.GlobalMessageBean;
import org.icefaces.demo.emporium.test.TestFlags;
import org.icefaces.demo.emporium.util.StringUtil;
import org.icefaces.demo.emporium.util.TimestampUtil;
import org.icefaces.demo.emporium.watcher.AuctionWatcher;

@ManagedBean(name=AuctionService.BEAN_NAME,eager=true)
@ApplicationScoped
public class AuctionService implements Serializable {
	public static final String BEAN_NAME = "auctionService";
	private static final Logger log = Logger.getLogger(AuctionService.class.getName());
	
	public static final int MINIMUM_ITEMS = TestFlags.TEST_MANY_ITEMS ? 5000 : 10;

	private AuctionWatcher renderer = AuctionWatcher.getInstance();
	private List<AuctionItem> auctions = new Vector<AuctionItem>(MINIMUM_ITEMS);
	private String productInfoString;
	private String startTime = TimestampUtil.deploystamp();
	
	@ManagedProperty(value="#{" + GlobalMessageBean.BEAN_NAME + "}")
	private GlobalMessageBean globalMessage;
	
	@PostConstruct
	private void initAuctionService() {
		log.info(TestFlags.getLogStatus());
		log.info("Starting up AuctionService, generating " + MINIMUM_ITEMS + " auction items.");
		
		generateDefaultData();
		
		renderer.start(this);
	}
	
	@PreDestroy
	private void cleanupAuctionService() {
		// Note as of Tomcat 7 the PreDestroy doesn't seem to be called properly for ApplicationScoped beans
		// So although we ideally want to stop the IntervalPushRenderer here, instead we have to use a
		//  ServletContextListener to reliably do so. See util.ContextListener for details.
		renderer.stop();
	}
	
	@Override
	public void finalize() {
		cleanupAuctionService();
	}
	
	public void generateDefaultData() {
		auctions.clear();
		for (int i = 0; i < MINIMUM_ITEMS; i++) {
			addAuction(AuctionItemGenerator.makeUniqueItem(auctions), true);
		}
	}
	
	public void checkAuctionExpiry() {
		// Start adding items to get above our minimum as needed
		if (auctions.size() < MINIMUM_ITEMS) {
			addAuction(AuctionItemGenerator.makeUniqueItem(auctions));
		}
		
		// Loop through all items looking for anything that is expired, which we will remove
		for (AuctionItem currentItem : auctions) {
			if (currentItem.isExpired()) {
				deleteAuction(currentItem);
				break;
			}
		}
	}
	
	public AuctionItem getAuctionByIndex(int index) {
		if ((index > 0) && (index < auctions.size())) {
			return auctions.get(index);
		}
		return null;
	}
	
	public boolean isUniqueItemName(String name) {
		if (StringUtil.validString(name)) {
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
		deleteAuction(toRemove, false);
	}
	
	public void deleteAuction(AuctionItem toRemove, boolean voluntaryDelete) {
		if (auctions.remove(toRemove)) {
			sortAuctions();
			
			if (globalMessage != null) {
				if (toRemove.getBids() > 0) {
					globalMessage.addMessage("Auction won for item '" + toRemove.getName() + "' with " + toRemove.getBids() + " bids and a winning price of " +
							          NumberFormat.getCurrencyInstance().format(toRemove.getPrice()) + ".");
				}
				else {
					globalMessage.addMessage("Auction " + (voluntaryDelete ? "deleted" : "expired") + " for item '" + toRemove.getName() + "' with no bids and a final listed price of " +
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
				if (StringUtil.validString(bidUsername)) {
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
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getProductInfo() {
		if (productInfoString == null) {
			productInfoString = new StringBuilder("Powered by ")
				.append(ProductInfo.PRODUCT)
				.append(" ").append(ProductInfo.PRIMARY).append(".")
				.append(ProductInfo.SECONDARY).append(".")
				.append(ProductInfo.TERTIARY).append(" from ")
				.append(ProductInfo.COMPANY).append(" (revision ")
				.append(ProductInfo.REVISION).append(")").toString();
		}
		return productInfoString;
	}
}
