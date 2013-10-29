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

import org.icefaces.bean.AllWindowsClosed;
import org.icefaces.bean.WindowDisposed;
import org.icefaces.impl.push.SessionViewManager;
import org.icefaces.util.EnvUtils;

import javax.annotation.PreDestroy;
import javax.faces.application.Application;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WindowScopeManager extends SessionAwareResourceHandlerWrapper {
    public static final String SessionSynchronizationMonitor = WindowScopeManager.class.getName() + "$SessionSynchronizationMonitor";
    public static final String ScopeName = "window";
    private static final Logger log = Logger.getLogger(WindowScopeManager.class.getName());
    private static final String seed = Integer.toString(new Random().nextInt(1000), 36);
    private static SharedMapLookupStrategy sharedMapLookupStrategy;

    static {
        try {
            sharedMapLookupStrategy = new LiferayOriginalRequestWindowScopeSharing();
        } catch (Exception e) {
            sharedMapLookupStrategy = new TimeBasedHeuristicWindowScopeSharing();
        }
    }

    private ResourceHandler wrapped;

    public WindowScopeManager(ResourceHandler wrapped) {
        this.wrapped = wrapped;
    }

    public ResourceHandler getWrapped() {
        return wrapped;
    }

    public void handleSessionAwareResourceRequest(FacesContext facesContext) throws IOException {
        wrapped.handleResourceRequest(facesContext);
    }

    public boolean isSessionAwareResourceRequest(FacesContext facesContext) {
        ExternalContext externalContext = facesContext.getExternalContext();
        Map parameters = externalContext.getRequestParameterMap();
        if (isDisposeWindowRequest(parameters)) {
            //force the running of the JSF lifecycle so that the registered phase listener has a chance to destroy
            //the @WindowDisposed annotated view scope beans
            return false;
        }
        return wrapped.isResourceRequest(facesContext);
    }

    public static ScopeMap lookupWindowScope(FacesContext context) {
        String id = lookupAssociatedWindowID(context.getExternalContext().getRequestMap());
        State state = getState(context);
        return state == null ? null : (ScopeMap) state.windowScopedMaps.get(id);
    }

    public static String determineWindowID(FacesContext context) {

        // ICE-7749: By default, in portals, the ExternalContext.getSessionMap() returns a map of
        // values that are in PortletSession.PORTLET_SCOPE.  But for the synchronization monitor we
        // are using here, it's initially put into PortletSession.APPLICATION_SCOPE so we need to
        // access it using our own session proxy which, with portlets, defaults to APPLICATION_SCOPE.
        HttpSession safeSession = EnvUtils.getSafeSession(context);
        Object synchronizationMonitor = safeSession.getAttribute(SessionSynchronizationMonitor);
        if (synchronizationMonitor == null) {
            log.log(Level.FINE, "synchronization monitor not set by session listener");
            synchronizationMonitor = new SynchronizationMonitorObject();
            safeSession.setAttribute(SessionSynchronizationMonitor, synchronizationMonitor);
        }

        synchronized (synchronizationMonitor) {
            State state = getState(context);
            ExternalContext externalContext = context.getExternalContext();
            String id = externalContext.getRequestParameterMap().get("ice.window");
            try {
                for (Object scopeMap : new ArrayList(state.windowScopedMaps.values())) {
                    ScopeMap map = (ScopeMap) scopeMap;
                    if (!map.getId().equals(id)) {
                        map.disactivateIfUnused(context);
                    }
                }
            } catch (Throwable e) {
                log.log(Level.FINE, "Failed to remove window scope map", e);
            }
            try {
                for (Object scopeMap : new ArrayList(state.disposedWindowScopedMaps)) {
                    ScopeMap map = (ScopeMap) scopeMap;
                    if (!map.getId().equals(id)) {
                        map.discardIfExpired(context);
                    }
                }
            } catch (Throwable e) {
                log.log(Level.FINE, "Failed to remove window scope map", e);
            }

            Map requestMap = externalContext.getRequestMap();
            if (id == null) {
                ScopeMap scopeMap = sharedMapLookupStrategy.lookup(context);

                if (scopeMap == null) {
                    if (state.disposedWindowScopedMaps.isEmpty()) {
                        scopeMap = new ScopeMap(context);
                    } else {
                        scopeMap = (ScopeMap) state.disposedWindowScopedMaps.removeFirst();
                    }
                    scopeMap.activate(state);
                }

                associateWindowID(scopeMap.id, requestMap);
                return scopeMap.id;
            } else {
                if (state.windowScopedMaps.containsKey(id)) {
                    associateWindowID(id, requestMap);
                    return id;
                } else {
                    //this must be a postback request while the window is reloading or redirecting
                    for (Object disposedScopeMap : new ArrayList(state.disposedWindowScopedMaps)) {
                        ScopeMap scopeMap = (ScopeMap) disposedScopeMap;
                        if (scopeMap.getId().equals(id)) {
                            scopeMap.activate(state);
                            associateWindowID(id, requestMap);
                            return id;
                        }
                    }

                    //unknown window scope, corresponding ScopeMap might have been erased when not used (no beans stored in it)
                    //most probably a postback received after the server or application was restarted
                    ScopeMap scopeMap = new ScopeMap(context);
                    scopeMap.activate(state);
                    associateWindowID(scopeMap.id, requestMap);
                    return scopeMap.id;
                }
            }
        }
    }

    private static boolean isDisposeWindowRequest(Map parameters) {
        return "ice.dispose.window".equals(parameters.get("ice.submit.type"));
    }

    private static synchronized String generateID() {
        return seed + Long.toString(System.currentTimeMillis(), 36);
    }

    public static void disposeWindows(final HttpSession session) {
        // The strategy is to invoke @PreDestroy on as many applicable window scoped beans as possible and not to bail
        // out on the first fail.
        State state = (State) session.getAttribute(WindowScopeManager.class.getName());

        //ICE-9071: It's possible for a session to expire without a WindowScopeManager if the session was created and
        //invalidated outside of ICEfaces' scope.
        if(state == null){
            return;
        }

        notifyPreDestroyForAll(state.windowScopedMaps.values());
        notifyPreDestroyForAll(state.disposedWindowScopedMaps);
    }

    private static void notifyPreDestroyForAll(Collection<ScopeMap> scopeMaps) {
        for (final ScopeMap scopeMap : scopeMaps) {
            if (!scopeMap.isPreDestroyInvoked()) {
                try {
                    notifyPreDestroy(scopeMap.values());
                } finally {
                    scopeMap.preDestroyInvoked();
                }
            }
        }
    }

    private static void notifyPreDestroy(Collection beans) {
        try {
            for (final Object bean : beans) {
                try {
                    callAnnotatedMethod(bean, PreDestroy.class);
                } catch (Exception exception) {
                    log.log(Level.FINE,
                            "An exception occurred while trying to invoke @PreDestroy on a window scoped bean: " +
                                    exception.getMessage());
                }
            }
        } catch (Exception exception) {
            log.log(Level.FINE,
                    "An exception occurred while trying to invoke @PreDestroy on window scoped beans: " +
                            exception.getMessage());
        }
    }

    public static synchronized void disposeWindow(final FacesContext context, String id, Timer timer) {
        final State state = getState(context);
        ScopeMap scopeMap = (ScopeMap) state.windowScopedMaps.get(id);
        //verify if the ScopeMap is present
        //it's possible to have dispose-window request arriving after an application restart or re-deploy
        if (scopeMap != null) {
            scopeMap.disactivate(state);
            scopeMap.discardIfExpired(context);
        }

        //notify annotated scope beans that all windows were closed
        if (state.windowScopedMaps.isEmpty()) {
            long windowScopeExpiration = EnvUtils.getWindowScopeExpiration(context);
            //copy session beans before they're cleared on FacesContext.release()
            final Map session = new HashMap(context.getExternalContext().getSessionMap());
            timer.schedule(new AllWindowsClosedNotifier(state, session), windowScopeExpiration * 2);
        }

        disposeViewScopeBeans(context);
    }

    private static void disposeViewScopeBeans(FacesContext facesContext) {
        ExceptionHandler oldHandler = facesContext.getExceptionHandler();
        //all Exceptions will be ignored since no further action can be taken
        //during window disposal
        facesContext.setExceptionHandler(new DiscardingExceptionHandler(oldHandler));
        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (null == viewRoot) {
            return;
        }
        Map viewMap = viewRoot.getViewMap();
        Iterator keys = viewMap.keySet().iterator();
        while (keys.hasNext()) {
            Object key = keys.next();
            Object object = viewMap.get(key);
            if (object.getClass().isAnnotationPresent(WindowDisposed.class)) {
                keys.remove();
                callAnnotatedMethod(object, PreDestroy.class);
                if (log.isLoggable(Level.FINE)) {
                    log.log(Level.FINE, "Closing window disposed ViewScoped bean " + key);
                }
            }
        }
        facesContext.setExceptionHandler(oldHandler);
    }

    private static void callAnnotatedMethod(Object object, Class annotation) {
        Class theClass = object.getClass();
        try {
            while (null != theClass) {
                Method[] methods = object.getClass().getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(annotation)) {
                        method.setAccessible(true);
                        method.invoke(object);
                        return;
                    }
                }
                theClass = theClass.getSuperclass();
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Failed to invoke" + annotation + " on " + theClass, e);
        }
    }

    public static void sessionCreated(HttpSession session) {
        session.setAttribute(SessionSynchronizationMonitor, new SynchronizationMonitorObject());
    }

    public static class ScopeMap extends HashMap {
        private String id = generateID();
        private long activateTimestamp = System.currentTimeMillis();
        private long deactivateTimestamp = -1;
        private boolean preDestroyInvoked;

        public String getId() {
            return id;
        }

        public ScopeMap(FacesContext facesContext) {
            boolean processingEvents = facesContext.isProcessingEvents();
            try {
                facesContext.setProcessingEvents(true);
                ScopeContext context = new ScopeContext(ScopeName, this);
                facesContext.getApplication().publishEvent(facesContext, PostConstructCustomScopeEvent.class, context);
            } finally {
                facesContext.setProcessingEvents(processingEvents);
                sharedMapLookupStrategy.associate(facesContext, id);
            }
        }

        private void disactivateIfUnused(FacesContext facesContext) {
            if (!EnvUtils.containsBeans(this)) {
                //the map *does not* contain objects (most probably beans) other than the ones inserted by the framework
                disactivate(getState(facesContext));
            }
        }

        private void discardIfExpired(FacesContext facesContext) {
            State state = getState(facesContext);
            if (System.currentTimeMillis() > (deactivateTimestamp + state.expirationPeriod)) {
                try {
                    if (!isPreDestroyInvoked()) {
                        boolean processingEvents = facesContext.isProcessingEvents();
                        try {
                            facesContext.setProcessingEvents(true);
                            ScopeContext context = new ScopeContext(ScopeName, this);
                            facesContext.getApplication().publishEvent(facesContext, PreDestroyCustomScopeEvent.class, context);
                        } finally {
                            preDestroyInvoked();
                            facesContext.setProcessingEvents(processingEvents);
                        }
                    }
                } finally {
                    state.disposedWindowScopedMaps.remove(this);
                }
            }
        }

        private void activate(State state) {
            state.windowScopedMaps.put(id, this);
            activateTimestamp = System.currentTimeMillis();
        }

        private void disactivate(State state) {
            deactivateTimestamp = System.currentTimeMillis();
            state.disposedWindowScopedMaps.addLast(state.windowScopedMaps.remove(id));
        }

        public void preDestroyInvoked() {
            preDestroyInvoked = true;
        }

        public boolean isPreDestroyInvoked() {
            return preDestroyInvoked;
        }
    }

    public static String lookupAssociatedWindowID(Map requestMap) {
        return (String) requestMap.get(WindowScopeManager.class.getName());
    }

    public static void associateWindowIDToRequest(String id, FacesContext facesContext) {
        associateWindowID(id, facesContext.getExternalContext().getRequestMap());
    }

    private static void associateWindowID(String id, Map requestMap) {
        requestMap.put(WindowScopeManager.class.getName(), id);
    }

    private static State getState(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(false);
        if (session != null) {
            return EnvUtils.instanceofPortletSession(session) ?
                    getPortletState(context, session) : getServletState(context, session);
        } else {
            return null;
        }
    }

    private static State getServletState(FacesContext context, Object session) {
        HttpSession servletSession = (HttpSession) session;
        State state = (State) servletSession.getAttribute(WindowScopeManager.class.getName());
        if (state == null) {
            state = new State(EnvUtils.getWindowScopeExpiration(context));
            servletSession.setAttribute(WindowScopeManager.class.getName(), state);
        }
        return state;
    }

    //portlet session is accessed in a separate method to avoid ClassNotFound errors in servlet environment
    private static State getPortletState(FacesContext context, Object session) {
        javax.portlet.PortletSession portletSession = (javax.portlet.PortletSession) session;
        State state = (State) portletSession.getAttribute(WindowScopeManager.class.getName(), javax.portlet.PortletSession.APPLICATION_SCOPE);
        if (state == null) {
            state = new State(EnvUtils.getWindowScopeExpiration(context));
            portletSession.setAttribute(WindowScopeManager.class.getName(), state, javax.portlet.PortletSession.APPLICATION_SCOPE);
        }
        return state;
    }

    public static class State implements Externalizable {
        public HashMap windowScopedMaps = new HashMap();
        private LinkedList disposedWindowScopedMaps = new LinkedList();
        public long expirationPeriod;

        public State() {
        }

        private State(long expirationPeriod) {
            this.expirationPeriod = expirationPeriod;
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(windowScopedMaps);
            out.writeObject(disposedWindowScopedMaps);
            out.writeLong(expirationPeriod);
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            windowScopedMaps = (HashMap) in.readObject();
            disposedWindowScopedMaps = (LinkedList) in.readObject();
            expirationPeriod = in.readLong();
        }
    }

    private static interface SharedMapLookupStrategy {
        ScopeMap lookup(FacesContext context);

        void associate(FacesContext context, String windowID);
    }

    private static class TimeBasedHeuristicWindowScopeSharing implements SharedMapLookupStrategy {
        private long sameWindowMaxDelay = -1;

        public ScopeMap lookup(FacesContext context) {
            if (sameWindowMaxDelay == -1) {
                sameWindowMaxDelay = EnvUtils.getWindowScopeExpiration(context);
            }

            State state = getState(context);
            Iterator i = state.windowScopedMaps.values().iterator();
            while (i.hasNext()) {
                ScopeMap sm = (ScopeMap) i.next();
                if (sm.activateTimestamp + sameWindowMaxDelay > System.currentTimeMillis()) {
                    return sm;
                }
            }

            return null;
        }

        public void associate(FacesContext context, String windowID) {
            //cannot share the windowID
        }
    }

    private static class LiferayOriginalRequestWindowScopeSharing implements SharedMapLookupStrategy {
        private Class PortalUtilClass;
        private Method GetHttpServletRequest;
        private Method GetOriginalServletRequest;

        private LiferayOriginalRequestWindowScopeSharing() throws ClassNotFoundException, NoSuchMethodException {
            PortalUtilClass = Class.forName("com.liferay.portal.util.PortalUtil");
            GetHttpServletRequest = PortalUtilClass.getDeclaredMethod("getHttpServletRequest", javax.portlet.PortletRequest.class);
            GetOriginalServletRequest = PortalUtilClass.getDeclaredMethod("getOriginalServletRequest", HttpServletRequest.class);
        }

        public ScopeMap lookup(FacesContext context) {
            State state = getState(context);
            ExternalContext externalContext = context.getExternalContext();
            HttpServletRequest originalRequest = getOriginalServletRequest(externalContext);
            String sharedWindowID = (String) originalRequest.getAttribute(WindowScopeManager.class.getName());
            return sharedWindowID == null ? null : (ScopeMap) state.windowScopedMaps.get(sharedWindowID);
        }

        public void associate(FacesContext context, String windowID) {
            ExternalContext externalContext = context.getExternalContext();
            HttpServletRequest originalRequest = getOriginalServletRequest(externalContext);
            originalRequest.setAttribute(WindowScopeManager.class.getName(), windowID);

        }

        private HttpServletRequest getOriginalServletRequest(ExternalContext externalContext) {
            try {
                javax.portlet.PortletRequest portletRequest = (javax.portlet.PortletRequest) externalContext.getRequest();
                HttpServletRequest httpPortletRequest = (HttpServletRequest) GetHttpServletRequest.invoke(PortalUtilClass, portletRequest);
                HttpServletRequest originalRequest = (HttpServletRequest) GetOriginalServletRequest.invoke(PortalUtilClass, httpPortletRequest);
                return originalRequest;
            } catch (InvocationTargetException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        }
    }

    static class DiscardingExceptionHandler extends ExceptionHandlerWrapper {
        ExceptionHandler wrapped;

        public DiscardingExceptionHandler(ExceptionHandler wrapped) {
            this.wrapped = wrapped;
        }

        public void processEvent(SystemEvent exceptionQueuedEvent) {
            Throwable throwable = ((ExceptionQueuedEvent) exceptionQueuedEvent)
                    .getContext().getException();
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "Exception during window disposal " + throwable);
            }
        }

        public javax.faces.context.ExceptionHandler getWrapped() {
            return wrapped;
        }
    }

    public static class DetermineOrDisposeScope implements PhaseListener {
        private Timer timer = new Timer("WindowScopeManager timer", true);

        public DetermineOrDisposeScope() {
            FacesContext context = FacesContext.getCurrentInstance();
            Application application = context.getApplication();
            //shutdown timer
            application.subscribeToEvent(PreDestroyApplicationEvent.class, new SystemEventListener() {
                public boolean isListenerForSource(Object source) {
                    return true;
                }

                public void processEvent(SystemEvent event) {
                    timer.cancel();
                }
            });
        }

        public void afterPhase(final PhaseEvent event) {
            FacesContext facesContext = event.getFacesContext();
            ExternalContext externalContext = facesContext.getExternalContext();
            Map parameters = externalContext.getRequestParameterMap();
            if (isDisposeWindowRequest(parameters)) {
                //shortcut the lifecycle to avoid running it with certain parts discarded or disposed
                facesContext.responseComplete();
                String windowID = (String) parameters.get("ice.window");
                disposeWindow(facesContext, windowID, timer);
                if (EnvUtils.isICEpushPresent()) {
                    try {
                        String[] viewIDs = externalContext.getRequestParameterValuesMap().get("ice.view");
                        for (int i = 0; i < viewIDs.length; i++) {
                            SessionViewManager.removeView(facesContext, viewIDs[i]);
                        }
                    } catch (RuntimeException e) {
                        //missing ice.view parameters means that none of the views within the page
                        //was registered with PushRenderer before page unload
                        log.log(Level.FINE, "Exception during dispose-window ", e);
                    }
                }
            }
        }

        public void beforePhase(final PhaseEvent event) {
            FacesContext context = FacesContext.getCurrentInstance();
            try {
                WindowScopeManager.determineWindowID(context);
            } catch (Exception e) {
                log.log(Level.FINE, "Unable to set up WindowScope ", e);
            }
        }

        public PhaseId getPhaseId() {
            return PhaseId.RESTORE_VIEW;
        }
    }

    public static class SaveScopeState implements PhaseListener {

        public void afterPhase(final PhaseEvent event) {
            FacesContext context = FacesContext.getCurrentInstance();
            try {
                ExternalContext externalContext = context.getExternalContext();
                Object session = externalContext.getSession(false);
                if (session != null) {
                    if (EnvUtils.instanceofPortletSession(session)) {
                        javax.portlet.PortletSession portletSession = (javax.portlet.PortletSession) session;
                        Object state = portletSession.getAttribute(WindowScopeManager.class.getName(), javax.portlet.PortletSession.APPLICATION_SCOPE);
                        if (state != null) {
                            portletSession.setAttribute(WindowScopeManager.class.getName(), state, javax.portlet.PortletSession.APPLICATION_SCOPE);
                        }
                    } else {
                        HttpSession servletSession = (HttpSession) session;
                        Object state = servletSession.getAttribute(WindowScopeManager.class.getName());
                        if (state != null) {
                            servletSession.setAttribute(WindowScopeManager.class.getName(), state);
                        }
                    }
                }
            } catch (Exception e) {
                log.log(Level.FINE, "Unable to reset WindowScope", e);
            }
        }

        public void beforePhase(final PhaseEvent event) {
        }

        public PhaseId getPhaseId() {
            return PhaseId.RENDER_RESPONSE;
        }
    }

    private static class AllWindowsClosedNotifier extends TimerTask {
        private final State state;
        private final Map session;

        public AllWindowsClosedNotifier(State state, Map session) {
            this.state = state;
            this.session = session;
        }

        public void run() {
            //re-verify that all windows are still closed
            if (state.windowScopedMaps.isEmpty()) {
                ArrayList expiredMaps = new ArrayList();
                //collect expired maps
                Iterator disposedMaps = state.disposedWindowScopedMaps.iterator();
                while (disposedMaps.hasNext()) {
                    ScopeMap scopeMap = (ScopeMap) disposedMaps.next();
                    if (System.currentTimeMillis() > (scopeMap.deactivateTimestamp + state.expirationPeriod)) {
                        expiredMaps.add(scopeMap);
                    }
                }
                //notify @PreDestroy on window scoped beans
                notifyPreDestroyForAll(expiredMaps);
                //notify @AllWindowsClosed on session scoped beans
                Iterator objects = session.values().iterator();
                while (objects.hasNext()) {
                    Object object = objects.next();
                    callAnnotatedMethod(object, AllWindowsClosed.class);
                }
            }
        }
    }

    private static class SynchronizationMonitorObject implements Serializable {
    }
}