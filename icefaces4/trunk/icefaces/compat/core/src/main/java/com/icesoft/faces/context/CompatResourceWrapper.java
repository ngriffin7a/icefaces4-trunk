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

import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.icefaces.impl.util.Base64;

public class CompatResourceWrapper extends javax.faces.application.Resource {
    com.icesoft.faces.context.Resource compatResource;
    BaseResourceOptions options;
    Map headers = new HashMap();

    public CompatResourceWrapper(com.icesoft.faces.context.Resource resource)  {
        this.compatResource = resource;
        options = new BaseResourceOptions();
        //populate options with parameters from our resource
        try {
            resource.withOptions(options);
        } catch (Exception e)  {
            //should we log this and proceed or throw away the resource?
            e.printStackTrace();
        }
        if (options.mimeType == null && options.fileName != null) {
            options.mimeType = FacesContext.getCurrentInstance()
                    .getExternalContext().getMimeType(options.fileName);
        }
        headers.put("ETag", Base64.encode(String.valueOf(resource.calculateDigest().hashCode())));
        headers.put("Cache-Control", "public");
        headers.put("Last-Modified", options.lastModified);
        if (options.expiresBy != null) {
            headers.put("Expires", options.expiresBy);
        }
        if (options.attachement && options.contentDispositionFileName != null) {
            headers.put("Content-Disposition", "attachment; filename" + options.contentDispositionFileName);
        }
    }
    
    public String getContentType()  {
        return options.mimeType;
    }
    public void setContentType(String contentType)  {
        options.mimeType = contentType;
    }

    public String getLibraryName() {
        if (true) { throw new UnsupportedOperationException(); }
        return "org.icefaces";
    }
    public void setLibraryName(String libraryName)  {
        if (true) { throw new UnsupportedOperationException(); }
    }

    public String getResourceName()  {
        return options.fileName;
    }

    public void setResourceName(String name)  {
        options.fileName = name;
    }

    public InputStream getInputStream() throws IOException  {
        return compatResource.open();
    }

    public Map getResponseHeaders() {
        return headers;
    }

    public String getRequestPath()  {
        if (true) { throw new UnsupportedOperationException(); }
        return null;
    }

    public URL getURL()  {
        if (true) { throw new UnsupportedOperationException(); }
        return null;
    }

    public boolean userAgentNeedsUpdate(FacesContext facesContext)  {
        return false;
    }
}

class BaseResourceOptions implements com.icesoft.faces.context.Resource.Options {
    Date lastModified = new Date();
    Date expiresBy;
    String mimeType;
    String fileName;
    boolean attachement;
    String contentDispositionFileName;

    public void setMimeType(String type) {
        mimeType = type;
    }

    public void setLastModified(Date date) {
        lastModified = date;
    }

    public void setFileName(String name) {
        fileName = name;
    }

    public void setExpiresBy(Date date) {
        expiresBy = date;
    }

    public void setAsAttachement() {
        attachement = true;
    }

    // ICE-4342
    // Encoded filename in Content-Disposition header; to be used in save file dialog;
    // See http://greenbytes.de/tech/tc2231/
    public void setContentDispositionFileName(String contentDispositionFileName) {
        this.contentDispositionFileName = contentDispositionFileName;
    }
}
