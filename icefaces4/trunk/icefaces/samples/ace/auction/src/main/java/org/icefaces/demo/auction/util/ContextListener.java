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

package org.icefaces.demo.auction.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.icefaces.demo.auction.watcher.AuctionWatcher;
import org.icefaces.demo.auction.watcher.ChatWatcher;

/**
 * As of Tomcat 7 the PreDestroy annotation on ApplicationScoped beans doesn't seem to be called
 * This is important in this Auction application as we need to shutdown the scheduled Executor that
 *  is rendering every X seconds
 * So we'll use this ServletContextListener to perform the same functionality
 */
public class ContextListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		AuctionWatcher.getInstance().stop();
		ChatWatcher.getInstance().stop();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
	}
}
