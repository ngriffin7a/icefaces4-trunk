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

package org.icefaces.impl.component;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public class NavigationNotifierHandler extends ResourceHandlerWrapper {
    private static final String RSH_JS = "navigation-notifier/rsh.js";
    private static final String RSH_BLANK_HTML = "navigation-notifier/blank.html";
    private static final String BLANK_HTML = "blank.html";
    private static final String UTF_8 = "UTF-8";
    private ResourceHandler handler;
    private Resource cachedRSHdotJS;

    public NavigationNotifierHandler(ResourceHandler handler) {
        this.handler = handler;
    }

    public ResourceHandler getWrapped() {
        return handler;
    }

    public Resource createResource(String resourceName) {
        return super.createResource(resourceName);
    }

    public Resource createResource(String resourceName, String libraryName) {
        final Resource resource = super.createResource(resourceName, libraryName);
        if (RSH_JS.equals(resourceName)) {
            if (cachedRSHdotJS == null) {
                try {
                    Resource blank = super.createResource(RSH_BLANK_HTML, libraryName);
                    String path = blank.getRequestPath();
                    byte[] content = readIntoByteArray(resource.getInputStream());
                    String replacedContent = new String(content, UTF_8).replaceAll(BLANK_HTML, path);
                    cachedRSHdotJS = new CachedResource(resource, replacedContent.getBytes(UTF_8));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            return cachedRSHdotJS;
        } else {
            return resource;
        }
    }

    public Resource createResource(String resourceName, String libraryName, String contentType) {
        return super.createResource(resourceName, libraryName, contentType);
    }

    private static byte[] readIntoByteArray(InputStream in) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead); // write
        }
        out.flush();

        return out.toByteArray();
    }

    private class CachedResource extends Resource {
        private Resource resource;
        private byte[] content;

        private CachedResource(Resource resource, byte[] content) {
            this.resource = resource;
            this.content = content;
            setContentType(resource.getContentType());
            setLibraryName(resource.getLibraryName());
            setResourceName(resource.getResourceName());
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(content);
        }

        public Map<String, String> getResponseHeaders() {
            return resource.getResponseHeaders();
        }

        public String getRequestPath() {
            return resource.getRequestPath();
        }

        public URL getURL() {
            return resource.getURL();
        }

        public boolean userAgentNeedsUpdate(FacesContext context) {
            return resource.userAgentNeedsUpdate(context);
        }
    }
}
