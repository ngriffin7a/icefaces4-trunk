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
import java.util.Date;

import org.icefaces.demo.auction.util.TimestampUtil;

public class ChatMessage implements Serializable {
	private static final long serialVersionUID = -4152579047055859257L;

	private static final String SYSTEM_SENDER_NAME = "System";
	
	private Date timestamp;
	private String sender;
	private String text;
	private boolean system;
	
	public ChatMessage() {
		this(null, null, null, false);
	}
	
	public ChatMessage(boolean system, String text) {
		this(new Date(), SYSTEM_SENDER_NAME, text, system);
	}
	
	public ChatMessage(String sender, String text) {
		this(new Date(), sender, text, false);
	}

	public ChatMessage(Date timestamp, String sender, String text, boolean system) {
		this.timestamp = timestamp;
		this.sender = sender;
		this.text = text;
		this.system = system;
		
		// Use a trimmed version of our text
		if (this.text != null) {
			this.text = this.text.trim();
		}
	}
	
	public String getTimestampFormatted() {
		return timestamp != null ? new StringBuilder("(").append(TimestampUtil.stamp(timestamp)).append(")").toString() : null;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getSenderFormatted() {
		return sender != null ? new StringBuilder(sender).append(": ").toString() : null;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isSystem() {
		return system;
	}
	public void setSystem(boolean system) {
		this.system = system;
	}

	@Override
	public String toString() {
		// Format example:
		// (16:45:30) Peter: Hello there
		String timestampFormatted = getTimestampFormatted();
		String senderFormatted = getSenderFormatted();
		return new StringBuilder(
				timestampFormatted != null ? timestampFormatted : "")
				.append(' ')
				.append(senderFormatted != null ? senderFormatted : "Anonymous")
				.append(text).toString();
	}
}
