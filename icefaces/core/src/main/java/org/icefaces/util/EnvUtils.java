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

package org.icefaces.util;

import org.icefaces.application.ProductInfo;
import org.icefaces.bean.WindowDisposed;
import org.icefaces.impl.application.AuxUploadResourceHandler;
import org.icefaces.impl.push.servlet.ICEpushResourceHandler;
import org.icefaces.impl.push.servlet.ProxyHttpServletRequest;
import org.icefaces.impl.push.servlet.ProxyHttpServletResponse;
import org.icefaces.impl.push.servlet.ProxyServletContext;
import org.icefaces.impl.push.servlet.ProxySession;

import javax.faces.application.Resource;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnvUtils {

    private static Logger log = Logger.getLogger(EnvUtils.class.getName());

    //The key used to store the current configuration in the application map.
    public static String ICEFACES_ENV_CONFIG = "org.icefaces.env.config";

    //Parameters configurable using context parameters
    public static String ICEFACES_AUTO = "org.icefaces.render.auto";
    public static String ICEFACES_AUTOID = "org.icefaces.autoid";
    public static String COMPRESS_DOM = "org.icefaces.compressDOM";
    public static String COMPRESS_RESOURCES = "org.icefaces.compressResources";
    public static String DELTA_SUBMT = "org.icefaces.deltaSubmit";
    public static String FOCUS_MANAGED = "org.icefaces.focusManaged";
    public static String DIFF_CONFIG = "org.icefaces.diffConfig";
    public static String LAZY_PUSH = "org.icefaces.lazyPush";
    public static String STANDARD_FORM_SERIALIZATION = "org.icefaces.standardFormSerialization";
    public static String STRICT_SESSION_TIMEOUT = "org.icefaces.strictSessionTimeout";
    public static String SUBTREE_DIFF = "org.icefaces.subtreeDiff";
    public static String WINDOW_SCOPE_EXPIRATION = "org.icefaces.windowScopeExpiration";
    public static String MANDATORY_RESOURCE_CONFIG = "org.icefaces.mandatoryResourceConfiguration";
    public static String UNIQUE_RESOURCE_URLS = "org.icefaces.uniqueResourceURLs";
    public static String LAZY_WINDOW_SCOPE = "org.icefaces.lazyWindowScope";
    public static String DISABLE_DEFAULT_ERROR_POPUPS = "org.icefaces.disableDefaultErrorPopups";
    public static String FAST_BUSY_INDICATOR = "org.icefaces.fastBusyIndicator";
    public static String REPLAY_NAVIGATION_ON_RELOAD = "org.icefaces.replayNavigationOnReload";
    public static String GENERATE_HEAD_UPDATE = "org.icefaces.generateHeadUpdate";
    public static String INCLUDE_SCROLL_OFFSETS = "org.icefaces.includeScrollOffsets";
    public static String RELOAD_ON_UPDATE_FAILURE = "org.icefaces.reloadOnUpdateFailure";
    public static String RESOURCE_VERSION = "org.icefaces.resourceVersion";
    public static String VERSIONABLE_TYPES = "org.icefaces.versionableTypes";
    public static String COALESCE_RESOURCES = "org.icefaces.coalesceResources";
    public static String WARN_BEFORE_SESSION_EXPIRY_INTERVAL = "org.icefaces.warnBeforeSessionExpiryInterval";
    public static String CLIENT_SIDE_ELEMENT_UPDATE_DETERMINATION = "org.icefaces.clientSideElementUpdateDetermination";
    public static String ACE_FILE_ENTRY_REQUIRE_JAVASCRIPT = "org.icefaces.ace.fileEntry.requireJavascript";
    public static String PUBLIC_CONTEXT_PATH = "org.icefaces.publicContextPath";


    //Parameters configurable using context parameters but only in compatibility mode
    public static String CONNECTION_LOST_REDIRECT_URI = "org.icefaces.connectionLostRedirectURI";
    public static String SESSION_EXPIRED_REDIRECT_URI = "org.icefaces.sessionExpiredRedirectURI";

    //Parameters configurable on a per page-basis as attributes of <ice:config/>
    public static String ICEFACES_RENDER = "org.icefaces.render";
    public static String ARIA_ENABLED = "org.icefaces.aria.enabled";
    public static String BLOCK_UI_ON_SUBMIT = "org.icefaces.blockUIOnSubmit";
    public static String MESSAGE_PERSISTENCE = "org.icefaces.messagePersistence";
    public static String COMPRESS_IDS = "org.icefaces.compressIDs";

    //Other parameters used internally by ICEfaces framework.
    public static final String HEAD_DETECTED = "org.icefaces.headDetected";
    public static final String BODY_DETECTED = "org.icefaces.bodyDetected";
    private static String RESOURCE_PREFIX = "/javax.faces.resource/";
    private static String PATH_TEMPLATE = "org.icefaces.resource.pathTemplate";
    private static String DUMMY_RESOURCE = "auxupload.txt";
    private static String USER_AGENT_COOKIE = "com.icesoft.user-agent";
    private static String HYPERBROWSER = "HyperBrowser";
    private static String[] DEFAULT_TEMPLATE = new String[]{RESOURCE_PREFIX, ".jsf"};

    //Use reflection to identify the JSF implementation.
    private static boolean isImplTested = false;

    private static boolean isMojarra = false;
    private static final String MOJARRA_STATE_SAVING_MARKER = "~com.sun.faces.saveStateFieldMarker~";


    private static boolean isMyFaces = false;
    private static final String MYFACES_STATE_SAVING_MARKER = "~org.apache.myfaces.saveStateFieldMarker~";

    //Use reflection to detect if Flow is available (JSF 2.2 implementations only)
    private static Class FlowClass;

    static {
        try {
            FlowClass = Class.forName("javax.faces.flow.Flow");
        } catch (Throwable t) {
            log.log(Level.FINE, "Flow classes not available: ", t);
        }
    }

    private static String stateMarker;

    //Use reflection to identify if the Portlet classes are available.
    private static Class PortletSessionClass;
    private static Class PortletRequestClass;
    private static Class PortletResponseClass;
    private static Class PortletResourceResponseClass;

    static {
        try {
            PortletSessionClass = Class.forName("javax.portlet.PortletSession");
            PortletRequestClass = Class.forName("javax.portlet.PortletRequest");
            PortletResponseClass = Class.forName("javax.portlet.PortletResponse");
            PortletResourceResponseClass = Class.forName("javax.portlet.ResourceResponse");
        } catch (Throwable t) {
            log.log(Level.FINE, "Portlet classes not available: ", t);
        }
    }

    //Use reflection to identify if a Liferay specific class is available.
    private static Class LiferayClass;

    static {
        try {
            LiferayClass = Class.forName("com.liferay.portal.theme.ThemeDisplay");
        } catch (Throwable t) {
            log.log(Level.FINE, "Liferay class not available: ", t);
        }
    }

    //Use reflection to identify if a WebSphere Portal specific class is available.
    private static Class WebSpherePortalClass;

    static {
        try {
            WebSpherePortalClass = Class.forName("com.ibm.ws.portletcontainer.PortletContainer");
        } catch (Throwable t) {
            log.log(Level.FINE, "WebSphere Portal class not available: ", t);
        }
    }

    //Use reflection to identify if a Pluto Portal specific class is available.
    private static Class PlutoPortalClass;

    static {
        try {
            PlutoPortalClass = Class.forName("org.apache.pluto.container.PortletRequestContext");
        } catch (Throwable t) {
            log.log(Level.FINE, "Pluto Portal class not available: ", t);
        }
    }

    //Use reflection to identify if ICEpush is available.
    private static boolean icepushPresent;

    static {
        try {
            Class.forName("org.icepush.PushContext");
            icepushPresent = true;
        } catch (ClassNotFoundException e) {
            icepushPresent = false;
        }
    }

    /**
     * Whether stock JSF components' set attributes are tracked.
     */
    private static boolean isStockAttributeTracking = false;

    static {
        try {
            javax.faces.component.html.HtmlOutputText comp = new javax.faces.component.html.HtmlOutputText();
            isStockAttributeTracking = isAttributeTracking(comp);
        } catch (Throwable t) {
        }
    }

    /**
     * A set of Strings representing all of the reserved words in Java
     */
    public static final HashSet<String> JAVA_RESERVED_WORDS;

    static{
        JAVA_RESERVED_WORDS = new HashSet(53);
        JAVA_RESERVED_WORDS.add("abstract");
        JAVA_RESERVED_WORDS.add("assert");
        JAVA_RESERVED_WORDS.add("boolean");
        JAVA_RESERVED_WORDS.add("break");
        JAVA_RESERVED_WORDS.add("byte");
        JAVA_RESERVED_WORDS.add("case");
        JAVA_RESERVED_WORDS.add("catch");
        JAVA_RESERVED_WORDS.add("char");
        JAVA_RESERVED_WORDS.add("class");
        JAVA_RESERVED_WORDS.add("const");
        JAVA_RESERVED_WORDS.add("continue");
        JAVA_RESERVED_WORDS.add("default");
        JAVA_RESERVED_WORDS.add("do");
        JAVA_RESERVED_WORDS.add("double");
        JAVA_RESERVED_WORDS.add("else");
        JAVA_RESERVED_WORDS.add("enum");
        JAVA_RESERVED_WORDS.add("extends");
        JAVA_RESERVED_WORDS.add("false");
        JAVA_RESERVED_WORDS.add("final");
        JAVA_RESERVED_WORDS.add("finally");
        JAVA_RESERVED_WORDS.add("float");
        JAVA_RESERVED_WORDS.add("for");
        JAVA_RESERVED_WORDS.add("goto");
        JAVA_RESERVED_WORDS.add("if");
        JAVA_RESERVED_WORDS.add("implements");
        JAVA_RESERVED_WORDS.add("import");
        JAVA_RESERVED_WORDS.add("instanceof");
        JAVA_RESERVED_WORDS.add("int");
        JAVA_RESERVED_WORDS.add("interface");
        JAVA_RESERVED_WORDS.add("long");
        JAVA_RESERVED_WORDS.add("native");
        JAVA_RESERVED_WORDS.add("new");
        JAVA_RESERVED_WORDS.add("null");
        JAVA_RESERVED_WORDS.add("package");
        JAVA_RESERVED_WORDS.add("private");
        JAVA_RESERVED_WORDS.add("protected");
        JAVA_RESERVED_WORDS.add("public");
        JAVA_RESERVED_WORDS.add("return");
        JAVA_RESERVED_WORDS.add("short");
        JAVA_RESERVED_WORDS.add("static");
        JAVA_RESERVED_WORDS.add("strictfp");
        JAVA_RESERVED_WORDS.add("super");
        JAVA_RESERVED_WORDS.add("switch");
        JAVA_RESERVED_WORDS.add("synchronized");
        JAVA_RESERVED_WORDS.add("this");
        JAVA_RESERVED_WORDS.add("throw");
        JAVA_RESERVED_WORDS.add("throws");
        JAVA_RESERVED_WORDS.add("transient");
        JAVA_RESERVED_WORDS.add("true");
        JAVA_RESERVED_WORDS.add("try");
        JAVA_RESERVED_WORDS.add("void");
        JAVA_RESERVED_WORDS.add("volatile");
        JAVA_RESERVED_WORDS.add("while");
    }

    private static interface OriginalRequestGetter {
        public HttpServletRequest get(FacesContext context);
    }

    private static OriginalRequestGetter ORIGINAL_REQUEST_GETTER;

    private static class ServletEnvironmentRequestGetter implements OriginalRequestGetter {
        public HttpServletRequest get(FacesContext context) {
            return (HttpServletRequest) context.getExternalContext().getRequest();
        }
    }

    private static class LiferayOriginalRequestGetter implements OriginalRequestGetter {
        private Class PortalUtilClass;
        private Method GetHttpServletRequest;
        private Method GetOriginalServletRequest;

        private LiferayOriginalRequestGetter() throws ClassNotFoundException, NoSuchMethodException {
            PortalUtilClass = Class.forName("com.liferay.portal.util.PortalUtil");
            GetHttpServletRequest = PortalUtilClass.getDeclaredMethod("getHttpServletRequest", javax.portlet.PortletRequest.class);
            GetOriginalServletRequest = PortalUtilClass.getDeclaredMethod("getOriginalServletRequest", HttpServletRequest.class);
        }

        public HttpServletRequest get(FacesContext context) {
            try {
                javax.portlet.PortletRequest portletRequest = (javax.portlet.PortletRequest) context.getExternalContext().getRequest();
                HttpServletRequest httpPortletRequest = (HttpServletRequest) GetHttpServletRequest.invoke(PortalUtilClass, portletRequest);
                return (HttpServletRequest) GetOriginalServletRequest.invoke(PortalUtilClass, httpPortletRequest);
            } catch (InvocationTargetException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        }
    }

    private static class WebspherePortalOriginalRequestGetter implements OriginalRequestGetter {
        public HttpServletRequest get(FacesContext context) {
            //get original HTTP request using method described here: http://wpcertification.blogspot.ro/2010/05/accessing-underlying-httpservletrequest.html
            Object request = context.getExternalContext().getRequest();
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            while(httpServletRequest instanceof HttpServletRequestWrapper){
                HttpServletRequestWrapper httpServletRequestWrapper = (HttpServletRequestWrapper)httpServletRequest;
                httpServletRequest =  (HttpServletRequest)httpServletRequestWrapper.getRequest();
            }
            return httpServletRequest;
        }
    }

    private static class PlutoPortalOriginalRequestGetter implements OriginalRequestGetter {
        private Class PortletRequestContext;
        private Method GetHttpServletRequest;

        private PlutoPortalOriginalRequestGetter() throws ClassNotFoundException, NoSuchMethodException {
            PortletRequestContext = Class.forName("org.apache.pluto.container.PortletRequestContext");
            GetHttpServletRequest = PortletRequestContext.getDeclaredMethod("getContainerRequest");
        }

        public HttpServletRequest get(FacesContext context) {
            try {
                Map requestMap = context.getExternalContext().getRequestMap();
                Object requestContext = requestMap.get("org.apache.pluto.container.PortletRequestContext");
                HttpServletRequest httpServletRequest = (HttpServletRequest) GetHttpServletRequest.invoke(requestContext);
                return  httpServletRequest;
            } catch (IllegalAccessException e) {
                return null;
            } catch (InvocationTargetException e) {
                return null;
            }
        }
    }

    static {
        try {
            if (isLiferay()) {
                ORIGINAL_REQUEST_GETTER = new LiferayOriginalRequestGetter();
            } else if (isWebSpherePortal()) {
                ORIGINAL_REQUEST_GETTER = new WebspherePortalOriginalRequestGetter();
            } else if (isPlutoPortal()) {
                ORIGINAL_REQUEST_GETTER = new PlutoPortalOriginalRequestGetter();
            } else {
                ORIGINAL_REQUEST_GETTER = new ServletEnvironmentRequestGetter();
            }
        } catch (ClassNotFoundException e) {
            log.warning("Cannot find portal class: " + e.getMessage());
        } catch (NoSuchMethodException e) {
            log.warning("Cannot get method of class: " + e.getMessage());
        }
    }

    /**
     * Returns the value of the context parameter org.icefaces.aria.enabled.  The default value is true and indicates
     * that views are ARIA (Accessible Rich Internet Applications) enabled.  This context parameter is application-wide
     * and works together with the 'ariaEnabled' attribute of the ICEfaces configuration tag <ice:config> so that ARIA support
     * can be turned on and off selectively on a per page basis.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.aria.enabled.  The default is true.
     */
    public static boolean isAriaEnabled(FacesContext facesContext) {
        Object ariaEnabled = getViewParam(facesContext, ARIA_ENABLED);
        if (null == ariaEnabled) {
            return EnvConfig.getEnvConfig(facesContext).ariaEnabled;
        }
        return (Boolean.TRUE.equals(ariaEnabled));
    }

    /**
     * Returns the value of the context parameter org.icefaces.autoid.  The default value is true and indicates
     * that the majority of standard JSF components will write their ids to the page markup.  This allows page updates
     * to be targetted as granularly as possible.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.autoid.  The default is true.
     */
    public static boolean isAutoId(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).autoId;
    }

    /**
     * Returns the value of the context parameter org.icefaces.render.auto.  The default value is true and indicates
     * that DOM changes will automatically be applied to each page.  This context parameter is application-wide and works
     * together with the render attribute of the ICEfaces configuration tag <ice:config> so that DOM updates can be turned on
     * and off selectively on a per page basis.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.render.auto.  The default is true.
     */
    public static boolean isAutoRender(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).autoRender;
    }

    /**
     * Returns the value of the context parameter org.icefaces.compressIDs.  The default value is false and indicates
     * that IDs will not be compressed.  The context parameter is application-wide and works
     * together with the compressIDs attribute of the ICEfaces configuration tag <ice:config> so that ID
     * compression can be turned on and off selectively on a per page basis.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.compressIDs.  The default is false.
     */
    public static boolean isCompressIDs(FacesContext facesContext) {
        Object compressIDs = getViewParam(facesContext, COMPRESS_IDS);
        if (null == compressIDs) {
            return EnvConfig.getEnvConfig(facesContext).compressIDs;
        }
        return (Boolean.TRUE.equals(compressIDs));
    }

    /**
     * Returns the value of the context parameter org.icefaces.blockUIOnSubmit.  The default value is false and indicates
     * that the UI will not be blocked after a request has been submitted.  To help deal with the problems with double-submits,
     * this parameter can be set to true.
     * <p/>
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.blockUIOnSubmit.  The default is false.
     */
    public static boolean isBlockUIOnSubmit(FacesContext facesContext) {
        Object blockUIOnSubmit = getViewParam(facesContext, BLOCK_UI_ON_SUBMIT);
        if (null == blockUIOnSubmit) {
            return EnvConfig.getEnvConfig(facesContext).blockUIOnSubmit;
        }
        return (Boolean.TRUE.equals(blockUIOnSubmit));
    }

    /**
     * Programmatically override the value of blockUIOnSubmit.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @param value 
     */
    public static void setBlockUIOnSubmit(FacesContext facesContext, boolean value) {
        setViewParam(facesContext, BLOCK_UI_ON_SUBMIT, value);
    }

    /**
     * Returns the value of the context parameter org.icefaces.compressDOM.  The default value is false and indicates
     * that, between requests, the server-side DOM will be serialized and compressed to save memory.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.compressDOM.  The default is false.
     */
    public static boolean isCompressDOM(FacesContext facesContext) {
        //consider making this a per-view setting
        return EnvConfig.getEnvConfig(facesContext).compressDOM;
    }

    /**
     * Returns the value of the context parameter org.icefaces.compressResources.  The default value is true and indicates
     * that, for resource requests, certain resources should be automatically compressed via gzip before being sent.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.compressResources.  The default is true.
     */
    public static boolean isCompressResources(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).compressResources;
    }

    /**
     * Returns the value of the context parameter org.icefaces.connectionLostRedirectURI.  The default value is the String
     * "null" and indicates that no URI has been set and the default behaviour is taken when the Ajax Push connection is lost.
     * Setting a URI value tells ICEfaces to redirect to that view if the Ajax Push connection is lost.
     * <p/>
     * Note: This value is only relevant when running ICEfaces 2+ with the compatible component suite:
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.connectionLostRedirectURI.  The default is the String "null".
     */
    public static String getConnectionLostRedirectURI(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).connectionLostRedirectURI;
    }

    /**
     * Returns the value for org.icefaces.diffConfig. This is configured
     * as a space separated name=value string.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.diffConfig.  The default is null.
     */
    public static String getDiffConfig(FacesContext facesContext) {
        Object diffConfig = getViewParam(facesContext, DIFF_CONFIG);
        if (null == diffConfig) {
            return EnvConfig.getEnvConfig(facesContext).diffConfig;
        }
        return (String) diffConfig;
    }

    /**
     * Returns the value of the context parameter org.icefaces.deltaSubmit.  The default value is false and indicates that
     * the delta submit features is not currently enabled.  When delta submit is enabled, form submission is done in a way that
     * minimizes what is sent across the wire.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.deltaSubmit.  The default is false.
     */
    public static boolean isDeltaSubmit(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).deltaSubmit;
    }

    /**
     * Returns the value of the context parameter org.icefaces.focusManaged.  The default value is true and indicates that
     * the focus retention feature is currently enabled.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.focusManaged.  The default is true.
     */
    public static boolean isFocusManaged(FacesContext facesContext) {
        Object focusManaged = getViewParam(facesContext, FOCUS_MANAGED);
        if (null == focusManaged) {
            return EnvConfig.getEnvConfig(facesContext).focusManaged;
        }
        return (Boolean.TRUE.equals(focusManaged));
    }

    /**
     * Returns the value of the context parameter org.icefaces.lazyPush.  The default value is true and indicates that
     * ICEpush will be initially lazily.  In other words, ICEpush will not activate and open a blocking connection
     * until the first push request is made.  By setting lazyPush to false, ICEpush will be automatically activated for
     * each ICEfaces page.
     * <p/>
     * This context parameter is application-wide and works together with the lazyPush attribute of the ICEfaces
     * configuration tag <ice:config> so that ICEpush can be set to activate lazily on a per-page basis.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.lazyPush.  The default is true.
     */
    public static boolean isLazyPush(FacesContext facesContext) {
        Object lazyPush = getViewParam(facesContext, LAZY_PUSH);
        if (null == lazyPush) {
            return EnvConfig.getEnvConfig(facesContext).lazyPush;
        }
        return (Boolean.TRUE.equals(lazyPush));
    }


    /**
     * Returns true if Liferay classes are detected via reflection.
     *
     * @return Returns true if Liferay classes are detected via reflection.
     */
    public static boolean isLiferay() {
        return LiferayClass != null;
    }


    /**
     * Returns true if WebSphere Portal class is detected via reflection.
     *
     * @return Returns true if WebSphere Portal class is detected via reflection.
     */
    public static boolean isWebSpherePortal() {
        return WebSpherePortalClass != null;
    }

    /**
     * Returns true if Pluto Portal class is detected via reflection.
     *
     * @return Returns true if Pluto Portal class is detected via reflection.
     */
    public static boolean isPlutoPortal() {
        return PlutoPortalClass != null;
    }


    /**
     * Returns true if JSF Partial State Saving is active.
     *
     * @param facesContext The current FacesContext instance.
     * @return Returns the current state of JSF Partial State Saving.  The default is true.
     */
    public static boolean isPartialStateSaving(FacesContext facesContext) {
        return !("false".equalsIgnoreCase(
                FacesContext.getCurrentInstance().getExternalContext()
                        .getInitParameter("javax.faces.PARTIAL_STATE_SAVING")));
    }

    /**
     * Returns the value of the context parameter org.icefaces.sessionExpiredRedirectURI.  The default value is the String
     * "null" and indicates that no URI has been set and the default behaviour is taken when the session expires.  Setting
     * a URI value tells ICEfaces to redirect to that view if the Ajax Push connection is lost.
     * <p/>
     * Note: This value is only relevant when running ICEfaces 2+ with the compatible component suite:
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.sessionExpiredRedirectURI.  The default is the String "null".
     */
    public static String getSessionExpiredRedirectURI(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).sessionExpiredRedirectURI;
    }

    /**
     * Returns the value of the context parameter org.icefaces.standardFormSerialization.  The default value is false and indicates
     * that ICEfaces should do optimized for submission based on the submitting element.  Setting this value to true indicates that
     * ICEfaces should do a normal, full form submission.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.standardFormSerialization.  The default is false.
     */
    public static boolean isStandardFormSerialization(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).standardFormSerialization;
    }

    /**
     * Returns the value of the context parameter org.icefaces.strictSessionTimeout.  The default value is false and indicates
     * that ICEfaces should not interfere with container-managed session timeout.  Setting this value to true indicates that
     * ICEfaces should attempt to enforce the configured session timeout by ignoring intervening push activity.  Only
     * user events result in extending the session lifetime.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.strictSessionTimeout.  The default is false.
     */
    public static boolean isStrictSessionTimeout(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).strictSessionTimeout;
    }

    /**
     * Returns the value of the context parameter org.icefaces.subtreeDiff.  The default value is true and indicates
     * that diffs will be calculated for subtree rendering.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.subtreeDiff.  The default is true.
     */
    public static boolean isSubtreeDiff(FacesContext facesContext) {
        Object subtreeDiff = getViewParam(facesContext, SUBTREE_DIFF);
        if (null == subtreeDiff) {
            return EnvConfig.getEnvConfig(facesContext).subtreeDiff;
        }
        return (Boolean.TRUE.equals(subtreeDiff));
    }

    /**
     * Returns the value of the context parameter org.icefaces.windowScopeExpiration.  The default value is 1000 milliseconds
     * and indicates the length of time window-scoped values remain valid in the session after a reload or redirect occurs.
     * This allows for postbacks that might occur quickly after a reload or redirect to successfully retrieve the relevant
     * window-scoped values.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @return Returns the current setting of org.icefaces.windowScopeExpiration.  The default is 1000 milliseconds.
     */
    public static long getWindowScopeExpiration(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).windowScopeExpiration;
    }

    public static String getMandatoryResourceConfig(FacesContext facesContext) {
        String configValue = EnvConfig.getEnvConfig(facesContext)
                .mandatoryResourceConfig;
        String result = configValue;
        String overValue = (String) getViewParam(facesContext, MANDATORY_RESOURCE_CONFIG);
        if (null != overValue) {
            result = overValue;
        }
        if (null != result) {
            result = result.trim();
        }
        return result;
    }

    public static boolean isUniqueResourceURLs(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).uniqueResourceURLs;
    }

    public static String getPublicContextPath(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).publicContextPath;
    }


    public static void createSessionOnPageLoad(FacesContext context) {
        final ExternalContext externalContext = context.getExternalContext();
        //create session if GET request
        externalContext.getSession(externalContext.getRequestContentLength() == 0);
    }

    public static boolean isSessionInvalid(FacesContext context) {
        final ExternalContext externalContext = context.getExternalContext();
        //if session invalid or expired block other resource handlers from running
        return externalContext.getSession(false) == null;
    }

    /**
     * Returns true if the browser is enhanced with additional features.
     *
     * @param facesContext The current FacesContext.
     * @return true if browser is enhanced.
     */
    public static boolean isEnhancedBrowser(FacesContext facesContext) {
        Cookie cookie = (Cookie) facesContext.getExternalContext()
                .getRequestCookieMap().get(USER_AGENT_COOKIE);
        if (null != cookie) {
            return cookie.getValue().startsWith(HYPERBROWSER);
        }
        return false;
    }

    /**
     * Returns true if the browser is enhanced via auxiliary upload.
     *
     * @param facesContext The current FacesContext.
     * @return true if browser supports auxiliary upload.
     */
    public static boolean isAuxUploadBrowser(FacesContext facesContext) {
        boolean isAux = facesContext.getExternalContext().getSessionMap()
                .containsKey(AuxUploadResourceHandler.AUX_REQ_MAP_KEY);
        return isAux;
    }

    public static boolean isICEfacesView(FacesContext facesContext) {
        //Check to see if the view is configured to use ICEfaces (default is to enable ICEfaces).
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Map viewMap = viewRoot.getViewMap();

        Object icefacesRender = viewMap.get(ICEFACES_RENDER);
        if (null == icefacesRender) {
            icefacesRender = EnvConfig.getEnvConfig(facesContext).autoRender;
            viewMap.put(ICEFACES_RENDER, icefacesRender);
        }
        //using .equals on Boolean to obtain boolean robustly
        return (Boolean.TRUE.equals(icefacesRender));
    }

    public static boolean isICEpushPresent() {
        return icepushPresent;
    }

    public static boolean hasHeadAndBodyComponents(FacesContext facesContext) {
        //ICE-5613: ICEfaces must have h:head and h:body tags to render resources into
        //Without these components, ICEfaces is disabled.
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Map viewMap = viewRoot.getViewMap();
        if (!viewMap.containsKey(HEAD_DETECTED) || !viewMap.containsKey(BODY_DETECTED)) {
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "ICEfaces disabled for view " + viewRoot.getViewId() +
                        "\n  h:head tag available: " + viewMap.containsKey(HEAD_DETECTED) +
                        "\n  h:body tag available: " + viewMap.containsKey(BODY_DETECTED));
            }
            return false;
        }
        return true;
    }


    //remove this once multi-form ViewState is addressed

    public static boolean needViewStateHack() {
        return isMojarra();
    }

    public static String[] getPathTemplate() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        boolean isPortlet = EnvUtils.instanceofPortletRequest(externalContext.getRequest());

        //ICE-8540
        //The optimization of storing the path template doesn't work well in portlets because
        //the path will include the host and port, which could change based on the URL used
        //by the client.  So we skip the optimization when using portlets.
        String[] pathTemplate = null;
        if( !isPortlet ){
            Map applicationMap = externalContext.getApplicationMap();
            pathTemplate = (String[]) applicationMap.get(PATH_TEMPLATE);
            if (null != pathTemplate) {
                return pathTemplate;
            }
        }
        Resource dummyResource = facesContext.getApplication()
                .getResourceHandler().createResource(DUMMY_RESOURCE);
        if (null != dummyResource) {
            String dummyPath = dummyResource.getRequestPath();
            pathTemplate = extractPathTemplate(dummyPath);
        }
        if (null == pathTemplate) {
            return DEFAULT_TEMPLATE;
        }

        if(!isPortlet){
            Map applicationMap = externalContext.getApplicationMap();
            applicationMap.put(PATH_TEMPLATE, pathTemplate);
        }
        return pathTemplate;
    }

    private static String[] extractPathTemplate(String path) {
        int start = path.indexOf(DUMMY_RESOURCE);
        if (start < 0) {
            return null;
        }
        String pre = path.substring(0, start);
        String post = path.substring(start + DUMMY_RESOURCE.length());
        return new String[]{pre, post};
    }

    public static boolean instanceofPortletSession(Object session) {
        return PortletSessionClass != null && PortletSessionClass.isInstance(session);
    }

    public static boolean instanceofPortletRequest(Object request) {
        return PortletRequestClass != null && PortletRequestClass.isInstance(request);
    }

    public static boolean instanceofPortletResponse(Object response) {
        return PortletResponseClass != null && PortletResponseClass.isInstance(response);
    }

    public static boolean instanceofPortletResourceResponse(Object response) {
        return PortletResourceResponseClass != null && PortletResourceResponseClass.isInstance(response);
    }

    public static ServletContext getSafeContext(FacesContext fc) {
        ExternalContext ec = fc.getExternalContext();
        Object rawReq = ec.getRequest();
        if (instanceofPortletRequest(rawReq)) {
            return new ProxyServletContext(fc);
        }
        return (ServletContext) ec.getContext();
    }

    public static HttpServletRequest getSafeRequest(FacesContext fc) {
        ExternalContext ec = fc.getExternalContext();
        Object rawReq = ec.getRequest();
        if (instanceofPortletRequest(rawReq)) {
            return new ProxyHttpServletRequest(fc);
        }
        return (HttpServletRequest) rawReq;
    }

    public static HttpServletResponse getSafeResponse(FacesContext fc) {
        ExternalContext ec = fc.getExternalContext();
        Object rawRes = ec.getResponse();
        if (instanceofPortletResponse(rawRes)) {
            return new ProxyHttpServletResponse(fc);
        }
        return (HttpServletResponse) rawRes;
    }

    //Leave this for backwards compatibility that defaults to a PORTLET_APPLICATION scope.
    public static HttpSession getSafeSession(FacesContext fc) {
        return getSafeSession(fc, true);
    }

    public static HttpSession getSafeSession(FacesContext fc, boolean createSession) {
        return getSafeApplicationScopeSession(fc, createSession);
    }

    private static HttpSession getSafeSession(FacesContext fc, int scope, boolean createSession) {
        ExternalContext ec = fc.getExternalContext();
        Object rawSess = ec.getSession(createSession);
        if(rawSess == null){
            return null;
        }
        if (instanceofPortletSession(rawSess)) {
            return new ProxySession(fc, scope);
        }
        return (HttpSession) rawSess;
    }

    public static HttpSession getSafePortletScopeSession(FacesContext fc) {
        return getSafeSession(fc, ProxySession.PORTLET_SCOPE, true);
    }

    public static HttpSession getSafeApplicationScopeSession(FacesContext fc) {
        return getSafeSession(fc, ProxySession.APPLICATION_SCOPE, true);
    }

    public static HttpSession getSafePortletScopeSession(FacesContext fc, boolean createSession) {
        return getSafeSession(fc, ProxySession.PORTLET_SCOPE, createSession);
    }

    public static HttpSession getSafeApplicationScopeSession(FacesContext fc, boolean createSession) {
        return getSafeSession(fc, ProxySession.APPLICATION_SCOPE, createSession);
    }

    public static HttpServletRequest getOriginalServletRequest(FacesContext context) {
        return ORIGINAL_REQUEST_GETTER.get(context);
    }

    public static boolean isSecure(FacesContext fc){
        //ICE-8902
        //The isSecure() method is only available in JSF 2.1 which causes problems with older
        //versions of the two portlet bridges that don't override it.  In that case, ignore the
        //exception and query the request via reflection to avoid any portlet runtime dependencies.

        try {
            return fc.getExternalContext().isSecure();
        } catch (UnsupportedOperationException uoe) {
            //If isSecure is not overridden, then ignore and use the request directly.
        }

       return getSafeRequest(fc).isSecure();
    }

    public static boolean isPushRequest(FacesContext facesContext) {
        ExternalContext ec = facesContext.getExternalContext();
        String reqPath = ec.getRequestServletPath();
        String pathInfo = ec.getRequestPathInfo();
        Map<String, String> parameterMap = ec.getRequestParameterMap();
        String reqParam = parameterMap.get("ice.submit.type");

        if (reqPath != null && reqPath.contains(ICEpushResourceHandler.BLOCKING_CONNECTION_RESOURCE_NAME) ||
                pathInfo != null && pathInfo.contains(ICEpushResourceHandler.BLOCKING_CONNECTION_RESOURCE_NAME) ||
                "ice.push".equals(reqParam)) {
            return true;
        }
        if (parameterMap.containsKey("ice.pushid")) {
            return true;
        }
        return false;
    }

    public static boolean isLazyWindowScope(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).lazyWindowScope;
    }

    public static boolean disableDefaultErrorPopups(FacesContext facesContext) {
        Object disableDefaultErrorPopups = getViewParam(facesContext, DISABLE_DEFAULT_ERROR_POPUPS);
        if (null == disableDefaultErrorPopups) {
            return EnvConfig.getEnvConfig(facesContext).disableDefaultErrorPopups;
        }
        return (Boolean.TRUE.equals(disableDefaultErrorPopups));
    }


    /**
     * Programmatically override the value of disableDefaultErrorPopups.
     *
     * @param facesContext The current FacesContext instance used to access the application map.
     * @param value
     */
    public static void setDisableDefaultErrorPopups(FacesContext facesContext, boolean value) {
        setViewParam(facesContext, DISABLE_DEFAULT_ERROR_POPUPS, value);
    }


    public static boolean isFastBusyIndicator(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).fastBusyIndicator;
    }

    public static boolean isReplayNavigationOnReload(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).replayNavigationOnReload;
    }

    public static boolean isMessagePersistence(final FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).messagePersistence;
    }

    public static boolean isMojarra() {
        testImpl();
        return isMojarra;
    }

    public static boolean isMyFaces() {
        testImpl();
        return isMyFaces;
    }

    /**
     * Determine whether the current JSF implementation is version 2.2
     * @return true if JSF implementation is 2.2
     */
    public static boolean isJSF22() {
        return FacesContext.class.getPackage().getImplementationVersion().startsWith("2.2");
    }


    private static void testImpl() {
        if (!isImplTested) {
            String implName = FacesContext.getCurrentInstance()
                    .getClass().getName();
            if (implName.startsWith("com.sun")) {
                isMojarra = true;
                isImplTested = true;
            } else if (implName.startsWith("org.apache")) {
                isMyFaces = true;
                isImplTested = true;
            }
        }
    }

    public static String getStateMarker() {
        if (stateMarker == null) {
            if (EnvUtils.isMojarra()) {
                stateMarker = MOJARRA_STATE_SAVING_MARKER;
            } else if (EnvUtils.isMyFaces()) {
                stateMarker = MYFACES_STATE_SAVING_MARKER;
            } else {
                log.warning("could not determine JSF implementation");
            }
        }
        return stateMarker;
    }

    public static boolean containsBeans(Map<String, Object> scopeMap) {
        //skip the objects saved in the map by ICEfaces framework while testing for the existence of beans
        for (Map.Entry entry : scopeMap.entrySet()) {
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (!value.getClass().getName().startsWith("org.icefaces.impl") & !(
                    (value instanceof String) || (value instanceof Character) ||
                            (value instanceof Boolean) || (value instanceof Number))) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsDisposedBeans(Map<String, Object> scopeMap) {
        //skip the objects saved in the map by ICEfaces framework while testing for the existence of beans
        for (Map.Entry entry : scopeMap.entrySet()) {
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (value != null && value.getClass().isAnnotationPresent(WindowDisposed.class)) {
                return true;
            }
        }
        return false;
    }

    //utility method to get per-view configuration values
    static Object getViewParam(FacesContext facesContext, String name) {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (null == viewRoot) {
            return null;
        }
        Map viewMap = viewRoot.getViewMap();
        if(viewMap == null){
            return null;
        }
        return viewMap.get(name);
    }

    static void setViewParam(FacesContext facesContext, String name, Object value) {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (null != viewRoot) {
            viewRoot.getViewMap().put(name, value);
        }
    }

    public static boolean generateHeadUpdate(FacesContext context) {
        return EnvConfig.getEnvConfig(context).generateHeadUpdate;
    }

    public static boolean isIncludeScrollOffsets(FacesContext context) {
        return EnvConfig.getEnvConfig(context).includeScrollOffsets;
    }

    public static boolean reloadOnUpdateFailure(FacesContext context) {
        return EnvConfig.getEnvConfig(context).reloadOnUpdateFailure;
    }

    public static String getResourceVersion(FacesContext context) {
        return EnvConfig.getEnvConfig(context).resourceVersion;
    }

    public static String getVersionableTypes(FacesContext context) {
        return EnvConfig.getEnvConfig(context).versionableTypes;
    }

    /**
     * @return Whether stock JSF components' set attributes are tracked.
     * @see #isAttributeTracking
     */
    public static boolean isStockAttributeTracking() {
        return isStockAttributeTracking;
    }

    /**
     * For stock JSF components, all setting of attributes, whether by setter
     * methods, or by puts on the the attribute map (which can delegate to
     * setter methods), result in List UIComponent.attributesThatAreSet, aka
     * UIComponentBase.getAttributes().get(
     *     "javax.faces.component.UIComponentBase.attributesThatAreSet")
     * containing that attribute name, as of JSF RI 1.2_05. This optimisation
     * is disabled for components not in javax.faces.component.* packages,
     * even if the component extend the stock ones. Meaning that the stock
     * attributes become decellerated in third party components. It's possible
     * to create the attributesThatAreSet List for 3rd party components, and
     * have it track attribute maps puts for attributes that do not have
     * setter methods. But setter method tracking is only enabled as of
     * //TODO// JSF RI 1.2_xx, so we need to ascertain that separately, for ICEfaces
     * extended and custom component rendering.
     *
     * @param comp An arbitrary component whose attributes are known
     * @return If the JSF implementation tracks this component's set attributes
     */
    private static boolean isAttributeTracking(javax.faces.component.html.HtmlOutputText comp) {
        boolean tracked = false;
        comp.setTitle("value");
        comp.getAttributes().put("lang", "value");
        comp.getAttributes().put("no_method", "value");
        List attributesThatAreSet = (List) comp.getAttributes().get(
            "javax.faces.component.UIComponentBase.attributesThatAreSet");

        if (attributesThatAreSet != null &&
            attributesThatAreSet.contains("title") &&
            attributesThatAreSet.contains("lang") &&
            attributesThatAreSet.contains("no_method")) {
            tracked = true;
        }
        return tracked;
    }

    /**
     *  When using reflection to programmatically create a class, e.g. Class.forName(),
     *  it may be desirable for security purposes to ensure that the parameter is a
     *  valid Java identifier before trying to use it.  This utility method examines the
     *  identifier to ensure that it follows the rules laid out in the specification.
     *
     *  This includes checking the starting character of each part using
     *  Character.isJavaIdentifierStart, all other characters using Character.isJavaIdentifierPart,
     *  and ensuring that each part is not a reserved Java keyword.
     *
     *  @param identifier A String representing a fully qualified classname.
     *  @return Returns true if the supplied identifier qualifies as a fully qualified classname.
     *
     */
    public static boolean isValidJavaIdentifier(String identifier) {

        if (identifier == null || identifier.length() == 0) {
            return false;
        }

        //Break the identifier into parts based on the dot
        String[] parts = identifier.split("\\.");

        //Check each individual part
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

             //Must start with a valid Java identifier character
            if (!Character.isJavaIdentifierStart(part.charAt(0))) {
                return false;
            }

            Iterator words = JAVA_RESERVED_WORDS.iterator();
            while (words.hasNext()) {
                String reservedWord = (String)words.next();
                if(part.equals(reservedWord)){
                    return false;
                }
            }

            //Rest of identifier part must also contain valid characters
            for(int index = 1; index < part.length(); index++){
                if (!Character.isJavaIdentifierPart(part.charAt(index))) {
                    return false;
                }
            }

        }

        return true;
    }


    public static boolean isCoallesceResources(FacesContext context) {
        return EnvConfig.getEnvConfig(context).coalesceResources;
    }

    public static String getUserAgent(FacesContext fc) {

        //In most cases this approach works
        HttpServletRequest req = getSafeRequest(fc);
        String userAgent = req.getHeader("user-agent");
        if (userAgent != null) {
            return userAgent;
        }

        //Unfortunately, the old PortletFaces Bridge does not provide the "user-agent"
        //header as part of the header map.  So we have to try something a bit more complex.
        if( req instanceof ProxyHttpServletRequest){
            return ((ProxyHttpServletRequest)req).getUserAgentFromPortletFacesBridge();
        }

        return null;
    }

    public static long getWarnBeforeExpiryInterval(FacesContext context) {
        return EnvConfig.getEnvConfig(context).warnBeforeExpiryInterval;
    }

    public static boolean isClientSideElementUpdateDetermination(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).clientSideElementUpdateDetermination;
    }

    public static boolean isFileEntryRequireJavascript(FacesContext facesContext) {
        return EnvConfig.getEnvConfig(facesContext).fileEntryRequireJavascript;
    }
}

