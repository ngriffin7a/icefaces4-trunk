/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package com.icesoft.applications.faces.auctionMonitor.beans;

import com.icesoft.applications.faces.auctionMonitor.AuctionState;
import com.icesoft.applications.faces.auctionMonitor.stubs.ItemType;
import com.icesoft.applications.faces.auctionMonitor.validators.BidValidator;
import com.icesoft.faces.async.render.SessionRenderer;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;

import java.util.Calendar;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

/**
 * Class used to represent a single item within the auction This class handles
 * such things as pricing, bidding, UI management, etc.
 */
public class AuctionMonitorItemBean extends ItemType {
	private static Log log = LogFactory.getLog(AuctionMonitorItemBean.class);
	private int localBidCount;
	private double localHighBid;
	private boolean expanded;
	private boolean bidExpanded;
	private double oldBid;
	private double tempLocalBid;
	private String bidMessage;
	private String imageUrl;
	private long[] timeLeftBrokenDown;
	private String timeLeftStyleClass;
	private Effect effect;

	private static final String NO_PHOTO_URL = "./images/noimage.gif";
	private static final String TIME_LEFT_5 = "./images/time_left_5.gif";
	private static final String TIME_LEFT_10 = "./images/time_left_10.gif";
	private static final String TIME_LEFT_15 = "./images/time_left_15.gif";
	private static final String TIME_LEFT_30 = "./images/time_left_30.gif";
	private static final String TIME_LEFT_60 = "./images/time_left_45.gif";
	private static final String TIME_LEFT_DAYS = "./images/time_left_days.gif";
	private static final String TIME_LEFT_HOURS = "./images/time_left_hours.gif";
	private static final String TRIANGLE_OPEN = "./images/triangle_open.gif";
	private static final String TRIANGLE_CLOSED = "./images/triangle_close.gif";
	private static final String SUCCESS = "success";

	private static final String STYLE_CLASS_EXPANDED_ROW = "rowClassHilite";

	public static final int MAX_BID_INCREASE = 1000000;
	public static final long MAX_BID = 1000000000;

	private static final int TIME_DAYS = 24 * 60 * 60 * 1000;
	private static final int TIME_HOURS = 60 * 60 * 1000;
	private static final int TIME_MINUTES = 60 * 1000;
	
	// Error message for no bid entered
	private static final FacesMessage ERROR_NO_BID = new FacesMessage("<br />You must enter a bid.","<br />You must enter a bid.");

	// private static final double NEW_BID_PRICE_INCREASE = 0.5;

	public AuctionMonitorItemBean(ItemType item) {
		super(item);
		localHighBid = getCurrentPrice();

		// Instead of a meaningless initial price of 0.0,
		tempLocalBid = localHighBid + 1;
	}

	public String getPicture() {
		try {
			imageUrl = getPictureURL().toString();
			if (imageUrl.startsWith("file:")) {
				imageUrl = imageUrl.substring(5);
			}
		} catch (NullPointerException e) {
			if (log.isWarnEnabled()) {
				log.warn("Failed to get the picture for an item because of "
						+ e);
			}
		}

		return (null == imageUrl ? NO_PHOTO_URL : imageUrl);
	}

	public Effect getEffect() {
		return effect;
	}

	public void setEffect(Effect effect) {
		this.effect = effect;
	}

	public void setCurrentPrice(double currentPrice) {
	}

	public double getCurrentPrice() {
		double newBid = getLocalBid();

		if ((oldBid > 0) && (newBid > oldBid)) {
			if (effect == null) {
				effect = new Highlight("#FFCC0B");
			}
			effect.setFired(false);
		}
		oldBid = newBid;

		return oldBid;
	}

	public void setBidCount(int bidCount) {
	}

	public int getBidCount() {
		try {
			localBidCount = Integer.parseInt(AuctionState.getAuctionMap()
					.get(getItemID() + ".bidCount").toString());
		} catch (NullPointerException e) {
			if (log.isWarnEnabled()) {
				log.warn("Failed to get the bid count for an item because of "
						+ e);
			}
		}

		return (localBidCount);
	}

	public void setTempLocalBid(double tempLocalBid) {
		this.tempLocalBid = tempLocalBid;
	}

	public double getTempLocalBid() {
		return tempLocalBid;
	}

	/**
	 * User generated bid is processed if validation was successful. Current
	 * price is updated and push is requested for the auction group.
	 * 
	 * @param event
	 *            jsf action event,
	 */
	public void localBid(ActionEvent event) {
		// If the user enter a blank value in the bid box, we will get a 0.0
		// here.
		if (tempLocalBid == 0.0) {
			// We should throw an error
			setBidMessage(ERROR_NO_BID.getSummary());
			ERROR_NO_BID.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(ERROR_NO_BID);

		} else {
			localHighBid = tempLocalBid;
			Map auctionMap = AuctionState.getAuctionMap();
			auctionMap.put(getItemID() + ".price", new Double(localHighBid));
			auctionMap.put(
					getItemID() + ".bidCount",
					new Integer(Integer.parseInt(auctionMap.get(
							getItemID() + ".bidCount").toString()) + 1));
			tempLocalBid++;
			getCurrentPrice();
		}
		SessionRenderer.render("auction");
	}

