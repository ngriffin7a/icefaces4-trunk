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
import org.icepush.PushContext;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionExpiredListener implements HttpSessionListener {

    private static Logger LOGGER = Logger.getLogger(SessionExpiredListener.class.getName());

    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        WindowScopeManager.sessionCreated(httpSessionEvent.getSession());
    }

    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

        FacesContext fc = FacesContext.getCurrentInstance();

        //If there is no FacesContext, then the session likely timed out of it's own accord rather
        //then being invalidated programmatically as part of a JSF lifecycle.  In that case,
        //we can't put an exception into the queue.  There are also times when the FacesContext is not null
        //but it's also not in a state where in can be used to publish an event (typically see this in
        //portals).  In these cases, we simply catch the exception and log a warning.
        if (fc != null) {
            try {
                Application app = fc.getApplication();
                if (app == null) {
                    ApplicationFactory factory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
                    app = factory.getApplication();
                }

                ExceptionQueuedEventContext ctxt =
                        new ExceptionQueuedEventContext(fc, new SessionExpiredException("Session has expired"));
                app.publishEvent(fc, ExceptionQueuedEvent.class, ctxt);
            } catch (Exception e) {
                LOGGER.log( Level.WARNING, "could not publish SessionExpiredException: " + e.getMessage());
            }
        }

        HttpSession session = httpSessionEvent.getSession();

        try {
            // Not everything might be available to us anymore from the session, causing a possible exception.
            WindowScopeManager.disposeWindows(session);
        } catch (Exception exception) {
            LOGGER.log(
                Level.WARNING,
                "An exception occurred while trying to invoke @PreDestroy on window scoped beans: " +
                    exception.getMessage());
        }
        //If the session is destroyed and ICEpush is available, we can request a push request immediately
        //which should result in a SessionExpiredException being sent to the client.
        if (EnvUtils.isICEpushPresent()) {
            ServletContext servletContext = session.getServletContext();
            PushContext pushContext = PushContext.getInstance(servletContext);
            if( pushContext != null ){
                pushContext.push(session.getId());
            }
        }
    }

}
