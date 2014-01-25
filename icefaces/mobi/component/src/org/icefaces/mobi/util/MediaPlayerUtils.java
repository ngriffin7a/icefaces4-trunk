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
package org.icefaces.mobi.util;

import static org.icefaces.mobi.util.Constants.SPACE;
import static org.icefaces.mobi.util.HTML.CLASS_ATTR;
import static org.icefaces.mobi.util.HTML.STYLE_ATTR;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpSession;

import org.icefaces.application.ResourceRegistry;
import org.icefaces.ace.util.IceOutputResource;
import org.icefaces.ace.util.ClientDescriptor;

public class MediaPlayerUtils {

    private static final Logger logger = Logger.getLogger(MediaPlayerUtils.class.getName());


    public static String deriveAndSetSourceAttribute(UIComponent comp, FacesContext context){
        String src = null;
        Object mediaObject = comp.getAttributes().get("value");
        if (null != mediaObject) {
            String scope = ((String)comp.getAttributes().get("scope")).toLowerCase().trim();
            if (!scope.equals("flash") && !(scope.equals("window")) && !(scope.equals("application"))
                    && (!scope.equals("request")) && (!scope.equals("view"))) {
                scope = "session";
            }
            String name = (String)comp.getAttributes().get("name");
            if (null == name || name.equals("")){
                name = "media_" + comp.getClientId();
            }
            src = processSrcAttribute(context, mediaObject, name, 
                (String)comp.getAttributes().get("type"), scope, 
                (String)comp.getAttributes().get("url"));
        }
        return src;
    }
    
    public static String processStaticSrc(FacesContext facesContext, Object o, String url) {
        String value = String.valueOf(o);
        // support url as an alias for value
        if (value == null) {
            value = url;
        }
        if (value != null) {
            return facesContext.getApplication().getViewHandler().getResourceURL(facesContext, value);
        } else {
            return "";
        }
    }
    
    private static String processSrcAttribute(FacesContext facesContext, Object o,
                                      String name, String mimeType,
                                      String scope, String url) {
        if (o instanceof IceOutputResource) {
            IceOutputResource iceResource = (IceOutputResource) o;
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("ALREADY have IceOutputResource so returning:-" + iceResource.getRequestPath());
            }
            return registerAndGetPath(facesContext, scope, iceResource);
        }
        if (o instanceof byte[]) {
            IceOutputResource ior = new IceOutputResource(name, o, mimeType);
            String registeredPath = 
                    registerAndGetPath(facesContext, scope, ior);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("instance of byte array returning path=" + registeredPath + " with mimeType=" + ior.getContentType());
            }
            return registeredPath;
        } else {
            return processStaticSrc(facesContext, o, url);
        }

    }
    
    private static String registerAndGetPath(FacesContext facesContext,
            String scope, IceOutputResource iceResource) {
        String registeredPath = "";
        if (scope.equals("flash"))  {
            registeredPath = ResourceRegistry.addSessionResource(iceResource);
        } else if (scope.equals("application"))  {
            registeredPath = ResourceRegistry.addApplicationResource(iceResource);
        } else if (scope.equals("window")) {
            registeredPath = ResourceRegistry.addWindowResource(iceResource);
        } else if (scope.equals("view"))  {
            registeredPath = ResourceRegistry.addViewResource(iceResource);
        } else if (scope.equals("session")) {
            registeredPath = ResourceRegistry.addSessionResource(iceResource);
        }

        //all but application scope may require a session for the resource
        HttpSession session  = (HttpSession) facesContext
                .getExternalContext().getSession(false);
        if (null != session)  {
            registeredPath += ";jsessionid=" + 
                    MobiJSFUtils.getSessionIdCookie(facesContext);
        }

        return registeredPath;
    }


    public static void encodeBaseMediaElementStart(ResponseWriter writer, UIComponent uic, 
        FacesContext context, String elem, String src) throws IOException{
        writer.startElement(elem, null);
        
        //src attribute
        writer.writeAttribute("src", src, null);
        
        //loop attribute
        if ((Boolean)uic.getAttributes().get("loop")){
            writer.writeAttribute("loop", "loop", null);
        }
        //preload attribute
        String preloadVal = "auto";
        String preload = (String)uic.getAttributes().get("preload");
        if (preload != null && preload.length() > 0){
            if (preload.toLowerCase().trim().equals("none")){
                preloadVal = "none";
            }
            if (preload.toLowerCase().trim().equals("metadata")){
                preloadVal="metadata";
            }
        }
        writer.writeAttribute("preload", preloadVal, null);
        //controls attribute
        if ((Boolean)uic.getAttributes().get("controls")){
            writer.writeAttribute("controls", "controls", null);
        }
        //type attribute
        String type = (String)uic.getAttributes().get("type");
        if (type != null && type.length() > 0) {
            writer.writeAttribute("type", type, null);
        }
        //muted attribute
        if ((Boolean)uic.getAttributes().get("muted")){
            writer.writeAttribute("muted", "muted", null);
        }
    }
    
    public static void encodeBaseMediaElementEnd(ResponseWriter writer, UIComponent uic, 
        FacesContext context, String elem, String src) throws IOException{
        writer.endElement(elem);
        
        ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
        // write inline image link
        String linkLabel = (String)uic.getAttributes().get("linkLabel");
        if (!client.isIOS() && linkLabel != null)  {
            writer.startElement("br", null);
            writer.endElement("br");
            writer.startElement("a", null);
            writer.writeAttribute("target", "_blank", null);
            writer.writeAttribute("href", src, null);
            writer.writeText(uic.getAttributes().get("linkLabel"), null);
            writer.endElement("a");
        }
    }
    
    public static void writeStandardLayoutAttributes(ResponseWriter writer,
                    UIComponent component, String baseClass) throws IOException  {
        StringBuilder inputStyle = new StringBuilder(baseClass);
        String styleClass = (String)component.getAttributes().get("styleClass");
        if (null != styleClass)  {
            inputStyle.append(SPACE).append(styleClass);
        }
        if( inputStyle.length() > 0 ){
            writer.writeAttribute(CLASS_ATTR, inputStyle, null);
        }
        String style = (String)component.getAttributes().get("style");
        if (null != style)  {
            writer.writeAttribute(STYLE_ATTR, style, null);
        }
    }

}
