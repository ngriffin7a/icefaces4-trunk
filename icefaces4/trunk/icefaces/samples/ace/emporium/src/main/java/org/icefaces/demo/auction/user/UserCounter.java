package org.icefaces.demo.auction.user;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.demo.auction.util.TimestampUtil;

@ManagedBean(name=UserCounter.BEAN_NAME,eager=true)
@ApplicationScoped
public class UserCounter implements Serializable {
	private static final Logger log = Logger.getLogger(UserCounter.class.getName());
	
	public static final String BEAN_NAME = "userCounter";
	
	private int totalSessions = 0;
	private int currentSessions = 0;
	private String startTimestamp;
	
	@PostConstruct
	private void initUserCounter() {
		startTimestamp = TimestampUtil.datestamp();
		
		log.info("Starting up user counter at " + startTimestamp + ".");
	}
	
	@PreDestroy
	private void cleanupUserCounter() {
		log.info("Shutting down user counter. There are currently "
				+ currentSessions
				+ " active visitors. The total visitor count was "
				+ totalSessions + " sessions since " + startTimestamp
				+ " (current time is " + TimestampUtil.datestamp() + ").");
	}
	
	public int countUser() {
		totalSessions++;
		currentSessions++;
		
		log.info("User Counter increased: " + totalSessions + " total, " + currentSessions + " current since " + startTimestamp);
		
		return totalSessions;
	}
	
	public int cleanupUser() {
		currentSessions--;
		
		log.info("User Counter decreased: " + totalSessions + " total, " + currentSessions + " current since " + startTimestamp);
		
		return currentSessions;
	}
}
