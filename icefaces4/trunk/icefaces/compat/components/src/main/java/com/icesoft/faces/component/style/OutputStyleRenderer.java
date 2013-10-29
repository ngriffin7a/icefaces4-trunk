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

package com.icesoft.faces.component.style;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.util.CoreUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icefaces.util.EnvUtils;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.beans.Beans;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.Resource;

/**
 * Created by IntelliJ IDEA. User: rmayhew Date: May 30, 2006 Time: 3:59:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class OutputStyleRenderer extends DomBasicRenderer {

    private static final Log log = LogFactory.getLog(OutputStyleRenderer.class);
    private static final String IE_EXTENTION = "_ie";
    private static final String IE_7_EXTENTION = "_ie7";
    private static final String IE_8_EXTENSION = "_ie8";
    private static final String SAFARI_EXTENTION = "_safari";
    private static final String SAFARI_MOBILE_EXTENTION = "_safarimobile";
    private static final String CSS_EXTENTION = ".css";
    private static final String DT_EXTENTION = "_dt";
    private static final String OPERA_EXTENTION = "_opera";
    private static final String OPERA_MOBILE_EXTENTION = "_operamobile";

    private static final int DEFAULT_TYPE = 0;
    private static final int IE = 1;
    private static final int SAFARI = 2;
    private static final int DT = 3;
    private static final int IE_7 = 4;
    private static final int SAFARI_MOBILE = 5;
    private static final int OPERA = 6;
    private static final int OPERA_MOBILE = 7;
    private static final int IE_8 = 8;

    private static final String[] extensions = {"", IE_EXTENTION, SAFARI_EXTENTION, 
        DT_EXTENTION, IE_7_EXTENTION, SAFARI_MOBILE_EXTENTION, OPERA_EXTENTION,
        OPERA_MOBILE_EXTENTION, IE_8_EXTENSION};

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, OutputStyle.class);
        try {
            DOMContext domContext =
                    DOMContext.attachDOMContext(facesContext, uiComponent);
            if (!domContext.isInitialized()) {
                OutputStyle outputStyle = (OutputStyle) uiComponent;
                Element styleEle = buildCssElement(domContext);
                String href = outputStyle.getHref();
                styleEle.setAttribute(HTML.HREF_ATTR, getResourceURL(facesContext,href));
                domContext.setRootNode(styleEle);
                int browserType = browserType(facesContext, uiComponent);
                if (browserType != DEFAULT_TYPE) {
                    if (href.endsWith(CSS_EXTENTION)) {
                        int i = href.indexOf(CSS_EXTENTION);
                        if (i > 0) {
                            String start = href.substring(0, i);
                            Element ieStyleEle = buildCssElement(domContext);
                            String extention = IE_EXTENTION;
                            if (browserType == SAFARI) {
                                extention = SAFARI_EXTENTION;
                            }
                            if (browserType == DT) {
                                extention = DT_EXTENTION;
                            }
                            if(browserType == IE_7){
                                extention = IE_7_EXTENTION;
                            }
                            if(browserType == IE_8){
                                extention = IE_8_EXTENSION;
                            }
                            if(browserType == SAFARI_MOBILE){
                                extention = SAFARI_MOBILE_EXTENTION;
                            }
                            if(browserType == OPERA){
                                extention = OPERA_EXTENTION;
                            }
                            if(browserType == OPERA_MOBILE){
                                extention = OPERA_MOBILE_EXTENTION;
                            }
                            String browserSpecificFilename = useSpecific(facesContext, start, extention);
                            if (browserSpecificFilename != null) {
                                // W3C spec: To make a style sheet preferred, set the rel attribute to "stylesheet" and name the style sheet with the title attribute
                                ieStyleEle.setAttribute(HTML.TITLE_ATTR, extention);
                                String hrefURL = CoreUtils.resolveResourceURL(facesContext, browserSpecificFilename);
                                ieStyleEle.setAttribute(HTML.HREF_ATTR, hrefURL);
                                styleEle.getParentNode().appendChild(ieStyleEle);
                            }
                        } else {
                            throw new RuntimeException(
                                    "OutputStyle file attribute is too short. " +
                                    "Needs at least one character before .css. Current Value is [" +
                                    href + "]");
                        }
                    } else {
                        Matcher matcher = Pattern.compile(".*javax\\.faces\\.resource/((.*)\\.css)(\\..*)?\\?ln=([^&]*)(&.*|$)").matcher(href);
                        if (matcher.matches()) {
                            Element ieStyleEle = buildCssElement(domContext);
                            String extension = browserType >= 0 && browserType < extensions.length ? extensions[browserType] : IE_EXTENTION;
                            ieStyleEle.setAttribute(HTML.TITLE_ATTR, extension);
                            String hrefURL = new StringBuffer(matcher.group(0)).insert(matcher.end(2), extension).toString();
                            ieStyleEle.setAttribute(HTML.HREF_ATTR, hrefURL);
                            String resourceName = new StringBuffer(matcher.group(1)).insert(matcher.end(2) - matcher.start(2), extension).toString();
                            Resource resource = facesContext.getApplication().getResourceHandler().createResource(resourceName, matcher.group(4));
                            if (resource != null) {
                                styleEle.getParentNode().appendChild(ieStyleEle);
                            }
                        }
                    }
                }

            }
            domContext.stepOver();
        } catch (Exception e) {
            log.error("Error in OutputStyleRenderer", e);
        }
    }

    private Element buildCssElement(DOMContext domContext) {
        Element styleEle = domContext.createElement("link");
        styleEle.setAttribute(HTML.REL_ATTR, "stylesheet");
        styleEle.setAttribute(HTML.TYPE_ATTR, "text/css");
        return styleEle;
    }

    private int browserType(FacesContext facesContext, UIComponent uiComponent) {
        int result = DEFAULT_TYPE;
        String useragent = ((OutputStyle)uiComponent).getUserAgent();
        if(useragent != null){
            return _browserType(useragent);
        }

        Object o = facesContext.getExternalContext().getRequest();
        if (o != null) {
            if (!EnvUtils.instanceofPortletRequest(o)) {
                HttpServletRequest request = (HttpServletRequest) o;
                useragent = request.getHeader("user-agent");
                if(useragent == null){
                    useragent = ((OutputStyle)uiComponent).getUserAgent();
                }
				if(useragent == null){
					//Get User-Agent for Weblogic/Webshpere
					useragent = request.getHeader("User-Agent");
				}
				if(useragent == null){
					//Get User-Agent for OC4J
					useragent = request.getHeader("USER-AGENT");
				}
                if(useragent == null){
                	if (log.isDebugEnabled()) {
                		log.debug("Not able to find user agent. Returning default");
                	}
                    return DEFAULT_TYPE;
                }
                if(((OutputStyle)uiComponent).getUserAgent() == null){
                    ((OutputStyle)uiComponent).setUserAgent(useragent.toLowerCase());
                }
                String user = useragent.toLowerCase();
                result = _browserType( user);

            } else {
            	if (log.isDebugEnabled()) {
            		log.debug(
                        "OutputStyleRenderer: Request is not HttpServletRequest. Its [" +
                        o.getClass().getName() + "]");
            	}
            }
        } else {
        	if (log.isDebugEnabled()) {
        		log.debug(
                    "IceStyleReader: facesContext.getExternalContext().getRequest() is null");
        	}
        }
        return result;
    }

    private int _browserType(String user) {
        int result = DEFAULT_TYPE;
        if (Beans.isDesignTime()) {
            result = DT;
        } else {
            if (user.indexOf("opera") < 0 && user.indexOf("msie") != -1) {
                result = IE;
                if(user.indexOf("msie 7") != -1){
                    result = IE_7;
                }
                if(user.indexOf("msie 8") != -1){
                    result = IE_8;
                }
            } else if (user.indexOf("safari") != -1) {
                result = SAFARI;
                if(user.indexOf("mobile") != -1) {
                    result = SAFARI_MOBILE;
                }
            } else if (user.indexOf("opera") != -1) {
                result = OPERA;
                if(user.indexOf("240x320") != -1) {
                    result = OPERA_MOBILE;
                }
            }
        }
        return result;
    }

    // Contributed code. See http://jira.icesoft.org/browse/ICE-8758.
    private String useSpecific(FacesContext facesContext, String start, String extention) {
        // assume we are not going to use the specific file
        boolean useSpecific=false;
        // this is the file we are going to verify existence of.
        String browserSpecificFilename = start + extention + CSS_EXTENTION;


        String realRoot = facesContext.getExternalContext().getRealPath("/");
        if (realRoot != null) {
            File realParent = new File(realRoot);


            // first use case of absolute file (/...),
            //
            // find our real root path, we need this to determine
            // existence of the file containing the extension.
            //
            // Then append the browser specific filename to the root and find
            // the file.
            if (browserSpecificFilename.startsWith("/")) {
                File browserSpecificFile = null;

                // can start with /[context_name]/
                // if it does, we need to strip it off

                String contextName = null;
                Object servletContextObject = FacesContext.getCurrentInstance().getExternalContext().getContext();
                if (servletContextObject != null && servletContextObject instanceof ServletContext) {
                    contextName = ((ServletContext)servletContextObject).getContextPath();
                }

                // if context name is available then we need to strip it off of the URL if it
                // is there otherwise just use href as-is.
                if (contextName != null) {

                    // starts with context name?
                    String contextRoot = contextName +"/";
                    if (browserSpecificFilename.startsWith(contextRoot)) {
                        browserSpecificFile = new File (realParent,browserSpecificFilename.substring(contextRoot.length()));
                    }
                    else {
                        browserSpecificFile = new File (realParent,browserSpecificFilename.substring(1));
                    }

                }
                useSpecific=browserSpecificFile != null ? browserSpecificFile.exists(): false;
            }
            // second use case of relative file (.../...),
            //
            // find the directory of the relative path of the request
            //
            //
            // Then append the browser specific filename to the root and find
            // the file.
            else {
                String pathInfo = facesContext.getExternalContext().getRequestServletPath();
                if (pathInfo != null) {
                    pathInfo=new File(pathInfo).getParent();
                    File realPath = new File (realParent,pathInfo);
                    File browserSpecificFile = new File (realPath,browserSpecificFilename);
                    useSpecific=browserSpecificFile.exists();
                }
            }
        }
        return (useSpecific ? browserSpecificFilename : null);
    }
}
