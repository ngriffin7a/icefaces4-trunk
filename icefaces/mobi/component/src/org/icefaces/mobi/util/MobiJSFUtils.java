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

package org.icefaces.mobi.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.icefaces.impl.application.AuxUploadResourceHandler;
import org.icefaces.impl.application.AuxUploadSetup;
import org.icefaces.impl.util.CoreUtils;
import org.icefaces.impl.util.DOMUtils;
import org.icefaces.util.EnvUtils;

import org.icefaces.ace.util.ClientDescriptor;

public class MobiJSFUtils {
    
    private static Logger logger = Logger.getLogger(Utils.class.getName());
    
    static String COOKIE_FORMAT = "org.icemobile.cookieformat";

    private static Map<String, String> CONTENT_TYPES;
    static {
        CONTENT_TYPES = new HashMap<String, String>();
        CONTENT_TYPES.put(".png", "image/png");
        CONTENT_TYPES.put(".jpg", "image/jpg");
        CONTENT_TYPES.put(".mp3", "audio/mp3");
        CONTENT_TYPES.put(".mp4", "video/mp4");
    }

    public static HttpServletRequest getRequest(FacesContext facesContext){
        return (HttpServletRequest)facesContext.getExternalContext().getRequest();
    }
        
    public static ClientDescriptor getClientDescriptor(){
        HttpServletRequest request = MobiJSFUtils.getRequest(FacesContext
                .getCurrentInstance());
        ClientDescriptor client = ClientDescriptor.getInstance(request);
        return client;
    }
    
    public static boolean decodeComponentFile(FacesContext facesContext,
            String clientId, Map fileMeta) throws IOException {
        HttpServletRequest request = (HttpServletRequest) facesContext
                .getExternalContext().getRequest();
        boolean isValid = false;

        String partUploadName = clientId;
        Part part = null;
        InputStream fileStream = null;
        String contentType = null;
        Map auxMap = AuxUploadResourceHandler.getAuxRequestMap();
        try {
            part = request.getPart(partUploadName);
            if (null == part) {
                part = (Part) auxMap.get(partUploadName);
            }
        } catch (IOException e) {
            throw e;
        } catch (Throwable t) {
            // ignore Throwable here since auxUpload is not multipart
            // and not-null part must be checked and we may not have
            // getPart API on Servlet 2.5
        }
        if (null == part) {
            Map commonsMeta = (Map) request.getAttribute("org.icemobile.file." + clientId);
            if (null == commonsMeta) {
                commonsMeta = (Map)
                    auxMap.get("org.icemobile.file." + clientId);
            }
            if (null != commonsMeta) {
                contentType = (String) commonsMeta.get("contentType");
                fileStream = (InputStream) commonsMeta.get("stream");
            }
        }
        if (null != part) {
            contentType = part.getContentType();
            fileStream = part.getInputStream();
        }

        String fileName = Long.toString(
                Math.abs(UUID.randomUUID().getMostSignificantBits()), 32);
        String fileExtension = Utils.FILE_EXT_BY_CONTENT_TYPE.get(contentType);

        //final case is a simulated upload
        if (null == contentType)  {
            String simulatedFile = request.getParameter(partUploadName);
            if (null != simulatedFile)  {
                //missing contentType indicates simulator
                fileExtension = simulatedFile.substring(
                        simulatedFile.lastIndexOf(".") );
                contentType = CONTENT_TYPES.get(fileExtension);
                fileStream = MobiJSFUtils.class
                    .getClassLoader().getResourceAsStream(
                        "META-INF/resources/org.icefaces.component.skins/simulator/" +
                        simulatedFile );
            }
        }

        if (null != fileExtension) {
            fileName += fileExtension;
        } else {
            fileName += ".oth";
        }

        isValid = createMapOfFile(fileMeta, request, fileStream, fileName,
                contentType, facesContext);

        return isValid;
    }
    
    public static boolean createMapOfFile(Map map, HttpServletRequest request,
            InputStream fileStream, String fileName, String contentType,
            FacesContext facesContext) throws IOException {

        final String UPLOADS_FOLDER = "uploads";
        String FILE_SEPARATOR = System.getProperty("file.separator");
        String folder = CoreUtils.getRealPath(facesContext, FILE_SEPARATOR);
        // logger.info("path="+folder);
        String sessionId = CoreUtils.getSessionId(facesContext);
        String relativePath = "";

        if (sessionId != null && sessionId.length() > 0) {
            relativePath += FILE_SEPARATOR + UPLOADS_FOLDER + FILE_SEPARATOR
                    + sessionId + FILE_SEPARATOR + fileName;
            folder += UPLOADS_FOLDER + FILE_SEPARATOR + sessionId
                    + FILE_SEPARATOR;
            // logger.info("folder is"+folder);
        } else {
            relativePath += UPLOADS_FOLDER + FILE_SEPARATOR + fileName;
            folder += UPLOADS_FOLDER + FILE_SEPARATOR;
        }
        File dirFile = new File(folder);
        File newFile = new File(dirFile, fileName);

        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        try {
            Utils.copyStream(fileStream, new FileOutputStream(newFile));
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error writing uploaded file to disk ", e);
        }
        map.put("file", newFile);
        map.put("contentType", contentType);
        map.put("relativePath", relativePath);
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("wrote " + newFile.getAbsolutePath());
        }

