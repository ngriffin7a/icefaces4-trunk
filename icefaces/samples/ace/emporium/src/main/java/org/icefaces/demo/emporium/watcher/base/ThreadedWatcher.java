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

package org.icefaces.demo.emporium.watcher.base;

import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public abstract class ThreadedWatcher implements Serializable {
	private static final long serialVersionUID = 2694944821384436851L;

	private static final Logger log = Logger.getLogger(ThreadedWatcher.class.getName());
	
	protected ScheduledExecutorService thread;
	protected ScheduledFuture<?> threadFuture;
	
	public void start(String name, int startDelaySeconds, int intervalSeconds, Runnable toStart) {
		// Stop any old executor first
		stop(false);
		
		log.info("Starting " + name + " every " + intervalSeconds + " seconds.");
		
		thread = Executors.newSingleThreadScheduledExecutor();
		threadFuture = thread.scheduleAtFixedRate(toStart, startDelaySeconds, intervalSeconds, TimeUnit.SECONDS);
	}
	
	public void stop() {
		stop(true);
	}
	
	public void stop(boolean logRequest) {
		if (logRequest) {
			log.info("Stop requested for " + thread + ".");
		}
		
		if (thread != null) {
			if (threadFuture != null) {
				threadFuture.cancel(true);
			}
			thread.shutdown();
			thread.shutdownNow();
			thread = null;
		}
	}
	
	@Override
	public void finalize() {
		stop(false);
	}
}
