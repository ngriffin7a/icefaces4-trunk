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

import org.icefaces.util.EnvUtils;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Map;

public class ExternalServletContextSetup extends ViewHandlerWrapper {
    private ViewHandler handler;
    private String publicContextPath;

    public ExternalServletContextSetup(ViewHandler handler) {
        this.handler = handler;

        publicContextPath = EnvUtils.getPublicContextPath(FacesContext.getCurrentInstance());
        if (publicContextPath != null) {
            //normalize context path
            if (!publicContextPath.startsWith("/")) {
                publicContextPath = "/" + publicContextPath;
            }
            if (publicContextPath.endsWith("/")) {
                publicContextPath = publicContextPath.substring(0, publicContextPath.length() - 1);
            }
        }
    }

    public ViewHandler getWrapped() {
        return handler;
    }

    public String getActionURL(FacesContext context, String viewId) {
        return convertURL(context, super.getActionURL(context, viewId));
    }

    public String getRedirectURL(FacesContext context, String viewId, Map<String, List<String>> parameters, boolean includeViewParams) {
        return convertURL(context, super.getRedirectURL(context, viewId, parameters, includeViewParams));
    }

    public String getResourceURL(FacesContext context, String path) {
        return convertURL(context, super.getResourceURL(context, path));
    }

    public String getBookmarkableURL(FacesContext context, String viewId, Map<String, List<String>> parameters, boolean includeViewParams) {
        return convertURL(context, super.getBookmarkableURL(context, viewId, parameters, includeViewParams));
    }

    private String convertURL(FacesContext context, String path) {
        if (publicContextPath == null || path.contains("://")) {
            return path;
        } else {
            String localContextPath = context.getExternalContext().getRequestContextPath();
            if (path.startsWith(localContextPath)) {
                return publicContextPath + path.substring(localContextPath.length(), path.length());
            } else {
                return path;
            }
        }
    }
}
