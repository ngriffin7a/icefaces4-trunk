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

package org.icefaces.mobi.component.sms;

import org.icefaces.impl.util.CoreUtils;
import org.icefaces.ace.util.HTML;
import org.icefaces.mobi.renderkit.CoreRenderer;
import org.icefaces.mobi.util.MobiJSFUtils;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.util.ClientDescriptor;
import org.icefaces.util.EnvUtils;

import javax.el.ValueExpression;
import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class SmsRenderer extends CoreRenderer {
    private static final Logger logger = Logger.getLogger(SmsRenderer.class.getName());

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        Sms sms = (Sms) uiComponent;
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = sms.getClientId();
		UIComponent fallbackFacet = sms.getFacet("fallback");

		writer.startElement(HTML.SPAN_ELEM, sms);
		writer.writeAttribute(HTML.ID_ATTR, clientId, null);
		writer.writeAttribute(HTML.CLASS_ATTR, "mobi-sms", null);

		writer.startElement(HTML.BUTTON_ELEM, sms);
		writer.writeAttribute(HTML.ID_ATTR, clientId + "_button", null);
		writer.writeAttribute(HTML.NAME_ATTR, clientId + "_button", null);
		writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
		String style = sms.getStyle();
		if (style != null) writer.writeAttribute(HTML.STYLE_ATTR, style, null);
		String styleClass = sms.getStyleClass();
		if (styleClass != null) writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
		writer.writeAttribute(HTML.TABINDEX_ATTR, sms.getTabindex(), null);
        try {
            if (sms.isDisabled()) writer.writeAttribute(HTML.DISABLED_ATTR, "disabled", null);
			String number = determineNumber(sms);
			String message = determineMessage(sms);
			String launchFailed = fallbackFacet != null ? "ice.mobi.fallback.setupLaunchFailed('"+clientId+"_button','"+clientId+"_fallback');" : "";
		    String script = launchFailed + "bridgeit.sms(" + number + ", " + message + ");";
		    writer.writeAttribute(HTML.ONCLICK_ATTR, script, null);
        } catch (Exception e){
            writer.writeAttribute(HTML.DISABLED_ATTR, "disabled", null);
            logger.info("ERROR: mobi:sms requires non null number and message attributes. Consider using numberInputId and messageInputId instead.");
            FacesMessage fm = new FacesMessage(" ERROR: mobi:sms requires non null values for number and message attributes. Consider using numberInputId and messageInputId instead.") ;
            facesContext.addMessage(clientId, fm);
        }
		writer.startElement(HTML.SPAN_ELEM, sms);
		writer.write(sms.getButtonLabel());
		writer.endElement(HTML.SPAN_ELEM);

		// themeroller support
		writer.startElement("span", sms);
		writer.startElement("script", sms);
		writer.writeAttribute("type", "text/javascript", null);
		writer.write("new ice.mobi.button('"+clientId+"_button');");
		writer.endElement("script");
		writer.endElement("span");

		writer.endElement(HTML.BUTTON_ELEM);

		if (fallbackFacet != null) {
			writer.startElement(HTML.SPAN_ELEM, sms);
			writer.writeAttribute(HTML.ID_ATTR, clientId + "_fallback", null);
			writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);
			if (fallbackFacet.isRendered()) fallbackFacet.encodeAll(facesContext);
			writer.endElement(HTML.SPAN_ELEM);
		}
		writer.startElement("script", sms);
		writer.writeAttribute("type", "text/javascript", null);
		writer.writeText("if (!bridgeit.isSupportedPlatform('sms') && document.getElementById('"+clientId+"_fallback')) {", null);
		writer.writeText("document.getElementById('"+clientId+"_button').style.display='none';", null);
		writer.writeText("document.getElementById('"+clientId+"_fallback').style.display='inline';", null);
		writer.writeText("}", null);
		writer.endElement("script");

		writer.endElement(HTML.SPAN_ELEM);
    }

	private String determineNumber(Sms sms) {
		String numberInputId = sms.getNumberInputId();
		String number = numberInputId != null ? "document.getElementById('" + determineId(numberInputId) + "').value"
			: "'" + escapeString(sms.getNumber()) + "'";
		return number;
	}

	private String determineMessage(Sms sms) {
		String messageInputId = sms.getMessageInputId();
		String message = messageInputId != null ? "document.getElementById('" + determineId(messageInputId) + "').value"
			: "'" + escapeString(sms.getMessage()) + "'";
		return message;
	}

	private String determineId(String id) {
		String result;
		UIComponent component = ComponentUtils.findComponent(FacesContext.getCurrentInstance().getViewRoot(), id);
		if (component != null) {
			String clientId = component.getClientId(FacesContext.getCurrentInstance());
			if (component instanceof org.icefaces.ace.component.autocompleteentry.AutoCompleteEntry) {
				result = clientId + "_input";
			} else if (component instanceof org.icefaces.ace.component.combobox.ComboBox) {
				result = clientId + "_input";
			} else if (component instanceof org.icefaces.ace.component.maskedentry.MaskedEntry) {
				result = clientId + "_field";
			} else if (component instanceof org.icefaces.ace.component.textareaentry.TextAreaEntry) {
				result = clientId + "_input";
			} else if (component instanceof org.icefaces.ace.component.textentry.TextEntry) {
				result = clientId + "_input";
			} else {
				result = clientId;
			}
		} else {
			result = id;
		}
		return result;
	}

    public static String escapeString(String value) {
        StringBuilder sb = new StringBuilder();
        char c;
        for (int idx = 0; idx < value.length(); idx++) {
            c = value.charAt(idx);
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
				case '\'':
                    sb.append("\\'");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
}
