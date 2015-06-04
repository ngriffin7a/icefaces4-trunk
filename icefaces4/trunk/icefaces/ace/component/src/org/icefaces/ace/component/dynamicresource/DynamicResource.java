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

package org.icefaces.ace.component.dynamicresource;

import org.icefaces.application.ResourceRegistry;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DynamicResource extends DynamicResourceBase {
    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }


    public Resource getResource() {
        return new ProxyResource(super.getResource(), isAttachment(), getFileName(), getMimeType());
    }

    private class ProxyResource extends Resource {
        private Resource resource;
        private String requestPath;
        private HashMap responseHeaders = new HashMap();

        public ProxyResource(Resource resource, boolean attachment, String fileName, String mimeType) {
            this.resource = resource;
            Map headers = resource.getResponseHeaders();
            if (headers != null) {
                responseHeaders.putAll(headers);
            }
            if (fileName != null) {
                setAttachmentFileName(fileName);
            }
            if (mimeType != null) {
                setContentType(mimeType);
            }

            String scope = getScope();
            if ("session".equals(scope)) {
                this.requestPath = ResourceRegistry.addSessionResource(this);
            } else if ("view".equals(scope)) {
                this.requestPath = ResourceRegistry.addViewResource(this);
            } else if ("window".equals(scope)) {
                this.requestPath = ResourceRegistry.addWindowResource(this);
            } else if ("application".equals(scope)) {
                this.requestPath = ResourceRegistry.addApplicationResource(this);
            } else {
                this.requestPath = ResourceRegistry.addSessionResource(this);
            }
        }

        public InputStream getInputStream() throws IOException {
            return resource.getInputStream();
        }

        public Map<String, String> getResponseHeaders() {
            Date lastModified = getLastModified();
            if (lastModified != null) {
                responseHeaders.put("Last-Modified", DATE_FORMAT.format(lastModified));
            }

            return responseHeaders;
        }

        public String getRequestPath() {
            return requestPath;
        }

        public URL getURL() {
            return resource.getURL();
        }

        public boolean userAgentNeedsUpdate(FacesContext context) {
            return resource.userAgentNeedsUpdate(context);
        }

        public String getContentType() {
            return resource.getContentType();
        }

        public void setContentType(String contentType) {
            resource.setContentType(contentType);
        }

        public String getLibraryName() {
            return resource.getLibraryName();
        }

        public void setLibraryName(String libraryName) {
            resource.setLibraryName(libraryName);
        }

        public String getResourceName() {
            return Integer.toHexString(Math.abs((resource.getResourceName() + responseHeaders.hashCode() + getContentType()).hashCode()));
        }

        public void setResourceName(String resourceName) {
            //resource.setResourceName(resourceName);
        }

        public void setAttachmentFileName(String fileName) {
            String name = encodeContentDispositionFilename(fileName);
            if (name != null) responseHeaders.put("Content-Disposition", "attachment; filename" + name);
        }
    }

    // Encode filename for Content-Disposition header; to be used in save file dialog;
    // See http://greenbytes.de/tech/tc2231/
    // Some code suggested by Deryk Sinotte
    public static String encodeContentDispositionFilename(String fileName) {
        if (fileName == null || fileName.trim().length() == 0) return null;
        String userAgent = getUserAgent();
        String defaultFileName = "=\"" + fileName + "\"";

        //WebLogic does not provide the user-agent for some reason
        //System.out.println("RegisteredResource.encodeContentDispositionFilename: user-agent = " + userAgent);
        if (userAgent == null || userAgent.trim().length() == 0) return defaultFileName;


        userAgent = userAgent.toLowerCase();
        try {
            if (userAgent.indexOf("msie") > -1) return encodeForIE(fileName);
            if (userAgent.indexOf("firefox") > -1 || userAgent.indexOf("opera") > -1) return encodeForFirefox(fileName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return defaultFileName;
    }

    // contributed by Robert Vojta
    private static String encodeForIE(String fileName)
            throws UnsupportedEncodingException {
        /*
         * http://greenbytes.de/tech/tc2231/#attwithfnrawpctenca
         *
         * IE decodes %XY to characters and than if it detects
         * UTF-8 stream (after decoding of %XY), than it creates
         * UTF-8 string.
         *
         * We use this behavior to offer correct file name
         * for download.
         */
        StringBuffer encodedFileName = new StringBuffer();
        encodedFileName.append("=\""); // ICEfaces 1.7.2 bug
        encodedFileName.append(URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
        encodedFileName.append("\""); // ICEfaces 1.7.2 bug

        return encodedFileName.toString();
    }

    // contributed by Robert Vojta
    private static String encodeForFirefox(String fileName)
            throws UnsupportedEncodingException {
        /*
         * http://greenbytes.de/tech/tc2231/#attwithfn2231utf8
         */
        StringBuffer encodedFileName = new StringBuffer();

        encodedFileName.append("*=UTF-8''");

        encodedFileName.append(URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));

        return encodedFileName.toString();
    }

    private static String getUserAgent() {
        Map<String, String> headerMap = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap();
        return (String) headerMap.get("user-agent");
    }
}
