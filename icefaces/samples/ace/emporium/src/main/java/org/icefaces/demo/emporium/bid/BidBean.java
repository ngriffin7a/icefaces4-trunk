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

package org.icefaces.demo.emporium.bid;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.icefaces.ace.model.table.RowState;
import org.icefaces.ace.model.table.RowStateMap;
import org.icefaces.demo.emporium.bid.model.AuctionItem;
import org.icefaces.demo.emporium.settings.SettingsBean;
import org.icefaces.demo.emporium.util.FacesUtils;

@ManagedBean(name=BidBean.BEAN_NAME)
@ViewScoped
public class BidBean implements Serializable {
	private static final long serialVersionUID = 1517509014530895068L;

	public static final String BEAN_NAME = "bidBean";
	
	private boolean renderBidPanel = false;
	private boolean showHistoryDialog = false;
	private Double currentBid;
	private AuctionItem bidItem;
	private RowStateMap stateMap;
	private boolean renderFilter = false;

	public boolean isRenderBidPanel() {
		return renderBidPanel;
	}
	public void setRenderBidPanel(boolean renderBidPanel) {
		this.renderBidPanel = renderBidPanel;
	}
	public boolean getCheckRenderBidPanel() {
		// Before checking our renderBidPanel flag let's make sure our item isn't expired
		if (bidItem != null) {
			// If it is we'll stop bidding and reset our row selection state
			if (bidItem.isExpired()) {
				stopBidding();
				unselectRows();
			}
		}
		
		return renderBidPanel;
	}
	public boolean isShowHistoryDialog() {
		return showHistoryDialog;
	}
	public void setShowHistoryDialog(boolean showHistoryDialog) {
		this.showHistoryDialog = showHistoryDialog;
	}
	public Double getCurrentBid() {
		return currentBid;
	}
	public void setCurrentBid(Double currentBid) {
		this.currentBid = currentBid;
	}
	public AuctionItem getBidItem() {
		return bidItem;
	}
	public void setBidItem(AuctionItem bidItem) {
		this.bidItem = bidItem;
	}
	public RowStateMap getStateMap() {
		return stateMap;
	}
	public void setStateMap(RowStateMap stateMap) {
		this.stateMap = stateMap;
	}
	public boolean isRenderFilter() {
		return renderFilter;
	}
	public void setRenderFilter(boolean renderFilter) {
		this.renderFilter = renderFilter;
	}
	
	public void startBidding(AuctionItem item) {
		setBidItem(item);
		updateBidding();
		
		renderBidPanel = true;
	}
	
	public void updateBidding() {
		if (bidItem != null) {
			SettingsBean settings = (SettingsBean)FacesUtils.getManagedBean(SettingsBean.BEAN_NAME);
			currentBid = bidItem.getPrice() + settings.getBidIncrement();
		}
	}
	
	public void stopBidding() {
		setBidItem(null);
		currentBid = null;
		renderBidPanel = false;
	}
	
	public void unselectRows() {
		if (stateMap != null) {
			RowState rowState = null;
			for (Object rowData : stateMap.getSelected()) {
				rowState = stateMap.get(rowData);
				rowState.setSelected(false);
			}
		}
	}
}
