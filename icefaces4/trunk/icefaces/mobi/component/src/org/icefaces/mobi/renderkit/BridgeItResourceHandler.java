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

package org.icefaces.mobi.renderkit;

import org.icefaces.impl.util.Base64;
import org.icefaces.impl.util.Util;
import org.icefaces.util.EnvUtils;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
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

public class BridgeItResourceHandler extends ResourceHandlerWrapper {
	private static final String BRIDGEIT_API = "core/bridgeit.js";
	private static final String ICEFACES_MOBI_LIB = "icefaces.mobi";
    private static final byte[] NO_BYTES = new byte[0];
    private ResourceHandler handler;
    private Resource apiJS = null;

    public BridgeItResourceHandler(ResourceHandler handler) {
        this.handler = handler;
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
        if (BRIDGEIT_API.equals(resourceName)) {
            if (apiJS == null) {
                    apiJS = recreateResource(super.createResource(resourceName, ICEFACES_MOBI_LIB),
                            "http://bridgeit.github.io/bridgeit.js/src/bridgeit.js");
            }
            return apiJS;
        } else {
            return super.createResource(resourceName, libraryName, contentType);
        }
    }

    private Resource recreateResource(Resource resource, String url) {
        return new ResourceEntry(BRIDGEIT_API, url);
    }

    private class ResourceEntry extends Resource {
        private Date lastModified = new Date();
        private String localPath;
        private String requestPath;

        private ResourceEntry(String localPath, String requestPath) {
            this.localPath = localPath;
            this.requestPath = requestPath;
        }

        public String getLibraryName() {
            return ICEFACES_MOBI_LIB;
        }

        public String getResourceName() {
            return BRIDGEIT_API;
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
