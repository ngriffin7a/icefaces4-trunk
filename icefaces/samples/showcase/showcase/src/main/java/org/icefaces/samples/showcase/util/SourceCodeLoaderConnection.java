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

package org.icefaces.samples.showcase.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.OrderedBidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;

@ManagedBean
@ApplicationScoped  
/**
 * Manages and loads a cache of formatted source files rendered by the 
 * SourceCodeServlet.
 *
 * Path to an instance of SourceCodeLoaderServlet must be set by the 
 * context-param SOURCE_SERVLET_URL in web.xml.
 *
 * The maximum number of bytes of formatted source that can be cached
 * must be set by the context-param MAX_CACHE_SIZE in web.xml.
 *        
 * This class is intended to be called as if it were a map via EL.
 * 
 * See simple_resource_decorator.xhtml for an example of use.
 * @author Nils Lundquist 
 * @since 2.1
 */
public class SourceCodeLoaderConnection implements Map, Serializable{
    // The max size of the cache of source code to hold. 
    // Doesn't include overhead of data structures.
    private static Integer MAX_CACHE_SIZE;
    // String URL to the local instance of SourceCodeLoaderServlet
    private static String SOURCE_SERVLET_URL;
    private boolean brokenUrl = false;
    // Cached files sorted from oldest to youngest 
    // Bidimap to enforce uniques keys & efficient lookup of oldest value  
    // Key of path; val is tuple of path, source & timestamp; sort by asc. time
    private TreeBidiMap cache = new TreeBidiMap();  
    // Size of source cache in bytes
    private long cacheSize = 0; 
    // Logger reference
    private Logger logger;
	// true if initial request was made using SSL
	private static boolean IS_SECURE;

    public SourceCodeLoaderConnection() {}         

    // Set logger, load the Servlet URL and cache size contstants from web.xml
    @PostConstruct
    private void initialize() {   
        // If no logger exists for this class, create and add a new one
        if ((logger = LogManager.getLogManager().getLogger(this.getClass().getName())) == null) {
            logger = Logger.getLogger(this.getClass().getName());
            LogManager.getLogManager().addLogger(logger);
        }

        // Pull the maximum cache size from web.xml using FacesUtils
        // We'll default this value if it isn't found
        MAX_CACHE_SIZE =
            Integer.parseInt(FacesUtils.getFacesParameter("org.icefaces.samples.showcase.MAX_SOURCE_CACHE_SIZE", "20971520"));
        
        // Next we'll try to build a URL to load the source from
        // This is basically http://localhost:8080/showcase/, but the port and address could change
        // We'll try to dynamically get this from the ExternalContext request, otherwise we'll check it from the web.xml
        //  and finally just default it
        Object request = FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if ((request != null) && (request instanceof HttpServletRequest)) {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            String hostName = "localhost";
            try {
            	//construct a url (SOURCE_SERVLET_URL) from getLocalName is not reliable
            	//should use host from requestURL.
            	URL requestUrl = new URL(httpRequest.getRequestURL().toString());
            	hostName = requestUrl.getHost();
            } catch (MalformedURLException mfException) {}
			IS_SECURE = httpRequest.isSecure();
            String protocol = IS_SECURE ? "https://" : "http://";
            SOURCE_SERVLET_URL = protocol +
                                 hostName +
                                 ":" +
                                 httpRequest.getLocalPort() +
                                 httpRequest.getContextPath() +
                                 "/";
        }
        else {
            String fromFile = FacesUtils.getFacesParameter("org.icefaces.samples.showcase.SOURCE_SERVLET_URL");
            if (fromFile != null) {
                SOURCE_SERVLET_URL = fromFile;
            }
            else {
                SOURCE_SERVLET_URL = "http://localhost:8080/comp-suite/";
            }
        }
        SOURCE_SERVLET_URL += "sourcecodeStream.html?path=";
        
        //logger.info("Reading source code from url [" + SOURCE_SERVLET_URL + "].");
    }