	/**
	 * Bid validation method is store locally so that it persists across server
	 * pushes.
	 * 
	 * @return current auction item bid message, empty if no validation errors
	 *         have occured.
	 */
	public String getBidMessage() {
		return bidMessage;
	}

	/**
	 * Sets the bid error message so that it can persist between events.
	 * 
	 * @param bidMessage
	 *            bid error message to set, can be an empty string if no error.
	 */
	public void setBidMessage(String bidMessage) {
		this.bidMessage = bidMessage;
	}

	public double getLocalBid() {
		try {
			localHighBid = Double.parseDouble(AuctionState.getAuctionMap()
					.get(getItemID() + ".price").toString());
		} catch (NullPointerException e) {
			if (log.isErrorEnabled()) {
				log.error("Error getting local bid:");
			}
		}

		return localHighBid;
	}

	public void setTimeLeft(long timeLeft) {
	}

	public long getTimeLeft() {
		Calendar endTimeCal = (Calendar) AuctionState.getAuctionMap().get(
				getItemID() + ".endTime");
		long endMillis = endTimeCal.getTime().getTime();
		return (endMillis - Calendar.getInstance().getTime().getTime());
	}

	public long[] getTimeLeftBrokenDown() {
		long left, days, hours, minutes, seconds;
		left = getTimeLeft();
		days = left / TIME_DAYS;
		left = left - days * TIME_DAYS;
		hours = left / TIME_HOURS;
		left = left - hours * TIME_HOURS;
		minutes = left / TIME_MINUTES;
		left = left - minutes * TIME_MINUTES;
		seconds = left / 1000;
		return new long[] { days, hours, minutes, seconds };
	}

	public void setTimeLeftStyleClass(String timeLeftStyleClass) {
	}

	public String getTimeLeftStyleClass() {
		return timeLeftStyleClass;
	}

	public void setTimeImageUrl(String timeImageUrl) {
	}

	public String getTimeImageUrl() {
		timeLeftBrokenDown = getTimeLeftBrokenDown();
		String timeImageUrl;
		if (0 != timeLeftBrokenDown[0]) {
			timeImageUrl = TIME_LEFT_DAYS;
			timeLeftStyleClass = "timeCellDays";
		} else if (0 != timeLeftBrokenDown[1]) {
			timeImageUrl = TIME_LEFT_HOURS;
			timeLeftStyleClass = "timeCellHours";
		} else if (timeLeftBrokenDown[2] >= 30) {
			timeImageUrl = TIME_LEFT_60;
			timeLeftStyleClass = "timeCellMins";
		} else if (timeLeftBrokenDown[2] >= 15) {
			timeImageUrl = TIME_LEFT_30;
			timeLeftStyleClass = "timeCellMins";
		} else if (timeLeftBrokenDown[2] >= 10) {
			timeImageUrl = TIME_LEFT_15;
			timeLeftStyleClass = "timeCellMins";
		} else if (timeLeftBrokenDown[2] >= 5) {
			timeImageUrl = TIME_LEFT_10;
			timeLeftStyleClass = "timeCellMins";
		} else {
			timeImageUrl = TIME_LEFT_5;
			timeLeftStyleClass = "timeCellMins";
		}

		return timeImageUrl;
	}

	public void setTimeLeftString(String timeLeftString) {
	}

	public String getTimeLeftString() {
		if (getTimeLeft() < 0) {
			return " Expired";
		}

		StringBuffer buf = new StringBuffer();
		buf.append("  ");
		if (0 != timeLeftBrokenDown[0]) {
			buf.append(Long.toString(timeLeftBrokenDown[0]));
			buf.append("d ");
		}

		if (0 != timeLeftBrokenDown[1]) {
			buf.append(Long.toString(timeLeftBrokenDown[1]));
			buf.append(":");
			if (timeLeftBrokenDown[2] < 10) {
				buf.append("0");
			}
		}

		buf.append(Long.toString(timeLeftBrokenDown[2]));
		buf.append(":");

		if (timeLeftBrokenDown[3] < 10) {
			buf.append("0");
		}

		buf.append(Long.toString(timeLeftBrokenDown[3]));

		return buf.toString();
	}

	public void setExpandedStyleClass(String expandedStyleClass) {
	}

	public String getExpandedStyleClass() {
		if (expanded) {
			return STYLE_CLASS_EXPANDED_ROW;
		} else {
			return "";
		}
	}

	public String pressExpandButton() {
		expanded = !expanded;
		return SUCCESS;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public void setExpandTriangleImage(String expandTriangleImage) {
	}

	public String getExpandTriangleImage() {
		if (expanded) {
			return TRIANGLE_OPEN;
		} else {
			return TRIANGLE_CLOSED;
		}
	}

	public boolean isExpired() {
		if (getTimeLeft() < 0) {
			return (true);
		}
		return (false);
	}

	public boolean isBidExpanded() {
		return bidExpanded;
	}

	public void setBidExpanded(boolean bidExpanded) {
		this.bidExpanded = bidExpanded;
	}
}
