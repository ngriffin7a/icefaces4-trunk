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

import java.net.URI;

/**
 * I am a resource registry meant to be used by the component renderers to load
 * Javascript code, CSS rules, and also register any kind of resource the
 * component renderer needs (such as images, movies, flash...).
 */
public interface ResourceRegistry {

    /**
     * Register Javascript code to be served and load the code by inserting a
     * reference to it into the page. The code will be loaded only once, the
     * number of method invocations or number of component instances using this
     * registry are irrelevant.
     *
     * @param resource the Javascript code
     * @return the URI of the resource
     */
    URI loadJavascriptCode(Resource resource);

    /**
     * Register Javascript code to be served and load the code by inserting a
     * reference to it into the page. The code will be loaded only once, the
     * number of method invocations or number of component instances using this
     * registry are irrelevant.
     *
     * @param resource      the main Javascript code
     * @param linkerHandler handler used to specify any other resource relatively
     *                      referenced by the main resource
     * @return the URI of the resource
     */
    URI loadJavascriptCode(Resource resource, ResourceLinker.Handler linkerHandler);

    /**
     * Register CSS rules to be served and load the rules by inserting a
     * reference to them into the page. The rules will be loaded only once, the
     * number of method invocations or number of component instances using this
     * registry are irrelevant.
     *
     * @param resource the css rules
     * @return the URI of the resource
     */
    URI loadCSSRules(Resource resource);

    /**
     * Register CSS rules to be served and load the rules by inserting a
     * reference to them into the page. The rules will be loaded only once, the
     * number of method invocations or number of component instances using this
     * registry are irrelevant.
     *
     * @param resource      the main css rules
     * @param linkerHandler handler used to specify any other resource relatively
     *                      referenced by the main resource (such as '@import' rules)
     * @return the URI of the resource
     */
    URI loadCSSRules(Resource resource, ResourceLinker.Handler linkerHandler);

    /**
     * Register resource to be served. The URI is encoded using
     * {@link javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext,String)} so
     * that proper resolution is achieved when template subdirectories or
     * forwards are used.
     *
     * @param resource the resource
     * @return the URI of the resource
     */
    URI registerResource(Resource resource);

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
    URI registerResource(Resource resource, ResourceLinker.Handler linkerHandler);
}