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

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushMessage;
import org.icefaces.application.PushRenderer;
import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.util.ClientDescriptor;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The NotificationBean is responsible for sending a notification message
 * as well as testing the park capabilities of the bridge.  That is to say
 * when a mobile device goes to sleep or the container is paused the ICEfaces
 * bridge must disconnect and reconnect when the device or application become
 * active again.
 * <p/>
 * Once a message subject and contents have be created it can be send via
 * a normal push or a priority push.  After the specified delay the message will
 * be send.  If a normal push the current session will be updated and the message
 * echo values will be updated.  If a priority push is selected the user
 * will receive the screen update as before, if the container was parked then
 * user may receive a system notification or email depending on how the container
 * and server where configured.
 */
@ManagedBean(name = CloudPushBean.BEAN_NAME)
@SessionScoped
public class CloudPushBean implements Serializable {

    private transient final static Logger log =
            Logger.getLogger(CloudPushBean.class.getName());

    public static final String BEAN_NAME = "cloudPushBean";

    @ManagedProperty(value = "#{scheduledPushExecutor}")
    private ScheduledPushExecutor scheduledPushExecutor;

    // render group is the current session id.
    private String renderGroup;

    // Notification message values.
    private String subject = "ICEmobile Notification";
    private String message = "This is a  message from  ICEmobile.";
    private int notificationDelay = 5;

    // echo strings that are only set when the push is executed in the future.
    private String echoedSubject;
    private String echoedMessage;
    private String email = "";


    public CloudPushBean() {

    }

    /**
     * Sends a normal push message to the calling client.  When called the echo
     * values for subject and message are cleared and a new push event is queued
     * for later execution specified by the value notificationDelay.  When the push
     * is executed the new message and subject values are copied int into the
     * echo fields.
     *
     * @param event jsf action event
     */
    public void sendNormalPushMessage(ActionEvent event) {
        clearPreviousPushMessage();
        final PortableRenderer portable = PushRenderer.getPortableRenderer();
        scheduledPushExecutor.schedule(new Runnable() {
            public void run() {
                try {
                    assignPreviousPushMessage();
                    portable.render(renderGroup);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, notificationDelay, TimeUnit.SECONDS);
    }

    /**
     * Sends a priority push message to the calling client.  When called the echo
     * values for subject and message are cleared and a new push event is queued
     * for later execution specified by the value notificationDelay.  When the push
     * is executed the new message and subject values are copied int into the
     * echo fields.
     *
     * @param event jsf action event
     */
    public void sendPriorityPushMessage(ActionEvent event) {
        final PushMessage myMessage = new PushMessage(subject, message);
        clearPreviousPushMessage();
        final PortableRenderer portable = PushRenderer.getPortableRenderer();
        scheduledPushExecutor.schedule(new Runnable() {
            public void run() {
                try {
                    assignPreviousPushMessage();
                    portable.render(renderGroup, myMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, notificationDelay, TimeUnit.SECONDS);

    }

    /**
     * Utility to clear the echoed subject and message output fields.
     */
    private void clearPreviousPushMessage() {
        echoedSubject = "";
        echoedMessage = "";
    }

    /**
     * Utility to copy the users specified subject and message to the respective
     * echoed variables.
     */
    private void assignPreviousPushMessage() {
        echoedSubject = subject;
        echoedMessage = message;
    }

    public String getEchoedSubject() {
        return echoedSubject;
    }

    public String getEchoedMessage() {

        HttpSession session = FacesUtils.getHttpSession(false);
        if (session != null){
            renderGroup = session.getId();
            PushRenderer.addCurrentSession(renderGroup);
        }

        return echoedMessage;
    }

    public int getNotificationDelay() {
        return notificationDelay;
    }

    public void setNotificationDelay(int notificationDelay) {
        this.notificationDelay = notificationDelay;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets the application scoped bean scheduledPushExecutor via Bean managed
     * properties.
     *
     * @param scheduledPushExecutor application scoped scheduledPushExecutor Bean
     *                              instance.
     */
    public void setScheduledPushExecutor(ScheduledPushExecutor scheduledPushExecutor) {
        this.scheduledPushExecutor = scheduledPushExecutor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAcquireEmail() {
        ClientDescriptor clientDescriptor = ClientDescriptor.getInstance((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
        return clientDescriptor.isDesktopBrowser();
    }
}
