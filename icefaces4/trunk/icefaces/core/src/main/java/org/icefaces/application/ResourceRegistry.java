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

package org.icefaces.application;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.el.ELContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.application.Application;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.servlet.http.HttpSession;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.icefaces.util.EnvUtils;
import org.icefaces.impl.util.Util;
import org.icefaces.impl.application.WindowScopeManager;

/**
 * <p>
 *   The <code>ResourceRegistry</code> allows an application to register
 *   javax.faces.application.Resource instances at runtime.  Each Resource
 *   is registered in a specified scope (Application, Session, View, Flash
 *   Window) so that the resource can be garbage collected when the scope
 *   expires.
 * </p>
 */
public class ResourceRegistry extends ResourceHandlerWrapper  {
    private static Logger log = Logger.getLogger(ResourceRegistry.class.getName());
    private ResourceHandler wrapped;

    private static String CURRENT_KEY = "org.icefaces.resourceRegistry.resourceKey";
    private static String RESOURCE_PREFIX = "/javax.faces.resource/";
    private static String MAP_PREFIX = "org.icefaces.resource-";
    private static String BYTES_PREFIX = "bytes=";
    private static String CONTENT_LENGTH = "Content-Length";
    private static String CONTENT_RANGE = "Content-Range";
    private static String RANGE = "Range";

    public ResourceRegistry(ResourceHandler wrapped)  {
        this.wrapped = wrapped;
    }

    public ResourceHandler getWrapped() {
        return wrapped;
    }


    public void handleResourceRequest(FacesContext facesContext) throws IOException {
        ExternalContext externalContext = facesContext.getExternalContext();
        Application application = facesContext.getApplication();
        String key = extractResourceId(facesContext);
        log.finest("extractResourceId: " + key);

        boolean useRanges = false;
        int rangeStart = 0;
        int rangeEnd = 0; 
        String rangeHeader = externalContext.getRequestHeaderMap()
                .get(RANGE);
        if (null != rangeHeader)  {
            try {
                if (rangeHeader.startsWith(BYTES_PREFIX))  {
                    String range = rangeHeader
                            .substring(BYTES_PREFIX.length() );
                    int splitIndex = range.indexOf("-");
                    String startString = range.substring(0, splitIndex);
                    String endString = range.substring(splitIndex + 1);
                    rangeStart = Integer.parseInt(startString);
                    // ICE-9256 rangeEnd == 0 means use contentLength below
                    if (!"".equals( endString )) {
                        rangeEnd = Integer.parseInt(endString);
                    }
                    useRanges = true;
                }
            } catch (Exception e)  {
                useRanges = false;
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Unable to decode range header " + rangeHeader);
                }
            }
        }

        if (useRanges)  {
            externalContext.setResponseStatus(206);
        }

        if (log.isLoggable(Level.FINE)) {
            log.fine("handleResourceRequest " + key + " path: " +
                    externalContext.getRequestServletPath() + " info: " +
                    externalContext.getRequestPathInfo());
        }
        if (null == key)  {
            wrapped.handleResourceRequest(facesContext);
            return;
        }

        ELContext elContext = facesContext.getELContext();
        
        ResourceRegistryHolder holder = (ResourceRegistryHolder) elContext
            .getELResolver().getValue(elContext, null, MAP_PREFIX + key);
        log.finest("ELResolver ResourceRegistryHolder: " + holder);

