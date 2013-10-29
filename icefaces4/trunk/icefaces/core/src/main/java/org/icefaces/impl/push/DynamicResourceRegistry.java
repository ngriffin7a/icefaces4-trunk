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

package org.icefaces.impl.push;

import org.icefaces.impl.push.DynamicResourceDispatcher;
import org.icefaces.impl.push.http.DynamicResource;
import org.icefaces.impl.push.http.DynamicResourceLinker;

import javax.faces.context.FacesContext;
import java.net.URI;
import java.util.Map;

public interface DynamicResourceRegistry {
    /**
     * Register resource to be served. The URI is encoded using
     * {@link javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext,String)} so
     * that proper resolution is achieved when template subdirectories or
     * forwards are used.
     *
     * @param resource the resource
     * @return the URI of the resource
     */
    URI registerResource(DynamicResource resource);

    /**
     * Register resource to be served. The URI is encoded using
     * {@link javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext,String)} so
     * that proper resolution is achieved when template subdirectories or
     * forwards are used.
     *
     * @param resource      the resource
     * @param linkerHandler handler used to specify any other resource relatively
     *                      referenced by the main resource (such as '@import' rules)
     * @return the URI of the resource
     */
    URI registerResource(DynamicResource resource, DynamicResourceLinker.Handler linkerHandler);

    public class Locator {

        public static DynamicResourceRegistry locate(FacesContext context) {
            Map applicationMap = context.getExternalContext().getApplicationMap();
            return (DynamicResourceRegistry) applicationMap.get(DynamicResourceDispatcher.class.getName());
        }
    }
}
