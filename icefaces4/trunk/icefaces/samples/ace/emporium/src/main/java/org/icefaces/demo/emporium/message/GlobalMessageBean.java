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

package org.icefaces.demo.emporium.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.demo.emporium.bid.model.AuctionItem;
import org.icefaces.demo.emporium.test.TestFlags;
import org.icefaces.demo.emporium.util.FacesUtils;
import org.icefaces.demo.emporium.util.TimestampUtil;
import org.icefaces.demo.emporium.watcher.AuctionWatcher;

@ApplicationScoped
@ManagedBean(name=GlobalMessageBean.BEAN_NAME)
public class GlobalMessageBean implements Serializable {
	public static final String BEAN_NAME = "globalMessage";
	
	private static final int FULL_LOG_COUNT = 100;
	private static final int MESSAGE_DISPLAY_TIME = 5000;
	
	private List<String> messages = new ArrayList<String>(0);
	private AuctionItem lastUpdated = null;
	private boolean hasNew = false;
	private Thread waitThread;
	
	private void startWait() {
		// Stop any old thread
		if ((waitThread != null) && (waitThread.isAlive())) {
			waitThread.interrupt();
		}
		waitThread = null;
		
		// Start a new thread
		waitThread = new Thread(new Runnable() {
			@Override
			public void run() {
				hasNew = true;
				
				try {
					Thread.sleep(MESSAGE_DISPLAY_TIME);
					
					// Reset our new flag and last updated object
					// This will hide the notification panel and remove the client side row styling
					hasNew = false;
					lastUpdated = null;
					AuctionWatcher.getInstance().manualPush();
				}catch (InterruptedException ignored) { }
			}
		});
		waitThread.setName("Emporium GlobalMessageBean wait");
		waitThread.start();
	}
	
	public void resetLog() {
		messages.clear();
	}
	
	public AuctionItem getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(AuctionItem lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public boolean getHasNew() {
		return hasNew;
	}
	
	public List<String> getMessages() {
		return messages;
	}
	
	public int getFullLogCount() {
		return FULL_LOG_COUNT;
	}
	
	public int getMessagesCount() {
		if ((messages == null) || (messages.isEmpty())) {
			return 0;
		}
		return messages.size();
	}
	
	public int getVisibleLogCount() {
		int toReturn = getMessagesCount();
		
		if (toReturn > FULL_LOG_COUNT) {
			return FULL_LOG_COUNT;
		}
		return toReturn;
	}
	
	public void addMessage(String message) {
		if (!TestFlags.TEST_GROWL_MESSAGES) {
			messages.add(0, TimestampUtil.stamp() + message);
			
			// Check if we exceed our max size, in which case we'll remove the last (oldest) element
			if (messages.size() > FULL_LOG_COUNT) {
				messages.remove(messages.size()-1);
			}
			
			startWait();
		}
		else {
			FacesUtils.addGlobalInfoMessage(message);
		}
	}
}
