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

package org.icefaces.ace.component.flipswitch;

import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.PassThruAttributeWriter;
import org.icefaces.ace.util.ClientDescriptor;
import org.icefaces.ace.util.UserAgentInfo;
import org.icefaces.ace.renderkit.CoreRenderer;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * In addition to the rendering the renderer performs decode as well. This component
 * doesn't use a hidden field for it value instead takes advantage of param support of JSF2
 */

public class FlipSwitchRenderer extends CoreRenderer {

    private static final Logger logger = Logger.getLogger(FlipSwitchRenderer.class.getName());
    private static final String JS_NAME = "flipswitch.js";
    private static final String JS_MIN_NAME = "flipswitch-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.flipswitch";

    // The decode method, in the renderer, is responsible for taking the values
    // 
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        // The RequestParameterMap holds the values received from the browser
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = uiComponent.getClientId(facesContext);
        FlipSwitch flipswitch = (FlipSwitch) uiComponent;
        if (flipswitch.isDisabled()) {
            return;
        }
        //update with hidden field
        String submittedString = String.valueOf(requestParameterMap.get(clientId + "_hidden"));
        if (submittedString != null) {
            boolean submittedValue = isChecked(submittedString);
            flipswitch.setSubmittedValue(submittedValue);
            decodeBehaviors(facesContext, flipswitch);
        }
    }

    // The encodeEnd method, in the renderer, is responsible for rendering
    //  the html markup, as well as the javacript necessary for 
    //  the browser. Typically the encodeEnd(-)
    //  method and possibly the encodeChildren(-) method would be used too,
    //  but we've put all the rendering here, in this one method.
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();
        FlipSwitch flipswitch = (FlipSwitch) uiComponent;
        ClientBehaviorHolder cbh = (ClientBehaviorHolder)uiComponent;
        boolean hasBehaviors = !cbh.getClientBehaviors().isEmpty();

        writeJavascriptFile(facesContext, uiComponent, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
        writer.startElement(HTML.ANCHOR_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, HTML.NAME_ATTR);
        String styleClass = FlipSwitch.FLIPSWITCH_OFF_CLASS;
        String switchValue = String.valueOf(flipswitch.getValue());
        boolean isChecked = this.isChecked(switchValue);
        if (isChecked) {
            styleClass = FlipSwitch.FLIPSWITCH_ON_CLASS;
        }
        writer.writeAttribute("class", styleClass, "class");

        PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, flipswitch.getAttributesNames());
        PassThruAttributeWriter.renderBooleanAttributes(writer, uiComponent, flipswitch.getBooleanAttNames());
        String labelOn = flipswitch.getLabelOn();
        String labelOff = flipswitch.getLabelOff();
//        String event = flipswitch.getDefaultEventName();
        boolean disabled = flipswitch.isDisabled();
        boolean readonly = flipswitch.isReadonly();
        if (disabled) {
            writer.writeAttribute("disabled", "disabled", null);
        }
        StringBuilder builder = new StringBuilder(255);
        builder.append("mobi.flipswitch.init('").append(clientId).append("',{ event: event,elVal: this,");
        builder.append("singleSubmit: ").append(flipswitch.isSingleSubmit());

        if (hasBehaviors){
            String behaviors = this.encodeClientBehaviors(facesContext, cbh, "click").toString();
            behaviors = behaviors.replace("\"", "\'");
            builder.append(behaviors);
        }
        // Mobi-526 pass transformer hack flag
        if (isTransformerHack(facesContext)) {
            logger.finest("Transformer Prime hack active");
            builder.append(", transHack: 'true'");
        }
        builder.append("}); return false; ");

        String jsCall = builder.toString();
        if (!disabled | !readonly)writer.writeAttribute("onclick", jsCall, null);
        writer.writeAttribute("class", styleClass, "class");
        writer.startElement(HTML.SPAN_ELEM, uiComponent);

        boolean switchVal = (Boolean) flipswitch.getValue();
        writer.writeAttribute("class", "mobi-flipswitch-txt-on" + (switchVal ? " ui-state-active" : "" ), null);
        writer.write(labelOn);
        writer.endElement(HTML.SPAN_ELEM);
        writeHiddenField(uiComponent, clientId, writer, switchVal);

        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute("class", "mobi-flipswitch-txt-off", null);
        writer.write(labelOff);
        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.ANCHOR_ELEM);

    }

    private void writeHiddenField(UIComponent uiComponent, String clientId,
                                  ResponseWriter writer, boolean switchValue) throws IOException {
        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("name", clientId + "_hidden", null);
        writer.writeAttribute("id", clientId + "_hidden", null);
        writer.writeAttribute("value", switchValue, null);
        writer.endElement("input");
    }

    private boolean isChecked(String hiddenValue) {
        return hiddenValue.equalsIgnoreCase("on") ||
                hiddenValue.equalsIgnoreCase("yes") ||
                hiddenValue.equalsIgnoreCase("true");
    }

    //forced converter support. It's either a boolean or string.
    @Override
    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent,
                                    Object submittedValue) throws ConverterException {
        if (submittedValue instanceof Boolean) {
            return submittedValue;
        } else {
            return Boolean.valueOf(submittedValue.toString());
        }
    }



    /**
     * will render it's own children
     */
    public boolean getRendersChildren() {
        return true;
    }

    /**
     * MOBI-526 Check if the device is An Asus Transformer Prime for a
     * bad hack involving suppression of double clicks on flipswitch.
     * @param pageContext
     * @return
     */
    private boolean isTransformerHack(FacesContext pageContext) {
        Object request = pageContext.getExternalContext().getRequest();
        if (request instanceof HttpServletRequest) {
            HttpServletRequest hsr = (HttpServletRequest) request;
            ClientDescriptor client = ClientDescriptor.getInstance(hsr);

            String ua = client.getUserAgent();
            if (ua != null) {
                ua = ua.toLowerCase();
                return ua.contains( UserAgentInfo.TABLET_TRANSORMER_PRIME );
            }
        }
        return false;
    }
}