        //For portlets it may be necessary to use a backup plan for setting and getting
        //session based dynamic resources
        if (null == holder)  {
            HttpSession session = EnvUtils.getSafeSession(facesContext, false);
            if (session != null) {
                holder = (ResourceRegistryHolder) session.getAttribute(MAP_PREFIX + key);
                log.finest("Session ResourceRegistryHolder: " + holder);
                if (null != holder)  {
                    //workaround for ICE-7685 suspecting PFB bug
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Resource lookup required direct sesssion access");
                    }
                }
            }
        }

        if (null == holder)  {
            wrapped.handleResourceRequest(facesContext);
            return;
        }
        //TODO: also check the name
        Resource resource = holder.resource;
        log.finest("Resource: " + resource);
        String contentType = resource.getContentType();
        if (contentType != null) {
            externalContext.setResponseContentType(resource.getContentType());
        }
        Map<String,String> headers = resource.getResponseHeaders();
        String contentLength = "";
        for (String header : headers.keySet())  {
            if (useRanges)  {
                if (CONTENT_LENGTH.equals(header))  {
                    contentLength = headers.get(CONTENT_LENGTH);
                    continue;
                 }
            }
            externalContext.setResponseHeader(header, headers.get(header));
        }

        InputStream in = resource.getInputStream();
        OutputStream out = externalContext.getResponseOutputStream();

        if (Util.acceptGzip(externalContext) && 
                EnvUtils.isCompressResources(facesContext) && 
                Util.shouldCompress(resource.getContentType()) )  {

            externalContext.setResponseHeader("Content-Encoding", "gzip");
            Util.compressStream(in, out);

        } else {
            //ranges can be used for subsequent uncompressed responses
            externalContext.setResponseHeader("Accept-Ranges", "bytes");

            if (useRanges)  {
                int cl = Integer.parseInt(contentLength);
                rangeEnd = (rangeEnd == 0) ? cl-1: rangeEnd;
                externalContext.setResponseHeader(CONTENT_RANGE,
                        "bytes " + rangeStart + "-" + rangeEnd + "/" +
                        contentLength );
                externalContext.setResponseHeader(CONTENT_LENGTH, 
                        "" + (1 + rangeEnd - rangeStart));
                Util.copyStream(in, out, rangeStart, rangeEnd);
            } else {
                Util.copyStream(in, out);
            }

        }

    }

    /**
     * Add the provided resource to the custom scope Map.  This is intended to 
     * be used only in cases not covered by the other scope-specific methods.
     *
     * @param scopeMap the resource
     * @param scopeMap the resource
     * @return the requestPath of the resource
     */
    public static String addResource(Map scopeMap, Resource resource)  {
        return addResource("r", scopeMap, resource );
    }

    private static String addResource(String prefix, Map scopeMap, 
            Resource resource)  {
        String name = resource.getResourceName();
        String key;
        if ( (null != name) && (name.length() > 0) )  {
            key = name;
        } else {
            key = prefix + UUID.randomUUID().toString();
        }
        ResourceRegistryHolder holder =
                new ResourceRegistryHolder(key, resource);
        scopeMap.put(MAP_PREFIX + key, holder);
        if ("s".equals(prefix))  {
            EnvUtils.getSafeSession(FacesContext.getCurrentInstance()).setAttribute(MAP_PREFIX + key, holder);
        }
        String[] pathTemplate = EnvUtils.getPathTemplate();
        String path = pathTemplate[0] + key + pathTemplate[1];
        path = FacesContext.getCurrentInstance().getExternalContext().encodeResourceURL(path);
        log.finest("\nresourceName: " + name + "\nkey: " + key + "\nholder: " + holder + "\npath: " + path);
        return path;
    }

    private static String extractResourceId(FacesContext facesContext)  {
        ExternalContext externalContext = facesContext.getExternalContext();

        int markerStart = -1;
        String path = externalContext.getRequestServletPath();
        if( path != null ){
            markerStart = path.indexOf(RESOURCE_PREFIX);
        }
        if (-1 == markerStart)  {
            path = externalContext.getRequestPathInfo();
            if( path != null ){
                markerStart = path.indexOf(RESOURCE_PREFIX);
            }
        }

        // With Liferay (and likely portals in general), the reference to javax.faces.resource
        // gets set to a parameter rather than part of the URL so we need a slightly different algorithm.
        if (-1 == markerStart)  {
            final String resId = "javax.faces.resource";
            Iterator names = externalContext.getRequestParameterNames();
            while (names.hasNext()) {
                String name =  (String)names.next();

                // With the older bridge, the resource identifier was not be encoded in any way
                // but with the newer bridge it is so we need to check both cases.
                if( name.equalsIgnoreCase(resId) || name.endsWith(resId) ){
                    String res = externalContext.getRequestParameterMap().get(name);
                    //Need to strip the file extension (e.g. .jsf) if it's there because the original key
                    //did not use it.
                    String[] pathTemplate = EnvUtils.getPathTemplate();
                    String suffix = pathTemplate[1];
                    if(suffix != null && suffix.trim().length() > 0 ){
                        int suffixStart = res.indexOf(suffix);
                        if(suffixStart > 0){
                            res = res.substring(0,suffixStart);
                        }
                    }
                    return res;
                }
            }
        }
        if (-1 == markerStart)  {
            return null;
        }
        try {
            //strip off the javax.faces.resource prefix and remove
            //any extension found in the path template
            String key = path.substring(
                    markerStart + RESOURCE_PREFIX.length(), 
                    path.length() - EnvUtils.getPathTemplate()[1].length());
            return key;
        } catch (Exception e)  {
            log.log(Level.FINE, "could not extract resource id", e);
            return null;
        }
    }

    public static Resource getResourceByName(FacesContext facesContext, String resName) {
        log.finest("resName: " + resName);
        log.finest("lookup : '" + MAP_PREFIX + resName + "'");
        if (resName == null) {
            return null;
        }
        ELContext elContext = facesContext.getELContext();
        ResourceRegistryHolder holder = (ResourceRegistryHolder) elContext
            .getELResolver().getValue(elContext, null, MAP_PREFIX + resName);
        log.finest("ELResolver ResourceRegistryHolder: " + holder);
        if (holder == null) {
            HttpSession session = EnvUtils.getSafeSession(facesContext, false);
            if (session != null) {
                holder = (ResourceRegistryHolder) session.getAttribute(MAP_PREFIX + resName);
                log.finest("Session ResourceRegistryHolder: " + holder);
            }
        }
        if (null == holder)  {
            return null;
        }
        return holder.resource;
    }

    /**
     * Add the provided resource in application scope.
     *
     * @param resource the resource
     * @return the requestPath of the resource
     */
    public static String addApplicationResource(Resource resource)  {
        return addResource("a", FacesContext.getCurrentInstance()
                .getExternalContext().getApplicationMap(), resource );
    }

    /**
     * Add the provided resource in session scope.  Note that session scope
     * resources should be Serializable to support cluster replication
     * and session passivation.
     *
     * @param resource the resource
     * @return the requestPath of the resource
     */
    public static String addSessionResource(Resource resource)  {
        return addResource("s", FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap(), resource );
    }

    /**
     * Add the provided resource in flash scope.
     *
     * @param resource the resource
     * @return the requestPath of the resource
     */
    public static String addFlashResource(Resource resource)  {
        return addResource("f", FacesContext.getCurrentInstance()
                .getExternalContext().getFlash(), resource );
    }

    /**
     * Add the provided resource in view scope.
     *
     * @param resource the resource
     * @return the requestPath of the resource
     */
    public static String addViewResource(Resource resource)  {
        return addResource("v", FacesContext.getCurrentInstance()
                .getViewRoot().getViewMap(), resource );
    }

    /**
     * Add the provided resource in window scope.
     *
     * @param resource the resource
     * @return the requestPath of the resource
     */
    public static String addWindowResource(Resource resource)  {
        return addResource("w", WindowScopeManager.lookupWindowScope(
                FacesContext.getCurrentInstance()), resource );
    }


}

//Hold the resources in an instance of this private class to provide
//security by ensuring that only resources stored via this API are served
class ResourceRegistryHolder implements Serializable {
    public String key;
    public Resource resource;
    
    ResourceRegistryHolder(String key, Resource resource)  {
        this.key = key;
        this.resource = resource;
    }
}
