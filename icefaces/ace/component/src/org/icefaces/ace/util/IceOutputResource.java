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

package org.icefaces.ace.util;

import javax.faces.application.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class IceOutputResource extends Resource implements Serializable {
    private final static Logger logger = Logger.getLogger(IceOutputResource.class.getName());

    protected String compId;
    //just use the default one from ResourceRegistry
    public static final String ICE_COMPS_LIBRARY = "javax.faces.resource";
    protected final static Map<String, String> supportedMimeTypes = new HashMap<String, String>();
    // HTTP Date format required by the HTTP/1.1 RFC
    private static final String LAST_MODIFIED_PATTERN = "EEE, dd MMM yyyy HH:mm:ss zzz";
    protected String name;
    protected String libraryName;
    protected String scope;
    private Object content;


    public IceOutputResource(String compId, Object o, String mimeType) {
        this.name = compId;
        super.setResourceName(name);
        this.content = o;
        this.libraryName = ICE_COMPS_LIBRARY;
        super.setLibraryName(libraryName);
        super.setContentType(mimeType);
    }


    @Override
    public InputStream getInputStream() throws IOException {
        // TODO Auto-generated method stub
        InputStream inStream = null;
        if (logger.isLoggable(Level.FINER)) {
            logger.fine("for name=" + super.getResourceName() + ".......getINputStream with type=" + super.getContentType());
        }
        if (null != content) {
            if (content instanceof byte[]) {
                inStream = new ByteArrayInputStream((byte[]) content);
            } else if (content instanceof InputStream) {
                inStream = (InputStream) content;
            } else {//try to see if we can read it in?
                try {
                    inStream = new ByteArrayInputStream(content.toString().getBytes());
                } catch (Exception ex) {
                    logger.log(Level.WARNING, "Unable to service request due to unsupported data type",ex);
                }
            }
        } else if (logger.isLoggable(Level.FINER)) {
            logger.finer("Content stream request was null");
        }
        return inStream;
    }


    @Override
    public String getRequestPath() {
        // need to identify the resource with proper prefix of library and name
        StringBuilder buf = new StringBuilder(
                FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        //       logger.info(" for name="+super.getResourceName()+" gettingREQUEST PATH WITH CONTENT TYPE="+super.getContentType());
        buf.append("/javax.faces.resource");
        buf.append("/").append(super.getResourceName()).append(".jsf?ln=").append(libraryName);
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("Request path for program resource " + this.toString() + " : '" + buf.toString() + "'");
        }

        return buf.toString();
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        //check component for mimeType when creating this for supported mime types??
        logger.finer("getRESPONSEHEADERS MIMEType=" + super.getContentType());
        Map<String, String> result = new HashMap<String, String>(6, 1.0f);
        SimpleDateFormat format = new SimpleDateFormat(LAST_MODIFIED_PATTERN);
        // make it modified so they always request the whole resource.
        // TODO: make this smarter when going into production.
        result.put("Last-Modified", format.format(new Date()));
        result.put("Content-Type", super.getContentType());
        if (content instanceof byte[]) {
            result.put("Content-Length",
                    Integer.toString(((byte[]) content).length));
        }
        return result;
    }

    @Override
    public URL getURL() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        String serverName = context.getRequestHeaderMap()
                .get("x-forwarded-host");
        if (null == serverName) {
            serverName = context.getRequestServerName() + ":" + 
            context.getRequestServerPort();
        }
        StringBuilder buff = new StringBuilder(context.getRequestScheme());
        buff.append("://");
        buff.append(serverName);
        buff.append(getRequestPath());
        URL url = null;
        try {
            url = new URL(buff.toString());
        } catch (java.net.MalformedURLException e) {
            logger.log(Level.WARNING, "Error creating IceOutputResource URL",e);
        }
        return url;

    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext arg0) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s { name=%s libraryName=%s }"
                , this.getClass().getName()
                , name
                , libraryName);
    }

    //start with images and audio for now ??? WIll we even use this?
    static {
        supportedMimeTypes.put("abs", "audio/x-mpeg");
        supportedMimeTypes.put("mp3", "audio/x-mpeg");
        supportedMimeTypes.put("mp3", "audio/x-mpeg");
        supportedMimeTypes.put("wav", "audio/x-wav");
        supportedMimeTypes.put("gif", "image/gif");
        supportedMimeTypes.put("tif", "image/tiff");
        supportedMimeTypes.put("jpg", "image/jpg");
        supportedMimeTypes.put("jpeg", "image/jpeg");
        supportedMimeTypes.put("png", "image/png");

        //eventually "pdf", "application/pdf" ???
    }


}
