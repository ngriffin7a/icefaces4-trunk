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

import org.icefaces.impl.event.ResourceOrdering;
import org.icefaces.util.EnvUtils;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderComponentEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoalescingResourceHandler extends ResourceHandlerWrapper {

    private final static Logger log = Logger.getLogger(CoalescingResourceHandler.class.getName());

    public static final String COALESCED = "coalesced";
    public static final String CSS_EXTENSION = ".css";
    public static final String COALESCED_CSS = COALESCED + CSS_EXTENSION;
    public static final String JS_EXTENSION = ".js";
    public static final String COALESCED_JS = COALESCED + JS_EXTENSION;
    public static final String ICE_CORE_LIBRARY = "ice.core";
    private ResourceHandler handler;
    private String mapping;

    public CoalescingResourceHandler(ResourceHandler handler) {
        this.handler = handler;
    }

    public ResourceHandler getWrapped() {
        return handler;
    }

    public Resource createResource(String resourceName, String libraryName, String contentType) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (EnvUtils.isCoallesceResources(context)) {

            ExternalContext ec = context.getExternalContext();
            boolean isPortlet = EnvUtils.instanceofPortletRequest(ec.getRequest());

            if (ICE_CORE_LIBRARY.equals(libraryName) && COALESCED_CSS.equals(resourceName)) {
                CoalescingResource.Infos resourceInfos = getResourceInfos(context, CSS_EXTENSION);
                if( isPortlet ){
                    return new CoalescingPortletResource(COALESCED_CSS,
                                                         ICE_CORE_LIBRARY,
                                                         getMapping(context),
                                                         isExtensionMapping(context),
                                                         resourceInfos);
                } else {
                    return new CoalescingResource(COALESCED_CSS,
                                                  ICE_CORE_LIBRARY,
                                                  getMapping(context),
                                                  isExtensionMapping(context),
                                                  resourceInfos);
                }
            } else if (ICE_CORE_LIBRARY.equals(libraryName) && COALESCED_JS.equals(resourceName)) {
                CoalescingResource.Infos resourceInfos = getResourceInfos(context, JS_EXTENSION);
                if( isPortlet ){
                    return new CoalescingPortletResource(COALESCED_JS,
                                                         ICE_CORE_LIBRARY,
                                                         getMapping(context),
                                                         isExtensionMapping(context),
                                                         resourceInfos);
                } else {
                    return new CoalescingResource(COALESCED_JS,
                                                  ICE_CORE_LIBRARY,
                                                  getMapping(context),
                                                  isExtensionMapping(context),
                                                  resourceInfos);
                }
            } else {
                return super.createResource(resourceName, libraryName, contentType);
            }
        } else {
            return super.createResource(resourceName, libraryName, contentType);
        }
    }

    private static CoalescingResource.Infos getResourceInfos(FacesContext fc, String extension){
        HttpSession sess = EnvUtils.getSafeSession(fc);
        return (CoalescingResource.Infos)sess.getAttribute(CoalescingResourceHandler.class.getName() + extension);
    }

    public Resource createResource(String resourceName) {
        return this.createResource(resourceName, null);
    }

    public Resource createResource(String resourceName, String libraryName) {
        return this.createResource(resourceName, libraryName, null);
    }

    public static class ResourceCollector implements SystemEventListener, ResourceOrdering.ResourceIterator {
        private ArrayList<CoalescingResource.Info> orderedResourceInfos = new ArrayList<CoalescingResource.Info>();

        public void resource(String name, String library, String target) {
            orderedResourceInfos.add(new CoalescingResource.Info(name, library));
        }

        public ResourceCollector() {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceOrdering resourceOrdering = (ResourceOrdering) context.getExternalContext().getApplicationMap().get(ResourceOrdering.class.getName());
            resourceOrdering.traverseOrderedResources(this);

        }

        public boolean isListenerForSource(Object source) {
            return EnvUtils.isICEfacesView(FacesContext.getCurrentInstance()) && (source instanceof UIViewRoot);
        }

        public void processEvent(SystemEvent event) {
            FacesContext context = FacesContext.getCurrentInstance();
            UIViewRoot root = (UIViewRoot) event.getSource();
            replaceResources(context, root, CSS_EXTENSION);
            replaceResources(context, root, JS_EXTENSION);
        }

        private void replaceResources(FacesContext context, UIViewRoot root, String extension) {
            UIComponent resourceContainer = getHeadResourceContainer(root);
            resourceContainer.setInView(false);

            UIOutput coallescedResourceComponent = new UIOutput();
            Map attrs = coallescedResourceComponent.getAttributes();
            String name = COALESCED + extension;
            attrs.put("name", name);
            attrs.put("library", ICE_CORE_LIBRARY);
            ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
            String rendererType = resourceHandler.getRendererTypeForResourceName(name);
            coallescedResourceComponent.setRendererType(rendererType);
            coallescedResourceComponent.setTransient(true);

            HttpSession sess = EnvUtils.getSafeSession(context);
            CoalescingResource.Infos previousResourceInfos = getResourceInfos(context,extension);

            CoalescingResource.Infos resourceInfos = new CoalescingResource.Infos();
            List children = resourceContainer.getChildren();
            ArrayList<UIComponent> toBeReAdded = new ArrayList<UIComponent>();
            for (UIComponent next : new ArrayList<UIComponent>(children)) {
                Map<String, Object> nextAttributes = next.getAttributes();
                String nextName = (String) nextAttributes.get("name");
                if (nextName == null) {
                    //cannot process component resources without names
                    continue;
                } else {
                    String nextLibrary = (String) nextAttributes.get("library");
                    String iceType = (String) nextAttributes.get("ice.type");
                    Resource nextResource = null;
                    if (iceType == null) {
                        nextResource = resourceHandler.createResource(nextName, nextLibrary);
                    }
                    if (nextName.endsWith(extension) && !"jsf.js".equals(nextName) &&
                            nextResource != null && !isExternalResource(nextResource)) {
                        CoalescingResource.Info info = new CoalescingResource.Info(nextName, nextLibrary);

                        if (!context.isPostback() || previousResourceInfos.resources.contains(info)) {
                            resourceInfos.resources.add(info);
                        } else {
                            toBeReAdded.add(next);
                        }
                        root.removeComponentResource(context, next);
                    }
                }
            }

            //add back the additional resource components, but make sure the coalesced resource is first
            root.addComponentResource(context, coallescedResourceComponent);
            for (UIComponent next : toBeReAdded) {
                root.addComponentResource(context, next);
            }

            HttpServletRequest originalRequest = EnvUtils.getOriginalServletRequest(context);
            if (previousResourceInfos == null) {
                sess.setAttribute(CoalescingResourceHandler.class.getName() + extension, resourceInfos);
            } else {
                ArrayList<CoalescingResource.Info> currentResources = resourceInfos.resources;
                ArrayList<CoalescingResource.Info> previousResources = previousResourceInfos.resources;
                //replace the resource infos only if there are new resources found in the new list
                if (previousResources.equals(currentResources)) {
                    //force a reload of the coalesced resource -- this is mostly to make theme changing work
                    previousResourceInfos.modified = !context.isPostback();
                } else {
                    ArrayList<CoalescingResource.Info> sameRequestPreviousResources = (ArrayList<CoalescingResource.Info>) originalRequest.getAttribute(CoalescingResourceHandler.class.getName() + extension);
                    //merge resources that are required by different views but for the same request
                    if (sameRequestPreviousResources != null) {
                        //the resources collected previously are merged with the currently collected ones so that when multiple
                        //views are processed (such as in portlets) the coalescing resource contains the resources needed by all the views
                        ArrayList<CoalescingResource.Info> viewMergedResources = new ArrayList<CoalescingResource.Info>();
                        Iterator<CoalescingResource.Info> i = orderedResourceInfos.iterator();
                        while (i.hasNext()) {
                            CoalescingResource.Info info = i.next();
                            if (currentResources.contains(info) || sameRequestPreviousResources.contains(info)) {
                                viewMergedResources.add(info);
                            }
                        }
                        //append remaining unordered resources
                        HashSet<CoalescingResource.Info> remainingResources = new HashSet();
                        remainingResources.addAll(currentResources);
                        remainingResources.addAll(sameRequestPreviousResources);
                        remainingResources.removeAll(viewMergedResources);
                        viewMergedResources.addAll(remainingResources);

                        resourceInfos.resources = viewMergedResources;
                        resourceInfos.modified = true;
                    }
                    sess.setAttribute(CoalescingResourceHandler.class.getName() + extension, resourceInfos);
                }
            }
            originalRequest.setAttribute(CoalescingResourceHandler.class.getName() + extension, resourceInfos.resources);

            resourceContainer.setInView(true);
        }

        /**
         * Avoid coalescing any resources that are "external" to the application. This mainly
         * pertains to the scripts required for Google Maps API.
         */
        private static boolean isExternalResource(Resource rez){
            String path = rez.getRequestPath();
            URI rezURI = URI.create(path);

            //As a first check, external resource URIs must be absolute.  Absolute URIs must include
            //a scheme. So if it's not absolute, it cannot refer to an external resource.
            if( !rezURI.isAbsolute() ){
                return false;
            }

            //However, portlet resources can have a scheme (e.g. http) but not be external
            String rezHost = rezURI.getHost();
            if(rezHost == null){
                return false;
            }

            //Check if the host for the resource is the same as the host identified in the request.  If
            //they don't match then we consider the request an "external" request.
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String requestHost = ec.getRequestServerName();
            if(rezHost.trim().equals(requestHost)){
                return false;
            }

            log.log(Level.FINE,"external resource detected: resource host [" + rezHost + "] is different from request host [" + requestHost + "]");
            return true;
        }
    }

    private static UIComponent getHeadResourceContainer(UIViewRoot root) {
        String facetName = EnvUtils.isMojarra() ? "javax_faces_location_HEAD": "head";
        return root.getFacets().get(facetName);
    }


    private String getMapping(FacesContext context) {
        if (mapping == null) {
            ExternalContext extContext = context.getExternalContext();
            String servletPath = extContext.getRequestServletPath();
            String pathInfo = extContext.getRequestPathInfo();

            if (servletPath == null) {
                mapping = "/";
            } else if (servletPath.length() == 0) {
                mapping = "/*";
            }
            if (pathInfo != null) {
                mapping = servletPath;
            } else if (servletPath.indexOf(".") > -1) {
                mapping = servletPath.substring(servletPath.lastIndexOf('.'));
            } else {
                mapping = servletPath;
            }
        }

        return mapping;
    }

    private boolean isExtensionMapping(FacesContext context) {
        String m = getMapping(context);
        return (m != null && !m.startsWith("/"));
    }


    //register ResourceCollector dynamically to make sure it is invoked last when PreRenderComponentEvent is fired
    public static class RegisterListener implements SystemEventListener {
        public void processEvent(SystemEvent event) throws AbortProcessingException {
            FacesContext context = FacesContext.getCurrentInstance();
            if (EnvUtils.isCoallesceResources(context)) {
                    context.getApplication().subscribeToEvent(PreRenderComponentEvent.class, new ResourceCollector());
            }
        }

        public boolean isListenerForSource(Object source) {
            return true;
        }
    }
}
