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

package org.icefaces.demo.auction.controller;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.icefaces.ace.event.SelectEvent;
import org.icefaces.ace.event.UnselectEvent;
import org.icefaces.demo.auction.bean.BidBean;
import org.icefaces.demo.auction.bean.ChartBean;
import org.icefaces.demo.auction.bean.PostBean;
import org.icefaces.demo.auction.model.AuctionItem;
import org.icefaces.demo.auction.service.AuctionService;
import org.icefaces.demo.auction.util.FacesUtils;
import org.icefaces.util.JavaScriptRunner;

@ManagedBean(name=BidController.BEAN_NAME)
@ApplicationScoped
public class BidController implements Serializable {
	private static final Logger log = Logger.getLogger(BidController.class.getName());
	
	// TODO Should rename this AuctionController as it's more generic than just bids
	public static final String BEAN_NAME = "bidController";
	
	public void selectItem(SelectEvent event) {
		BidBean bidBean = (BidBean)FacesUtils.getManagedBean(BidBean.BEAN_NAME);
		bidBean.startBidding((AuctionItem)event.getObject());
		
		// On row selection also focus the bid field and select it via Javascript on the client
		JavaScriptRunner.runScript(FacesContext.getCurrentInstance(), "focusBid();");
	}
	
	public void unselectItem(UnselectEvent event) {
		BidBean bidBean = (BidBean)FacesUtils.getManagedBean(BidBean.BEAN_NAME);
		bidBean.stopBidding();
	}
	
	public void submitBid(ActionEvent event) {
		BidBean bidBean = (BidBean)FacesUtils.getManagedBean(BidBean.BEAN_NAME);
		String parentId = event.getComponent().getParent().getClientId(); // Used in case we have to add an error message
		
		// Need to validate two cases: bid is less than current price OR bid is over max bid increase compared to price
		// First validate that the bid actually exceeds the price we're comparing to
		if (bidBean.getCurrentBid() <= bidBean.getBidItem().getPrice()) {
			FacesUtils.addWarnMessage(parentId, "Note your bid does not exceed the current price of " +
					NumberFormat.getCurrencyInstance().format(bidBean.getBidItem().getPrice()) + ", updating your current bid.");
			
			// Also note we update the user bid to match the minimum we expect
			bidBean.updateBidding();
			return;
		}
		if ((bidBean.getCurrentBid() - bidBean.getBidItem().getPrice()) > AuctionItem.MAX_BID_INCREASE) {
			FacesUtils.addWarnMessage(parentId, "You cannot increase the bid more than " +
					NumberFormat.getCurrencyInstance().format(AuctionItem.MAX_BID_INCREASE) + " at once, resetting your current bid.");
			
			// Reset our current bid to match what the maximum increase should be
			bidBean.setCurrentBid(bidBean.getBidItem().getPrice() + AuctionItem.MAX_BID_INCREASE - 1);
			return;
		}
		
		// Try to update the bid, if we fail we'll want to notify just this user that they got outbid
		AuctionService service = (AuctionService)FacesUtils.getManagedBean(AuctionService.BEAN_NAME);
		if (!service.placeBid(bidBean.getBidItem(), bidBean.getCurrentBid())) {
			FacesUtils.addWarnMessage(parentId, "Your bid of " +
					NumberFormat.getCurrencyInstance().format(bidBean.getCurrentBid()) + " does not exceed the current price, please bid again.");
		}
		bidBean.updateBidding();
	}
	
	public void cancelBid(ActionEvent event) {
		BidBean bidBean = (BidBean)FacesUtils.getManagedBean(BidBean.BEAN_NAME);
		bidBean.stopBidding();
		bidBean.unselectRows();
	}
	
	public void openGraphPriceDialog(ActionEvent event) {
		openHistoryDialog(ChartBean.ChartType.PRICE_CHANGE);
	}
	
	public void openGraphBidDialog(ActionEvent event) {
		openHistoryDialog(ChartBean.ChartType.BID_INCREASE);
	}
	
	public void openViewHistoryDialog(ActionEvent event) {
		openHistoryDialog(null);
	}
	
	public void openHistoryDialog(ChartBean.ChartType type) {
		// Refresh our chart data as part of opening the dialog
		ChartBean chartBean = (ChartBean)FacesUtils.getManagedBean(ChartBean.BEAN_NAME);
		chartBean.setType(type);
		chartBean.refresh();
		
		BidBean bidBean = (BidBean)FacesUtils.getManagedBean(BidBean.BEAN_NAME);
		bidBean.setShowHistoryDialog(true);
	}
	
	public void closeHistoryDialog(ActionEvent event) {
		BidBean bidBean = (BidBean)FacesUtils.getManagedBean(BidBean.BEAN_NAME);
		bidBean.setShowHistoryDialog(false);
		
		// Clear any old chart data
		ChartBean chartBean = (ChartBean)FacesUtils.getManagedBean(ChartBean.BEAN_NAME);
		chartBean.clear();
	}
	
	public void closeHistoryDialogListener(AjaxBehaviorEvent event) {
		closeHistoryDialog(null);
	}
	
	public void postAuction(ActionEvent event) {
		AuctionService service = (AuctionService)FacesUtils.getManagedBean(AuctionService.BEAN_NAME);
		PostBean postBean = (PostBean)FacesUtils.getManagedBean(PostBean.BEAN_NAME);
		
		// TODO ICE-10611 Because the ace:dateTimeEntry doesn't seem to respect min/max date for time, we need to check our expiry date
		Date dateCheck = getMinExpiryDate();
		if (postBean.getExpiryDate().before(dateCheck)) {
			FacesUtils.addGlobalErrorMessage("Expiry date is too soon, please enter a date and time at least " + AuctionItem.EXPIRY_DATE_MINIMUM + " minutes away.");
			postBean.setExpiryDate(dateCheck);
			return;
		}
		dateCheck = getMaxExpiryDate();
		if (postBean.getExpiryDate().after(dateCheck)) {
			FacesUtils.addGlobalErrorMessage("Expiry date is too far in the future, please enter a date and time a maximum of 7 days away.");
			postBean.setExpiryDate(dateCheck);
			return;
		}
		
		// If our date is valid we need to apply our expiry date from the calendar to our object
		postBean.getToAdd().setExpiryDate(postBean.getExpiryDate().getTime());
		
		// Add our new auction, then clear the data
		log.info("Posting a new user auction item: " + postBean.getToAdd());
		service.addAuction(postBean.getToAdd());
		postBean.clear();
		
		// Redirect back to the list after posting
		TabController tabController = (TabController)FacesUtils.getManagedBean(TabController.BEAN_NAME);
		tabController.auctionListTab(event);
	}
	
	public Date getMinExpiryDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, AuctionItem.EXPIRY_DATE_MINIMUM); // Allow a minimum of 30 minutes away
		return cal.getTime();
	}
	
	public Date getMaxExpiryDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 7); // Allow up to a week away
		return cal.getTime();
	}
}