class EnvConfig {
    private static Logger log = Logger.getLogger(EnvConfig.class.getName());

    private static final String DEFAULT_VERSIONABLE_TYPES = "*/javascript */css image/*";

    boolean autoRender;
    boolean autoId;
    boolean ariaEnabled;
    boolean blockUIOnSubmit;
    boolean compressDOM;
    boolean compressResources;
    boolean compressIDs;
    String connectionLostRedirectURI;
    String diffConfig;
    boolean deltaSubmit;
    boolean lazyPush;
    boolean pushActive;
    String sessionExpiredRedirectURI;
    boolean standardFormSerialization;
    boolean strictSessionTimeout;
    boolean subtreeDiff;
    long windowScopeExpiration;
    String mandatoryResourceConfig;
    boolean uniqueResourceURLs;
    boolean lazyWindowScope;
    boolean messagePersistence;
    public boolean disableDefaultErrorPopups;
    public boolean fastBusyIndicator;
    boolean replayNavigationOnReload;
    boolean generateHeadUpdate;
    String resourceVersion;
    String versionableTypes;
    public boolean includeScrollOffsets;
    public boolean focusManaged;
    public boolean reloadOnUpdateFailure;
    public boolean coalesceResources;
    public long warnBeforeExpiryInterval;
    public boolean clientSideElementUpdateDetermination;
    public boolean fileEntryRequireJavascript;
    public String publicContextPath;

