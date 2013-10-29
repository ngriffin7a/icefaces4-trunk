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
package org.icefaces.ace.component.graphicimage;


import org.icefaces.application.ResourceRegistry;
import org.icefaces.ace.util.Attribute;
import org.icefaces.ace.util.IceOutputResource;

import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GraphicImage extends GraphicImageBase {
    private static final Logger logger =
            Logger.getLogger(GraphicImage.class.toString());
    //src is NOT part of the pass through attributes
    private Attribute[] attributesNames = {new Attribute("alt", null),
            new Attribute("height", null),
            new Attribute("width", null),
            new Attribute("style", null),
            new Attribute("lang", null),
            new Attribute("usemap", null),
            new Attribute("longdesc", null),
            new Attribute("title", null),
            new Attribute("onclick", "click"),
            new Attribute("onkeypress", "keypress")};

    private Attribute[] booleanAttNames = {new Attribute("readonly", null),
            new Attribute("disabled", null)};


    public String processSrcAttribute(FacesContext facesContext, Object o, String name, String mimeType,
                                      String scope) {
//        ResourceRegistry registry = this.lookupRegistry(facesContext);
        if (o instanceof IceOutputResource) {
            //register resource..
            IceOutputResource iceResource = (IceOutputResource) o;
            //set name for resource to component id??
            return registerAndGetPath(scope, iceResource);
        }
//	        if (o instanceof Resource){
//	        	//it's a jsf resource ...do we want to support that??
//	        	Resource resource = (Resource)o;
//	        	return resource.getRequestPath();
//	        }
        if (o instanceof byte[]) {
            // have to create the resource first and cache it in ResourceRegistry
            //create IceOutputResource
            IceOutputResource ior = new IceOutputResource(name, o, mimeType);
            String registeredPath = registerAndGetPath(scope, ior);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Returning path=" + registeredPath + " FOR SCOPE+" + scope);
            }
            return registeredPath;
        } else {
            // just do default from compat ImageRenderer as it's a static file
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("just getting the string representation");
            }
            return processStaticImage(facesContext);
        }
    }


    private String registerAndGetPath(String scope,
                                      IceOutputResource iceResource) {
        String registeredPath = "";
        if (scope.equals("flash")) {
            registeredPath = ResourceRegistry.addFlashResource(iceResource);
        } else if (scope.equals("application")) {
            registeredPath = ResourceRegistry.addApplicationResource(iceResource);
        } else if (scope.equals("window"))
            registeredPath = ResourceRegistry.addWindowResource(iceResource);
        else if (scope.equals("view"))
            registeredPath = ResourceRegistry.addViewResource(iceResource);
        else if (scope.equals("session"))
            registeredPath = ResourceRegistry.addSessionResource(iceResource);
        return registeredPath;
    }


    protected String processStaticImage(FacesContext facesContext) {
        String value = (String) super.getValue();
        // support url as an alias for value
        if (value == null) {
            value = super.getUrl();
        }
        if (value != null) {
//            String ctxtPath = facesContext.getExternalContext().getRequestContextPath();
            return facesContext.getApplication().getViewHandler().getResourceURL(facesContext, value);
        } else {
            return "";
        }
    }

    private ResourceRegistry lookupRegistry(FacesContext facesContext) {
        Map applicationMap = facesContext.getExternalContext().getApplicationMap();
        if (applicationMap.containsKey(ResourceRegistry.class.getName())) {
            ResourceRegistry rr = (ResourceRegistry) applicationMap.get(ResourceRegistry.class.getName());
            return rr;
        }
        return null;
    }

    public Attribute[] getAttributesNames() {
        return attributesNames;
    }

    public void setAttributesNames(Attribute[] attributesNames) {
        this.attributesNames = attributesNames;
    }


    public Attribute[] getBooleanAttNames() {
        return booleanAttNames;
    }


    public void setBooleanAttNames(Attribute[] booleanAttNames) {
        this.booleanAttNames = booleanAttNames;
    }

    private static String getImageSource(FacesContext context, UIComponent component, String attrName) {
        String resourceName = (String) component.getAttributes().get("name");
        if (resourceName != null) {
            String libName = (String) component.getAttributes().get("library");
            ResourceHandler handler = context.getApplication().getResourceHandler();
            Resource res = handler.createResource(resourceName, libName);
            if (res == null) {
                if (context.isProjectStage(ProjectStage.Development)) {
                    String msg = "Unable to find resource " + resourceName;
                    context.addMessage(component.getClientId(context),
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    msg,
                                    msg));
                }
                return "RES_NOT_FOUND";
            } else {
                String requestPath = res.getRequestPath();
                return context.getExternalContext().encodeResourceURL(requestPath);
            }
        } else {
            String value = (String) component.getAttributes().get(attrName);
            if (value == null || value.length() == 0) {
                return "";
            }
            if (value.contains(ResourceHandler.RESOURCE_IDENTIFIER)) {
                return value;
            } else {
                value = context.getApplication().getViewHandler().
                        getResourceURL(context, value);
                return (context.getExternalContext().encodeResourceURL(value));
            }
        }
    }
}
