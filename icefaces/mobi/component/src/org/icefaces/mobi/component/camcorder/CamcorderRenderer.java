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

package org.icefaces.mobi.component.camcorder;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.icefaces.mobi.renderkit.BaseInputResourceRenderer;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.util.MobiJSFUtils;
import org.icefaces.mobi.util.CSSUtils;
import org.icefaces.util.ClientDescriptor;
import org.icefaces.util.EnvUtils;

import static org.icefaces.ace.util.HTML.*;

public class CamcorderRenderer extends BaseInputResourceRenderer {
    private static final Logger logger = Logger.getLogger(CamcorderRenderer.class.getName());

    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Camcorder camcorder = (Camcorder) uiComponent;
        String clientId = camcorder.getClientId();
        if (!camcorder.isDisabled()) {
           try {
              Map<String, Object> map = new HashMap<String, Object>();
              boolean isValid = extractVideo(facesContext, map, clientId);
              if (isValid){
                 if (map !=null){
                    camcorder.setValue(map);
             //     trigger valueChange and add map as newEvent value old event is NA
                    uiComponent.queueEvent(new ValueChangeEvent(uiComponent,
    		    		    null, map));
                }
              }

           } catch (Exception e) {
              e.printStackTrace();
           }
        }
    }

    public boolean extractVideo(FacesContext facesContext, Map map, String clientId) throws IOException {
        return MobiJSFUtils.decodeComponentFile(facesContext, clientId, map);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        Camcorder camcorder = (Camcorder) uiComponent;
		ResponseWriterWrapper writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
		String clientId = camcorder.getClientId();
		writer.startElement(SPAN_ELEM, camcorder);
		writer.writeAttribute(ID_ATTR, clientId);
		writer.writeAttribute(CLASS_ATTR, "mobi-camcorder");
        String oldLabel = camcorder.getButtonLabel();
        if (MobiJSFUtils.uploadInProgress(camcorder))  {
            camcorder.setButtonLabel(camcorder.getCaptureMessageLabel()) ;
        } 
        //StringBuilder baseClass = new StringBuilder(CSSUtils.STYLECLASS_BUTTON);
        //ClientDescriptor cd = camcorder.getClient();
		// button element
		writer.startElement(BUTTON_ELEM, camcorder);
		writer.writeAttribute(ID_ATTR, clientId + "_button");
		writer.writeAttribute(NAME_ATTR, clientId + "_button");
		writer.writeAttribute(TYPE_ATTR, "button");
		if (camcorder.isDisabled()) writer.writeAttribute(DISABLED_ATTR, "disabled");
		String style = camcorder.getStyle();
		if (style != null) writer.writeAttribute(STYLE_ATTR, style);
		String styleClass = camcorder.getStyleClass();
		if (styleClass != null) writer.writeAttribute(CLASS_ATTR, styleClass);
		writer.writeAttribute(TABINDEX_ATTR, camcorder.getTabindex());
		//default value of unset in params is Integer.MIN_VALUE
		String script = "bridgeit.camcorder('" + clientId + "', 'callback"+clientId+"', {postURL:'" + camcorder.getPostURL() + "', ";
        script += "cookies:{'JSESSIONID':'" + MobiJSFUtils.getSessionIdCookie(facesContext) +  "'}";
		script += "});";
		writer.writeAttribute(ONCLICK_ATTR, script);
		writer.startElement(SPAN_ELEM, camcorder);
		writer.writeText(camcorder.getButtonLabel());
		writer.endElement(SPAN_ELEM);

		// themeroller and thumbnails support
		writer.startElement("span", camcorder);
		writer.startElement("script", camcorder);
		writer.writeAttribute("type", "text/javascript");
		writer.writeText("new ice.mobi.button('"+clientId+"_button');");
		writer.writeText("if (!window['thumbnails"+clientId+"']) window['thumbnails"+clientId+"'] = {};");
		writer.writeText("window['callback"+clientId+"'] = function(arg) {for (t in window['thumbnails"+clientId+"']) { " +
                "setTimeout(function() {var e = document.getElementById(t); " +
                "if (e) e.src = arg.preview; }, 350);" +
                "  setTimeout (function() { var hidElem =  document.getElementById(thumbnails" + clientId + "[t]); \n " +
                " if (hidElem) {hidElem.value = arg.preview;}\n} , 350); }}; ");
		writer.endElement("script");
		writer.endElement("span");
		writer.endElement(BUTTON_ELEM);

		writer.endElement(SPAN_ELEM);
        camcorder.setButtonLabel(oldLabel);
    }
}
