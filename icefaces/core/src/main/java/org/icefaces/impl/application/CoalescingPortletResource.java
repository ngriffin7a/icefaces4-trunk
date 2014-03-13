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

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import java.net.URL;

public class CoalescingPortletResource extends CoalescingResource {

    public CoalescingPortletResource(String name, String library, String mapping, boolean extensionMapping, Infos resourceInfos) {
        super(name, library, mapping, extensionMapping, resourceInfos);
    }

    public String getRequestPath() {
        Resource rez = getDynamicResourceViaDummyFile(getResourceName());
        String requestPath = rez.getRequestPath();
        return requestPath + "&dgst=" + calculateInfosDigest();
    }

    public URL getURL() {
        Resource rez = getDynamicResourceViaDummyFile(getResourceName());
        return rez.getURL();
    }

    private Resource getDynamicResourceViaDummyFile(String actualResourceName){
        FacesContext fc = FacesContext.getCurrentInstance();
        ResourceHandler handler = fc.getApplication().getResourceHandler();
        Resource rez = handler.createResource("coalesced.txt",getLibraryName(),getContentType());
        rez.setResourceName(getResourceName());
        return rez;
    }

}
