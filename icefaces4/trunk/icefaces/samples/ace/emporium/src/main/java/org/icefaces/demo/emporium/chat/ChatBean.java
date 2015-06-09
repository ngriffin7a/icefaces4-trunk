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

package org.icefaces.demo.emporium.chat;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.icefaces.demo.emporium.settings.SettingsBean;
import org.icefaces.demo.emporium.test.TestFlags;
import org.icefaces.demo.emporium.util.FacesUtils;
import org.icefaces.demo.emporium.util.StringUtil;

@ManagedBean(name=ChatBean.BEAN_NAME)
@CustomScoped(value="#{window}")
public class ChatBean implements Serializable {
	public static final String BEAN_NAME = "chatBean";
	private static final Logger log = Logger.getLogger(ChatBean.class.getName());
	
	public enum ChatPosition {
		TAB("Tab"),
		POPUP("Popup"),
		BOTTOM("Bottom Panel");
		
		private String value;
		public String getValue() { return value; }
		private ChatPosition(String value) { this.value = value; }
		@Override
		public String toString() { return value; }
	}
	
	private String cachedName; // Store our previous name successfully pulled from SettingsBean
	private String selectedRoom;
	private ChatRoom currentRoom;
	private String currentMessage;
	private ChatPosition position = ChatPosition.TAB;
	private boolean renderOccupants = true;
	
	@ManagedProperty(value="#{" + ChatService.BEAN_NAME + "}")
	private ChatService service;
	
	@PostConstruct
	private void initChatBean() {
		if (TestFlags.TEST_AUTOJOIN_CHAT) {
			// Join our default room
			if (service != null) {
				service.joinDefaultRoom(this);
			}
		}
	}
	
	@PreDestroy
	private void cleanupChatBean() {
		if ((currentRoom != null) && (service != null)) {
			log.info("ChatBean timed out, removing user " + getName() + " from chat room " + currentRoom.getName());
			service.leaveRoom(this, true);
		}
	}
	
	@Override
	public void finalize() {
		log.info("ChatBean finalize for " + getName() + ", going to try to cleanup");
		cleanupChatBean();
	}
	
	public void resetState() {
		setRenderOccupants(true);
		setCurrentMessage(null);
		setCurrentRoom(null);
		setSelectedRoom(null);
	}
	
	public String getName() {
		// Try to get our name each time from the SettingsBean
		// This saves us having to manually sync the names between beans
		try{
			SettingsBean settings = (SettingsBean)FacesUtils.getManagedBean(SettingsBean.BEAN_NAME);
			if ((settings != null) && (StringUtil.validString(settings.getName()))) {
				cachedName = settings.getName();
				return settings.getName();
			}
		}catch (Exception failedSettings) { }
		
		if (StringUtil.validString(cachedName)) {
			return cachedName;
		}
		
		return "Anonymous";
	}
	
	public String getSelectedRoom() {
		return selectedRoom;
	}

	public void setSelectedRoom(String selectedRoom) {
		this.selectedRoom = selectedRoom;
	}

	public ChatRoom getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(ChatRoom currentRoom) {
		this.currentRoom = currentRoom;
	}

	public String getCurrentMessage() {
		return currentMessage;
	}

	public void setCurrentMessage(String currentMessage) {
		this.currentMessage = currentMessage;
	}
	
	public ChatPosition getPosition() {
		return position;
	}

	public void setPosition(ChatPosition position) {
		this.position = position;
	}

	public boolean isRenderOccupants() {
		return renderOccupants;
	}

	public void setRenderOccupants(boolean renderOccupants) {
		this.renderOccupants = renderOccupants;
	}

	public ChatService getService() {
		return service;
	}

	public void setService(ChatService service) {
		this.service = service;
	}
	
	public boolean getIsPositionTab() {
		return position == ChatPosition.TAB;
	}
	
	public boolean getIsPositionPopup() {
		return position == ChatPosition.POPUP;
	}
	
	public boolean getIsPositionBottom() {
		return position == ChatPosition.BOTTOM;
	}
	
	public boolean getHasCurrentRoom() {
		return currentRoom != null;
	}
}
