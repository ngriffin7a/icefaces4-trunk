/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icefaces.samples.showcase.example.compat.progress;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= LongTaskPool.BEAN_NAME)
@ApplicationScoped
public class LongTaskPool implements Serializable {
    public static final String BEAN_NAME = "longTaskPool";
    
    private ExecutorService threadPool;
    
    public ExecutorService getThreadPool() { return threadPool; }
    
    public void setThreadPool(ExecutorService threadPool) { this.threadPool = threadPool; }
    
	@PostConstruct
	private void init() {
        // Prep the thread pool
	    threadPool = Executors.newCachedThreadPool();
	}
	
	@PreDestroy
	private void deinit() {
	    // Cleanup the thread pool
	    if ((threadPool != null) &&
	        (!threadPool.isShutdown())) {
	        
	        threadPool.shutdown();
	        threadPool = null;
        }
	}
}
