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

package org.icefaces.demo.emporium.tabs;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.demo.emporium.push.PushBean;
import org.icefaces.demo.emporium.util.FacesUtils;

@ManagedBean(name=TabController.BEAN_NAME)
@ApplicationScoped
public class TabController implements Serializable {
	private static final long serialVersionUID = 4856147366078794499L;

	public static final String BEAN_NAME = "tabController";
	
	public static final int TAB_AUCTION_LIST = 0;
	public static final int TAB_AUCTION_LOGS = 1;
	public static final int TAB_POST_AUCTION = 2;
	public static final int TAB_CHAT = 3;
	public static final int TAB_SETTINGS = 4;
	public static final int TAB_ABOUT = 5;
	
	public void moveToTab(int selectedIndex) {
		TabBean tabBean = (TabBean)FacesUtils.getManagedBean(TabBean.BEAN_NAME);
		
		int oldIndex = tabBean.getSelectedIndex();
		tabBean.setSelectedIndex(selectedIndex);
		processTabChange(oldIndex, selectedIndex);
	}
	
	public boolean checkTab(int indexCheck) {
		TabBean tabBean = (TabBean)FacesUtils.getManagedBean(TabBean.BEAN_NAME);
		
		return tabBean.getSelectedIndex() == indexCheck;
	}
	
	public void changeListener(ValueChangeEvent event) {
		try{
			processTabChange((Integer)event.getOldValue(), (Integer)event.getNewValue());
		}catch (ClassCastException failed) { }
	}
	
	private void processTabChange(int fromIndex, int toIndex) {
		// We want to manage our push groups based on the tab we're on
		// Basically if we're not on the first tab (Auction List) we don't need the constant 1/second push updates
		// Similary if we're not on that tab we'll want to join the manual push group
		PushBean pushBean = (PushBean)FacesUtils.getManagedBean(PushBean.BEAN_NAME);
		if (fromIndex == TAB_AUCTION_LIST) {
			pushBean.leaveIntervalGroup();
		}
		if (toIndex == TAB_AUCTION_LIST) {
			pushBean.joinIntervalGroup();
		}
	}
	
	public void auctionListTab(ActionEvent event) {
		moveToTab(TAB_AUCTION_LIST);
	}
	
	public void auctionLogsTab(ActionEvent event) {
		moveToTab(TAB_AUCTION_LOGS);
	}
	
	public void postAuctionTab(ActionEvent event) {
		moveToTab(TAB_POST_AUCTION);
	}
	
	public void chatTab(ActionEvent event) {
		moveToTab(TAB_CHAT);
	}
	
	public void settingsTab(ActionEvent event) {
		moveToTab(TAB_SETTINGS);
	}
	
	public void aboutTab(ActionEvent event) {
		moveToTab(TAB_ABOUT);
	}
	
	public boolean getIsAuctionListTab() {
		return checkTab(TAB_AUCTION_LIST);
	}
	
	public boolean getIsAuctionLogsTab() {
		return checkTab(TAB_AUCTION_LOGS);
	}
	
	public boolean getIsPostAuctionTab() {
		return checkTab(TAB_POST_AUCTION);
	}
	
	public boolean getIsChatTab() {
		return checkTab(TAB_CHAT);
	}
	
	public boolean getIsSettingsTab() {
		return checkTab(TAB_SETTINGS);
	}
	
	public boolean getIsAboutTab() {
		return checkTab(TAB_ABOUT);
	}
}
