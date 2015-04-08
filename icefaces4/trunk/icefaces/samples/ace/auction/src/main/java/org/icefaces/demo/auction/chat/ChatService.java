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

package org.icefaces.demo.auction.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.application.PushRenderer;

@ManagedBean(name=ChatService.BEAN_NAME,eager=true)
@ApplicationScoped
public class ChatService implements Serializable {
	public static final String BEAN_NAME = "chatService";
	
	private List<ChatRoom> rooms;
	
	@PostConstruct
	private void initChatService() {
		rooms = new ArrayList<ChatRoom>(3);
		rooms.add(new ChatRoom("Lounge"));
		rooms.add(new ChatRoom("Bidding Tips"));
		rooms.add(new ChatRoom("Item Help"));
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
		boolean status = removeFromRoom(person.getName(), person.getCurrentRoom());
		
		// We'll take this opportunity to also do a check for any expired people
		// This is in case a PreDestroy didn't fire, so we don't have "ghosts" sitting around
		List<String> expiredPeople = person.getCurrentRoom().checkExpiredOccupants();
		if ((expiredPeople != null) && (!expiredPeople.isEmpty())) {
			for (String removeName : expiredPeople) {
				removeFromRoom(removeName, person.getCurrentRoom());
			}
		}
		
		// Finally reset our person state for future joins, if leaving was successful
		if (status) {
			person.resetState();
		}
		
		return status;
	}
	
	private boolean removeFromRoom(String name, ChatRoom toLeave) {
		if (toLeave.removeOccupant(name)) {
			// Notify any remaining users
			addSystemMessage(toLeave, name + " left.");
			
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
