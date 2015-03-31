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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.demo.auction.model.AuctionItem;
import org.icefaces.demo.auction.util.ColorRGBA;
import org.icefaces.demo.auction.util.FacesUtils;
import org.icefaces.demo.auction.util.ListData;

@ManagedBean(name=SettingsBean.BEAN_NAME)
@CustomScoped(value="#{window}")
public class SettingsBean implements Serializable {
	public static final String BEAN_NAME = "settingsBean";
	private static final Logger log = Logger.getLogger(SettingsBean.class.getName());
	
	public static final String SETTING_COOKIE_NAME = "settings";
	
	private String name;
	private double bidIncrement;
	private String tabOrientation;
	private String notificationBackground;
	private String notificationForeground;
	private boolean saveCookie;
	
	@PostConstruct
	public void initValues() {
		// First initialize to defaults
		initDefaults();
		
		// Now try loading from a cookie
		try{
			name = FacesUtils.loadFromCookie(SETTING_COOKIE_NAME + "name", name).toString();
			bidIncrement = Double.parseDouble(FacesUtils.loadFromCookie(SETTING_COOKIE_NAME + "bidIncrement", bidIncrement).toString());
			tabOrientation = FacesUtils.loadFromCookie(SETTING_COOKIE_NAME + "tabOrientation", tabOrientation).toString();
			notificationBackground = FacesUtils.loadFromCookie(SETTING_COOKIE_NAME + "notificationBackground", notificationBackground).toString();
			notificationForeground = FacesUtils.loadFromCookie(SETTING_COOKIE_NAME + "notificationForeground", notificationForeground).toString();
		}catch (Exception failedLoad) {
			log.log(Level.WARNING, "Failed when trying to load settings from a cookie, defaulting instead.", failedLoad);
			initDefaults();
		}

		// Set our boolean flag based on whether we actually had settings cookies at all
		saveCookie = FacesUtils.getHasCookieStartingWith(SETTING_COOKIE_NAME); 
	}
	
	private void initDefaults() {
		// Get a user number from the current millisecond time, which should be unique enough
		String userNumber = String.valueOf(System.currentTimeMillis());
		userNumber = userNumber.substring(userNumber.length()-5);
		name = "User #" + userNumber;
		bidIncrement = AuctionItem.DEFAULT_BID_INCREMENT;
		tabOrientation = ListData.DEFAULT_TAB_ORIENTATION;
		notificationBackground = "rgba(255, 255, 255, " + ColorRGBA.DEFAULT_OPACITY + ")";
		notificationForeground = "rgba(0, 0, 0, 1.0)";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getBidIncrement() {
		return bidIncrement;
	}

	public void setBidIncrement(double bidIncrement) {
		this.bidIncrement = bidIncrement;
	}

	public String getTabOrientation() {
		return tabOrientation;
	}

	public void setTabOrientation(String tabOrientation) {
		this.tabOrientation = tabOrientation;
	}

	public String getNotificationBackground() {
		return notificationBackground;
	}

	public void setNotificationBackground(String notificationBackground) {
		this.notificationBackground = notificationBackground;
	}

	public String getNotificationForeground() {
		return notificationForeground;
	}

	public void setNotificationForeground(String notificationForeground) {
		this.notificationForeground = notificationForeground;
	}

	public boolean isSaveCookie() {
		return saveCookie;
	}

	public void setSaveCookie(boolean saveCookie) {
		this.saveCookie = saveCookie;
	}
}
