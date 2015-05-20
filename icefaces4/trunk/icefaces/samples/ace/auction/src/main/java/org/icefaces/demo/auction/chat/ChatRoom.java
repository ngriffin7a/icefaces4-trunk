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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoom implements Serializable {
	public static final int MAX_MESSAGES = 15;
	public static final String DEFAULT_PUSHGROUP_SUFFIX = "_pgroup";
	
	private String name;
	private String pushGroup;
	private List<ChatMessage> messages;
	private Map<String,ChatBean> occupants;
	
	public ChatRoom() {
		this("Unknown");
	}
	
	public ChatRoom(String name) {
		this(name, name + DEFAULT_PUSHGROUP_SUFFIX);
	}

	public ChatRoom(String name, String pushGroup) {
		this(name, pushGroup, new ArrayList<ChatMessage>(0));
	}
	
	public ChatRoom(String name, String pushGroup, List<ChatMessage> messages) {
		this(name, pushGroup, messages, new HashMap<String,ChatBean>());
	}
	
	public ChatRoom(String name, String pushGroup,
				     List<ChatMessage> messages, Map<String,ChatBean> occupants) {
		this.name = name;
		this.pushGroup = pushGroup;
		this.messages = messages;
		this.occupants = occupants;
		
		// Notify users of the chat room they're in
		addSystemMessage("Welcome to the '" + name + "' chat room.");
		
		// Fill out the rest of the maximum messages with blanks, to help with formatting
		for (int i = messages.size(); i < MAX_MESSAGES; i++) {
			addMessage(new ChatMessage());
		}
	}
	
	public int getMaxMessages() {
		return MAX_MESSAGES;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPushGroup() {
		return pushGroup;
	}
	public void setPushGroup(String pushGroup) {
		this.pushGroup = pushGroup;
	}
	public List<ChatMessage> getMessages() {
		if (messages == null) {
			messages = new ArrayList<ChatMessage>();
		}
		return messages;
	}
	public void setMessages(List<ChatMessage> messages) {
		this.messages = messages;
	}
	
	public Map<String,ChatBean> getOccupants() {
		if (occupants == null) {
			occupants = new HashMap<String,ChatBean>();
		}
		return occupants;
	}
	public List<String> getOccupantsList() {
		return new ArrayList<String>(occupants.keySet());
	}
	public void setOccupants(Map<String, ChatBean> occupants) {
		this.occupants = occupants;
	}

	public int getMessagesSize() {
		return getMessages().size();
	}
	
	public int getOccupantsSize() {
		return getOccupants().size();
	}
	
	public boolean addMessage(ChatMessage message) {
		boolean status = getMessages().add(message);
		
		if (messages.size() > MAX_MESSAGES) {
			messages.remove(0);
		}
		
		return status;
	}
	
	public boolean addSystemMessage(String text) {
		return addMessage(new ChatMessage(true, text));
	}
	
	public void addOccupant(ChatBean person) {
		getOccupants().put(person.getName(), person);
	}
	
	public boolean removeOccupant(String name) {
		return getOccupants().remove(name) != null;
	}
	
	public List<String> checkExpiredOccupants() {
		if (getOccupantsSize() > 0) {
			// Check our map for invalid objects, and return any names associated with them
			List<String> expired = new ArrayList<String>(0);
			for (Map.Entry<String,ChatBean> loopOccupant : getOccupants().entrySet()) {
				if (loopOccupant.getValue() == null) {
					expired.add(loopOccupant.getKey());
				}
			}
			
			return expired;
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return "Room " + name + " (" + getOccupantsSize() + " occupants, " + getMessagesSize() + " messages)";
	}
}
