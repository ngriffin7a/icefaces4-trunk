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

package org.icefaces.impl.push.servlet;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ICE-7190
 *
 * When we use the ExternalContext.getSessionMap() method, the PortletFaces Bridge assumes
 * that the map is a set of PORTLET-scoped attributes.  However, in the ICEfaces core, there is
 * sometimes the assumption that the attributes are actually APPLICATION-scoped.  Unfortunately,
 * the JSF API does not make allowances for this.  So in those cases where we need to use a
 * session that is agnostic to the environment it's running in, we use this proxy.  For ICE-7190
 * we only need the getAttribute and setAttribute methods but the logic needs to be done via
 * reflection to avoid runtime issue in non-portlet environments.
 */
public class ProxySession implements HttpSession {

    private static Logger log = Logger.getLogger(ProxySession.class.getName());
    private FacesContext facesContext;

    public static final int APPLICATION_SCOPE = 1;
    public static final int PORTLET_SCOPE = 2;

    private int currentScope = APPLICATION_SCOPE;

    private static final Class[] getAttributeParamTypes = {String.class, Integer.TYPE};
    private static final Class[] setAttributeParamTypes = {String.class, Object.class, Integer.TYPE};
    private static final Class[] getAttributeNamesParamTypes = {Integer.TYPE};

    public ProxySession(FacesContext facesContext) {
        this(facesContext,APPLICATION_SCOPE);
    }

    public ProxySession(FacesContext facesContext, int defaultScope) {
        this.facesContext = facesContext;
        currentScope = defaultScope;
    }

    public ServletContext getServletContext() {
        log.severe("ProxySession unsupported operation");
        throw new UnsupportedOperationException();
    }

    public HttpSessionContext getSessionContext() {
        log.severe("ProxySession unsupported operation");
        throw new UnsupportedOperationException();
    }

    public Object getValue(String s) {
        log.severe("ProxySession unsupported operation");
        throw new UnsupportedOperationException();
    }

    public String[] getValueNames() {
        log.severe("ProxySession unsupported operation");
        throw new UnsupportedOperationException();
    }

    public void putValue(String s, Object o) {
        log.severe("ProxySession unsupported operation");
        throw new UnsupportedOperationException();
    }

    public void removeValue(String s) {
        log.severe("ProxySession unsupported operation");
        throw new UnsupportedOperationException();
    }

    //By default, we want to get the APPLICATION-scoped attributes rather than
    //the PORTLET-scoped attributes.
    public Object getAttribute(String key) {
        Object sess = facesContext.getExternalContext().getSession(true);
        Class clazz = sess.getClass();
        Object val = null;
        try {
            Method meth = clazz.getMethod("getAttribute", getAttributeParamTypes);
            val = meth.invoke(sess, key, currentScope);
        } catch (Exception e) {
            log.log(Level.WARNING, "could not get attribute " + key + " on PortletSession ", e);
        }
        return val;
    }

    public Enumeration<String> getAttributeNames() {
        Object sess = facesContext.getExternalContext().getSession(true);
        Class clazz = sess.getClass();
        Enumeration<String> val = null;
        try {
            Method meth = clazz.getMethod("getAttributeNames", getAttributeNamesParamTypes);
            val = (Enumeration<String>)meth.invoke(sess, currentScope);
        } catch (Exception e) {
            log.log(Level.WARNING, "could not get attribute names from PortletSession ", e);
        }
        return val;
    }

    public Map getAttributesMap(){
        HashMap attributeMap = new HashMap();
        Enumeration<String> names = getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            attributeMap.put(name,getAttribute(name));
        }
        return attributeMap;
    }

    public long getCreationTime() {
        Object sess = facesContext.getExternalContext().getSession(true);
        Class clazz = sess.getClass();
        long val = -1;
        try {
            Method meth = clazz.getMethod("getCreationTime");
            val = (Long)meth.invoke(sess);
        } catch (Exception e) {
            log.log(Level.WARNING, "could not get creation time from PortletSession ", e);
        }
        return val;
    }

    public String getId() {
        Object sess = facesContext.getExternalContext().getSession(true);
        Class clazz = sess.getClass();
        String val = null;
        try {
            Method meth = clazz.getMethod("getId");
            val = (String)meth.invoke(sess);
        } catch (Exception e) {
            log.log(Level.WARNING, "could not get id from PortletSession ", e);
        }
        return val;
    }

    public long getLastAccessedTime() {
        Object sess = facesContext.getExternalContext().getSession(true);
        Class clazz = sess.getClass();
        long val = -1;
        try {
            Method meth = clazz.getMethod("getLastAccessedTime");
            val = (Long)meth.invoke(sess);
        } catch (Exception e) {
            log.log(Level.WARNING, "could not get last accessed time from PortletSession ", e);
        }
        return val;
    }

    public int getMaxInactiveInterval() {
        Object sess = facesContext.getExternalContext().getSession(true);
        Class clazz = sess.getClass();
        int val = -1;
        try {
            Method meth = clazz.getMethod("getMaxInactiveInterval");
            val = (Integer)meth.invoke(sess);
        } catch (Exception e) {
            log.log(Level.WARNING, "could not get maximum inactive interval from PortletSession ", e);
        }
        return val;
    }

    public void invalidate() {
        log.severe("ProxySession unsupported operation");
        throw new UnsupportedOperationException();
    }

    public boolean isNew() {
        Object sess = facesContext.getExternalContext().getSession(true);
        Class clazz = sess.getClass();
        boolean val = false;
        try {
            Method meth = clazz.getMethod("isNew");
            val = (Boolean)meth.invoke(sess);
        } catch (Exception e) {
            log.log(Level.WARNING, "could not get isNew from PortletSession ", e);
        }
        return val;
    }

    public void removeAttribute(String s) {
        log.severe("ProxySession unsupported operation");
        throw new UnsupportedOperationException();
    }

    public void setAttribute(String key, Object val) {
        Object sess = facesContext.getExternalContext().getSession(true);
        Class clazz = sess.getClass();
        try {
            Method meth = clazz.getMethod("setAttribute", setAttributeParamTypes);
            meth.invoke(sess, key, val, APPLICATION_SCOPE);
        } catch (Exception e) {
            log.log(Level.WARNING, "could not set attribute " + key + ", " + val + " on PortletSession ", e);
        }
    }

    public void setMaxInactiveInterval(int i) {
        log.severe("ProxySession unsupported operation");
        throw new UnsupportedOperationException();
    }
}
