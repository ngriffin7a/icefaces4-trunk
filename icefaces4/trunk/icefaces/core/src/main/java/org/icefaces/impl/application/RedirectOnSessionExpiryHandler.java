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
import javax.faces.context.*;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.io.IOException;
import java.util.Iterator;

public class RedirectOnSessionExpiryHandler extends ExceptionHandlerWrapper {
    private ExceptionHandler handler;

    public RedirectOnSessionExpiryHandler(ExceptionHandler handler) {
        this.handler = handler;
    }

    public ExceptionHandler getWrapped() {
        return handler;
    }

    @Override
    public void handle() throws FacesException {
        FacesContext fc = FacesContext.getCurrentInstance();
        String uri = EnvUtils.getSessionExpiredRedirectURI(fc);

        boolean redirect = false;
        for (Iterator<ExceptionQueuedEvent> iter = getUnhandledExceptionQueuedEvents().iterator(); iter.hasNext(); ) {
            ExceptionQueuedEvent event = iter.next();
            ExceptionQueuedEventContext queueContext = (ExceptionQueuedEventContext) event.getSource();
            Throwable ex = queueContext.getException();

            if (ex instanceof SessionExpiredException && uri != null) {
                redirect = true;
            }
        }

        if (redirect) {
            PartialResponseWriter writer = fc.getPartialViewContext().getPartialResponseWriter();
            String resolvedURI = fc.getApplication().getViewHandler().getResourceURL(fc, uri);
            try {
                writer.startDocument();
                writer.redirect(resolvedURI);
                writer.endDocument();
                fc.responseComplete();
            } catch (IOException e) {
                throw new FacesException(e);
            }
        } else {
            handler.handle();
        }
    }

    public static class Factory extends ExceptionHandlerFactory {
        private ExceptionHandlerFactory parent;

        public Factory(ExceptionHandlerFactory parent) {
            this.parent = parent;
        }

        public ExceptionHandler getExceptionHandler() {
            return new RedirectOnSessionExpiryHandler(parent.getExceptionHandler());
        }
    }
}
