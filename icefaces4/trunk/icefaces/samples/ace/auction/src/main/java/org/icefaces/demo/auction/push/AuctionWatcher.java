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

package org.icefaces.demo.auction.push;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;
import org.icefaces.demo.auction.service.AuctionService;
import org.icefaces.demo.auction.test.TestFlags;

public class AuctionWatcher {
	public static final String INTERVAL_PUSH_GROUP = "auctionWatcher";
	public static final String MANUAL_PUSH_GROUP = "auctionUpdate";
	public static final int INTERVAL_SECONDS = 1;
	private static final Logger log = Logger.getLogger(AuctionWatcher.class.getName());
	
	private static AuctionWatcher singleton = null;
	private ScheduledExecutorService renderThread;
	private ScheduledFuture<?> renderThreadFuture;
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
		if (!TestFlags.TEST_MANUAL_PUSH) {
			// Stop any old executor first
			stop(false);
			
			log.info("Starting AuctionPushRenderer every " + INTERVAL_SECONDS + " seconds for push group '" + INTERVAL_PUSH_GROUP + "'.");
			
			renderThread = Executors.newSingleThreadScheduledExecutor();
			renderThreadFuture = renderThread.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					if (!renderThread.isShutdown()) {
						renderer.render(INTERVAL_PUSH_GROUP);
						
						// Check our items for expiry and add more as needed
						toWatch.checkAuctionExpiry();
					}
				}
			}, 0, INTERVAL_SECONDS, TimeUnit.SECONDS);
		}
	}
	
	public void stop() {
		stop(true);
	}
	
	public void stop(boolean logRequest) {
		if (logRequest) {
			log.info("Stop requested for AuctionPushRenderer " + renderThread + ".");
		}
		
		if (renderThread != null) {
			if (renderThreadFuture != null) {
				renderThreadFuture.cancel(true);
			}
			renderThread.shutdown();
			renderThread.shutdownNow();
			renderThread = null;
		}
	}
	
	public boolean isRunning() {
		if ((renderThread != null) && (renderThreadFuture != null)) {
			return !renderThread.isShutdown() && !renderThreadFuture.isDone();
		}
		return false;
	}
	
	public boolean manualPush() {
		// We only need to do an Ajax Push if we don't have our interval thread running
		// Otherwise the interval thread will basically take care of updating users
		if (!isRunning()) {
			renderer.render(MANUAL_PUSH_GROUP);
			return true;
		}
		return false;
	}
	
	@Override
	public void finalize() {
		stop(false);
	}
}
