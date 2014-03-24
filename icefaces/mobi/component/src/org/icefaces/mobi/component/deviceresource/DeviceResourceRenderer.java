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

package org.icefaces.mobi.component.deviceresource;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;

import org.icefaces.mobi.util.Attribute;
import org.icefaces.mobi.util.HTML;
import org.icefaces.mobi.util.MobiJSFConstants;
import org.icefaces.mobi.util.MobiJSFUtils;
import org.icefaces.mobi.util.PassThruAttributeWriter;
import org.icefaces.mobi.util.CSSUtils;
import org.icefaces.util.ClientDescriptor;
import org.icefaces.mobi.util.Constants;
import org.icefaces.mobi.util.SXUtils;

@ListenerFor(systemEventClass = javax.faces.event.PostAddToViewEvent.class)
public class DeviceResourceRenderer  extends Renderer implements javax.faces.event.ComponentSystemEventListener {
    private static final Logger log = Logger.getLogger(DeviceResourceRenderer.class.getName());

    

    public static final String CSS_LOCATION = "org.icefaces.component.skins";
    public static final String UTIL_RESOURCE =
            "org.icefaces.component.util";
    public static final String RESOURCE_URL_ERROR = "MOBI_RES_NOT_FOUND";
    public static final String IOS_APP_ID = "727736414";
    
    public static final String META_CONTENTTYPE = "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>";
    public static final String META_VIEWPORT = "<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0'/>";
    public static final String META_IOS_WEBAPPCAPABLE = "<meta name='apple-mobile-web-app-capable' content='yes'/>";
    public static final String META_IOS_APPSTATUSBAR = "<meta name='apple-mobile-web-app-status-bar-style' content='black'/>";
    public static final String META_IOS_SMARTAPPBANNER = "<meta name='apple-itunes-app' content=\"app-id=%s, app-argument=%s\"/>";
    
    public static final String LINK_SHORTCUT_ICON = "<link href='%s/resources/images/favicon.ico' rel='shortcut icon' type='image/x-icon'/>";
    public static final String LINK_FAV_ICON = "<link href='%s/resources/images/favicon.ico' rel='icon' type='image/x-icon'/>";
    
    public static final String SCRIPT_ICEMOBILE = "<script type='text/javascript' src='%s%s/javascript/icemobile.js'></script>";
	public static final String SCRIPT_SIMULATOR = "simulator-interface.js";
	public static final String CSS_SIMULATOR = "simulator.css";