    /**
     * Returns formatted source located in the cache, or adds to the cache and
     * returns the source found at sourceCodePath, removing oldest cached files
     * if MAX_CACHE_SIZE exceeded.           
     * 
     * Implementing the map interface method `get` to allow parameter passing 
     * in EL versions prior to 2.0
     *
     * @param sourceCodePathObj The String location of the source file relative
     * to the web application root.
     * @return The XHTML formatted source code or a stack trace if the URL 
     * could not be UTF-8 encoded.
     */
    public String get(Object sourceCodePathObj) {
        if (SOURCE_SERVLET_URL == null || MAX_CACHE_SIZE == null ||
           !(sourceCodePathObj instanceof String)) {
            return null;
        }
        
        String sourceCodePath;
        // Try encoding sourceCodePathObj parameter, return a stack trace
        // instead of source if it fails.
        try {
            sourceCodePath = URLEncoder.encode((String)sourceCodePathObj, "UTF-8");
        } catch (UnsupportedEncodingException e) {
           logger.severe("UTF-8 is not supported by this platform.");
           return "";
        }
        
        CachedSource cs;
        // Only allow the cache to be accessed by a single user when being used
        // within one of these blocks.   
        synchronized(cache) {
            // Search the cache for formatted source code from this path. If it is 
            // found, update the formatted source code with the current timestamp 
            // and return the cached source.  
            if ((cs = (CachedSource)cache.get(sourceCodePath)) != null) {
                logger.finer("Source Cache Hit.");
                logger.finest("Hit: " + sourceCodePath); 
                cs.timestamp = System.currentTimeMillis()/1000;
                cache.put(cs.path, cs);
                return cs.source;
            }
        }

        logger.finer("Source Cache Miss.");
        logger.finest("Miss: " + sourceCodePath);
        
        URL servletUrl = null;
        InputStream inputStream = null;
        InputStreamReader inputReader = null;
        try{
			if (!IS_SECURE) {
				servletUrl = new URL(SOURCE_SERVLET_URL + sourceCodePath);
				inputStream = (InputStream)servletUrl.getContent();
			} else {
				// don't use a connection, just access methods directly
				inputStream = SourceCodeLoaderServlet.getServlet().getSource((String)sourceCodePathObj);
			}
            
            brokenUrl = false;
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Broken URL for the source code loader (" + SOURCE_SERVLET_URL + "), check your web.xml.");
            brokenUrl = true;
        }
        
        if (!brokenUrl) {
            try {
                // Set up streams and buffers for source servlet reading
                inputReader = new InputStreamReader(inputStream, "UTF-8"); 
                StringBuilder buf = new StringBuilder(16384);
    
                // Read into stringBuilder until EOF
                int readChar;
                while ((readChar = inputReader.read()) != -1) { 
                    buf.append((char)readChar);
                }
    
                // Extract page content from <body> tag and fix nbsp for valid XHTML
                String ret = buf.indexOf("&nbsp;") != -1
                              ?
                              buf.substring(buf.indexOf("<body>")+6, buf.lastIndexOf("</body>")).replace("&nbsp;","&#160;")
                              :
                              buf.toString();
                
                synchronized(cache) {
                    // If cache is full, remove files until the newly loaded string
                    // will fit and add it to the cache
                    while ((ret.length()*16) + cacheSize > MAX_CACHE_SIZE) { 
                        OrderedBidiMap iCache = cache.inverseOrderedBidiMap();
                        CachedSource c = (CachedSource)iCache.firstKey(); 
                        cache.remove(c.path);
                        cacheSize -= c.source.length()*16; 
                        logger.finer("Cache Oversized. Removing oldest file.");
                        logger.finest("Removed: " + c.path);
                    }
                    cache.put(sourceCodePath, new CachedSource(sourceCodePath, ret));
                    cacheSize += ret.length()*16;
                }
                
                // Return newly loaded and cached source
                return ret; 
            } catch (MalformedURLException e) {
               logger.severe("Attempted to connect to malformed URL.");
               logger.severe("Likely either EL param or web.xml SOURCE_SERVLET_URL param is incorrectly set");
               e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
               logger.severe("UTF-8 is not supported by this platform.");
               e.printStackTrace();
            } catch (IOException e) {
               logger.severe("IOException raised while reading characters from Servlet response stream.");
               e.printStackTrace();
            } catch (Exception e) {
               e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception ignoredClose) { }
                }
                if (inputReader != null) {
                    try {
                        inputReader.close();
                    } catch (Exception ignoredClose) { }
                }
            }
        }
        
        return "";
    }  

    // Private data structure to associate a path, source and timestamp 
    // with methods to implement Comparable such that the oldest timestamped
    // source is considered the lowest valued, and only equal if their paths
    // are equal.
    private class CachedSource implements Comparable {
        public String path;
        public String source;
        public Long timestamp;
        
        public CachedSource(String path, String source) {
            this.path = path;
            this.source = source;
            timestamp = System.currentTimeMillis()/1000;
        }
        
        public int compareTo(Object c) {
            if (c instanceof CachedSource) {    
                if (this.path.equals(((CachedSource)c).path)) return 0;   
                if (this.timestamp > ((CachedSource)c).timestamp) return -1;
                if (this.timestamp <= ((CachedSource)c).timestamp) return 1;
            } return -1;
        }  
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((path == null) ? 0 : path.hashCode());
            return result;
        }     
        
        @Override   
        public boolean equals(Object c) {
            if (c != null && c instanceof CachedSource) {
                if (this.path.equals(((CachedSource)c).path)) return true;   
                return false;
            } return false;
        }
    }   
    
    
    /**
    * Map interface stub
    */
    public Collection values() {return null;}
    public Object put(Object key, Object value) {return null;}
    public Set keySet() {return null;}
    public boolean isEmpty() {return false;}
    public int size() {return 0;}
    public void putAll(Map t) {}
    public void clear() {}
    public boolean containsValue(Object value) {return false;}
    public Object remove(Object key) {return null;  }
    public boolean containsKey(Object key) {return false;}
    public Set entrySet() {return null;}    
}
