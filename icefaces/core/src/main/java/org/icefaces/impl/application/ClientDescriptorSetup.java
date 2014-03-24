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

package org.icefaces.impl.application;

import org.icefaces.util.ClientDescriptor;
import org.icefaces.util.EnvUtils;

import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class ClientDescriptorSetup extends ResourceHandlerWrapper {
    private static final String KEY = "iceBrowser";
    private ResourceHandler handler;

    public ClientDescriptorSetup(ResourceHandler handler) {
        this.handler = handler;
    }

    public boolean isResourceRequest(FacesContext context) {
        HttpServletRequest servletRequest = EnvUtils.getSafeRequest(context);
        servletRequest.setAttribute(KEY, ClientDescriptor.getInstance(servletRequest));

        return super.isResourceRequest(context);
    }

    public ResourceHandler getWrapped() {
        return handler;
    }


}