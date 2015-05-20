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

package org.icefaces.demo.auction.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;
import org.icefaces.demo.auction.watcher.ChatWatcher;

@ManagedBean(name=ChatService.BEAN_NAME,eager=true)
@ApplicationScoped
public class ChatService implements Serializable {
	public static final String BEAN_NAME = "chatService";
	private static final Logger log = Logger.getLogger(ChatService.class.getName());
	
	private ChatWatcher expiry = ChatWatcher.getInstance();
	private PortableRenderer portable;
	private List<ChatRoom> rooms;
	
	@PostConstruct
	private void initChatService() {
		rooms = new ArrayList<ChatRoom>(3);
		rooms.add(new ChatRoom("Lounge"));
		rooms.add(new ChatRoom("Bidding Tips"));
		rooms.add(new ChatRoom("Item Help"));
		
		portable = PushRenderer.getPortableRenderer();
		
		expiry.start(this);
	}
	
	@PreDestroy
	public void cleanupChatService() {
		expiry.stop();
	}
	
	@Override
	public void finalize() {
		cleanupChatService();
	}
	
	public int checkOccupantExpiry() {
		int removedCount = 0;
		
		if ((rooms != null) && (!rooms.isEmpty())) {
			List<String> expiredPeople = null;
			for (ChatRoom loopRoom : rooms) {
				expiredPeople = loopRoom.checkExpiredOccupants();
				
				if ((expiredPeople != null) && (!expiredPeople.isEmpty())) {
					for (String removeName : expiredPeople) {
						if (removeFromRoom(removeName, loopRoom, true)) {
							removedCount++;
						}
					}
				}
			}
		}
		
		return removedCount;
	}

	public List<ChatRoom> getRooms() {
		return rooms;
	}

	public void setRooms(List<ChatRoom> rooms) {
		this.rooms = rooms;
	}
	
	public ChatRoom getRoomByName(String name) {
		for (ChatRoom loopRoom : rooms) {
			if (name.equalsIgnoreCase(loopRoom.getName())) {
				return loopRoom;
			}
		}
		return null;
	}
	
	public void joinDefaultRoom(ChatBean person) {
		if ((rooms != null) && (!rooms.isEmpty())) {
			joinRoom(person, rooms.get(0));
		}
	}
	
	public void joinRoom(ChatBean person, ChatRoom toJoin) {
		toJoin.addOccupant(person);
		
		// Update our bean state as well
		person.setCurrentRoom(toJoin);
		person.setSelectedRoom(null);
		
		PushRenderer.addCurrentView(toJoin.getPushGroup());
		
		// Notify any users in the room already
		addSystemMessage(toJoin, person.getName() + " joined.");
	}
	
	public boolean leaveRoom(ChatBean person) {
		return leaveRoom(person, false);
	}
	
	public boolean leaveRoom(ChatBean person, boolean fromDestroy) {
		boolean status = removeFromRoom(person.getName(), person.getCurrentRoom(), fromDestroy);
		
		// Finally reset our person state for future joins (regardless of status)
		person.resetState();
		
		return status;
	}
	
	private boolean removeFromRoom(String name, ChatRoom toLeave, boolean fromDestroy) {
		if (toLeave.removeOccupant(name)) {
			if (!fromDestroy) {
				try{
					PushRenderer.removeCurrentView(toLeave.getPushGroup());
				}catch (Exception failedRemove) {
					log.log(Level.SEVERE, "Failed to remove " + name + " from room push group " + toLeave.getPushGroup(), failedRemove);
				}
			}
			
			// Notify any remaining users
			addSystemMessage(toLeave, name + " left.");
			
			return true;
		}
		return false;
	}
	
	private void addSystemMessage(ChatRoom room, String text) {
		if (room.addSystemMessage(text)) {
			pushUpdateRoom(room.getPushGroup());
		}
	}
	
	public void addMessage(ChatRoom room, ChatMessage toAdd) {
		if (room.addMessage(toAdd)) {
			pushUpdateRoom(room.getPushGroup());
		}
	}
	
	private void pushUpdateRoom(String pushGroup) {
		// Just failsafe wrap this in a catch, in case it's used from an unexpected place
		try{
			if (FacesContext.getCurrentInstance() != null) {
				PushRenderer.render(pushGroup);
			}
			else if (portable != null) {
				portable.render(pushGroup);
			}
		}catch (Exception failedUpdate) {
			log.log(Level.SEVERE, "Failed to push update the room push group " + pushGroup, failedUpdate);
		}
	}
}
