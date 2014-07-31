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

package org.icefaces.samples.showcase.example.mobi.cloudpush;

import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The ScheduledPushExecutor executor is a utility to schedule a push notification
 * for future execution.
 */
@ManagedBean(name = ScheduledPushExecutor.BEAN_NAME)
@ApplicationScoped
public class ScheduledPushExecutor implements Serializable {

    private transient final static Logger log =
            Logger.getLogger(ScheduledPushExecutor.class.getName());

    public static final String BEAN_NAME = "scheduledPushExecutor";

    private transient ScheduledThreadPoolExecutor timerThreadPool;

    public void schedule(Runnable runner, long delay, TimeUnit unit) {
        getExecutor().schedule(runner, delay, unit);
    }
    
    private ScheduledThreadPoolExecutor getExecutor(){
        if( timerThreadPool == null ){
            timerThreadPool  = new ScheduledThreadPoolExecutor(10);
        }
        return timerThreadPool;
    }

    @PreDestroy
    public void shutdown() {
        timerThreadPool.purge();
        timerThreadPool.shutdownNow();
    }

}
