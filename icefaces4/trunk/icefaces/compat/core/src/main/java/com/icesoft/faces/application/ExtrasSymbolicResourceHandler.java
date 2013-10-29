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

package com.icesoft.faces.application;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.FacesContext;

public class ExtrasSymbolicResourceHandler extends ResourceHandlerWrapper {
    private ResourceHandler handler;

    public ExtrasSymbolicResourceHandler(ResourceHandler handler) {
        this.handler = handler;
    }

    public Resource createResource(String resourceName) {
        return this.createResource(resourceName, null, null);
    }

    public Resource createResource(String resourceName, String libraryName) {
        return this.createResource(resourceName, libraryName, null);
    }

    public Resource createResource(String resourceName, String libraryName, String contentType) {
        boolean uncompress = FacesContext.getCurrentInstance().isProjectStage(ProjectStage.Development);
        if (uncompress && libraryName != null &&
                (libraryName.equals("ice.compat") && (resourceName.equals("compat.js") || resourceName.equals("icefaces-compat.js")))) {
            String uncompressedResourceName = resourceName.replaceAll("\\.", ".uncompressed.");
            return super.createResource(uncompressedResourceName, libraryName, contentType);
        } else {
            return super.createResource(resourceName, libraryName, contentType);
        }
    }

    public ResourceHandler getWrapped() {
        return handler;
    }
}
