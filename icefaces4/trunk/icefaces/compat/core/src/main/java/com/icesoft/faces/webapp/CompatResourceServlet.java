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

package com.icesoft.faces.webapp;

import org.icefaces.impl.util.Util;
import org.icefaces.util.EnvUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompatResourceServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(CompatResourceServlet.class.getName());
    private static final String BASE_PATH = "com/icesoft/faces/resources";
    private ClassLoader loader;
    private final Date lastModified = new Date();
    private final String STARTUP_TIME = Util.formatHTTPDate(lastModified);
    private ServletContext servletContext;

    public void init(final ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.loader = this.getClass().getClassLoader();
        this.servletContext = servletConfig.getServletContext();
    }

    public void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String modifedHeader = httpServletRequest
                .getHeader("If-Modified-Since");
        if (null != modifedHeader) {
            try {
                Date modifiedSince = Util.parseHTTPDate(modifedHeader);
                if (modifiedSince.getTime() + 1000 > lastModified.getTime()) {
                    //respond with a not-modifed
                    httpServletResponse.setStatus(304);
                    //TODO: calculate ETag
                    //            httpServletResponse.setHeader(
                    //                    "ETag", encode(resource));
                    httpServletResponse.setDateHeader(
                            "Date", new Date().getTime());
                    httpServletResponse.setDateHeader(
                            "Last-Modified", lastModified.getTime());
                    return;
                }
            } catch (ParseException e) {
                //if the headers are corrupted, still just serve the resource
                log.log(Level.FINE, "failed to parse date: " + modifedHeader, e);
            } catch (NumberFormatException e) {
                //if the headers are corrupted, still just serve the resource
                log.log(Level.FINE, "failed to parse date: " + modifedHeader, e);
            }
        }

        String path = httpServletRequest.getPathInfo();

        //ICE-8772: although very low risk, we validate the path to help prevent
        //          Veracode from complaining about a potentially "crafted" value
        if(path.contains("..")){
            log.log(Level.WARNING, "resource path is potentially unsafe " + path);
            httpServletResponse.setStatus(404, "Resource not found");
            return;
        }

        final InputStream in = loader.getResourceAsStream(BASE_PATH + path);
        if (null == in) {
            httpServletResponse.setStatus(404, "Resource not found");
            return;
        }
        String mimeType = servletContext.getMimeType(path);
        if ("/blank".equalsIgnoreCase(path)) {
            mimeType = "text/html";
        }
        httpServletResponse.setHeader("Content-Type", mimeType);
        httpServletResponse.setHeader("Last-Modified", STARTUP_TIME);

        OutputStream out = httpServletResponse.getOutputStream();

        boolean compressResources = !"false".equalsIgnoreCase(
                servletContext.getInitParameter(EnvUtils.COMPRESS_RESOURCES));
        String acceptHeader = httpServletRequest.getHeader("Accept-Encoding");
        boolean acceptGzip = (null != acceptHeader) &&
                (acceptHeader.indexOf("gzip") >= 0);
        if (acceptGzip && compressResources && Util.shouldCompress(mimeType)) {
            httpServletResponse.setHeader("Content-Encoding", "gzip");
            Util.compressStream(in, out);
        } else {
            Util.copyStream(in, out);
        }
    }

}
