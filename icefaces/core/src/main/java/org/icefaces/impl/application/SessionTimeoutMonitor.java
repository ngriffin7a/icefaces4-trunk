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

import org.icefaces.impl.push.servlet.ICEpushResourceHandler;
import org.icefaces.util.EnvUtils;

import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class SessionTimeoutMonitor extends SessionAwareResourceHandlerWrapper {
    private static final Logger Log = Logger.getLogger(SessionTimeoutMonitor.class.getName());
    private ResourceHandler handler;
    private int c = 0;

    public SessionTimeoutMonitor(ResourceHandler handler) {
        this.handler = handler;
    }

    public ResourceHandler getWrapped() {
        return handler;
    }

    public boolean isSessionAwareResourceRequest(FacesContext context) {
        if (!EnvUtils.isStrictSessionTimeout(context)) {
            return handler.isResourceRequest(context);
        }

        HttpSession httpSession = EnvUtils.getSafeSession(context);
        Long lastAccessTime = (Long) httpSession.getAttribute(SessionTimeoutMonitor.class.getName());
        boolean isPushRelatedRequest = EnvUtils.isPushRequest(context);
        ExternalContext ec = context.getExternalContext();
        Map<String, String> parameterMap = ec.getRequestParameterMap();
        String reqParam = parameterMap.get("ice.submit.type");

        if ("ice.push".equals(reqParam) && (c++ > 1 && c < 35)) {
            try {
                HttpServletResponse r = (HttpServletResponse) context.getExternalContext().getResponse();
                r.setStatus(0, "");
                r.getOutputStream().write(0);
                r.flushBuffer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (lastAccessTime == null || !isPushRelatedRequest) {
            lastAccessTime = System.currentTimeMillis();
            httpSession.setAttribute(SessionTimeoutMonitor.class.getName(), System.currentTimeMillis());
        }

        int maxInactiveInterval = httpSession.getMaxInactiveInterval();
        if (System.currentTimeMillis() - lastAccessTime > maxInactiveInterval * 1000) {
            Log.fine("invalidating session enforcing strictSessionTimeout");
            httpSession.removeAttribute(SessionTimeoutMonitor.class.getName());
            context.getExternalContext().invalidateSession();
        }

        return handler.isResourceRequest(context);
    }

    public void handleSessionAwareResourceRequest(FacesContext context) throws IOException {
        handler.handleResourceRequest(context);
    }
}
