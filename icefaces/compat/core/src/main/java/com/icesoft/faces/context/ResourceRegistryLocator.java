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

package com.icesoft.faces.context;

import org.icefaces.impl.push.DynamicResourceRegistry;
import org.icefaces.impl.push.http.DynamicResource;
import org.icefaces.impl.push.http.DynamicResourceLinker;

import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.icesoft.faces.util.CoreUtils;

public class ResourceRegistryLocator {
    public static ResourceRegistry locate(FacesContext context) {
        Map applicationMap = context.getExternalContext().getApplicationMap();
        if (applicationMap.containsKey(ResourceRegistry.class.getName())) {
            return (ResourceRegistry) applicationMap.get(ResourceRegistry.class.getName());
        } else {
            ResourceRegistry registry = new DynamicResourceDispatcherAdapter(DynamicResourceRegistry.Locator.locate(context), context);
            applicationMap.put(ResourceRegistry.class.getName(), registry);
            return registry;
        }
    }

    private static class DynamicResourceDispatcherAdapter implements ResourceRegistry {
        private ArrayList cssRuleURIs = new ArrayList();
        private ArrayList jsCodeURIs = new ArrayList();
        private DynamicResourceRegistry resourceDispatcher;

        private DynamicResourceDispatcherAdapter(DynamicResourceRegistry resourceDispatcher, FacesContext context) {
            this.resourceDispatcher = resourceDispatcher;
        }

        public URI loadJavascriptCode(final Resource resource) {
            String uri = resourceDispatcher.registerResource(new DynamicResourceAdapter(resource)).toString();
            if (!jsCodeURIs.contains(uri)) {
                jsCodeURIs.add(uri);
            }
            return resolve(uri);
        }

        public URI loadJavascriptCode(Resource resource, final ResourceLinker.Handler linkerHandler) {
            String uri = resourceDispatcher.registerResource(new DynamicResourceAdapter(resource), new DynamicHandlerAdapter(linkerHandler)).toString();
            if (!jsCodeURIs.contains(uri)) {
                jsCodeURIs.add(uri);
            }
            return resolve(uri);
        }

        public URI loadCSSRules(Resource resource) {
            String uri = resourceDispatcher.registerResource(new DynamicResourceAdapter(resource)).toString();
            if (!cssRuleURIs.contains(uri)) {
                cssRuleURIs.add(uri);
            }
            return resolve(uri);
        }


        public URI loadCSSRules(Resource resource, ResourceLinker.Handler linkerHandler) {
            String uri = resourceDispatcher.registerResource(new DynamicResourceAdapter(resource), new DynamicHandlerAdapter(linkerHandler)).toString();
            if (!cssRuleURIs.contains(uri)) {
                cssRuleURIs.add(uri);
            }
            return resolve(uri);
        }

        public URI registerResource(Resource resource) {
            return resolve(resourceDispatcher.registerResource(new DynamicResourceAdapter(resource)).toString());
        }

        public URI registerResource(Resource resource, ResourceLinker.Handler linkerHandler) {
            return resolve(resourceDispatcher.registerResource(new DynamicResourceAdapter(resource), new DynamicHandlerAdapter(linkerHandler)).toString());
        }

        private URI resolve(String uri) {
            FacesContext context = FacesContext.getCurrentInstance();
            return URI.create(CoreUtils.resolveResourceURL(context, uri));
        }

        private static class DynamicResourceAdapter implements DynamicResource, Serializable {
            private final Resource resource;

            public DynamicResourceAdapter(Resource resource) {
                this.resource = resource;
            }

            public String calculateDigest() {
                return resource.calculateDigest();
            }

            public InputStream open() throws IOException {
                return resource.open();
            }

            public Date lastModified() {
                return resource.lastModified();
            }

            public void withOptions(final Options options) throws IOException {
                resource.withOptions(new Resource.Options() {
                    public void setMimeType(String mimeType) {
                        options.setMimeType(mimeType);
                    }

                    public void setLastModified(Date date) {
                        options.setLastModified(date);
                    }

                    public void setFileName(String fileName) {
                        options.setFileName(fileName);
                    }

                    public void setExpiresBy(Date date) {
                        options.setExpiresBy(date);
                    }

                    public void setAsAttachement() {
                        options.setAsAttachement();
                    }
                });
            }
        }

        private static class DynamicHandlerAdapter implements DynamicResourceLinker.Handler {
            private final ResourceLinker.Handler linkerHandler;

            public DynamicHandlerAdapter(ResourceLinker.Handler linkerHandler) {
                this.linkerHandler = linkerHandler;
            }

            public void linkWith(final DynamicResourceLinker linker) {
                linkerHandler.linkWith(new ResourceLinker() {
                    public void registerRelativeResource(String path, Resource resource) {
                        linker.registerRelativeResource(path, new DynamicResourceAdapter(resource));
                    }
                });
            }
        }
    }

    public interface ExtendedResourceOptions extends Resource.Options {
        public void setContentDispositionFileName(String contentDispositionFileName);
    }
}
