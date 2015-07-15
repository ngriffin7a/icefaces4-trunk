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

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;
import org.icefaces.demo.emporium.bid.AuctionService;
import org.icefaces.demo.emporium.test.TestFlags;
import org.icefaces.demo.emporium.watcher.base.ThreadedWatcher;

/**
 * Class used to continuously monitor auctions for expiry
 * This will also update the auction list every INTERVAL seconds (default 1 second) so that users can see the time left counting down
 */
public class AuctionWatcher extends ThreadedWatcher {
	private static final long serialVersionUID = -5294487159144795209L;
	
	public static final String INTERVAL_PUSH_GROUP = "auctionWatcher";
	public static final String MANUAL_PUSH_GROUP = "auctionUpdate";
	public static final int INTERVAL = 1;
	
	private static AuctionWatcher singleton = null;
	private PortableRenderer renderer = PushRenderer.getPortableRenderer();
	
	private AuctionWatcher() {
	}
	
	public static AuctionWatcher getInstance() {
		if (singleton == null) {
			singleton = new AuctionWatcher();
		}
		return singleton;
	}

	public void start(final AuctionService toWatch) {
		if (!TestFlags.TEST_NO_INTERVAL_PUSH) {
			super.start("AuctionWatcher", 0, INTERVAL, new Runnable() {
				@Override
				public void run() {
					if (!thread.isShutdown()) {
						renderer.render(INTERVAL_PUSH_GROUP);
						
						// Check our items for expiry and add more as needed
						toWatch.checkAuctionExpiry();
					}
				}
			});
		}
	}
	
	public void manualPush() {
		renderer.render(MANUAL_PUSH_GROUP);
	}
}
