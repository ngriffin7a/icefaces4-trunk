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

package org.icefaces.demo.emporium.test;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name=TestFlags.BEAN_NAME)
@ApplicationScoped
public class TestFlags implements Serializable  {
	private static final long serialVersionUID = 6059811793634285960L;

	public static final String BEAN_NAME = "testFlags";
	
	public static final boolean TEST_NO_INTERVAL_PUSH = false; // Debugging toggle to turn off AuctionWatcher from doing 1/second interval pushes
	public static final boolean TEST_EXPIRY = false; // Debugging toggle to create short lasting items (true) or normal (false)
	public static final boolean TEST_BIDROBOT = false; // Debugging toggle to make BidRobots always spawn and bid really often
	public static final boolean TEST_MANY_ITEMS = false; // Debugging toggle to make a huge number of random items instead of the usual
	public static final boolean TEST_AUTOJOIN_CHAT = false; // Debugging toggle to make any user autojoin the default chat room
	public static final boolean TEST_GROWL_MESSAGES = false; // Debugging toggle to test ace:growlMessages in place of our notification panel
	public static final boolean TEST_NO_AUTH = false; // Debugging toggle to disallow Authentication (to reset data) if set to true
	
	public boolean isTestNoIntervalPush() {
		return TEST_NO_INTERVAL_PUSH;
	}

	public boolean isTestExpiry() {
		return TEST_EXPIRY;
	}

	public boolean isTestBidrobot() {
		return TEST_BIDROBOT;
	}

	public boolean isTestManyItems() {
		return TEST_MANY_ITEMS;
	}

	public boolean isTestAutojoinChat() {
		return TEST_AUTOJOIN_CHAT;
	}

	public boolean isTestGrowlMessages() {
		return TEST_GROWL_MESSAGES;
	}
	
	public boolean isTestNoAuth() {
		return TEST_NO_AUTH;
	}
}
