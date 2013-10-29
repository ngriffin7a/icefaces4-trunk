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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Formatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SXUtils {

    public final static String SX_UPLOAD = "sxUploads";
    public final static String SX_UPLOAD_PROGRESS = "sxUploadProgress";
    public final static String SX_THUMB_MAP = "sxUploadThumbMap";
    public final static String SESSION_KEY_SX_REGISTERED = "sxRegistered";
    public final static String USER_AGENT_SX_PART = "ICEmobile-SX";
    public final static String USER_AGENT_SX_FULL = "HyperBrowser-ICEmobile-SX/1.0";
    public final static String COOKIE_FORMAT = "org.icemobile.cookieformat";

    /**
     * Get the SX Register URL.
     * 
     * Format is icemobile:c=register&r=<current-url>&JSESSIONID=<session-id>&u=<upload-url>
     * 
     * @param request The servlet request
     * @param uploadPath The path of the upload post from the context, eg 'icemobile'
     *   for JSP or 'javax.faces.resource./auxupload.txt.jsf' for JSF.
     * @return The escaped SX register URL.
     */
    public static String getRegisterSXURL(HttpServletRequest request,
            String uploadPath) {
        String sessionID = getSessionIdCookie(request);
        String redirectParm = "&r=" + Utils.getBaseURL(request);
        String forward = (String) request
                .getAttribute("javax.servlet.forward.servlet_path");
        if (forward == null) {
            forward = request.getRequestURI().substring(
                    request.getContextPath().length() + 1);
        } else if (forward.startsWith("/")) {
            forward = forward.substring(1);
        }
        String params = "";
        if (request.getQueryString() != null) {
            params = "?" + request.getQueryString();
            try {
                params = URLEncoder.encode(params,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        redirectParm += forward + params;
        String jsessionParam = "&JSESSIONID=" + sessionID;
        String uploadParam = "&u=" + Utils.getBaseURL(request)+uploadPath;
        String url = "icemobile:c=register" + redirectParm + jsessionParam
                + uploadParam;
        return url;
    }
    
    /**
     * Get the default SX Register URL ending in /icemobile
     * @param request The Servlet Request
     * @return The full upload URL
     */
    public static String getRegisterSXURL(HttpServletRequest request){
        String sessionID = getSessionIdCookie(request);
        String redirectParm = "&r=" + Utils.getBaseURL(request);
        String forward = (String)request.getAttribute("javax.servlet.forward.servlet_path");
        if( forward == null ){
            forward = "";
        }
        else if( forward.startsWith("/")){
            forward = forward.substring(1);
        }
        String params = "";
        if( request.getQueryString() != null ){
            params = "?"+request.getQueryString();
        }
        redirectParm += forward + params;
        String jsessionParam = "&JSESSIONID=" + sessionID;
        String uploadParam = "&u="+Utils.getBaseURL(request) + "/icemobile";
        String url = "icemobile:c=register"+redirectParm+jsessionParam+uploadParam;
        return url;
    }

    /**
     * Has the user registered ICEmobile-SX for the current application session?
     * 
     * @param request
     *            The ServletRequest
     * @return true if SX has been registered, else false
     */
    public static boolean isSXRegistered(HttpServletRequest request) {
        //JSP registration will 
        return request.getSession().getAttribute(SESSION_KEY_SX_REGISTERED) == Boolean.TRUE
                || request.getSession().getAttribute("iceAuxRequestMap") != null;
    }

    /**
     * Is the current request from ICEmobile-SX
     * 
     * @param request
     *            The Servlet Request
     * @return true if request from SX, else false
     */
    public static boolean isSXRequest(HttpServletRequest request) {
        String userAgent = Utils.getUserAgent(request);
        if (userAgent != null && userAgent.contains(USER_AGENT_SX_PART)) {
            return true;
        } else {
            return false;
        }
    }

    public static void setSXSessionKeys(HttpServletRequest request) {
        request.getSession().setAttribute(SESSION_KEY_SX_REGISTERED,
                Boolean.TRUE);
        request.getSession().setAttribute(Constants.USER_AGENT_COOKIE,
                USER_AGENT_SX_FULL);
        String cloudPushId = request.getParameter(Constants.CLOUD_PUSH_KEY);
        if (cloudPushId != null) {//TODO should cloudPushId be reset on uploads?
            request.getSession().setAttribute(Constants.CLOUD_PUSH_KEY,
                    cloudPushId);
        }
    }

    /**
     * Tracks the progress of an auxiliary upload with ICEmobile-SX. If no current upload
     * is in progress, will return 0, otherwise 1-99.
     * 
     * @param request The Servlet Request
     * @return 0, if no upload in progress, else 1-99
     */
    public static int getAuxiliaryUploadProgress(HttpServletRequest request) {
        Integer progress = (Integer)request.getSession().getAttribute(SX_UPLOAD_PROGRESS);
        if (null == progress) {
            return 0;
        }
        return progress.intValue();
    }

    /**
     * The session map of the auxiliary uploads. The map is keyed by
     * the id of the uploading components.
     * 
     * @param request The Servlet Request
     * @return The session map of uploads.
     */
    public static Map<String,File> getAuxiliaryUploadMap(HttpServletRequest request) {
        /* note: this aux upload map does not follow the session/request 
         * pattern of the ICefaces auxRequestMap, simply a session map
         * cleanup will be done through listeners
         */
        @SuppressWarnings("unchecked")
        Map<String,File> map = (Map<String,File>) request.getSession().getAttribute(SX_UPLOAD);
        if (null == map) {
            map = new HashMap<String,File>();
            request.getSession().setAttribute(SX_UPLOAD, map);
        } 
        return map;
    }

    public static String getICEmobileSXScript(HttpServletRequest request,
            String command, Map<String,String> params,
            String id, String sessionID, String uploadPath) {
        String uploadURL = Utils.getBaseURL(request)+uploadPath;
        String fullCommand = command + "?id=" + id + encodeParams(params);
        String script = "window.location='icemobile:c=";
        try{
            script += URLEncoder.encode(fullCommand, "UTF-8")
                    + "&r='+escape(window.location)+'";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (null != sessionID) {
            script += "&JSESSIONID=" + sessionID;
        }
        try {
            script += "&u=" + URLEncoder.encode(uploadURL,"UTF-8") + "'";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return script;
    }

    public static String encodeParams(Map<String,String> params)  {
        if (null == params)  {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (String name : params.keySet())  {
            //initial & required to follow id param
            result.append("&");
            result.append(URLEncoder.encode(name)).append("=");
            result.append(URLEncoder.encode(params.get(name)));
        }
        return result.toString();
    }

    public static String getICEmobileRegisterSXScript(HttpServletRequest request, String uploadPath) {
        String sessionID = getSessionIdCookie(request);
        String uploadURL = Utils.getBaseURL(request)+uploadPath;
        return "ice.registerAuxUpload('" + sessionID + "','" + uploadURL
                + "');";
    }
    
    public static void setAuxUploadProgress(int progress, HttpSession session){
        if( progress < 1 ||  progress > 99){
            progress = 0;
        }
        session.setAttribute(SX_UPLOAD_PROGRESS,Integer.valueOf(progress));
    }

    public static String getSessionIdCookie(HttpServletRequest request) {
        String sessionID = null;
        String cookieFormat = null;
        HttpSession httpSession = request.getSession(false);
        if (null != httpSession) {
            sessionID = httpSession.getId();
            cookieFormat = httpSession.getServletContext()
                .getInitParameter(COOKIE_FORMAT);
        } else {
            return sessionID;
        }

        if (null == cookieFormat) {
            return sessionID;
        }
        StringBuilder out = new StringBuilder();
        Formatter cookieFormatter = new Formatter(out);
        cookieFormatter.format(cookieFormat, sessionID);
        cookieFormatter.close();
        return out.toString();
    }

    public static Map<String,File> getSXUploadThumbMap(HttpServletRequest request){
        @SuppressWarnings("unchecked")
        Map<String,File> map = (Map<String,File>)request.getSession().getAttribute(SX_THUMB_MAP);
        if( map == null ){
            map = new HashMap<String,File>();
        }
        return map;
    }


}
