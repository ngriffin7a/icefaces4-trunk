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

package org.icefaces.demo.emporium.watcher;

import org.icefaces.demo.emporium.chat.ChatService;
import org.icefaces.demo.emporium.watcher.base.ThreadedWatcher;

/**
 * Class to monitor the chat rooms for expired occupants
 * This can happen if a person closes their browser without leaving the chat room,
 *  in which case we need to clean them from the occupant list
 * This check will run every INTERVAL minutes (by default 2 minutes)
 */
public class ChatWatcher extends ThreadedWatcher {
	private static final long serialVersionUID = 8532516032899510758L;

	public static final int INTERVAL = 60 * 2; // 2 minutes
	
	private static ChatWatcher singleton = null;
	
	private ChatWatcher() {
	}
	
	public static ChatWatcher getInstance() {
		if (singleton == null) {
			singleton = new ChatWatcher();
		}
		return singleton;
	}
	
	public void start(final ChatService toWatch) {
		super.start("ChatWatcher", INTERVAL*2, INTERVAL, new Runnable() {
			@Override
			public void run() {
				if (!thread.isShutdown()) {
					toWatch.checkOccupantExpiry();
				}
			}
		});
	}
}