    public void processEvent(ComponentSystemEvent event)
            throws AbortProcessingException {
        // http://javaserverfaces.java.net/nonav/docs/2.0/pdldocs/facelets/index.html
        // Finally make sure the component is only rendered in the header of the
        // HTML document.
        UIComponent component = event.getComponent();
        FacesContext context = FacesContext.getCurrentInstance();
        if (log.isLoggable(Level.FINER)) {
            log.finer("processEvent for component = " + component.getClass().getName());
        }
        context.getViewRoot().addComponentResource(context, component, HTML.HEAD_ELEM);
    }
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent uiComponent) throws IOException {
        
        DeviceResource comp = (DeviceResource)uiComponent;
        
        boolean ios6orHigher = false;
        boolean desktop = false;
        boolean isSimulated = false;
    
        ClientDescriptor client = ClientDescriptor
                .getInstance((HttpServletRequest)context.getExternalContext().getRequest());
        ios6orHigher = client.isIOS6() || client.isIOS7();
        if( !ios6orHigher ){
            desktop = client.isDesktopBrowser();
        }
        if (desktop) {
            isSimulated = client.isSimulator();
        }

        String contextRoot = context.getExternalContext().getRequestContextPath();
        
        ResponseWriter writer = context.getResponseWriter();
        
        writer.write(String.format(LINK_FAV_ICON, contextRoot));
        writer.write(String.format(LINK_SHORTCUT_ICON, contextRoot));
        if( !desktop ){
            writer.write(META_VIEWPORT);
            if( ios6orHigher ){
                writer.write(META_IOS_WEBAPPCAPABLE);
                writer.write(META_IOS_APPSTATUSBAR);
                if (isNeedAppBanner(context, comp, client))  {
                    String smartAppMeta = String.format(META_IOS_SMARTAPPBANNER, IOS_APP_ID, 
                            SXUtils.getRegisterSXURL(MobiJSFUtils.getRequest(context),
                                    MobiJSFConstants.SX_UPLOAD_PATH));
                    writer.write(smartAppMeta);
                    context.getAttributes().put(Constants.IOS_SMART_APP_BANNER_KEY, Boolean.TRUE);
                }
            }
        }

        if (client.isAndroid2OS()) {
            writeOverthrow(context);
        }

        if (isSimulated)  {
            writeSimulatorResources(context, comp);
        }
        encodeMarkers(writer, client);

    }

    private void writeOverthrow(FacesContext context) throws IOException {
        Resource ot = context.getApplication().getResourceHandler().createResource("overthrow.js", UTIL_RESOURCE);
        String src = ot.getRequestPath();
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeAttribute("src", src, null);
        writer.endElement("script");
    }

    private boolean isNeedAppBanner(FacesContext facesContext, 
            DeviceResource comp, ClientDescriptor client)  {
        ProjectStage projectStage = facesContext.getApplication().getProjectStage();
        if (ProjectStage.Development == projectStage)  {
            return false;
        }
        return (comp.isIncludeIOSSmartAppBanner() && !client.isSXRegistered());
    }

    private void writeOutDeviceStyleSheets(FacesContext facesContext, DeviceResource comp) throws IOException {
        
        /**
         * The component has three modes in which it executes.
         * 1.) no attributes - then component tries to detect a mobile device
         *     in from the user-agent.  If a mobile device is discovered, then
         *     it will fall into three possible matches, iphone, ipad,  android and
         *     blackberry.  If the mobile device is not not know then ipad
         *     is loaded. Library is always assumed to be DEFAULT_LIBRARY.
         *
         * 2.) name attribute - component will default to using a library name
         *     of DEFAULT_LIBRARY.  The name attribute specifies one of the
         *     possible device themes; iphone.css, android.css or bberry.css.
         *     Error will result if named resource could not be resolved.
         *
         * 3.) name and libraries attributes. - component will use the library
         *     and name specified by the user.  Component is fully manual in this
         *     mode. Error will result if name and library can not generate a
         *     value resource.
         */
        String resourceUrl = RESOURCE_URL_ERROR;
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.LINK_ELEM, comp);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.LINK_TYPE_TEXT_CSS, HTML.TYPE_ATTR);
        writer.writeAttribute(HTML.REL_ATTR, HTML.STYLE_REL_STYLESHEET, HTML.REL_ATTR);
        PassThruAttributeWriter.renderNonBooleanAttributes(
                writer, comp, new Attribute[]{new Attribute("media",null)});
        writer.writeURIAttribute(HTML.HREF_ATTR, resourceUrl, HTML.HREF_ATTR);
        writer.endElement(HTML.LINK_ELEM);
        
    }

    private void writeSimulatorResources(FacesContext facesContext,
            DeviceResource component) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        Resource simulatorCss = facesContext.getApplication()
            .getResourceHandler().createResource(
                CSS_SIMULATOR, CSS_LOCATION, "text/css");
        writer.startElement(HTML.LINK_ELEM, component);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.LINK_TYPE_TEXT_CSS,
                HTML.TYPE_ATTR);
        writer.writeAttribute(HTML.REL_ATTR, HTML.STYLE_REL_STYLESHEET,
                HTML.REL_ATTR);
        writer.writeURIAttribute(HTML.HREF_ATTR,
                simulatorCss.getRequestPath(), HTML.HREF_ATTR);
        writer.endElement(HTML.LINK_ELEM);

        Resource simulatorScript = facesContext.getApplication()
            .getResourceHandler().createResource(
                SCRIPT_SIMULATOR, UTIL_RESOURCE );
        String src = simulatorScript.getRequestPath();
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeAttribute("src", src, null);
        writer.endElement("script");

        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeText(
            "console.log('Welcome to the Matrix');",null);
        writer.endElement("script");
    }

    public void encodeMarkers(ResponseWriter writer, ClientDescriptor client) throws IOException {
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        String markers = " ui-mobile";
        if( client.isIE10Browser() ){
            markers += " ie10";
        }
        if( client.isAndroidBrowserOrWebView()){
            markers += " android-browser";
        }
        if( client.isDesktopBrowser()){
            markers += " desktop";
        }
        if( client.isSimulator() ){
            markers += " simulator";
        }
        writer.writeText("document.documentElement.className = document.documentElement.className+'" 
                + markers + "'; if (window.addEventListener) window.addEventListener('load', function() {document.body.className = 'ui-body-c';});", null);
        
        writer.endElement("script");
    }
    
    private String deriveLibrary(Map attributes){
        String library = (String) attributes.get(HTML.LIBRARY_ATTR);
        if( library == null ){
            library = CSS_LOCATION;
        }
        return library;
    }
    


}
