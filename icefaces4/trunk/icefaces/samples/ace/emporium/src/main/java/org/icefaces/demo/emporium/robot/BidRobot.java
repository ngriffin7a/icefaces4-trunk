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

package org.icefaces.demo.emporium.robot;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.demo.emporium.bid.AuctionService;
import org.icefaces.demo.emporium.bid.model.AuctionItem;
import org.icefaces.demo.emporium.bid.util.AuctionItemGenerator;
import org.icefaces.demo.emporium.test.TestFlags;
import org.icefaces.demo.emporium.user.UserCounter;
import org.icefaces.demo.emporium.util.FacesUtils;

/**
 * Class used to sometimes spontaneously generate bids to simulate some activity for a user
 */
@ManagedBean(name=BidRobot.BEAN_NAME)
@CustomScoped(value="#{window}")
public class BidRobot implements Serializable {
	private static final long serialVersionUID = -7678091811944698163L;
	
	public static final String BEAN_NAME = "bidRobot";
	private static final Logger log = Logger.getLogger(BidRobot.class.getName());
	
	private static final int MAX_BIDROBOTS = 50;
	
	private Random random = new SecureRandom();
	private Thread bidThread;
	private volatile boolean active = (random.nextInt(10) != 0); // Have a small 10% chance to not even bid
	private int bidCount = 0;
	private int maxBids;
	private long waitTimeMillis;
	
	@PostConstruct
	public void initBidRobot() {
		// Check our existing UserCounter, if we have too many users we don't need a BidRobot
		// This is because we don't want a ton of BidRobots just spamming the site
		UserCounter counter = (UserCounter)FacesUtils.getManagedBean(UserCounter.BEAN_NAME);
		if (counter.getCurrentSessions() >= MAX_BIDROBOTS) {
			active = false;
		}
		
		// Of course if we're intentionally trying to test the bid robots then ALWAYS activate them
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
			
			log.info("Initialize a BidRobot with " + maxBids + " bids left.");
			
			bidThread = new Thread(new Runnable() {
				@Override
				public void run() {
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
								service.placeBid(toBid, AuctionItemGenerator.generatePersonName(), AuctionItemGenerator.makeBid(toBid));
								bidCount++;
							}
						}
					}
				}
			});
			bidThread.setName("Emporium BidRobot (" + maxBids + " bids)");
			bidThread.start();
		}
	}
	
	@PreDestroy
	public void cleanupBidRobot() {
		if (active) {
			log.info("Destroying a BidRobot with " + bidCount + "/" + maxBids + " bids done.");
		}
		
		if (bidThread != null) {
			active = false;
			bidThread.interrupt();
			bidThread = null;
		}
	}
	
	@Override
	public void finalize() {
		cleanupBidRobot();
	}
	
	public String getInit() {
		return null;
	}
}
