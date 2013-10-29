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

import org.icefaces.impl.push.http.DynamicResource;
import org.icefaces.impl.push.http.DynamicResourceLinker;
import org.icefaces.impl.util.Base64;
import org.icefaces.impl.util.Util;
import org.icefaces.util.EnvUtils;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class DynamicResourceDispatcher extends ResourceHandlerWrapper implements DynamicResourceRegistry {
    private static Logger log = Logger.getLogger("org.icefaces.resourcedispatcher");
    private static final Pattern ICEfacesBridgeRequestPattern = Pattern.compile(".*\\.icefaces\\.jsf$");
    private static final Pattern ICEfacesResourceRequestPattern = Pattern.compile(".*/icefaces/.*");
    private static final String RESOURCE_PREFIX = "/icefaces/resource";
    private static final DynamicResourceLinker.Handler NOOPHandler = new DynamicResourceLinker.Handler() {
        public void linkWith(DynamicResourceLinker linker) {
            //do nothing!
        }
    };

    private ResourceHandler wrapped;

    public DynamicResourceDispatcher(ResourceHandler wrapped) {
        this.wrapped = wrapped;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.getApplicationMap().put(DynamicResourceDispatcher.class.getName(), this);
    }

    public ResourceHandler getWrapped() {
        return wrapped;
    }

    public boolean isResourceRequest(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        String path = getFullPath(externalContext);
        return shouldHandle(path) || wrapped.isResourceRequest(context);
    }

    public void handleResourceRequest(FacesContext context) throws IOException {
        ExternalContext externalContext = context.getExternalContext();
        String path = getFullPath(externalContext);
        if (shouldHandle(path)) {
            findSessionBasedResourceDispatcher().handleResourceRequest(context);
        } else {
            wrapped.handleResourceRequest(context);
        }
    }

    public URI registerResource(DynamicResource resource) {
        return registerResource(resource, NOOPHandler);
    }

    public URI registerResource(DynamicResource resource, DynamicResourceLinker.Handler linkerHandler) {
        return findSessionBasedResourceDispatcher().registerResource(resource, linkerHandler);
    }

    private SessionBasedResourceDispatcher findSessionBasedResourceDispatcher() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            throw new RuntimeException("Resource registration process needs access to current FacesContext instance.");
        }

        HttpSession session = EnvUtils.getSafeSession(context);
        Object o = session.getAttribute(SessionBasedResourceDispatcher.class.getName());
        SessionBasedResourceDispatcher dispatcher;
        if (o == null) {
            dispatcher = new SessionBasedResourceDispatcher();
            session.setAttribute(SessionBasedResourceDispatcher.class.getName(), dispatcher);
        } else {
            dispatcher = (SessionBasedResourceDispatcher) o;
        }
        return dispatcher;
    }

    public Resource createResource(String resourceName) {
        //MyFaces rejects resource names containing "?"
        String resourcePart = resourceName;
        if (resourceName.contains("?")) {
            int queryIndex = resourceName.indexOf("?");
            resourcePart = resourceName.substring(0, queryIndex);
        }

        return wrapped.createResource(resourcePart);
    }

    private static boolean shouldHandle(String path) {
        return null == path ? false :
                ICEfacesBridgeRequestPattern.matcher(path).find() || ICEfacesResourceRequestPattern.matcher(path).find();
    }

    private static String getFullPath(ExternalContext externalContext) {
        String path = externalContext.getRequestServletPath();
        String pathInfo = externalContext.getRequestPathInfo();
        if (null != pathInfo) {
            path = path + pathInfo;
        }
        return path;
    }

    private static class SessionBasedResourceDispatcher implements Serializable {
        private HashMap mappings = new HashMap();

        public void handleResourceRequest(FacesContext facesContext) throws IOException {
            ExternalContext externalContext = facesContext.getExternalContext();
            String path = getFullPath(externalContext);
            String uri = path;
            Object request = externalContext.getRequest();
            if (request instanceof HttpServletRequest) {
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                uri = java.net.URLDecoder.decode(httpServletRequest.getRequestURI(), "UTF-8");
            }

            Iterator i = mappings.values().iterator();
            while (i.hasNext()) {
                Mapping mapping = (Mapping) i.next();
                if (mapping != null && (mapping.matches(path) || mapping.matches(uri))) {
                    mapping.handleResourceRequest(facesContext);
                    return;
                }
            }

            externalContext.responseSendError(404, "Could not find requested dynamic resource.");
        }

        public URI registerResource(DynamicResource resource) {
            return registerResource(resource, NOOPHandler);
        }

        public URI registerResource(DynamicResource resource, DynamicResourceLinker.Handler handler) {
            if (handler == null)
                handler = NOOPHandler;
            final FileNameOption options = new FileNameOption();
            try {
                resource.withOptions(options);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String filename = options.getFileName();
            String dispatchFilename, uriFilename;
            if (filename == null || filename.trim().equals("")) {
                dispatchFilename = uriFilename = "";
            } else {
                dispatchFilename = convertToEscapedUnicode(filename);
                try {
                    uriFilename = java.net.URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
                } catch (UnsupportedEncodingException e) {
                    uriFilename = filename;
                    log.info(e.getMessage());
                }
            }
            final String name = RESOURCE_PREFIX + "/" + encode(resource) + "/";
            final String fullName = name + uriFilename;
            dispatchOn(fullName, ".*" + name.replaceAll("\\/", "\\/") + dispatchFilename + "$", resource);
            if (handler != NOOPHandler) {
                handler.linkWith(new RelativeResourceLinker(name));
            }

            return URI.create(fullName);
        }

        private void dispatchOn(String name, String expression, DynamicResource resource) {
            if (mappings.get(name) == null) {
                mappings.put(name, new Mapping(expression, resource));
            }
        }

        private class RelativeResourceLinker implements DynamicResourceLinker {
            private final String name;

            public RelativeResourceLinker(String name) {
                this.name = name;
            }

            public void registerRelativeResource(String path, DynamicResource relativeResource) {
                String fullName = name + path;
                String pathExpression = fullName.replaceAll("\\/", "\\/").replaceAll("\\.", "\\.");
                dispatchOn(fullName, ".*" + pathExpression + "$", relativeResource);
            }
        }
    }

    private static class ResourceServer implements Serializable {
        private final Date lastModified = new Date();
        private final DynamicResource resource;

        public ResourceServer(DynamicResource resource) {
            this.resource = resource;
        }

        public void handleResourceRequest(FacesContext facesContext) throws IOException {
            ExternalContext externalContext = facesContext.getExternalContext();
            try {
                Date modifiedSince = Util.parseHTTPDate(
                        externalContext.getRequestHeaderMap()
                                .get("If-Modified-Since"));
                if (lastModified.getTime() > modifiedSince.getTime() + 1000) {
                    respond(facesContext);
                } else {
                    externalContext.setResponseStatus(304);
                    externalContext.setResponseHeader(
                            "ETag", encode(resource));
                    externalContext.setResponseHeader(
                            "Date", Util.formatHTTPDate(new Date()));
                    externalContext.setResponseHeader(
                            "Last-Modified", Util.formatHTTPDate(lastModified));
                }
            } catch (Exception e) {
                respond(facesContext);
            }
        }

        public void respond(FacesContext facesContext) throws IOException {
            ExternalContext externalContext = facesContext.getExternalContext();
            ResourceOptions options = new ResourceOptions();
            resource.withOptions(options);
            if (options.mimeType == null && options.fileName != null) {
                options.mimeType = externalContext.getMimeType(options.fileName);
            }
            externalContext.setResponseHeader("ETag", encode(resource));
            externalContext.setResponseHeader("Cache-Control", "public");
            externalContext.setResponseHeader("Content-Type", options.mimeType);
            externalContext.setResponseHeader("Last-Modified",
                    Util.formatHTTPDate(options.lastModified));
            if (options.expiresBy != null) {
                externalContext.setResponseHeader("Expires",
                        Util.formatHTTPDate(options.expiresBy));
            }
            String contentDispositionFileName = Util.encodeContentDispositionFilename(options.fileName);
            if (options.attachement && contentDispositionFileName != null) {
                externalContext.setResponseHeader("Content-Disposition", "attachment; filename" + contentDispositionFileName);
            }
            InputStream inputStream = resource.open();
            if (inputStream == null) {
                throw new IOException("Resource of type " + resource.getClass().getName() + "[digest: " +
                        resource.calculateDigest() + "; mime-type: " + options.mimeType +
                        (options.attachement ? "; attachment: " + options.fileName : "") +
                        "] returned a null input stream.");
            } else {
                OutputStream out = externalContext.getResponseOutputStream();

                try {
                    if (Util.acceptGzip(externalContext) &&
                            EnvUtils.isCompressResources(facesContext) &&
                            Util.shouldCompress(options.mimeType)) {
                        externalContext.setResponseHeader("Content-Encoding", "gzip");
                        Util.compressStream(inputStream, out);
                    } else {
                        Util.copyStream(inputStream, out);
                    }
                } catch (IOException e) {
					inputStream.close();
                    log.log(Level.WARNING, "Failed to serve resource "
                            + externalContext.getRequestServletPath(), e);
                }
            }
        }

        public void shutdown() {
        }

        private class ResourceOptions implements ExtendedResourceOptions {
            private Date lastModified = new Date();
            private Date expiresBy;
            private String mimeType;
            private String fileName;
            private boolean attachement;
            private String contentDispositionFileName;

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
    }

    private static String encode(DynamicResource resource) {
        return Base64.encode(String.valueOf(resource.calculateDigest().hashCode()));
    }

    private static String convertToEscapedUnicode(String s) {
        char[] chars = s.toCharArray();
        String hexStr;
        StringBuffer stringBuffer = new StringBuffer(chars.length * 6);
        String[] leadingZeros = {"0000", "000", "00", "0", ""};
        for (int i = 0; i < chars.length; i++) {
            hexStr = Integer.toHexString(chars[i]).toUpperCase();
            stringBuffer.append("\\u");
            stringBuffer.append(leadingZeros[hexStr.length()]);
            stringBuffer.append(hexStr);
        }
        return stringBuffer.toString();
    }

    private static class FileNameOption implements DynamicResource.Options {
        private String fileName;

        public String getFileName() {
            return fileName;
        }

        public void setAsAttachement() {
        }

        public void setExpiresBy(Date date) {
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public void setLastModified(Date date) {
        }

        public void setMimeType(String mimeType) {
        }
    }

    public interface ExtendedResourceOptions extends DynamicResource.Options {
        public void setContentDispositionFileName(String contentDispositionFileName);
    }

    private static class Mapping implements Serializable {
        Pattern pattern;
        private ResourceServer server;

        private Mapping(String expression, DynamicResource resource) {
            this.pattern = Pattern.compile(expression);
            this.server = new ResourceServer(resource);
        }

        public boolean matches(String path) {
            return pattern.matcher(path).find();
        }

        public void handleResourceRequest(FacesContext facesContext) throws IOException {
            server.handleResourceRequest(facesContext);
        }
    }
}