        return (newFile.length() > 0);

    }
    
    public static boolean uploadInProgress(UIComponent comp)  {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String partUploadName = comp.getClientId(facesContext);
        return uploadInProgress(partUploadName);
    }
    
    public static boolean uploadInProgress(String clientId)  {
        Map auxMap = AuxUploadResourceHandler.pollAuxRequestMap();
        if (null == auxMap)  {
            return false;
        }
        boolean inProgress = auxMap.containsKey(clientId) ||
            auxMap.containsKey("org.icemobile.file." + clientId);
        return inProgress;
    }
    
    public static String getICEmobileSXScript(String command,
            Map<String,String> params, UIComponent comp){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)facesContext.getExternalContext().getRequest();
        String sessionID = getSessionIdCookie(facesContext);
        return SXUtils.getICEmobileSXScript(request, command, params,
                comp.getClientId(facesContext), sessionID,
                MobiJSFConstants.SX_UPLOAD_PATH);
    }

    public static String getICEmobileSXScript(String command, UIComponent comp){
        return getICEmobileSXScript(command, null, comp);
    }

    public static String getSessionIdCookie() {
        return getSessionIdCookie(FacesContext.getCurrentInstance());
    }

    public static String getSessionIdCookie(FacesContext facesContext) {
        String sessionID = EnvUtils.getSafeSession(facesContext).getId();
        String cookieFormat = (String) facesContext.getExternalContext()
                .getInitParameterMap().get(COOKIE_FORMAT);
        if (null == cookieFormat) {
            // if we have more of these, implement EnvUtils for ICEmobile
            return sessionID;
        }
        StringBuilder out = new StringBuilder();
        Formatter cookieFormatter = new Formatter(out);
        cookieFormatter.format(cookieFormat, sessionID);
        cookieFormatter.close();
        return out.toString();
    }
    
    public static String getICEmobileRegisterSXScript(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String sessionIdParam = getSessionIdCookie(facesContext);
        String uploadURL = AuxUploadSetup.getInstance().getUploadURL();
        return "mobi.registerAuxUpload('"+sessionIdParam+"','"+uploadURL+"');";
    }

    public static String getPostURL()  {
        return AuxUploadSetup.getInstance().getUploadURL();
    }

    /**
     * use this to ascertain that the domdiff does not wipe out the script tag
     * for updating components
     */
    public static int generateHashCode(Object value) {
        int hashCode = 0;
        if (value != null) {
            hashCode = value.toString().hashCode();
        }
        return hashCode;
    }
    
    /**
     * Return the name value pairs parameters as a ANSI escaped string formatted
     * in query string parameter format for ice.s or ice.se calls for f:param
     * support
     * 
     * @param children
     *            List of children
     * @return a String in the form function(p){name1,value1,name2,value2...});
     */
    public static String asParameterString(List<UIParameter> children) {
        StringBuffer builder = new StringBuffer();
        builder.append("function(p){");
        for (UIParameter param : children) { // assume all params are strings
            builder.append("p('" + DOMUtils.escapeAnsi(param.getName()) + "'")
                    .append(",'")
                    .append(DOMUtils.escapeAnsi((String) param.getValue())
                            .replace(' ', '+')).append("');");
        }

        builder.append("}");
        return builder.toString();
    }

    /**
     * use this one for mobi:ajaxRequest until JSON builder replaces this stuff
     */
    public static String asParameterStringForMobiAjax(List<UIParameter> children){
        if (children.isEmpty()){
            return "{}";
        }
        StringBuffer builder = new StringBuffer();
        builder.append("{");
        boolean first = true;
        for (UIParameter param : children){
            if (!first){
                builder.append(", ");
            }
            builder.append("'").append(DOMUtils.escapeAnsi(param.getName())).append("'");
            builder.append(": '")
                    .append(DOMUtils.escapeAnsi((String) param.getValue())
                            .replace(' ', '+')).append("'");
            first = false;
        }
        builder.append("}");
        return builder.toString();
    }

}
