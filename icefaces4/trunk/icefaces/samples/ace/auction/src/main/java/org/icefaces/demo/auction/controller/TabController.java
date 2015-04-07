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

import org.icefaces.demo.auction.bean.TabBean;
import org.icefaces.demo.auction.util.FacesUtils;

@ManagedBean(name=TabController.BEAN_NAME)
@ApplicationScoped
public class TabController implements Serializable {
	public static final String BEAN_NAME = "tabController";
	
	public static final int TAB_AUCTION_LIST = 0;
	public static final int TAB_AUCTION_LOGS = 1;
	public static final int TAB_POST_AUCTION = 2;
	public static final int TAB_CHAT = 3;
	public static final int TAB_SETTINGS = 4;
	
	public void moveToTab(int selectedIndex) {
		TabBean tabBean = (TabBean)FacesUtils.getManagedBean(TabBean.BEAN_NAME);
		tabBean.setSelectedIndex(selectedIndex);
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
}
