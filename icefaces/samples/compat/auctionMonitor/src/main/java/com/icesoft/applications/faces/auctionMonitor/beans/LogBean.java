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

package com.icesoft.applications.faces.auctionMonitor.beans;

import com.icesoft.applications.faces.auctionMonitor.ChatState;
import com.icesoft.applications.faces.auctionMonitor.MessageLog;
import com.icesoft.applications.faces.auctionMonitor.stubs.StubServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PreDestroy;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Singleton bean that should be in the application scope as a holder for the
 * message log The LogBean is not a true singleton class as the constructor is
 * still public This is necessary as the bean is created from Tomcat and needs a
 * constructor
 */
public class LogBean {
    public static final String LOG_PATH =
            "com.icesoft.applications.faces.auctionMonitor.messageLog";
    private static final int CONTROL_LOG_CAP = 20;
    private static final Runtime core = Runtime.getRuntime();
    private static Log log = LogFactory.getLog(LogBean.class);
    private static LogBean singleton;
    private ChatState state = ChatState.getInstance();
    private ExternalContext externalContext;
    private ArrayList controlLog = new ArrayList(0);
    private MessageLog messageLog = new MessageLog(0);
    private String autoLoad = " ";
    private Timer nightlyReset;

    private static final int TIME_DAYS = 24 * 60 * 60 * 1000;

    public LogBean() {
        externalContext =
                FacesContext.getCurrentInstance().getExternalContext();
        // This was commented out in 2007 due to ICE-1613, but that bug is less valid three years later
        timedReset();
    }

    public long getFreeMemory() {
        return (core.freeMemory());
    }

    public long getTotalMemory() {
        return (core.totalMemory());
    }

    public long getMaxMemory() {
        return (core.maxMemory());
    }

    public ArrayList getControlLog() {
        return (controlLog);
    }

    public MessageLog getMessageLog() {
        return (storeMessageLog());
    }

    public String getAutoLoad() {
        getInstance();
        if (this.autoLoad.equals(" ")) {
            this.autoLoad = "LogBean-Loaded";
        }
        return (this.autoLoad);
    }

    public String getTimeStampState() {
        if (state.getTimeStampEnabled()) {
            return ("Disable");
        }

        return ("Enable");
    }

    public String getTimeStampMessage() {
        if (state.getTimeStampEnabled()) {
            return ("enabled");
        }

        return ("disabled");
    }

    private MessageLog getContextMessageLog() {
        // Get and check the applicationMap from the context
        Map applicationMap = externalContext.getApplicationMap();
        if (applicationMap == null) {
            return (new MessageLog(0));
        }

        // Get and check the message log from the applicationMap
        messageLog = (MessageLog) applicationMap.get(LogBean.LOG_PATH);
        if (messageLog == null) {
            messageLog = new MessageLog(0);
        }

        return (messageLog);
    }

    /**
     * Method to return a singleton instance of this class
     *
     * @return this LogBean object
     */
    public static synchronized LogBean getInstance() {
        if (singleton == null) {
            singleton = new LogBean();
        }
        return (singleton);
    }

