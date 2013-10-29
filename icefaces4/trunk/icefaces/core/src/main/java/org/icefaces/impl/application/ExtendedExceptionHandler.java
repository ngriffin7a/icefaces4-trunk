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

package org.icefaces.impl.application;

import org.icefaces.application.SessionExpiredException;
import org.icefaces.util.EnvUtils;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.ProjectStage;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExtendedExceptionHandler extends ExceptionHandlerWrapper {

    private final static Logger log = Logger.getLogger(ExtendedExceptionHandler.class.getName());

    private ExceptionHandler wrapped;

    public ExtendedExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public boolean isListenerForSource(Object o) {
        return super.isListenerForSource(o);
    }

    /**
     * Logic in here is based on Ed Burn's example at
     * http://weblogs.java.net/blog/edburns/archive/2009/09/03/dealing-gracefully-viewexpiredexception-jsf2
     * <p/>
     * The basic premise is that when sessions expire, the next request from the client will trigger a
     * ViewExpiredException and we'd rather see a SessionExpiredException.  So under specific circumstances,
     * we handle ViewExpiredExceptions by removing them and pushing a custom SessionExpiredException into the
     * queue.
     *
     * @throws FacesException
     */
    @Override
    public void handle() throws FacesException {
        boolean sessionExpired = false;
        for (Iterator<ExceptionQueuedEvent> iter = getUnhandledExceptionQueuedEvents().iterator(); iter.hasNext(); ) {
            ExceptionQueuedEvent event = iter.next();
            ExceptionQueuedEventContext queueContext = (ExceptionQueuedEventContext) event.getSource();
            Throwable ex = queueContext.getException();

            FacesContext fc = FacesContext.getCurrentInstance();
            if (fc.isProjectStage(ProjectStage.Development)) {
                log.log(Level.WARNING,"queued exception", ex);
            }

            if ((ex instanceof ViewExpiredException) && PhaseId.RESTORE_VIEW.equals(queueContext.getPhaseId())) {

                ViewExpiredException vex = (ViewExpiredException)ex;
                String viewId = vex.getViewId();

                ExternalContext ec = fc.getExternalContext();
                Object sessObj = ec.getSession(false);

                boolean isPortalEnvironment = EnvUtils.instanceofPortletSession(sessObj);

                if (sessObj != null || !isPortalEnvironment) {
                    if (!isValidSession(fc,viewId) || isPortalEnvironment) {
                        //Remove the ViewExpiredException and set the sessionExpired flag so that we know
                        //to add the SessionExpiredException which is done late to avoid ConcurrentModificationException.
                        iter.remove();
                        sessionExpired = true;
                        break;
                    }
                }
            }
        }

        //Do the processing outside of the iterator to avoid ConcurrentModificationException
        if (sessionExpired) {
            FacesContext fc = FacesContext.getCurrentInstance();
            Application app = fc.getApplication();
            if (app == null) {
                ApplicationFactory factory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
                app = factory.getApplication();
            }

            ExceptionQueuedEventContext ctxt =
                    new ExceptionQueuedEventContext(fc, new SessionExpiredException("session expired (causing view to expire)"));
            app.publishEvent(fc, ExceptionQueuedEvent.class, ctxt);
        }

        //Once we've removed the ViewExpiredException and added the SessionExpiredException, we can
        //let default processing take over again.
        getWrapped().handle();
    }

    private boolean isValidSession(FacesContext fc, String viewId) {

        //If the session is available but the getters on the session throw IllegalStateExceptions,
        //then the session is considered invalid.  If the session is not new and the calculation
        //passes, then the session is considered valid.
        boolean validSession = false;
        HttpSession httpSession = EnvUtils.getSafeSession(fc,false);

        try {

            boolean newSession = httpSession.isNew();
            long lastAccessed = httpSession.getLastAccessedTime();
            int maxInactive = httpSession.getMaxInactiveInterval();
            long now = System.currentTimeMillis();

            if (!newSession && (now - lastAccessed) <= (maxInactive * 1000)) {
                validSession = true;
            }

            log.log(Level.FINE, "checking for session validity: " +
                    "\n  isNew         : " + newSession +
                    "\n  now           : " + now +
                    "\n  last accessed : " + lastAccessed +
                    "\n  max inactive  : " + (maxInactive * 1000) +
                    "\n  time is fine  : " + ((now - lastAccessed) <= (maxInactive * 1000)) +
                    "\n  valid         : " + validSession
            );

        } catch (IllegalStateException ignored) {
            //An IllegalStateException here simply means the session is invalid
        }
        return validSession;
    }
}
