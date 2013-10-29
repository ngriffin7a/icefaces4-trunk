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

import org.icefaces.resources.*;
import org.icefaces.util.UserAgentContext;

import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.*;

public class RestoreResourceDependencies implements SystemEventListener {
    private static final Set<VisitHint> HINTS = EnumSet.of(VisitHint.SKIP_ITERATION);

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final UserAgentContext uaContext = UserAgentContext.getInstance(facesContext);
        UIViewRoot viewRoot = facesContext.getViewRoot();
        VisitContext visitContext = VisitContext.createVisitContext(facesContext, null, HINTS);


        viewRoot.visitTree(visitContext, new VisitCallback() {
            public VisitResult visit(VisitContext context, UIComponent target) {
                VisitResult result = VisitResult.ACCEPT;
                Class<UIComponent> compClass = (Class<UIComponent>) target.getClass();

                ICEResourceDependencies resourceDependencies = compClass.getAnnotation(ICEResourceDependencies.class);
                ICEResourceDependency resourceDependency = compClass.getAnnotation(ICEResourceDependency.class);
                ICEResourceLibrary library = compClass.getAnnotation(ICEResourceLibrary.class);

                if (resourceDependencies != null) {
                    for (ICEResourceDependency resDep : resourceDependencies.value()) {
                        ResourceInfo resInfo = ICEResourceUtils.getResourceInfo(uaContext, resDep, library);
                        if (resInfo != null) addResourceDependency(facesContext, resInfo);
                    }
                }

                ResourceInfo resInfo = ICEResourceUtils.getResourceInfo(uaContext, resourceDependency, library);
                if (resInfo != null)
                    addResourceDependency(facesContext, resInfo);

                return result;
            }
        });
    }

    private void addResourceDependency(FacesContext context, ResourceInfo resourceInfo) {
        UIViewRoot viewRoot = context.getViewRoot();
        ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
        String name = resourceInfo.name;
        String library = resourceInfo.library;
        String target = resourceInfo.target;
        target = target == null || "".equals(target) ? "head" : target;

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

        //add only if missing
        if (position == -1) {
            String rendererType = resourceHandler.getRendererTypeForResourceName(name);
            viewRoot.addComponentResource(context, ResourceOutputUtil.createResourceComponent(name, library, rendererType, true), target);
        }
    }

    public boolean isListenerForSource(Object source) {
        return source instanceof UIViewRoot;
    }

    private static String fixResourceParameter(String value) {
        return value == null || "".equals(value) ? null : value;
    }
}
