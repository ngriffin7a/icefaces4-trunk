package org.icefaces.demo.auction.controller;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.ace.event.SelectEvent;
import org.icefaces.ace.event.UnselectEvent;
import org.icefaces.demo.auction.bean.BidBean;
import org.icefaces.demo.auction.model.AuctionItem;
import org.icefaces.demo.auction.service.AuctionService;
import org.icefaces.demo.auction.util.FacesUtils;

@ManagedBean(name=BidController.BEAN_NAME)
@ApplicationScoped
public class BidController implements Serializable {
	public static final String BEAN_NAME = "bidController";
	
	private static final double MAX_BID_INCREASE = 100.0;
	
	public void selectItem(SelectEvent event) {
		BidBean bidBean = (BidBean)FacesUtils.getManagedBean(BidBean.BEAN_NAME);
		bidBean.startBidding((AuctionItem)event.getObject());
	}
	
	public void unselectItem(UnselectEvent event) {
		BidBean bidBean = (BidBean)FacesUtils.getManagedBean(BidBean.BEAN_NAME);
		bidBean.stopBidding();
	}
	
	public void submitBid(ActionEvent event) {
		BidBean bidBean = (BidBean)FacesUtils.getManagedBean(BidBean.BEAN_NAME);
		
		// Need to validate two cases: bid is less than current price OR bid is over max bid increase compared to price
		// First validate that the bid actually exceeds the price we're comparing to
		if (bidBean.getCurrentBid() <= bidBean.getBidItem().getPrice()) {
			// TODO Notify user of invalid bid, since it must exceed the current bid
			bidBean.updateBidding(); // Also note we update the user bid to match the minimum we expect
			return;
		}
		if ((bidBean.getCurrentBid() - bidBean.getBidItem().getPrice()) > MAX_BID_INCREASE) {
			// TODO Notify the user that their bid cannot exceed MAX_BID_INCREASE
			bidBean.setCurrentBid(bidBean.getBidItem().getPrice() + MAX_BID_INCREASE);
			return;
		}
		
		// Try to update the bid, if we fail we'll want to notify just this user that they got outbid
		AuctionService service = (AuctionService)FacesUtils.getManagedBean(AuctionService.BEAN_NAME);
		if (!service.updateBid(bidBean.getBidItem(), bidBean.getCurrentBid())) {
			// TODO Notify just this user that they got outbid
		}
		bidBean.updateBidding();
	}
	
	public void cancelBid(ActionEvent event) {
		BidBean bidBean = (BidBean)FacesUtils.getManagedBean(BidBean.BEAN_NAME);
		bidBean.stopBidding();
	}
}
