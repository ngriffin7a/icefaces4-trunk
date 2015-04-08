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

package org.icefaces.demo.auction.bid.model;

import java.io.Serializable;
import java.util.Date;

public class AuctionHistory implements Serializable {
	private static final long serialVersionUID = 8759997075273076877L;
	
	private Date date;
	private double bidIncrease;
	private double price;
	
	public AuctionHistory(double bidIncrease, double price) {
		this(new Date(), bidIncrease, price);
	}

	public AuctionHistory(Date date, double bidIncrease, double price) {
		this.date = date;
		this.bidIncrease = bidIncrease;
		this.price = price;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getBidIncrease() {
		return bidIncrease;
	}
	public void setBidIncrease(double bidIncrease) {
		this.bidIncrease = bidIncrease;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return String.valueOf(bidIncrease);
	}
}
