package org.icefaces.demo.auction.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.icefaces.demo.auction.push.AuctionWatcher;

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
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
	}
}