    /**
     * Method to silently reset the auction every night at midnight
     */
    private void timedReset() {
        // Generate the date for tomorrow at midnight
        Calendar tomorrow = new GregorianCalendar();
        tomorrow.add(Calendar.DATE, 1);
        Date midnight = new GregorianCalendar(tomorrow.get(Calendar.YEAR),
                                              tomorrow.get(Calendar.MONTH),
                                              tomorrow.get(Calendar.DATE)).getTime();

        
        // Setup the timer task object, or cancel existing tasks on the current object 
        if (nightlyReset == null) {
            nightlyReset = new Timer("Nightly Auction Reset", true);
        }
        else {
            nightlyReset.cancel();
            nightlyReset.purge();
        }
        
        // Schedule a task to reset at midnight, then every 24 hours past that
        nightlyReset.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                log.info("Nightly reset of auction items.");
                resetAuction(true);
            }
        }, midnight, TIME_DAYS);
    }


    /**
     * Method to update and return the message log stored in the faces context
     *
     * @return MessageLog this message log, to save having to go through the
     *         context
     */
    public MessageLog storeMessageLog() {
        externalContext.getApplicationMap().put(LOG_PATH, messageLog);
        return (messageLog);
    }


    /**
     * Action method wrapper to be called from the page
     *
     * @return String "resetAuction" to be used by JSF action if needed
     */
    public String resetAuction() {
        return resetAuction(false);
    }

    /**
     * Method to clear outstanding bids and restore them to their initial state
     *
     * @param silent true if no notification should be given
     * @return String "resetAuction" to be used by JSF action if needed
     */
    public String resetAuction(boolean silent) {
        // Get the latest message log
        getContextMessageLog();

        // Call the reset method on the stub server
        StubServer.resetAuction();
        appendControl("Auction has been reset");

        // Notify of the bid reset, store the updated log, and force a reRender
        if (!silent) {
            messageLog.addMessage("Notice",
                                  "Auction has been reset by the administrator.",
                                  null);
        }
        storeMessageLog();
        state.updateAll();

        return ("resetAuction");
    }

    /**
     * Convenience method to clear all items from the log
     *
     * @return String "clearMessageLog" to be used by JSF action if needed
     */
    public String clearMessageLog() {
        // Get the latest message log
        getContextMessageLog();

        // Add an admin message then clear the log
        appendControl("Message log (size " + messageLog.size() + ") cleared");
        messageLog = (MessageLog) clear(messageLog);

        // Notify of clearing the message log, store the updated log, and force a reRender
        messageLog.addMessage("Notice",
                              "Message log was cleared by the administrator.",
                              null);
        storeMessageLog();
        state.updateAll();

        return ("clearMessageLog");
    }

    /**
     * Method to dump the latest message log to the terminal
     *
     * @return String "printMessageLog" to be used by JSF action if needed
     */
    public String printMessageLog() {
        // Get the latest message log
        getContextMessageLog();

        if (log.isInfoEnabled()) {
            log.info("Message log dump started");
        }
        int size = messageLog.size();
        for (int i = 0; i < size; i++) {
            if (log.isInfoEnabled()) {
                log.info("> " + messageLog.getMessageAt(i));
            }
        }
        if (log.isInfoEnabled()) {
            log.info("Message log dump end");
        }
        appendControl("Message log (size " + size + ") printed");

        return ("printMessageLog");
    }

    /**
     * Method to toggle timestamps to on or off
     *
     * @return String "toggleTimeStamp" to be used by JSF action if needed
     */
    public String toggleTimeStamp() {
        // Toggle timestamps and notify administrator
        state.toggleTimeStamp();
        appendControl("Timestamps set to " + state.getTimeStampEnabled());

        // Notify of the current timestamp state, store the updated log, and force a reRender
        messageLog.addMessage("Notice", "Timestamps have been " +
                                        getTimeStampMessage() +
                                        " by the administrator.", null);
        storeMessageLog();
        state.updateAll();

        return ("toggleTimeStamp");
    }

    /**
     * Convenience method to pad the passed message with the time then append it
     * to the control log
     *
     * @param controlMessage message to append
     */
    private void appendControl(String controlMessage) {
        if (controlLog.size() > CONTROL_LOG_CAP) {
            controlLog = clear(controlLog);
            appendControl("Control log cleared (max size of " +
                          CONTROL_LOG_CAP + ")");
        }

        controlLog.add((new Time(System.currentTimeMillis()) + ": " +
                        controlMessage));
    }

    /**
     * Convenience method to clear a passed ArrayList Basically clones the
     * functionality of a Vector.removeAll
     *
     * @param toClear to remove all elements from
     * @return ArrayList cleared (size = 0)
     */
    private ArrayList clear(ArrayList toClear) {
        // Loop through the ArrayList and remove each element
        for (int i = 0, len = toClear.size(); i < len; i++) {
            toClear.remove(0);
        }

        return (toClear);
    }
    
    @PreDestroy
    public void dispose() {
        if (nightlyReset != null) {
            nightlyReset.cancel();
            nightlyReset.purge();
            nightlyReset = null;
        }
    }
}
