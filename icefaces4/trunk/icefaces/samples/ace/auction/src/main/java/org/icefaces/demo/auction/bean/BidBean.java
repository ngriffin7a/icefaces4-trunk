package org.icefaces.demo.auction.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.icefaces.demo.auction.model.AuctionItem;

@ManagedBean(name=BidBean.BEAN_NAME)
@ViewScoped
public class BidBean implements Serializable {
	public static final String BEAN_NAME = "bidBean";
	
	private static final double BID_INCREMENT = 1.0;
	
	private boolean renderBidPanel = false;
	private Double currentBid;
	private AuctionItem bidItem;
	
	public boolean isRenderBidPanel() {
		return renderBidPanel;
	}
	public void setRenderBidPanel(boolean renderBidPanel) {
		this.renderBidPanel = renderBidPanel;
	}
	public Double getCurrentBid() {
		return currentBid;
	}
	public void setCurrentBid(Double currentBid) {
		this.currentBid = currentBid;
	}
	public AuctionItem getBidItem() {
		return bidItem;
	}
	public void setBidItem(AuctionItem bidItem) {
		this.bidItem = bidItem;
	}
	
	public void startBidding(AuctionItem item) {
		setBidItem(item);
		updateBidding();
		
		renderBidPanel = true;
	}
	
	public void updateBidding() {
		if (bidItem != null) {
			currentBid = bidItem.getPrice() + BID_INCREMENT;
		}
	}
	
	public void stopBidding() {
		setBidItem(null);
		currentBid = null;
		renderBidPanel = false;
	}
}