    public EnvConfig(Map initMap) {
        init(initMap);
    }

    public void init(Map initMap) {
        StringBuilder info = new StringBuilder();

        autoRender = decodeBoolean(initMap, EnvUtils.ICEFACES_AUTO, true, info);
        autoId = decodeBoolean(initMap, EnvUtils.ICEFACES_AUTOID, true, info);
        ariaEnabled = decodeBoolean(initMap, EnvUtils.ARIA_ENABLED, true, info);
        blockUIOnSubmit = decodeBoolean(initMap, EnvUtils.BLOCK_UI_ON_SUBMIT, false, info);
        compressDOM = decodeBoolean(initMap, EnvUtils.COMPRESS_DOM, false, info);
        compressResources = decodeBoolean(initMap, EnvUtils.COMPRESS_RESOURCES, false, info);
        compressIDs = decodeBoolean(initMap, EnvUtils.COMPRESS_IDS, false, info);
        connectionLostRedirectURI = decodeString(initMap, EnvUtils.CONNECTION_LOST_REDIRECT_URI, null, info);
        diffConfig = decodeString(initMap, EnvUtils.DIFF_CONFIG, null, info);
        deltaSubmit = decodeBoolean(initMap, EnvUtils.DELTA_SUBMT, false, info);
        focusManaged = decodeBoolean(initMap, EnvUtils.FOCUS_MANAGED, true, info);
        lazyPush = decodeBoolean(initMap, EnvUtils.LAZY_PUSH, true, info);
        generateHeadUpdate = decodeBoolean(initMap, EnvUtils.GENERATE_HEAD_UPDATE, false, info);
        includeScrollOffsets = decodeBoolean(initMap, EnvUtils.INCLUDE_SCROLL_OFFSETS, true, info);
        reloadOnUpdateFailure = decodeBoolean(initMap, EnvUtils.RELOAD_ON_UPDATE_FAILURE, false, info);
        sessionExpiredRedirectURI = decodeString(initMap, EnvUtils.SESSION_EXPIRED_REDIRECT_URI, null, info);
        standardFormSerialization = decodeBoolean(initMap, EnvUtils.STANDARD_FORM_SERIALIZATION, false, info);
        strictSessionTimeout = decodeBoolean(initMap, EnvUtils.STRICT_SESSION_TIMEOUT, false, info);
        subtreeDiff = decodeBoolean(initMap, EnvUtils.SUBTREE_DIFF, true, info);
        windowScopeExpiration = decodeLong(initMap, EnvUtils.WINDOW_SCOPE_EXPIRATION, 1000, info);
        mandatoryResourceConfig = decodeString(initMap, EnvUtils.MANDATORY_RESOURCE_CONFIG, "none", info);
        uniqueResourceURLs = decodeBoolean(initMap, EnvUtils.UNIQUE_RESOURCE_URLS, true, info);
        lazyWindowScope = decodeBoolean(initMap, EnvUtils.LAZY_WINDOW_SCOPE, true, info);
        messagePersistence = decodeBoolean(initMap, EnvUtils.MESSAGE_PERSISTENCE, true, info);
        disableDefaultErrorPopups = decodeBoolean(initMap, EnvUtils.DISABLE_DEFAULT_ERROR_POPUPS, false, info);
        fastBusyIndicator = decodeBoolean(initMap, EnvUtils.FAST_BUSY_INDICATOR, false, info);
        replayNavigationOnReload = decodeBoolean(initMap, EnvUtils.REPLAY_NAVIGATION_ON_RELOAD, false, info);
        resourceVersion = decodeString(initMap, EnvUtils.RESOURCE_VERSION, ProductInfo.RESOURCE_VERSION, info);
        versionableTypes = decodeString(initMap, EnvUtils.VERSIONABLE_TYPES, DEFAULT_VERSIONABLE_TYPES, info);
        coalesceResources = decodeBoolean(initMap, EnvUtils.COALESCE_RESOURCES, false, info);
        warnBeforeExpiryInterval = decodeLong(initMap, EnvUtils.WARN_BEFORE_SESSION_EXPIRY_INTERVAL, -1L, info);
        clientSideElementUpdateDetermination = decodeBoolean(initMap, EnvUtils.CLIENT_SIDE_ELEMENT_UPDATE_DETERMINATION, false, info);
        fileEntryRequireJavascript = decodeBoolean(initMap, EnvUtils.ACE_FILE_ENTRY_REQUIRE_JAVASCRIPT, true, info);
        publicContextPath = decodeString(initMap, EnvUtils.PUBLIC_CONTEXT_PATH, null, info);

        log.info("ICEfaces Configuration: \n" + info);
    }

