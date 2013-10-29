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

package org.icefaces.impl.application;

import org.icefaces.impl.util.Util;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class CoalescingResource extends Resource {
    private Date lastModified = new Date();
    private String name;
    private String library;
    private Infos resourceInfos;
    private String mimeType;
    private String mapping;
    private boolean extensionMapping;

    public CoalescingResource(String name, String library, String mapping, boolean extensionMapping, Infos resourceInfos) {
        this.name = name;
        this.library = library;
        this.mapping = mapping;
        this.extensionMapping = extensionMapping;
        this.resourceInfos = resourceInfos;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        this.mimeType = externalContext.getMimeType(this.name);
    }

    public String getLibraryName() {
        return library;
    }

    public String getResourceName() {
        return name;
    }

    public InputStream getInputStream() throws IOException {
        ArrayList<InputStream> streams = new ArrayList();
        for (Info next : resourceInfos.resources) {
            ResourceHandler handler = FacesContext.getCurrentInstance().getApplication().getResourceHandler();
            Resource resource = handler.createResource(next.name, next.library);
            streams.add(new SkipBOMInputStream(resource.getInputStream()));
            streams.add(new ByteArrayInputStream("\n\r".getBytes("UTF-8")));
        }

        return new SequenceInputStream(Collections.enumeration(streams));
    }

    public Map<String, String> getResponseHeaders() {
        HashMap headers = new HashMap();
        headers.put("ETag", eTag());
        headers.put("Cache-Control", "public");
        headers.put("Content-Type", mimeType);
        headers.put("Date", Util.formatHTTPDate(new Date()));
        headers.put("Last-Modified", Util.formatHTTPDate(lastModified));

        return headers;
    }

    public String getContentType() {
        return mimeType;
    }

    public String getRequestPath() {
        String path;
        if (extensionMapping) {
            path = ResourceHandler.RESOURCE_IDENTIFIER + '/' + name + mapping;
        } else {
            path = ResourceHandler.RESOURCE_IDENTIFIER + '/' + name;
            path = (mapping == null) ? path : mapping + path;
        }

        path = path + "?ln=" + library;
        path = path + "&dgst=" + calculateDigest(resourceInfos);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getViewHandler().getResourceURL(facesContext, path);
    }

    public URL getURL() {
        try {
            return FacesContext.getCurrentInstance().getExternalContext().getResource(name);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean userAgentNeedsUpdate(FacesContext context) {
        Map<String,String> requestHeaders = context.getExternalContext().getRequestHeaderMap();
        return !requestHeaders.containsKey("If-Modified-Since") || resourceInfos.modified;
    }

    private String eTag() {
        return calculateDigest(resourceInfos);
    }

    public static class Info implements Serializable {
        private String name, library;

        public Info(String name, String library) {
            this.name = name;
            this.library = library;
        }

        public String toString() {
            return name + '[' + library + ']';
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Info info = (Info) o;

            if (library != null ? !library.equals(info.library) : info.library != null) return false;
            if (name != null ? !name.equals(info.name) : info.name != null) return false;

            return true;
        }

        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (library != null ? library.hashCode() : 0);
            return result;
        }
    }

    public static class Infos implements Externalizable {
        public boolean modified = true;
        public ArrayList<Info> resources;

        public Infos() {
            this.modified = true;
            this.resources = new ArrayList<Info>();
        }

        public void writeExternal(ObjectOutput objectOutput) throws IOException {
            //do nothing
        }

        public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
            modified = true;
            resources = new ArrayList<Info>();
        }
    }

    private String calculateDigest(Infos resourceInfos) {
        StringBuffer digest = new StringBuffer();
        for (Info info: resourceInfos.resources) {
            digest.append(info.toString());
        }
        return Long.toString(Math.abs(digest.toString().hashCode()), 36);
    }

    protected String calculateInfosDigest(){
        return calculateDigest(resourceInfos);
    }


}
