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

package org.icefaces.impl.event;

import org.icefaces.impl.renderkit.DOMRenderKit;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.resources.*;
import org.icefaces.util.EnvUtils;
import org.icefaces.util.UserAgentContext;

import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitWrapper;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MandatoryResourcesSetup implements SystemEventListener {
    private final static Logger log = Logger.getLogger(MandatoryResourcesSetup.class.getName());

    public boolean isListenerForSource(Object source) {
        if (!(source instanceof UIViewRoot)) {
            return false;
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(facesContext)) {
            // If ICEfaces is not configured for this view, we don't need to process this event.
            return false;
        }
        if (!EnvUtils.hasHeadAndBodyComponents(facesContext)) {
            // If ICEfaces is configured for this view, but the h:head and/or h:body components
            // are not available, we cannot process it but we log the reason.
            if (log.isLoggable(Level.WARNING)) {
                log.log(Level.WARNING, "ICEfaces configured for view " + facesContext.getViewRoot().getViewId() +
                        " but h:head and h:body components are required");
            }
            return false;
        }
        return true;
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        //make resource containers transient so that the removal and addition of resource is not track by the JSF state saving
        Collection<UIComponent> facets = context.getViewRoot().getFacets().values();
        for (UIComponent c: facets) {
            c.setInView(false);
        }
        //add mandatory resources, replace any resources previously added by JSF
        addMandatoryResources(context);
        //jsf.js might be added already by a page or component
        UIComponent jsfResource = ResourceOutputUtil.createTransientScriptResourceComponent("jsf.js", "javax.faces");
        //add jsf.js resource or replace it if already added by JSF
        addOrCollectReplacingResource(context, "jsf.js", "javax.faces", "head", jsfResource);
        //restore resource containers to non-transient state
        for (UIComponent c: facets) {
            c.setInView(true);
        }
    }

    private RenderKit findDOMRenderKit(FacesContext facesContext)  {
        RenderKit rk = facesContext.getRenderKit();

        //loop relies on DOMRenderKit being RenderKitWrapper
        while (rk instanceof RenderKitWrapper)  {
            if (rk instanceof DOMRenderKit)  {
                return rk;
            }
            rk = ((RenderKitWrapper) rk).getWrapped();
        }
        return rk;
    }


    private void addMandatoryResources(FacesContext context) {
        RenderKit rk = findDOMRenderKit(context);
        if (rk instanceof DOMRenderKit) {
            DOMRenderKit drk = (DOMRenderKit) rk;
            Set<ResourceInfo> addedResourceDependencies = new HashSet<ResourceInfo>();
            List<MandatoryResourceComponent> mandatoryResourceComponents = drk.getMandatoryResourceComponents();
            String resourceConfig = EnvUtils.getMandatoryResourceConfig(context);


            //pad with spaces to allow contains checking
            String resourceConfigPad = " " + resourceConfig + " ";
            UserAgentContext uaContext = UserAgentContext.getInstance(context);

            for (MandatoryResourceComponent mrc : mandatoryResourceComponents) {
                String compClassName = mrc.value();
                if (!"all".equalsIgnoreCase(resourceConfig)) {
                    String tagName = mrc.tagName();
                    if (!resourceConfigPad.contains(" " + compClassName + " ") &&
                            (tagName != null && tagName.length() > 0 &&
                                    !resourceConfigPad.contains(" " + tagName + " "))) {
                        continue;
                    }
                }
                try {
                    Class<UIComponent> compClass = (Class<UIComponent>) Class.forName(compClassName);
                    // Iterate over ResourceDependencies, ResourceDependency
                    // annotations, creating components for
                    // each unique one, so they'll add the mandatory
                    // resources.
                    ICEResourceDependencies resourceDependencies = compClass.getAnnotation(ICEResourceDependencies.class);
                    ICEResourceLibrary library = compClass.getAnnotation(ICEResourceLibrary.class);
                    ICEResourceDependency resourceDependency = compClass.getAnnotation(ICEResourceDependency.class);

                    if (resourceDependencies != null) {
                        for (ICEResourceDependency resDep : resourceDependencies.value()) {
                            ResourceInfo resInfo = ICEResourceUtils.getResourceInfo(uaContext, resDep, library);
                            if (resInfo != null)
                                addMandatoryResourceDependency(context, compClassName, addedResourceDependencies, resInfo);
                        }
                    }

                    ResourceInfo resInfo = ICEResourceUtils.getResourceInfo(uaContext, resourceDependency, library);
                    if (resInfo != null) {
                        addMandatoryResourceDependency(context, compClassName, addedResourceDependencies, resInfo);
                    }
                } catch (Exception e) {
                    if (log.isLoggable(Level.WARNING)) {
                        log.log(Level.WARNING, "When processing mandatory " +
                                "resource components, could not create instance " +
                                "of '" + compClassName + "'");
                    }
                }
            }
        }
    }

    private static void addMandatoryResourceDependency(
            FacesContext facesContext,
            String compClassName,
            Set<ResourceInfo> addedResDeps,
            ResourceInfo resDep) {
        if (addedResDeps.contains(resDep)) {
            return;
        }
        addedResDeps.add(resDep);
        addMandatoryResource(facesContext, compClassName, resDep.name,
                resDep.library, resDep.target);
    }

    private static void addMandatoryResource(FacesContext facesContext,
                                             String compClassName, String name,
                                             String library,
                                             String target) {
        if (target == null || target.length() == 0) {
            target = "head";
        }

        ResourceHandler resourceHandler = facesContext.getApplication().getResourceHandler();
        String rendererType = resourceHandler.getRendererTypeForResourceName(name);
        if (rendererType == null || rendererType.length() == 0) {
            if (log.isLoggable(Level.WARNING)) {
                log.log(Level.WARNING, "Could not determine renderer type " +
                        "for mandatory resource, for component: " + compClassName +
                        ". Resource name: " + name + ", library: " + library);
            }
        } else {
            UIComponent component = ResourceOutputUtil.createResourceComponent(name, library, rendererType, true);
            addOrCollectReplacingResource(facesContext, name, library, target, component);
        }
    }

    public static void addOrCollectReplacingResource(FacesContext context,
                                                     String name,
                                                     String library,
                                                     String target,
                                                     UIComponent component) {
        UIViewRoot viewRoot = context.getViewRoot();
        List<UIComponent> componentResources = viewRoot.getComponentResources(context, target);
        int position = -1;
        for (int i = 0; i < componentResources.size(); i++) {
            UIComponent c = componentResources.get(i);
            Map<String, Object> attributes = c.getAttributes();
            String resourceName = (String) attributes.get("name");
            String resourceLibrary = fixResourceParameter((String) attributes.get("library"));
            String normalizedLibrary = fixResourceParameter(library);
            if (name.equals(resourceName) && (normalizedLibrary == resourceLibrary/*both null*/ || normalizedLibrary.equals(resourceLibrary))) {
                position = i;
                break;
            }
        }

        if (position == -1) {
            viewRoot.addComponentResource(context, component, target);
        }
    }

    private static String calculateKey(String name, String library, String target) {
        return name + ":" + library + ":" + target;
    }

    private static String fixResourceParameter(String value) {
        return value == null || "".equals(value) ? null : value;
    }

}
