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

package org.icefaces.mobi.component.scan;


import java.io.IOException;
import java.lang.Boolean;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.System;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.sun.corba.se.impl.monitoring.MonitoredObjectImpl;
import org.icefaces.impl.application.AuxUploadResourceHandler;
import org.icefaces.mobi.renderkit.BaseInputRenderer;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.util.MobiJSFUtils;
import org.icefaces.mobi.util.CSSUtils;
import org.icefaces.util.ClientDescriptor;
import org.icefaces.util.EnvUtils;

import static org.icefaces.ace.util.HTML.*;

public class ScanRenderer extends BaseInputRenderer {
    private static final Logger logger = Logger.getLogger(ScanRenderer.class.getName());

    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Scan scan = (Scan) uiComponent;
        String clientId = scan.getClientId();
        if (scan.isDisabled()) {
            return;
        }
        Map requestParameterMap = facesContext.getExternalContext()
                .getRequestParameterMap();
        String valueId = clientId;
        Object submitted = requestParameterMap.get(valueId);
        if (null == submitted)  {
            Map auxMap = AuxUploadResourceHandler.getAuxRequestMap();
            submitted = auxMap.get(valueId);
        }
        if (null != submitted) {
            String submittedString = String.valueOf(submitted);
            if (submittedString != null){
                Object convertedValue = this.getConvertedValue(facesContext, uiComponent, submittedString);
                this.setSubmittedValue(scan, convertedValue);
                scan.setButtonLabel(scan.getButtonLabel()) ;
            }

        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        Scan scan = (Scan) uiComponent;
		ResponseWriterWrapper writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
		String clientId = scan.getClientId();
		UIComponent fallbackFacet = scan.getFacet("fallback");

		writer.startElement(SPAN_ELEM, scan);
		writer.writeAttribute(ID_ATTR, clientId);
		writer.writeAttribute(CLASS_ATTR, "mobi-scan");
        writer.startElement(INPUT_ELEM, uiComponent);
        writer.writeAttribute(TYPE_ATTR, "hidden");
        writer.writeAttribute(ID_ATTR, clientId+"-hid");
        writer.writeAttribute(NAME_ATTR, clientId );
        writer.endElement(INPUT_ELEM);
        String oldLabel = scan.getButtonLabel();
        String capturedLabel=scan.getCaptureMessageLabel();
  //   System.out.println(" uploadin progress="+MobiJSFUtils.uploadInProgress(scan));
 /*       if (MobiJSFUtils.uploadInProgress(scan))  {
            scan.setButtonLabel(capturedLabel) ;
        } */
        StringBuilder baseClass = new StringBuilder(CSSUtils.STYLECLASS_BUTTON);
        //ClientDescriptor cd = scan.getClient();
		// button element
		writer.startElement(BUTTON_ELEM, scan);
		writer.writeAttribute(ID_ATTR, clientId + "_button");
		writer.writeAttribute(NAME_ATTR, clientId + "_button");
		writer.writeAttribute(TYPE_ATTR, "button");
		//writeStandardAttributes(writer, scan, baseClass.toString(), IDevice.DISABLED_STYLE_CLASS);
		//default value of unset in params is Integer.MIN_VALUE
        // register callback for ICE-10126
		String launchFailed = fallbackFacet != null ? "ice.mobi.fallback.setupLaunchFailed('"+clientId+"_button','"+clientId+"_fallback');" : "";
		String script = launchFailed + "bridgeit.scan('" + clientId + "', 'callback"+clientId+"', {postURL:'" + scan.getPostURL() + "', "
		+ "cookies:{'JSESSIONID':'" + MobiJSFUtils.getSessionIdCookie(facesContext) +  "'}});";
		writer.writeAttribute(ONCLICK_ATTR, script);
		boolean disabled = scan.isDisabled();
		if (disabled) writer.writeAttribute(DISABLED_ATTR, "disabled");
		String style = scan.getStyle();
		if (style != null) writer.writeAttribute(STYLE_ATTR, style);
		String styleClass = scan.getStyleClass();
		if (styleClass != null) writer.writeAttribute(CLASS_ATTR, styleClass);
		writer.writeAttribute(TABINDEX_ATTR, scan.getTabindex());
		writer.startElement(SPAN_ELEM, scan);
		writer.writeText(scan.getButtonLabel());
		writer.endElement(SPAN_ELEM);

		// themeroller support
		writer.startElement("span", scan);
		writer.startElement("script", scan);
		writer.writeAttribute("type", "text/javascript");
        StringBuilder uiScript = new StringBuilder("new ice.mobi.button('");
        uiScript.append( clientId ).append( "_button');");
        //callback for ICE-10126
        uiScript.append("window['callback" + clientId + "'] = function(arg) {");
        String buttonId=clientId+"_button";
        String firstLine = "var buttonElem = document.getElementById('"+buttonId+"');";
        uiScript.append(firstLine);
        String secondLine=" if (buttonElem) { " +
                "console.log('have buttonElem');" +
                "var existingTextElem = buttonElem.firstChild; " +
                "if (existingTextElem){" +
                "     existingTextElem.innerHTML='"+capturedLabel+"';" +
                "} " +
             "}};"  ;
        uiScript.append(secondLine);
        writer.writeText(uiScript.toString());
//     System.out.println(" uiScript="+uiScript.toString());
		writer.endElement("script");
		writer.endElement("span");

		writer.endElement(BUTTON_ELEM);

		if (fallbackFacet != null) {
			writer.startElement(SPAN_ELEM, scan);
			writer.writeAttribute(ID_ATTR, clientId + "_fallback");
			writer.writeAttribute(STYLE_ATTR, "display:none;");
			if (fallbackFacet.isRendered()) fallbackFacet.encodeAll(facesContext);
			writer.endElement(SPAN_ELEM);
		}
		writer.startElement("script", scan);
		writer.writeAttribute("type", "text/javascript");
		writer.writeText("if (!bridgeit.isSupportedPlatform('scan') && document.getElementById('"+clientId+"_fallback')) {");
		writer.writeText("document.getElementById('"+clientId+"_button').style.display='none';");
		writer.writeText("document.getElementById('"+clientId+"_fallback').style.display='inline';");
		writer.writeText("}");
		writer.endElement("script");

		writer.endElement(SPAN_ELEM);
        scan.setButtonLabel(oldLabel);
    }


}