    public static EnvConfig getEnvConfig(FacesContext facesContext) {
        ExternalContext externalContext = facesContext.getExternalContext();
        Map appMap = externalContext.getApplicationMap();
        EnvConfig envConfig = (EnvConfig) appMap.get(EnvUtils.ICEFACES_ENV_CONFIG);
        if (null == envConfig) {
            envConfig = new EnvConfig(externalContext.getInitParameterMap());
            appMap.put(EnvUtils.ICEFACES_ENV_CONFIG, envConfig);
        }
        return envConfig;
    }


    boolean decodeBoolean(Map map, String name, boolean defaultValue, StringBuilder info) {
        String paramValue = (String) map.get(name);
        if (null == paramValue) {
            info.append(name).append(": ").append(defaultValue).append(" [default]\n");
            return defaultValue;
        }
        if ("true".equalsIgnoreCase(paramValue)) {
            info.append(name).append(": true\n");
            return true;
        }
        if ("false".equalsIgnoreCase(paramValue)) {
            info.append(name).append(": false\n");
            return false;
        }
        info.append(name).append(": ").append(defaultValue).append(" [default replacing malformed non-boolean value: ").append(paramValue).append("]\n");
        return defaultValue;
    }

    String decodeString(Map map, String name, String defaultValue, StringBuilder info) {
        String paramValue = (String) map.get(name);
        if (null == paramValue) {
            info.append(name).append(": ").append(defaultValue).append(" [default]\n");
            return defaultValue;
        }
        info.append(name).append(": ").append(paramValue).append("\n");
        return paramValue;
    }

    long decodeLong(Map map, String name, long defaultValue, StringBuilder info) {
        String paramValue = (String) map.get(name);
        if (null == paramValue) {
            info.append(name).append(" = ").append(defaultValue).append(" [default]\n");
            return defaultValue;
        }
        try {
            return Long.parseLong(paramValue);
        } catch (Exception e) {
            info.append(name).append(": ").append(defaultValue).append(" [default replacing malformed long value: ").append(paramValue).append("]\n");
        }
        return defaultValue;
    }

}
