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

package org.icefaces.demo.auction.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.demo.auction.model.AuctionItem;
import org.icefaces.demo.auction.util.FacesUtils;

@ManagedBean(name=PostBean.BEAN_NAME)
@CustomScoped(value="#{window}")
public class PostBean implements Serializable {
	public static final String BEAN_NAME = "postBean";
	
	private AuctionItem toAdd;
	private Date expiryDate;
	
	public void clear() {
		setToAdd(null);
		setExpiryDate(null);
	}

	public AuctionItem getToAdd() {
		if (toAdd == null) {
			toAdd = new AuctionItem();
			
			// Set some defaults
			toAdd.setPrice(0.1);
			toAdd.setShippingCost(1.0);
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR, 2);
			setExpiryDate(cal.getTime());
			toAdd.setExpiryDate(cal.getTimeInMillis());
			
			SettingsBean settingsBean = (SettingsBean)FacesUtils.getManagedBean(SettingsBean.BEAN_NAME);
			if ((settingsBean.getName() != null) && (!settingsBean.getName().isEmpty())) {
				toAdd.setSellerName(settingsBean.getName());
			}
		}
		
		return toAdd;
	}

	public void setToAdd(AuctionItem toAdd) {
		this.toAdd = toAdd;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
}
