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

package com.icesoft.faces.component.gmap;

import org.icefaces.impl.util.Base64;
import org.icefaces.impl.util.Util;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GMapResourceHandler extends ResourceHandlerWrapper {
    private static final String GMAP_JS = "gmap/gmap.js";
    private static final String GMAP_MAIN_JS = "gmap/main.js";
    private static final byte[] NO_BYTES = new byte[0];
    private ResourceHandler handler;
    private String gmapKey;
    private Resource gmapJS;

    public GMapResourceHandler(ResourceHandler handler) {
        this.handler = handler;
        gmapKey = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("com.icesoft.faces.gmapKey");
    }

    public ResourceHandler getWrapped() {
        return handler;
    }

    public Resource createResource(String resourceName) {
        return createResource(resourceName, null, null);
    }

    public Resource createResource(String resourceName, String libraryName) {
        return createResource(resourceName, libraryName, null);
    }

    public Resource createResource(String resourceName, String libraryName, String contentType) {
        Resource resource = super.createResource(resourceName, libraryName, contentType);
        if (GMAP_JS.equals(resourceName) && gmapKey != null) {
            if (gmapJS == null) {
                //change returned resource to point to a different URI
				if(!FacesContext.getCurrentInstance().getExternalContext().isSecure())
                return gmapJS = recreateResource(resource, "http://maps.googleapis.com/maps/api/js?key=" + gmapKey + "&sensor=true");
				else
				return gmapJS = recreateResource(resource, "https://maps.googleapis.com/maps/api/js?key=" + gmapKey + "&sensor=true");
            } else {
                //return cached resource
                return gmapJS;
            }
        } 
		else {
            return resource;
        }
    }

    private Resource recreateResource(Resource resource, String url) {
        return new ResourceEntry(GMAP_JS, url);
    }

    private class ResourceEntry extends Resource {
        private Date lastModified = new Date();
        private String localPath;
        private String requestPath;

        private ResourceEntry(String localPath, String requestPath) {
            this.localPath = localPath;
            this.requestPath = requestPath;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(NO_BYTES);
        }

        public Map<String, String> getResponseHeaders() {
            return Collections.emptyMap();
        }

        public String getContentType() {
            return "text/javascript";
        }

        public String getRequestPath() {
            return requestPath;
        }

        public URL getURL() {
            try {
                return FacesContext.getCurrentInstance().getExternalContext().getResource(localPath);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean userAgentNeedsUpdate(FacesContext context) {
            try {
                Date modifiedSince = Util.parseHTTPDate(context.getExternalContext().getRequestHeaderMap().get("If-Modified-Since"));
                return lastModified.getTime() > modifiedSince.getTime() + 1000;
            } catch (Throwable e) {
                return true;
            }
        }

        private String eTag() {
            return Base64.encode(String.valueOf(localPath.hashCode()));
        }
    }

}
