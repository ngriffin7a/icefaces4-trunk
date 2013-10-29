package org.icefaces.impl.push.servlet;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProxyServletContext implements ServletContext {

    private static final Logger log = Logger.getLogger(ProxyServletContext.class.getName());

    private FacesContext facesContext;
    private ExternalContext externalContext;

    public ProxyServletContext(FacesContext facesContext) {
        this.facesContext = facesContext;
        externalContext = facesContext.getExternalContext();
    }
    public String getContextPath() {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public ServletContext getContext(String s) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public int getMajorVersion() {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return -1;
    }

    public int getMinorVersion() {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return -1;
    }

    public int getEffectiveMajorVersion() {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return -1;
    }

    public int getEffectiveMinorVersion() {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return -1;
    }

    public String getMimeType(String s) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public Set<String> getResourcePaths(String s) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public URL getResource(String s) throws MalformedURLException {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public InputStream getResourceAsStream(String s) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String s) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public RequestDispatcher getNamedDispatcher(String s) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public Servlet getServlet(String s) throws ServletException {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public Enumeration<Servlet> getServlets() {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public Enumeration<String> getServletNames() {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public void log(String s) {
        externalContext.log(s);
    }

    public void log(Exception e, String s) {
        externalContext.log(s,e);
    }

    public void log(String s, Throwable throwable) {
        externalContext.log(s, throwable);
    }

    public String getRealPath(String s) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public String getServerInfo() {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public String getInitParameter(String s) {
        return externalContext.getInitParameter(s);
    }

    public Enumeration<String> getInitParameterNames() {
        Set keySet = externalContext.getInitParameterMap().keySet();
        return Collections.enumeration(keySet);
    }

    public boolean setInitParameter(String s, String s2) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return false;
    }

    public Object getAttribute(String s) {
        Class[] paramTypes = {String.class};
        Object[] params = {s};
        Object result = getMethodAndInvoke(externalContext.getContext(),"getAttribute", paramTypes, params);
        log.log(Level.FINE, "result: " + result);
        return result;
    }

    public Enumeration<String> getAttributeNames() {
        Class[] paramTypes = new Class[0];
        Object result = getMethodAndInvoke(externalContext.getContext(),"getAttributeNames", paramTypes, null);
        log.log(Level.FINE, "result: " + result);
        return (Enumeration)result;
    }

    public void setAttribute(String s, Object o) {
        Class[] paramTypes = {String.class, Object.class};
        Object[] params = {s,o};
        Object result = getMethodAndInvoke(externalContext.getContext(),"setAttribute", paramTypes, params);
        log.log(Level.FINE, "result: " + result);
    }

    public void removeAttribute(String s) {
        Class[] paramTypes = {String.class};
        Object[] params = {s};
        Object result = getMethodAndInvoke(externalContext.getContext(),"removeAttribute", paramTypes, params);
        log.log(Level.FINE, "result: " + result);
    }

    public String getServletContextName() {
        return externalContext.getContextName();
    }

    public ServletRegistration.Dynamic addServlet(String s, String s2) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public ServletRegistration.Dynamic addServlet(String s, Servlet servlet) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public ServletRegistration.Dynamic addServlet(String s, Class<? extends Servlet> aClass) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public <T extends Servlet> T createServlet(Class<T> tClass) throws ServletException {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public ServletRegistration getServletRegistration(String s) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public FilterRegistration.Dynamic addFilter(String s, String s2) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public FilterRegistration.Dynamic addFilter(String s, Filter filter) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public FilterRegistration.Dynamic addFilter(String s, Class<? extends Filter> aClass) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public <T extends Filter> T createFilter(Class<T> tClass) throws ServletException {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public FilterRegistration getFilterRegistration(String s) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public SessionCookieConfig getSessionCookieConfig() {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
    }

    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public void addListener(String s) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
    }

    public <T extends EventListener> void addListener(T t) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
    }

    public void addListener(Class<? extends EventListener> aClass) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
    }

    public <T extends EventListener> T createListener(Class<T> tClass) throws ServletException {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public JspConfigDescriptor getJspConfigDescriptor() {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public ClassLoader getClassLoader() {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public void declareRoles(String... strings) {
        log.severe("ProxyServletContext unsupported operation");
        if (true) throw new UnsupportedOperationException();
    }

    public static Object getMethodAndInvoke(Object obj, String method, Class[] types, Object[] params){
        try {
            Method meth = obj.getClass().getMethod(method, types);
            Object result = null;
            if( params == null || params.length == 0 ){
                result = meth.invoke(obj);
            } else {
                result = meth.invoke(obj, params);
            }
            return result;
        } catch (Exception e) {
            log.log(Level.FINE, "problem getting " + method + " from " + obj, e);
        }
        return null;
    }

}
