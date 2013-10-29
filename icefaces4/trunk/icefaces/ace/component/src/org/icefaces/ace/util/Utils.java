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

package org.icefaces.ace.util;

import java.beans.Beans;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.net.URLEncoder;

import javax.el.ValueExpression;
import javax.faces.component.*;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.ace.component.animation.AnimationBehavior;
import org.icefaces.impl.util.DOMUtils;
import org.icefaces.util.JavaScriptRunner;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class Utils {
    public static void renderChildren(FacesContext facesContext,
                                      UIComponent component)
            throws IOException {
        if (component.getChildCount() > 0) {
            for (Iterator it = component.getChildren().iterator();
                 it.hasNext();) {
                UIComponent child = (UIComponent) it.next();
                renderChild(facesContext, child);
            }
        }
    }


    public static void renderChild(FacesContext facesContext, UIComponent child)
            throws IOException {
        if (!child.isRendered()) {
            return;
        }

        child.encodeBegin(facesContext);
        if (child.getRendersChildren()) {
            child.encodeChildren(facesContext);
        } else {
            renderChildren(facesContext, child);
        }
        child.encodeEnd(facesContext);
    }

    public static UIComponent findNamingContainer(UIComponent uiComponent) {
        UIComponent parent = uiComponent.getParent();
        while (parent != null) {
            if (parent instanceof NamingContainer) {
                break;
            }
            parent = parent.getParent();
        }
        return parent;
    }


    public static UIComponent findForm(UIComponent uiComponent) {
        UIComponent parent = uiComponent.getParent();
        while (parent != null && !(parent instanceof UIForm)) {
            parent = findNamingContainer(parent);
        }
        return parent;
    }


    public static UIForm findParentForm(UIComponent comp) {
        if (comp == null) {
            return null;
        }
        if (comp instanceof UIForm) {
            return (UIForm) comp;
        }
        return findParentForm(comp.getParent());
    }

    public static void decodeBehavior(FacesContext facesContext, UIComponent uiComponent) {

    }


    public static boolean iterateEffects(AnimationBehavior.Iterator iterator) {
        if (!(iterator.getUIComponent() instanceof ClientBehaviorHolder)) return false;
        for (String effect : ((ClientBehaviorHolder)iterator.getUIComponent()).getClientBehaviors().keySet()) {
            for (ClientBehavior behavior: ((ClientBehaviorHolder)iterator.getUIComponent()).getClientBehaviors().get(effect)) {
                if (behavior instanceof AnimationBehavior) {
                    iterator.next(effect, (AnimationBehavior)behavior);
                }
            }
        }
        return true;
    }

    public static void writeConcatenatedStyleClasses(ResponseWriter writer,
                                                     String componentClass, String applicationClass)
            throws IOException {
        int componentLen = (componentClass == null) ? 0 :
                           (componentClass = componentClass.trim()).length();
        int applicationLen = (applicationClass == null) ? 0 :
                             (applicationClass = applicationClass.trim()).length();
        if (componentLen > 0 && applicationLen == 0) {
            writer.writeAttribute("class", componentClass, "styleClass");
        }
        else if (componentLen == 0 && applicationLen > 0) {
            writer.writeAttribute("class", applicationClass, "styleClass");
        }
        else if (componentLen > 0 || applicationLen > 0) {
            int totalLen = componentLen + applicationLen;
            if (componentLen > 0 && applicationLen > 0) {
                totalLen++;
            }

            StringBuilder sb = new StringBuilder(totalLen);
            if (componentLen > 0) {
                sb.append(componentClass);
            }
            if (applicationLen > 0) {
                if (sb.length() > 0) {
                    sb.append(' ');
                }
                sb.append(applicationClass);
            }
            writer.writeAttribute("class", sb.toString(), "styleClass");
        }
    }

    public static void writeConcatenatedStyleClasses(ResponseWriter writer,
                                                     String componentClass, String applicationClass, boolean disabled)
            throws IOException {
        final String disabledStr = "-disabled";
        int componentLen = (componentClass == null) ? 0 :
                           (componentClass = componentClass.trim()).length();
        int applicationLen = (applicationClass == null) ? 0 :
                             (applicationClass = applicationClass.trim()).length();
        if (componentLen > 0 && applicationLen == 0) {
            if (disabled) {
                String styleClass = (componentClass + disabledStr).intern();
                writer.writeAttribute("class", styleClass, "styleClass");
            }
            else {
                writer.writeAttribute("class", componentClass, "styleClass");
            }
        }
        else if (componentLen == 0 && applicationLen > 0 && !disabled) {
            writer.writeAttribute("class", applicationClass, "styleClass");
        }
        else if (componentLen > 0 || applicationLen > 0) {
            int totalLen = componentLen + applicationLen;
            if (disabled && componentLen > 0) {
                totalLen += disabledStr.length();
            }
            if (disabled && applicationLen > 0) {
                totalLen += disabledStr.length();
            }
            if (componentLen > 0 && applicationLen > 0) {
                totalLen++;
            }

            StringBuilder sb = new StringBuilder(totalLen);
            if (componentLen > 0) {
                sb.append(componentClass);
                if (disabled) {
                    sb.append(disabledStr);
                }
            }
            if (applicationLen > 0) {
                if (sb.length() > 0) {
                    sb.append(' ');
                }
                sb.append(applicationClass);
                if (disabled) {
                    sb.append(disabledStr);
                }
            }
            writer.writeAttribute("class", sb.toString(), "styleClass");
        }
    }

    public static void writeConcatenatedStyleClasses(ResponseWriter writer,
                                                     String[] componentClasses, String applicationClass, boolean disabled)
            throws IOException {
        final String disabledStr = "-disabled";
        int componentCount = (componentClasses == null ? 0 :
                              componentClasses.length);
        StringTokenizer st = new StringTokenizer(applicationClass, " ");
        int applicationCount = st.countTokens();

        if (componentCount == 1 && applicationCount == 0) {
            if (disabled) {
                String styleClass =
                        (componentClasses[0].trim() + disabledStr).intern();
                writer.writeAttribute("class", styleClass, "styleClass");
            }
            else {
                writer.writeAttribute("class", componentClasses[0], "styleClass");
            }
        }
        else if (componentCount == 0 && applicationCount == 1 && !disabled) {
            writer.writeAttribute("class", applicationClass, "styleClass");
        }
        else if (componentCount > 0 || applicationCount > 0) {
            StringBuilder sb = new StringBuilder(
                    (componentCount + applicationCount) * 16 );
            for (int i = 0; i < componentCount; i++) {
                concatenateStyleClass(sb, componentClasses[i], disabled,
                                      disabledStr);
            }
            while (st.hasMoreTokens()) {
                concatenateStyleClass(sb, st.nextToken(), disabled,
                                      disabledStr);
            }
            sb.trimToSize();
            writer.writeAttribute("class", sb.toString(), "styleClass");
        }
    }

    private static void concatenateStyleClass(StringBuilder sb,
                                              String styleClass, boolean disabled, String disabledStr) {
        if (sb.length() > 0) {
            sb.append(' ');
        }
        sb.append(styleClass);
        if (disabled) {
            sb.append(' ');
            sb.append(styleClass);
            sb.append(disabledStr);
        }
    }

    /**
     * Capture UIParameter (f:param) children of a component
     * @param component The component to work from
     * @return List of UIParameter objects, null if no UIParameter children present
     */
    public static List<UIParameter> captureParameters( UIComponent component) {
        List<UIComponent> children = component.getChildren();
        List<UIParameter>  returnVal = null;
        for (UIComponent child: children) {
            if (child instanceof UIParameter) {
                UIParameter param = (UIParameter) child;
                if (returnVal == null) {
                    returnVal = new ArrayList<UIParameter>();
                }
                returnVal.add( param );
            }
        }
        return returnVal;
    }

    /**
     * Return the name value pairs parameters as a ANSI escaped string
     * formatted in query string parameter format.
     * TODO: determine the correct escaping here
     * @param children List of children
     * @return a String in the form name1=value1&name2=value2...
     */
    public static String asParameterString ( List<UIParameter> children) {
        StringBuffer builder = new StringBuffer();
        for (UIParameter param: children) {
            builder.append(DOMUtils.escapeAnsi(param.getName()) )
                    .append("=").append(DOMUtils.escapeAnsi(
                    (String)param.getValue() ).replace(' ', '+')).append("&");
        }
        if (builder.length() > 0) {
            builder.setLength( builder.length() - 1 );
        }
        return builder.toString();
    }


    /**
     * Return the name value pairs parameters as a comma separated list. This is
     * simpler for passing to the javascript parameter rebuilding code.
     * @param children List of children
     * @return a String in the form name1, value1, name2, value2...
     */
    public static String[] asStringArray( List<UIParameter> children) {
        ArrayList builder = new ArrayList();
        for (UIParameter param: children) {
            builder.add(param.getName());
            builder.add(param.getValue().toString());
        }
        Object[] returnVal = new String[ builder.size() ];
        builder.toArray (returnVal);
        return (String[]) returnVal;
    }

    public static boolean superValueIfSet(UIComponent component, StateHelper sh, String attName, boolean superValue, boolean defaultValue) {
        ValueExpression ve = component.getValueExpression(attName);
        if (ve != null) {
            return superValue;
        }
        String valuesKey = attName + "_rowValues";
        Map clientValues = (Map) sh.get(valuesKey);
        if (clientValues != null) {
            String clientId = component.getClientId();
            if (clientValues.containsKey(clientId)) {
                return superValue;
            }
        }
        String defaultKey = attName + "_defaultValues";
        Map defaultValues = (Map) sh.get(defaultKey);
        if (defaultValues != null) {
            if (defaultValues.containsKey("defValue")) {
                return superValue;
            }
        }
        return defaultValue;
    }

	/* ------------------------------- */
	/* --- imported from icemobile --- */
	/* ------------------------------- */
	
    public static final Map<String, String> FILE_EXT_BY_CONTENT_TYPE = new HashMap<String, String>() {
        private static final long serialVersionUID = -8905491307471581114L;

        {
            put("video/mp4", ".mp4");
            put("audio/mp4", ".mp4");
            put("video/mpeg", ".mpg");
            put("video/mov", ".mov");
            put("video/3gpp", ".3gp");
            put("audio/wav", ".wav");
            put("audio/x-wav", ".wav");
            put("audio/x-m4a", ".m4a");
            put("audio/mpeg", ".mp3");
            put("audio/amr", ".amr");
            put("image/jpeg", ".jpg");
            put("image/jpg", ".jpg");
            put("image/png", ".png");
        }
    };
    
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";

    public static final Map<String, String> CONTENT_TYPE_BY_FILE_EXT = new HashMap<String, String>() {
        {
            put(".mp4", "video/mp4");
            put(".mp4", "audio/mp4");
            put(".mpg", "video/mpeg");
            put(".mov", "video/mov");
            put(".3gp", "video/3gpp");
            put(".wav", "audio/wav");
            put(".wav", "audio/x-wav");
            put(".m4a", "audio/x-m4a");
            put(".mp3", "audio/mpeg");
            put(".amr", "audio/amr");
            put(".jpg", "image/jpeg");
            put(".jpg", "image/jpg");
            put(".png", "image/png");
        }
    };

    public static Cookie getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equalsIgnoreCase(name)) {
                    return cookies[i];
                }
            }
        }
        return null;
    }

    public static void copyStream(InputStream in, OutputStream out)
            throws IOException {
        if( in != null && out != null ){
            byte[] buf = new byte[1000];
            int l = 1;
            while (l > 0) {
                l = in.read(buf);
                if (l > 0) {
                    out.write(buf, 0, l);
                }
            }
        }
        
    }

    public static int copyStream(InputStream in, OutputStream out, int start,
            int end) throws IOException {
        long skipped = in.skip((long) start);
        if (start != skipped) {
            throw new IOException("copyStream failed range start " + start);
        }
        byte[] buf = new byte[1000];
        int pos = start - 1;
        int count = 0;
        int l = 1;
        while (l > 0) {
            l = in.read(buf);
            if (l > 0) {
                pos = pos + l;
                if (pos > end) {
                    l = l - (pos - end);
                    out.write(buf, 0, l);
                    count += l;
                    break;
                }
                out.write(buf, 0, l);
                count += l;
            }
        }
        return count;
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    /**
     * Method to simply check for the Asus transformer prime.
     * @param client ClientDescriptor for request
     * @return true if the Asus was detected
     */
    public static boolean isTransformerHack(ClientDescriptor client ) {
         String ua = client.getUserAgent();
         if (ua != null) {
             ua = ua.toLowerCase();
             return ua.contains( UserAgentInfo.TABLET_TRANSORMER_PRIME );
         }
         return false;
     }
    /**
     * Get the base URL for the request.
     * 
     * The base URL will include the scheme, server name, port, and application
     * context, but not the page, or servlet path, or query string. The returned
     * URL will include a trailing slash. eg. http://server:8080/myapp/
     * 
     * @param request
     *            The ServletRequest
     * @return The base URL.
     */
    public static String getBaseURL(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String serverName = httpRequest.getHeader("x-forwarded-host");
        if (null == serverName) {
            serverName = httpRequest.getServerName() + ":"
                    + httpRequest.getServerPort();
        }
        return httpRequest.getScheme() + "://" + serverName
                + httpRequest.getContextPath() + "/";
    }

    public static String getCloudPushId(HttpServletRequest request) {
        String cloudPushId = null;
        cloudPushId = (String) request.getSession().getAttribute(
                Constants.CLOUD_PUSH_KEY);
        return cloudPushId;
    }

    public static String getAcceptHeader(HttpServletRequest request) {
        String accept = request.getHeader(Constants.HEADER_ACCEPT);
        return accept == null ? accept : accept.toLowerCase();
    }

    public static boolean acceptContains(HttpServletRequest request,
            String contains) {
        boolean result = false;
        String accept = getAcceptHeader(request);
        if (accept != null) {
            result = accept.contains(contains);
        }
        return result;
    }

    public static int generateHashCode(Object value) {
        int hashCode = 0;
        if (value != null) {
            hashCode = value.toString().hashCode();
        }
        return hashCode;
    }
    
    public static DateFormat getHttpDateFormat(){
        return new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
    }
	
	/**
     * used by DateSpinner and timeSpinner to detect which type of events to use
     * mobile devices get touch events Note that Blackberry and android pad are
     * still using generic events
     * 
     * @param context
     * @return true if mobile device
     */
	@Deprecated
    public static boolean isTouchEventEnabled(FacesContext context) {
        ClientDescriptor client = getClientDescriptor();
        // commenting out Blackberry at this time as support of touch events is
        // problematic
        // if (uai.sniffIphone() || uai.sniffAndroid() || uai.sniffBlackberry()
        if (client.isAndroidOS() && client.isTabletBrowser())
            return false;
        if (client.isIOS() || client.isAndroidOS() ) { //assuming android phone
            return true;
        }
        return false;
    }
	
    public static HttpServletRequest getRequest(FacesContext facesContext){
        return (HttpServletRequest)facesContext.getExternalContext().getRequest();
    }
        
    public static ClientDescriptor getClientDescriptor(){
        HttpServletRequest request = getRequest(FacesContext
                .getCurrentInstance());
        ClientDescriptor client = ClientDescriptor.getInstance(request);
        return client;
    }
	
	public static void registerLazyComponent(FacesContext facesContext, String clientId, String function) {
		JavaScriptRunner.runScript(facesContext, "ice.ace.lazy.registry['"+clientId+"'] = function(){ return "+function+"};");
	}
}
