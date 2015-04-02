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

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.demo.auction.bean.SettingsBean;
import org.icefaces.demo.auction.util.FacesUtils;
import org.icefaces.demo.auction.util.TimestampUtil;

@ManagedBean(name=SettingsController.BEAN_NAME)
@ApplicationScoped
public class SettingsController implements Serializable {
	public static final String BEAN_NAME = "settingsController";
	
	public void save(ActionEvent event) {
		SettingsBean bean = (SettingsBean)FacesUtils.getManagedBean(SettingsBean.BEAN_NAME);
		
		// Only need to do something on save, otherwise the submit will be enough for the current view
		if (bean.isSaveCookie()) {
			FacesUtils.addCookie(SettingsBean.SETTING_COOKIE_NAME + "name", bean.getName());
			FacesUtils.addCookie(SettingsBean.SETTING_COOKIE_NAME + "bidIncrement", String.valueOf(bean.getBidIncrement()));
			FacesUtils.addCookie(SettingsBean.SETTING_COOKIE_NAME + "tabOrientation", bean.getTabOrientation());
			FacesUtils.addCookie(SettingsBean.SETTING_COOKIE_NAME + "notificationBackground", bean.getNotificationBackground());
			FacesUtils.addCookie(SettingsBean.SETTING_COOKIE_NAME + "notificationForeground", bean.getNotificationForeground());
			FacesUtils.addCookie(SettingsBean.SETTING_COOKIE_NAME + "chartWidth", bean.getChartWidth());
			
			FacesUtils.addGlobalInfoMessage(TimestampUtil.stamp() + "Successfully saved your user settings to cookies.");
		}
		// Otherwise we should clear our old cookies
		else {
			FacesUtils.deleteCookie(SettingsBean.SETTING_COOKIE_NAME + "name");
			FacesUtils.deleteCookie(SettingsBean.SETTING_COOKIE_NAME + "bidIncrement");
			FacesUtils.deleteCookie(SettingsBean.SETTING_COOKIE_NAME + "tabOrientation");
			FacesUtils.deleteCookie(SettingsBean.SETTING_COOKIE_NAME + "notificationBackground");
			FacesUtils.deleteCookie(SettingsBean.SETTING_COOKIE_NAME + "notificationForeground");
			FacesUtils.deleteCookie(SettingsBean.SETTING_COOKIE_NAME + "chartWidth");
			
			FacesUtils.addGlobalInfoMessage(TimestampUtil.stamp() + "Successfully updated your user settings for the application.");
		}
	}
	
	public void saveAndReturn(ActionEvent event) {
		save(event);
		
		TabController tabController = (TabController)FacesUtils.getManagedBean(TabController.BEAN_NAME);
		tabController.auctionListTab(event);
	}
}
