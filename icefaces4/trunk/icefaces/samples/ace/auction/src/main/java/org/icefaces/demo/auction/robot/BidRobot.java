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

package org.icefaces.demo.auction.robot;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.demo.auction.model.AuctionItem;
import org.icefaces.demo.auction.service.AuctionItemGenerator;
import org.icefaces.demo.auction.service.AuctionService;
import org.icefaces.demo.auction.test.TestFlags;
import org.icefaces.demo.auction.util.FacesUtils;

/**
 * Class used to sometimes spontaneously generate bids to simulate some activity for a user
 */
@ManagedBean(name=BidRobot.BEAN_NAME)
@CustomScoped(value="#{window}")
public class BidRobot implements Serializable {
	public static final String BEAN_NAME = "bidRobot";
	
	private Random random = new SecureRandom();
	private Thread bidThread;
	private boolean active = (random.nextInt(10) != 0); // Have a small 10% chance to not even bid
	private int maxBids;
	private long waitTimeMillis;
	
	@PostConstruct
	public void initRobot() {
		if (TestFlags.TEST_BIDROBOT) {
			active = true;
		}
		
		if (active) {
			final AuctionService service = (AuctionService)FacesUtils.getManagedBean(AuctionService.BEAN_NAME);
			
			// Set some parameters of how the robot will behave
			if (!TestFlags.TEST_BIDROBOT) {
				maxBids = 5+random.nextInt(20);
			}
			else {
				maxBids = 100;
			}
			
			bidThread = new Thread(new Runnable() {
				@Override
				public void run() {
					int bidCount = 0;
					while ((bidCount < maxBids) && (active)) {
						if (!TestFlags.TEST_BIDROBOT) {
							waitTimeMillis = 1000 * (30+random.nextInt(60));
						}
						else {
							waitTimeMillis = 1000;
						}
						
						try {
							// Have the first bid come in quickly, to show activity right away
							if (bidCount == 0) {
								Thread.sleep(waitTimeMillis / 10);
							}
							else {
								Thread.sleep(waitTimeMillis);
							}
						}catch (InterruptedException ignored) {
							if (!active) {
								return;
							}
						}
						
						// Break our loop if we're not active anymore after our sleep
						if (!active) {
							return;
						}
						
						// Bid on a random item
						List<AuctionItem> auctions = service.getAuctions();
						if ((auctions != null) && (!auctions.isEmpty())) {
							AuctionItem toBid = auctions.get(random.nextInt(auctions.size()));
							
							// Obviously we only want to bid on a valid non-expired item
							if (!toBid.isExpired()) {
								// Place a randomly generated bid from our AuctionItemGenerator
								service.placeBid(toBid, AuctionItemGenerator.makeBid(toBid));
								bidCount++;
							}
						}
					}
				}
			});
			bidThread.start();
		}
	}
	
	@PreDestroy
	public void destroyRobot() {
		if (bidThread != null) {
			active = false;
			bidThread.interrupt();
			bidThread = null;
		}
	}
}
