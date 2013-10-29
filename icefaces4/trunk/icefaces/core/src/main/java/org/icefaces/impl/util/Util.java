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

package org.icefaces.impl.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

import org.icefaces.impl.renderkit.html_basic.SingleSubmitRenderer;

public class Util {
    private static Logger log = Logger.getLogger(Util.class.getName());
    private static List DEFAULT_EXCLUSIONS = Arrays.asList(
            "application/pdf",
            "application/zip", "application/x-compress", "application/x-gzip ",
            "application/java-archive");
    public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";

    public static Date parseHTTPDate(String date) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.ENGLISH);
        return formatter.parse(date);
    }

    public static String formatHTTPDate(Date date) {
        DateFormat formatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.ENGLISH);
        return formatter.format(date);
    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1000];
        int l = 1;
        while (l > 0) {
            l = in.read(buf);
            if (l > 0) {
                out.write(buf, 0, l);
            }
        }
		in.close();
    }

    public static int copyStream(InputStream in, OutputStream out,
            int start, int end) throws IOException {
        long skipped = in.skip((long) start);
        if (start != skipped)  {
            throw new IOException("copyStream failed range start " + start);
        }
        byte[] buf = new byte[1000];
        int pos = start - 1;
        int count = 0;
        int l = 1;
        while (l > 0) {
            l = in.read(buf);
            if (l > 0) {
                pos = pos + l;
                if (pos > end)  {
                    l = l - (pos - end);
                    out.write(buf, 0, l);
                    count += l;
                    break;
                }
                out.write(buf, 0, l);
                count += l;
            }
        }
        return count;
    }

    public static void compressStream(InputStream in, OutputStream out) throws IOException {
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        copyStream(in, gzip);
        gzip.finish();
    }

    public static boolean acceptGzip(ExternalContext externalContext) {
        String acceptHeader = externalContext.getRequestHeaderMap()
                .get("Accept-Encoding");
        boolean acceptGzip = (null != acceptHeader) &&
                (acceptHeader.indexOf("gzip") >= 0);
        return acceptGzip;
    }

    public static boolean shouldCompress(String contentType) {
        if(contentType == null){
            return false;
        }
        if ( contentType.startsWith("audio/") ||
             contentType.startsWith("video/") ||
             contentType.startsWith("image/") ) {
            return false;
        }
        if (DEFAULT_EXCLUSIONS.contains(contentType))  {
            return false;
        }
        return true;
    }

    /**
     * Determines whether the component is "under" singleSubmit.
     *
     * @param component to test for singleSubmit
     * @return true if the component is under singleSubmit
     */
    public static boolean withinSingleSubmit(UIComponent component)  {
        UIComponent parent = component;
        while (null != parent)  {
            if ( parent.getAttributes().containsKey(
                    SingleSubmitRenderer.SINGLE_SUBMIT_MARKER) )  {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }
    
    
    // ICE-4342
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
