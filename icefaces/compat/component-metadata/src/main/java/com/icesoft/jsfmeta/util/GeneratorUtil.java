
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

package com.icesoft.jsfmeta.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

public class GeneratorUtil {
    
    private static String WORKING_FOLDER;
    
    public GeneratorUtil() {
    }
    
    static{
        String result = ".";
        try {
            ClassLoader classLoader = Thread.currentThread()
            .getContextClassLoader();
            URL localUrl = classLoader.getResource(".");
            if(localUrl != null){
                result = convertFileUrlToPath(localUrl);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        WORKING_FOLDER = result;
    }
    
    public static String getWorkingFolder(){
        return WORKING_FOLDER;
    }
    
    public static String getBaseLineFolder(String resourceFile) throws MalformedURLException {
        String result = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL localUrl = classLoader.getResource(resourceFile);
            if (localUrl != null) {
                result = convertFileUrlToPath(localUrl);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        if(result == null){
            throw new MalformedURLException("no such file as "+ resourceFile);                        
        }
        return result;
    }
    
    public static File getDestFolder(String path) throws FileNotFoundException{
        File file = new File(path);
        if(!file.exists()){
            if(!file.mkdirs()){
                throw new FileNotFoundException(file.getPath());
            }
        }
        
        return file;
    }
    
    /**
     * Kind of hack-ish attempt at solving problem that if the directory,
     *  where we're building the component-metadata in,  has special
     *  characters in its path, like spaces, then the URL to it will be
     *  escaped, which will be interpretted as a different directory,
     *  unless we unescape it.
     */
    private static String convertFileUrlToPath(URL url) {
        String path = url.getPath();
        if( url.toExternalForm().startsWith("file:") ) {
            StringBuffer sb = new StringBuffer( path.length() );
            int pathLength = path.length();
            for(int i = 0; i < pathLength;) {
                char c = path.charAt(i);
                if( c == '%' ) {
                    if( (i+1) < pathLength && isHexDigit(path.charAt(i+1)) ) {
                        int increment = 2;
                        if( (i+2) < pathLength && isHexDigit(path.charAt(i+2)) )
                            increment++;
                        try {
                            char unescaped = (char) Integer.parseInt(
                                path.substring(i+1, i+increment), 16);
                            
                            sb.append( unescaped );
                            i += increment;
                            continue;
                        } catch(NumberFormatException nfe) {
                            // Not a valid hex escape, so just fall through,
                            //  and append it to the path
                        }
                    }
                }
                sb.append( c );
                i++;
            }
            path = sb.toString();
        }
        return path;
    }
    
    private static boolean isHexDigit(char c) {
        return ( (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') );
    }
}