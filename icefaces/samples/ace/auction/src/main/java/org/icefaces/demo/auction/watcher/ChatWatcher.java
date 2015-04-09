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

package org.icefaces.demo.auction.watcher;

import org.icefaces.demo.auction.chat.ChatService;
import org.icefaces.demo.auction.watcher.base.ThreadedWatcher;

public class ChatWatcher extends ThreadedWatcher {
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
