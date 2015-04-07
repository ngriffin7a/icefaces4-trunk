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

import org.icefaces.demo.auction.bean.ChatBean;
import org.icefaces.demo.auction.chat.ChatMessage;
import org.icefaces.demo.auction.service.ChatService;
import org.icefaces.demo.auction.util.FacesUtils;

@ManagedBean(name=ChatController.BEAN_NAME)
@ApplicationScoped
public class ChatController implements Serializable {
	public static final String BEAN_NAME = "chatController";
	
	public void submitMessage(ActionEvent event) {
		ChatBean bean = (ChatBean)FacesUtils.getManagedBean(ChatBean.BEAN_NAME);
		
		// Only bother submitting if we actually have a typed message
		if ((bean.getCurrentRoom() != null) && (bean.getCurrentMessage() != null) && (bean.getCurrentMessage().trim().length() > 0)) {
			ChatMessage message = new ChatMessage(bean.getName(), bean.getCurrentMessage());
			
			ChatService service = (ChatService)FacesUtils.getManagedBean(ChatService.BEAN_NAME);
			service.addMessage(bean.getCurrentRoom(), message);
		}
		
		// Clear our old message
		bean.setCurrentMessage(null);
	}
	
	public void toggleOccupantsList(ActionEvent event) {
		ChatBean bean = (ChatBean)FacesUtils.getManagedBean(ChatBean.BEAN_NAME);
		bean.setRenderOccupants(!bean.isRenderOccupants()); // Toggle our flag
	}
	
	public void positionTab(ActionEvent event) {
		changePositionAndTab(ChatBean.ChatPosition.TAB, TabController.TAB_CHAT);
	}
	
	public void positionPopup(ActionEvent event) {
		changePositionAndTab(ChatBean.ChatPosition.POPUP, TabController.TAB_AUCTION_LIST);
	}
	
	public void positionBottom(ActionEvent event) {
		changePositionAndTab(ChatBean.ChatPosition.BOTTOM, TabController.TAB_AUCTION_LIST);
	}
	
	private void changePositionAndTab(ChatBean.ChatPosition position, int tabIndex) {
		ChatBean bean = (ChatBean)FacesUtils.getManagedBean(ChatBean.BEAN_NAME);
		bean.setPosition(position);
		
		TabController tabController = (TabController)FacesUtils.getManagedBean(TabController.BEAN_NAME);
		tabController.moveToTab(tabIndex);
	}
}
