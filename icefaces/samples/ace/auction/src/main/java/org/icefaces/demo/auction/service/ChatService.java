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

package org.icefaces.demo.auction.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.application.PushRenderer;
import org.icefaces.demo.auction.bean.ChatBean;
import org.icefaces.demo.auction.chat.ChatMessage;
import org.icefaces.demo.auction.chat.ChatRoom;

@ManagedBean(name=ChatService.BEAN_NAME,eager=true)
@ApplicationScoped
public class ChatService implements Serializable {
	public static final String BEAN_NAME = "chatService";
	
	private List<ChatRoom> rooms;
	
	@PostConstruct
	private void initChatService() {
		rooms = new ArrayList<ChatRoom>(1);
		rooms.add(new ChatRoom("Lounge"));
	}

	public List<ChatRoom> getRooms() {
		return rooms;
	}

	public void setRooms(List<ChatRoom> rooms) {
		this.rooms = rooms;
	}
	
	public boolean getHasMultipleRooms() {
		return rooms != null && rooms.size() > 1;
	}
	
	public ChatRoom getDefaultRoom() {
		return !getHasMultipleRooms() ? rooms.get(0) : null;
	}
	
	public void joinRoom(ChatBean person, ChatRoom toJoin) {
		toJoin.addOccupant(person);
		PushRenderer.addCurrentView(toJoin.getPushGroup());
		
		// Notify any users in the room already
		addSystemMessage(toJoin, person.getName() + " has joined the chat room.");
	}
	
	public boolean leaveRoom(ChatBean person, ChatRoom toLeave) {
		boolean status = removeFromRoom(person.getName(), toLeave);
		
		// We'll take this opportunity to also do a check for any expired people
		// This is in case a PreDestroy didn't fire, so we don't have "ghosts" sitting around
		List<String> expiredPeople = toLeave.checkExpiredOccupants();
		if ((expiredPeople != null) && (!expiredPeople.isEmpty())) {
			for (String removeName : expiredPeople) {
				removeFromRoom(removeName, toLeave);
			}
		}
		
		return status;
	}
	
	private boolean removeFromRoom(String name, ChatRoom toLeave) {
		if (toLeave.removeOccupant(name)) {
			// Notify any remaining users
			addSystemMessage(toLeave, name + " has left the chat room.");
			
			return true;
		}
		return false;
	}
	
	private void addSystemMessage(ChatRoom room, String text) {
		room.addSystemMessage(text);
	}
	
	public void addMessage(ChatRoom room, ChatMessage toAdd) {
		if (room.addMessage(toAdd)) {
			PushRenderer.render(room.getPushGroup());
		}
	}
}
