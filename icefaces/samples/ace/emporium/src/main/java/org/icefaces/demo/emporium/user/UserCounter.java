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

package org.icefaces.demo.emporium.user;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.demo.emporium.util.TimestampUtil;

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
