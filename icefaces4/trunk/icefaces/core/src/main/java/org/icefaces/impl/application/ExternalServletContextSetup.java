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
