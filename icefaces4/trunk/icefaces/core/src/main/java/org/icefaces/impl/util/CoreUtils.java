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

package org.icefaces.impl.util;

import org.icefaces.impl.context.DOMPartialViewContext;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import java.util.logging.Level;

public class CoreUtils {
    private static Logger log = Logger.getLogger(CoreUtils.class.getName());
	private static Boolean portletEnvironment;
    
    public static String getRealPath(FacesContext facesContext, String path) {
        if (path == null) {
            path = "";
        }
        Object session = facesContext.getExternalContext().getSession(false);
        if (session == null) {
            log.log(Level.SEVERE, "getRealPath() session is null", new NullPointerException());
            return null;
        }
        if (isPortletEnvironment()) {
            return getRealPath(session, "getPortletContext", path);
        } else {
            return getRealPath(session, "getServletContext", path);
        }
    }
    
    private static String getRealPath(Object session, String getContext, String path) {
        try {
            Method getContextMethod = session.getClass().getMethod(getContext,  new Class[0]);
            Object context;
            context = getContextMethod.invoke(session, new Object[0]);
            Class[] classargs = {String.class};
            Method getRealPath =  context.getClass().getMethod("getRealPath", classargs);
            Object[] args = {path};
            return String.valueOf(getRealPath.invoke(context, args)); 
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error getting realpath", e);
            return null;
        }        
    }
    
    public static String getSessionId(FacesContext facesContext) {
        try {
            Object session = facesContext.getExternalContext().getSession(false);
            if (session == null) {
                log.log(Level.SEVERE, "getSessionId() session is null", new NullPointerException());
                return null;
            }
            Method getIdMethod = session.getClass().getMethod("getId",  new Class[0]);
            return String.valueOf(getIdMethod.invoke(session, new Object[0])); 
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error getting session id", e);
            return null;
        }        
    }
    
    public static boolean isPortletEnvironment() {
    	if (portletEnvironment == null) {
		    try {
                Class portletRequestClass = Class.forName("javax.portlet.PortletRequest");
		    	portletEnvironment = new Boolean(portletRequestClass.isInstance(FacesContext.getCurrentInstance().getExternalContext()
		    			.getRequest()));
		    } catch (Throwable e) {
                //both ClassNotFoundException and NoClassDefError
		    	//portlet not found
		    	portletEnvironment = Boolean.FALSE;
			}
    	}
    	return portletEnvironment.booleanValue();
    }

    public static void enableOnElementUpdateNotify(ResponseWriter writer, String id) throws IOException {
        writer.writeAttribute(DOMPartialViewContext.DATA_ELEMENTUPDATE, id, null);
    }
}
