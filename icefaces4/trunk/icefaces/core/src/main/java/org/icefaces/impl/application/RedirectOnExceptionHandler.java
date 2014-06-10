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
package org.icefaces.impl.application;

import org.icefaces.application.SessionExpiredException;
import org.icefaces.util.EnvUtils;

import javax.faces.FacesException;
import javax.faces.context.*;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RedirectOnExceptionHandler extends ExceptionHandlerWrapper {
    private ExceptionHandler handler;
    private Map<Class,String> mapping = new HashMap<Class, String>();

    public RedirectOnExceptionHandler(ExceptionHandler handler) {
        this.handler = handler;
        this.mapping = EnvUtils.getRedirectOnExceptionMapping(FacesContext.getCurrentInstance());
    }

    public ExceptionHandler getWrapped() {
        return handler;
    }

    public void handle() throws FacesException {
        FacesContext fc = FacesContext.getCurrentInstance();

        for (Iterator<ExceptionQueuedEvent> iter = getUnhandledExceptionQueuedEvents().iterator(); iter.hasNext(); ) {
            ExceptionQueuedEvent event = iter.next();
            ExceptionQueuedEventContext queueContext = (ExceptionQueuedEventContext) event.getSource();
            Throwable ex = queueContext.getException();

            //walk back to the original cause of the exception
            while (ex.getCause() != null) {
                ex = ex.getCause();
            }

            String redirectURL = mapping.get(ex.getClass());
            if (redirectURL != null) {
                PartialResponseWriter writer = fc.getPartialViewContext().getPartialResponseWriter();
                String uri = fc.getApplication().getViewHandler().getResourceURL(fc, redirectURL);
                try {
                    writer.startDocument();
                    writer.redirect(uri);
                    writer.endDocument();
                    fc.responseComplete();
                    return;
                } catch (IOException e) {
                    throw new FacesException(e);
                }
            }
        }

        handler.handle();
    }

    public static class Factory extends ExceptionHandlerFactory {
        private ExceptionHandlerFactory parent;

        public Factory(ExceptionHandlerFactory parent) {
            this.parent = parent;
        }

        public ExceptionHandler getExceptionHandler() {
            return new RedirectOnExceptionHandler(parent.getExceptionHandler());
        }
    }
}
