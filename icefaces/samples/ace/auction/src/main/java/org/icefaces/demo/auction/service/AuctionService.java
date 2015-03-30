package org.icefaces.demo.auction.service;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.demo.auction.model.AuctionItem;
import org.icefaces.demo.auction.push.AuctionWatcher;

@ManagedBean(name=AuctionService.BEAN_NAME,eager=true)
@ApplicationScoped
public class AuctionService implements Serializable {
	public static final String BEAN_NAME = "auctionService";
	private static final Logger log = Logger.getLogger(AuctionService.class.getName());
	
	public static final int MINIMUM_ITEMS = 10;
	
	private List<AuctionItem> auctions = new Vector<AuctionItem>(MINIMUM_ITEMS);
	private AuctionWatcher renderer = AuctionWatcher.getInstance();
	
	@PostConstruct
	public void setupAuction() {
		log.info("Starting up AuctionService, generating " + MINIMUM_ITEMS + " auction items.");
		for (int i = 0; i < MINIMUM_ITEMS; i++) {
			auctions.add(AuctionItemGenerator.makeItem());
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
		// TODO Notify users when a new auction is added
		auctions.add(toAdd);
	}
	
	public void deleteAuction(AuctionItem toRemove) {
		if (auctions.remove(toRemove)) {
			// TODO Notify users when something is removed
		}
	}
	
	public boolean updateBid(AuctionItem toUpdate, double newBid) {
		if (newBid > toUpdate.getPrice()) {
			toUpdate.setPrice(newBid);
			toUpdate.increaseBids();
			
			// TODO Notify users of the change in bid price
			
			return true;
		}
		return false;
	}
	
	public List<AuctionItem> getAuctions() {
		return auctions;
	}

	public void setAuctions(List<AuctionItem> auctions) {
		this.auctions = auctions;
	}
}
