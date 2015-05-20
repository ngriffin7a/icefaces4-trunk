/*
 * Copyright 2004-2015 ICEsoft Technologies Canada Corp.
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

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;

import org.icefaces.demo.auction.tabs.TabController;
import org.icefaces.demo.auction.util.FacesUtils;

@ManagedBean(name=SettingsController.BEAN_NAME)
@ApplicationScoped
public class SettingsController implements Serializable {
	public static final String BEAN_NAME = "settingsController";
	
	public void save(ActionEvent event) {
		SettingsBean bean = (SettingsBean)FacesUtils.getManagedBean(SettingsBean.BEAN_NAME);
		
		// Only need to do something on save, otherwise the submit will be enough for the current view
		if (bean.isSaveCookie()) {
			bean.save();
			
			FacesUtils.addGlobalInfoMessage("Successfully saved your user settings to cookies.");
		}
		// Otherwise we should clear our old cookies
		else {
			bean.delete();
			
			FacesUtils.addGlobalInfoMessage("Successfully updated your user settings for the application.");
		}
		
		TabController tabController = (TabController)FacesUtils.getManagedBean(TabController.BEAN_NAME);
		tabController.auctionListTab(event);
	}
	
	public void initializeTheme(ComponentSystemEvent event) {
		// We need to initialize the ICEfaces component theme that is customized via settings
		// To do this normally you'd use the ace:theme component in the view
		// But we can store our theme in cookies
		// Because ace:theme is processed very early in the JSF lifecycle the cookies aren't loaded yet
		// So instead we need to use f:event to call this method
		//  which will allow a hidden ace:themeSelect component to be rendered on the page, just once
		// This will basically duplicate the theme loading, except from cookies instead of a plain bean
		SettingsBean bean = (SettingsBean)FacesUtils.getManagedBean(SettingsBean.BEAN_NAME);
		if (!bean.getHasSetTheme()) {
			// Also if we're using the default theme then don't both resetting it, just skip the check by over-incrementing it
			String defaultTheme =
					FacesUtils.getFacesParameter(SettingsBean.ICEFACES_THEME_PARAM, SettingsBean.ICEFACES_THEME_DEFAULT);
			if (defaultTheme.equals(bean.getThemeName())) {
				bean.incrementThemeCheck();
			}
			
			bean.incrementThemeCheck();
		}
	}
}
