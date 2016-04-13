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

package org.icefaces.impl.push.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventListener;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.faces.FactoryFinder;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icefaces.impl.event.DebugTagListener;
import org.icefaces.util.EnvUtils;
import org.icepush.servlet.MainServlet;
import org.icepush.util.ExtensionRegistry;

public class ICEpushResourceHandler
extends ResourceHandlerWrapper
implements EventListener, PhaseListener, Serializable {
    private static final Logger LOGGER = Logger.getLogger(ICEpushResourceHandler.class.getName());

    public static final String BLOCKING_CONNECTION_RESOURCE_NAME = "listen.icepush.xml";
    public static final String CREATE_PUSH_ID_RESOURCE_NAME = "create-push-id.icepush.txt";
    public static final String NOTIFY_RESOURCE_NAME = "notify.icepush.txt";
    public static final String ADD_GROUP_MEMBER_RESOURCE_NAME = "add-group-member.icepush.txt";
    public static final String REMOVE_GROUP_MEMBER_RESOURCE_NAME = "remove-group-member.icepush.txt";
    public static final String ADD_NOTIFY_BACK_URI_NAME = "add-notify-back-uri.icepush.txt";
    public static final String HAS_NOTIFY_BACK_URI_NAME = "has-notify-back-uri.icepush.txt";
    public static final String REMOVE_NOTIFY_BACK_URI_NAME = "remove-notify-back-uri.icepush.txt";
    private static final Collection RESOURCES =
        Arrays.asList(
            BLOCKING_CONNECTION_RESOURCE_NAME,
            NOTIFY_RESOURCE_NAME,
            CREATE_PUSH_ID_RESOURCE_NAME,
            ADD_GROUP_MEMBER_RESOURCE_NAME,
            REMOVE_GROUP_MEMBER_RESOURCE_NAME,
            ADD_NOTIFY_BACK_URI_NAME,
            HAS_NOTIFY_BACK_URI_NAME,
            REMOVE_NOTIFY_BACK_URI_NAME
        );

    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    private transient AbstractICEpushResourceHandler resourceHandler;

    public ICEpushResourceHandler(final ResourceHandler resourceHandler) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        final ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String projectStage = servletContext.getInitParameter("javax.faces.PROJECT_STAGE");
        if (projectStage != null && !projectStage.equals("Production")) {
            facesContext.getApplication().subscribeToEvent(PostAddToViewEvent.class, new DebugTagListener());
        }
        if (EnvUtils.isICEpushPresent()) {
            String serverInfo = servletContext.getServerInfo();
            if (!serverInfo.startsWith("JBoss Web/3.0.0-CR1") && !serverInfo.startsWith("JBoss Web/3.0.0-CR2")) {
                this.resourceHandler = new ICEpushResourceHandlerImpl();
                this.resourceHandler.initialize(resourceHandler, servletContext, this);
            } else {
                final ICEpushResourceHandlerImpl impl = new ICEpushResourceHandlerImpl();
                this.resourceHandler = new BlockingICEpushResourceHandlerWrapper(impl);
                new Thread(
                    new Runnable() {
                        public void run() {
                            ICEpushResourceHandler.this.resourceHandler.initialize(resourceHandler, servletContext, ICEpushResourceHandler.this);
                            ICEpushResourceHandler.this.resourceHandler = impl;
                            lock.lock();
                            try {
                                condition.signalAll();
                            } finally {
                                lock.unlock();
                            }
                        }
                    }).start();
            }
        } else {
            this.resourceHandler = new ProxyICEpushResourceHandler(resourceHandler);
            LOGGER.log(Level.INFO, "Ajax Push Resource Handling not available.");
        }
    }

    public void afterPhase(final PhaseEvent event) {
        resourceHandler.afterPhase(event);
    }

    public void beforePhase(final PhaseEvent event) {
        resourceHandler.beforePhase(event);
    }

    @Override
    public Resource createResource(final String resourceName, final String libraryName, final String contentType) {
        return resourceHandler.createResource(resourceName, libraryName, contentType);
    }

    public PhaseId getPhaseId() {
        return resourceHandler.getPhaseId();
    }

    @Override
    public ResourceHandler getWrapped() {
        return resourceHandler.getWrapped();
    }

    @Override
    public void handleResourceRequest(final FacesContext facesContext) throws IOException {
        resourceHandler.handleResourceRequest(facesContext);
    }

    @Override
    public boolean isResourceRequest(final FacesContext facesContext) {
        return resourceHandler.isResourceRequest(facesContext);
    }

    public static void notifyContextShutdown(final ServletContext servletContext) {
        try {
            ((ICEpushResourceHandler)servletContext.getAttribute(ICEpushResourceHandler.class.getName())).
                shutdownMainServlet();
        } catch (final Throwable throwable) {
            //no need to log this exception for optional Ajax Push, but may be
            //useful for diagnosing other failures
            LOGGER.log(Level.FINE, "MainServlet not found in application scope: " + throwable);
        }
    }

    private void shutdownMainServlet() {
        resourceHandler.shutdownMainServlet();
    }

    private static abstract class AbstractICEpushResourceHandler
    extends ResourceHandlerWrapper
    implements EventListener, PhaseListener, Serializable {
        protected abstract void initialize(
            ResourceHandler resourceHandler, ServletContext servletContext,
            ICEpushResourceHandler icePushResourceHandler);

        protected abstract void shutdownMainServlet();
    }

    private static class ICEpushResourceHandlerImpl
    extends AbstractICEpushResourceHandler
    implements EventListener, PhaseListener, Serializable {
        private static final Logger LOGGER = Logger.getLogger(ICEpushResourceHandlerImpl.class.getName());

        private static final Pattern ICEpushRequestPattern = Pattern.compile(".*\\.icepush");
        private static final String RESOURCE_KEY = "javax.faces.resource";

        private final Lock mainServletLock = new ReentrantLock();

        private MainServlet mainServlet;
        private ResourceHandler resourceHandler;
        private ServletContext servletContext;

        private ICEpushResourceHandlerImpl() {
        }

        public void afterPhase(final PhaseEvent event) {
            // Do nothing.
        }

        public void beforePhase(final PhaseEvent event) {
            getMainServlet();
        }

        public PhaseId getPhaseId() {
            return PhaseId.RESTORE_VIEW;
        }

        @Override
        public ResourceHandler getWrapped() {
            return resourceHandler;
        }

        @Override
        public void handleResourceRequest(final FacesContext facesContext)
        throws IOException {
            String resourceName = facesContext.getExternalContext()
                    .getRequestParameterMap().get(RESOURCE_KEY);

            //Return safe, proxied versions of the request and response if we are
            //running in a portlet environment.
            HttpServletRequest request = EnvUtils.getSafeRequest(facesContext);
            HttpServletResponse response = EnvUtils.getSafeResponse(facesContext);

            if (RESOURCES.contains(resourceName)) {
                try {
                    getMainServlet().service(request, response);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            if (request instanceof ProxyHttpServletRequest) {
                resourceHandler.handleResourceRequest(facesContext);
                return;
            }
            String requestURI = request.getRequestURI();
            if (ICEpushRequestPattern.matcher(requestURI).find()) {
                try {
                    getMainServlet().service(request, response);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    resourceHandler.handleResourceRequest(facesContext);
                } catch (IOException e) {
                    //capture & log Tomcat specific exception
                    if (e.getClass().getName().endsWith("ClientAbortException")) {
                        LOGGER.fine("Browser closed the connection prematurely for " + requestURI);
                    } else if ("Broken pipe".equals(e.getMessage())) {
                        LOGGER.fine("Browser connection was severed " + requestURI);
                    } else if (e.getMessage().contains("Connection reset by peer")) {
                        LOGGER.fine("Browser reset the connection for " + requestURI);
                    } else {
                        throw e;
                    }
                }
            }
        }

        @Override
        public boolean isResourceRequest(final FacesContext facesContext) {
            String resourceName = facesContext.getExternalContext()
                    .getRequestParameterMap().get(RESOURCE_KEY);
            if (RESOURCES.contains(resourceName)) {
                return true;
            }
            ExternalContext externalContext = facesContext.getExternalContext();
            if (EnvUtils.instanceofPortletRequest(externalContext.getRequest())) {
                return resourceHandler.isResourceRequest(facesContext);
            }
            HttpServletRequest servletRequest = (HttpServletRequest) externalContext.getRequest();
            String requestURI = servletRequest.getRequestURI();
            boolean _check1 = resourceHandler.isResourceRequest(facesContext);
            boolean _check2 = ICEpushRequestPattern.matcher(requestURI).find();
            return _check1 || _check2;
        }

        protected MainServlet getMainServlet() {
            getMainServletLock().lock();
            try {
                if (mainServlet == null) {
                    try {
                        mainServlet =
                            (MainServlet)
                                ((Class)ExtensionRegistry.getBestExtension(servletContext, "org.icepush.MainServlet")).
                                    getMethod("getInstance", new Class[]{ServletContext.class}).
                                        invoke(null, servletContext);
                    } catch (final Exception exception) {
                        LOGGER.log(Level.SEVERE, "Cannot instantiate extension org.icepush.MainServlet.", exception);
                        throw new RuntimeException(exception);
                    }
                }
                return mainServlet;
            } finally {
                getMainServletLock().unlock();
            }
        }

        protected Lock getMainServletLock() {
            return mainServletLock;
        }

        @Override
        protected void initialize(
            final ResourceHandler resourceHandler, final ServletContext servletContext,
            final ICEpushResourceHandler icePushResourceHandler) {

            this.resourceHandler = resourceHandler;
            this.servletContext = servletContext;
            this.servletContext.setAttribute(ICEpushResourceHandler.class.getName(), icePushResourceHandler);
            LifecycleFactory lifecycleFactory = (LifecycleFactory)FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            Iterator<String> lifecycleIds = lifecycleFactory.getLifecycleIds();
            while (lifecycleIds.hasNext()) {
                String lifecycleId = lifecycleIds.next();
                Lifecycle lifecycle = lifecycleFactory.getLifecycle(lifecycleId);
                lifecycle.addPhaseListener(icePushResourceHandler);
            }
        }

        @Override
        protected void shutdownMainServlet() {
            getMainServlet().shutdown();
        }
    }

    private static class BlockingICEpushResourceHandlerWrapper
    extends AbstractICEpushResourceHandler
    implements EventListener, PhaseListener, Serializable {
        private static final Logger LOGGER = Logger.getLogger(BlockingICEpushResourceHandlerWrapper.class.getName());

        private final ICEpushResourceHandlerImpl resourceHandler;

        private BlockingICEpushResourceHandlerWrapper(final ICEpushResourceHandlerImpl resourceHandler) {
            this.resourceHandler = resourceHandler;
        }

        public void afterPhase(final PhaseEvent event) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                resourceHandler.afterPhase(event);
            } finally {
                lock.unlock();
            }
        }

        public void beforePhase(final PhaseEvent event) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                resourceHandler.beforePhase(event);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public Resource createResource(final String resourceName) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.createResource(resourceName);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public Resource createResource(final String resourceName, final String libraryName) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.createResource(resourceName, libraryName);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public Resource createResource(final String resourceName, final String libraryName, final String contentType) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.createResource(resourceName, libraryName, contentType);
            } finally {
                lock.unlock();
            }
        }

        public PhaseId getPhaseId() {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.getPhaseId();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public String getRendererTypeForResourceName(final String resourceName) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.getRendererTypeForResourceName(resourceName);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public ResourceHandler getWrapped() {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.getWrapped();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void handleResourceRequest(final FacesContext facesContext)
        throws IOException {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                resourceHandler.handleResourceRequest(facesContext);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public boolean isResourceRequest(final FacesContext facesContext) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.isResourceRequest(facesContext);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public boolean libraryExists(final String libraryName) {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                return resourceHandler.libraryExists(libraryName);
            } finally {
                lock.unlock();
            }
        }

        @Override
        protected void initialize(
            final ResourceHandler resourceHandler, final ServletContext servletContext,
            final ICEpushResourceHandler icePushResourceHandler) {

            this.resourceHandler.initialize(resourceHandler, servletContext, icePushResourceHandler);
        }

        @Override
        protected void shutdownMainServlet() {
            lock.lock();
            try {
                if (resourceHandler.mainServlet == null) {
                    condition.awaitUninterruptibly();
                }
                resourceHandler.shutdownMainServlet();
            } finally {
                lock.unlock();
            }
        }
    }

    private static class ProxyICEpushResourceHandler
    extends AbstractICEpushResourceHandler
    implements EventListener, PhaseListener, Serializable {
        private static final Logger LOGGER = Logger.getLogger(ProxyICEpushResourceHandler.class.getName());

        private final ResourceHandler resourceHandler;

        public ProxyICEpushResourceHandler(final ResourceHandler resourceHandler) {
            this.resourceHandler = resourceHandler;
        }

        public void afterPhase(final PhaseEvent event) {
            // Do nothing.
        }

        public void beforePhase(final PhaseEvent event) {
            // Do nothing.
        }

        public PhaseId getPhaseId() {
            return null;
        }

        @Override
        public ResourceHandler getWrapped() {
            return resourceHandler;
        }

        @Override
        protected void initialize(
            final ResourceHandler resourceHandler, final ServletContext servletContext,
            final ICEpushResourceHandler icePushResourceHandler) {

            // Do nothing.
        }

        @Override
        protected void shutdownMainServlet() {
            // Do nothing.
        }
    }
}
