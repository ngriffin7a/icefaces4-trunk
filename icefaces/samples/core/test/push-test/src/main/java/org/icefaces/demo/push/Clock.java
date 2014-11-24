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

package org.icefaces.demo.push;

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;
import org.icefaces.bean.WindowDisposed;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@ManagedBean(name = "Clock")
@ViewScoped
@WindowDisposed
public class Clock implements Serializable {
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss .SSS");
    private Timer timer = new Timer("push test clock");
    private long interval = 1000;
    private PortableRenderer renderer;
    private TimerTask intervalNotifier;
    private static int subCounter = 0;
    private String id;


    public Clock() {
        intervalNotifier = new IntervalNotifier();
        id = generateID();
        FacesContext.getCurrentInstance().getApplication().subscribeToEvent(PreDestroyApplicationEvent.class, new StopTimer());
    }

    @PostConstruct
    public void init() {
        renderer = PushRenderer.getPortableRenderer();
        PushRenderer.addCurrentView(id);
        timer.scheduleAtFixedRate(intervalNotifier, 0, interval);
    }

    @PreDestroy
    public void destroy() {
        intervalNotifier.cancel();
        timer.purge();
        timer.cancel();
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return formatter.format(new Date());
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
        intervalNotifier.cancel();
        timer.purge();
        intervalNotifier = new IntervalNotifier();
        timer.scheduleAtFixedRate(intervalNotifier, 0, interval);
    }

    private class IntervalNotifier extends TimerTask {
        public void run() {
            renderer.render(id);
        }
    }

    private synchronized String generateID() {
        return Integer.toString((++subCounter) + (hashCode() / 10000), 36).toUpperCase();
    }

    private class StopTimer implements SystemEventListener {
        public void processEvent(SystemEvent event) throws AbortProcessingException {
            try {
                timer.purge();
                timer.cancel();
            } catch (Throwable t) {
                //timer was already shut down
            }
        }

        public boolean isListenerForSource(Object source) {
            return true;
        }
    }
}
