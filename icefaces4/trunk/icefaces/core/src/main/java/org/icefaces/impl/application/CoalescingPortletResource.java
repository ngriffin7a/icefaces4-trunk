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
