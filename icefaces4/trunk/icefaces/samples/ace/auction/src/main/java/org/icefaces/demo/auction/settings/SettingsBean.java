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

package org.icefaces.demo.auction.settings;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.demo.auction.bid.model.AuctionItem;
import org.icefaces.demo.auction.util.FacesUtils;
import org.icefaces.demo.auction.util.ListData;

@ManagedBean(name=SettingsBean.BEAN_NAME)
@CustomScoped(value="#{window}")
public class SettingsBean implements Serializable {
	public static final String BEAN_NAME = "settingsBean";
	private static final Logger log = Logger.getLogger(SettingsBean.class.getName());
	
	public static final String SETTING_COOKIE_NAME = "settings";
	public static final String ICEFACES_THEME_PARAM = "org.icefaces.ace.theme";
	public static final String ICEFACES_THEME_DEFAULT = "sam";
	
	private boolean saveCookie;
	private int themeCheck = 0;
	
	private String name;
	private String location;
	private double bidIncrement;
	private String tabOrientation;
	private String notificationBackground;
	private String notificationForeground;
	private int popupWidth;
	private String themeName;
	
	@PostConstruct
	private void initSettingsBean() {
		// First initialize to defaults
		initDefaults();
		
		// Now try loading from a cookie
		try{
			name = FacesUtils.loadFromCookie(SETTING_COOKIE_NAME + "name", name).toString();
			location = FacesUtils.loadFromCookie(SETTING_COOKIE_NAME + "location", location).toString();
			bidIncrement = Double.parseDouble(FacesUtils.loadFromCookie(SETTING_COOKIE_NAME + "bidIncrement", bidIncrement).toString());
			tabOrientation = FacesUtils.loadFromCookie(SETTING_COOKIE_NAME + "tabOrientation", tabOrientation).toString();
			notificationBackground = FacesUtils.loadFromCookie(SETTING_COOKIE_NAME + "notificationBackground", notificationBackground).toString();
			notificationForeground = FacesUtils.loadFromCookie(SETTING_COOKIE_NAME + "notificationForeground", notificationForeground).toString();
			popupWidth = Integer.parseInt(FacesUtils.loadFromCookie(SETTING_COOKIE_NAME + "popupWidth", popupWidth).toString());
			themeName = FacesUtils.loadFromCookie(SETTING_COOKIE_NAME + "themeName", themeName).toString();
		}catch (Exception failedLoad) {
			log.log(Level.WARNING, "Failed when trying to load settings from a cookie, defaulting instead.", failedLoad);
			initDefaults();
		}

		// Set our boolean flag based on whether we actually had settings cookies at all
		saveCookie = FacesUtils.getHasCookieStartingWith(SETTING_COOKIE_NAME);
		
		log.info("User initialized with SettingsBean: " + toString());
	}
	
	private void initDefaults() {
		// Get a user number from the current millisecond time, which should be unique enough
		String userNumber = String.valueOf(System.currentTimeMillis());
		userNumber = userNumber.substring(userNumber.length()-5);
		name = "User #" + userNumber;
		location = ListData.DEFAULT_LOCATION;
		bidIncrement = AuctionItem.DEFAULT_BID_INCREMENT;
		tabOrientation = ListData.DEFAULT_TAB_ORIENTATION;
		notificationBackground = ListData.DEFAULT_BACKGROUND_COLOR.getRgba();
		notificationForeground = ListData.DEFAULT_FOREGROUND_COLOR.getRgba();
		popupWidth = 800;
		themeName = FacesUtils.getFacesParameter(ICEFACES_THEME_PARAM, ICEFACES_THEME_DEFAULT);
	}
	
	public void save() {
		FacesUtils.addCookie(SettingsBean.SETTING_COOKIE_NAME + "name", name);
		FacesUtils.addCookie(SettingsBean.SETTING_COOKIE_NAME + "location", location);
		FacesUtils.addCookie(SettingsBean.SETTING_COOKIE_NAME + "bidIncrement", String.valueOf(bidIncrement));
		FacesUtils.addCookie(SettingsBean.SETTING_COOKIE_NAME + "tabOrientation", tabOrientation);
		FacesUtils.addCookie(SettingsBean.SETTING_COOKIE_NAME + "notificationBackground", notificationBackground);
		FacesUtils.addCookie(SettingsBean.SETTING_COOKIE_NAME + "notificationForeground", notificationForeground);
		FacesUtils.addCookie(SettingsBean.SETTING_COOKIE_NAME + "popupWidth", popupWidth);
		FacesUtils.addCookie(SettingsBean.SETTING_COOKIE_NAME + "themeName", themeName);
	}
	
	public void delete() {
		FacesUtils.deleteCookie(SettingsBean.SETTING_COOKIE_NAME + "name");
		FacesUtils.deleteCookie(SettingsBean.SETTING_COOKIE_NAME + "location");
		FacesUtils.deleteCookie(SettingsBean.SETTING_COOKIE_NAME + "bidIncrement");
		FacesUtils.deleteCookie(SettingsBean.SETTING_COOKIE_NAME + "tabOrientation");
		FacesUtils.deleteCookie(SettingsBean.SETTING_COOKIE_NAME + "notificationBackground");
		FacesUtils.deleteCookie(SettingsBean.SETTING_COOKIE_NAME + "notificationForeground");
		FacesUtils.deleteCookie(SettingsBean.SETTING_COOKIE_NAME + "popupWidth");
		FacesUtils.deleteCookie(SettingsBean.SETTING_COOKIE_NAME + "themeName");
	}
	
	public boolean isSaveCookie() {
		return saveCookie;
	}

	public void setSaveCookie(boolean saveCookie) {
		this.saveCookie = saveCookie;
	}
	
	public int getThemeCheck() {
		return themeCheck;
	}

	public void setThemeCheck(int themeCheck) {
		this.themeCheck = themeCheck;
	}
	
	public void incrementThemeCheck() {
		themeCheck++;
	}
	
	public boolean getHasSetTheme() {
		return themeCheck > 1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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
	
	public int getPopupWidth() {
		return popupWidth;
	}

	public void setPopupWidth(int popupWidth) {
		this.popupWidth = popupWidth;
	}

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}
	
	@Override
	public String toString() {
		return name + " from " + location + " using theme " + themeName + " with fg/bg colors " + notificationForeground + "/" + notificationBackground;
	}
}
