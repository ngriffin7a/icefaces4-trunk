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

package org.icefaces.ace.component.radiobutton;


import org.icefaces.ace.component.buttongroup.ButtonGroup;
import org.icefaces.ace.component.radiobutton.RadioButton;
import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.ace.util.Utils;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@MandatoryResourceComponent(tagName="radioButton", value="org.icefaces.ace.component.radiobutton.RadioButton")
public class RadioButtonRenderer extends InputRenderer {
    private enum EventType {
        HOVER, FOCUS
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        RadioButton radioButton = (RadioButton) uiComponent;
        String clientId = uiComponent.getClientId();
        String hiddenValue = String.valueOf(requestParameterMap.get(clientId+"_hidden"));

        if (null==hiddenValue || hiddenValue.equals("null")){
            return;
        }else {
            boolean submittedValue = isChecked(hiddenValue);
            radioButton.setSubmittedValue(submittedValue);
        }

        decodeBehaviors(facesContext, radioButton);
    }


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        RadioButton radioButton = (RadioButton) uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
		Map<String, Object> labelAttributes = getLabelAttributes(uiComponent);
        String firstWrapperClass = "ice-ace-radiobutton-main";
        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);

        // Root Container
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        ComponentUtils.enableOnElementUpdateNotify(writer, clientId);

        encodeRootStyle(writer, radioButton);
		
		writeLabelAndIndicatorBefore(labelAttributes);

        // First Wrapper
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.CLASS_ATTR, firstWrapperClass, null);

        // Second Wrapper
        writer.startElement(HTML.SPAN_ELEM, uiComponent);

        if (ariaEnabled)
            encodeAriaAttributes(writer, radioButton);

        // Button Element
        writer.startElement(HTML.BUTTON_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_button", null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId+"_button", null);
		writer.writeAttribute(HTML.CLASS_ATTR, "ui-corner-all", null);

        encodeButtonTabIndex(writer, radioButton, ariaEnabled);
        encodeButtonStyle(writer, radioButton);

        renderPassThruAttributes(facesContext, radioButton, HTML.BUTTON_ATTRS, new String[]{"style"});

		writer.startElement(HTML.SPAN_ELEM, null);
		encodeIconStyle(writer, radioButton);
		writer.endElement(HTML.SPAN_ELEM);

        writer.endElement(HTML.BUTTON_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
		
		writeLabelAndIndicatorAfter(labelAttributes);
    }

    private void encodeAriaAttributes(ResponseWriter writer, RadioButton radioButton) throws IOException {
        writer.writeAttribute(HTML.ROLE_ATTR, "radio", null);
        writer.writeAttribute(HTML.ARIA_DESCRIBED_BY_ATTR, radioButton.getLabel(), null);
        writer.writeAttribute(HTML.ARIA_DISABLED_ATTR, radioButton.isDisabled(), null);
    }

    private void encodeButtonTabIndex(ResponseWriter writer, RadioButton radioButton, boolean ariaEnabled) throws IOException {
        Integer tabindex = radioButton.getTabindex();

        if (ariaEnabled && tabindex == null)
            tabindex = 0;

        if (tabindex != null)
            writer.writeAttribute(HTML.TABINDEX_ATTR, tabindex, null);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        RadioButton radioButton = (RadioButton) uiComponent;
        Object val = radioButton.getValue();

        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("autocomplete", "off", null);
        writer.writeAttribute("name",clientId+"_hidden", null);
        writer.writeAttribute("value",val, null);
        writer.endElement("input");
		
		encodeScript(facesContext, writer, radioButton, clientId);

        writer.endElement(HTML.DIV_ELEM);
    }

    private void encodeScript(FacesContext facesContext, ResponseWriter writer,
                              RadioButton radioButton, String clientId) throws IOException {
        UIComponent groupComp;
        String groupId = radioButton.getGroup();
		if (groupId != null) {
			groupId = groupId.trim();
			groupComp = radioButton.findComponent(groupId);
			groupId = groupComp instanceof ButtonGroup ? groupComp.getClientId(facesContext) : "";
		}
		if (groupId == null || "".equals(groupId)) {
			groupComp = findNearestButtonGroup(radioButton);
			if (groupComp != null) {
				groupId = ((ButtonGroup) groupComp).isMutuallyExclusive() ? groupComp.getClientId(facesContext) : "";
			} else {
				groupId = "";
			}
		}
        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);
        JSONBuilder jb = JSONBuilder.create();
        List<UIParameter> uiParamChildren = Utils.captureParameters(radioButton);

        jb.beginFunction("ice.ace.create")
          .item("radiobutton")
          .beginArray()
          .item(clientId)
          .beginMap()
          .entry("groupId", groupId)
          .entry("ariaEnabled", ariaEnabled);

        if (radioButton.isDisabled())
            jb.entry("disabled", true);

        if (uiParamChildren != null) {
            jb.beginMap("uiParams");
            for (UIParameter p : uiParamChildren)
                jb.entry(p.getName(), (String)p.getValue());
            jb.endMap();
        }

        encodeClientBehaviors(facesContext, radioButton, jb);

        jb.endMap().endArray().endFunction();

        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeText("ice.ace.jq(function(){" + jb.toString() + "});", null);
		writer.endElement("script");
    }
	
	private ButtonGroup findNearestButtonGroup(UIComponent component) {
		if (component == null) return null;
		UIComponent parent = component.getParent();
		if (parent == null) return null;
		if (parent instanceof ButtonGroup) return (ButtonGroup) parent;
		return findNearestButtonGroup(parent);
	}

    /**
     * support similar return values as jsf component
     * so can use strings true/false, on/off, yes/no to
     * support older browsers
     * @param hiddenValue
     * @return
     */
    private boolean isChecked(String hiddenValue) {
        return hiddenValue.equalsIgnoreCase("true") ||
               hiddenValue.equalsIgnoreCase("on") ||
               hiddenValue.equalsIgnoreCase("yes");
    }

    //forced converter support. It's either a boolean or string.
    @Override
    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent,
                                    Object submittedValue) throws ConverterException{
        if (submittedValue instanceof Boolean) {
            return submittedValue;
        }
        else {
            return Boolean.valueOf(submittedValue.toString());
        }
    }

    private void encodeButtonStyle(ResponseWriter writer, RadioButton radioButton) throws IOException {
        String buttonClasses = "";
        String disabledClass = "ui-state-disabled";
        Boolean val = (Boolean)radioButton.getValue();

        if (radioButton.isDisabled()) {
            buttonClasses += disabledClass + " ";
        }

        if (!buttonClasses.equals("")) {
            writer.writeAttribute(HTML.CLASS_ATTR, buttonClasses.trim(), null);
        }
    }

    private void encodeIconStyle(ResponseWriter writer, RadioButton radioButton) throws IOException {
        String iconClass = "ui-icon";
        String selectedStyle = "ui-icon-radio-on";
        String unselectedStyle = "ui-icon-radio-off";
        Boolean val = (Boolean)radioButton.getValue();

        if (val != null && val) {
            iconClass += " " + selectedStyle;
        } else {
            iconClass += " " + unselectedStyle;
        };

        writer.writeAttribute(HTML.CLASS_ATTR, iconClass, null);
    }

    private void encodeRootStyle(ResponseWriter writer, RadioButton radioButton) throws IOException {
        String styleClass = radioButton.getStyleClass();
        String styleClassVal = "ice-ace-radiobutton";
        String style = radioButton.getStyle();

        if (styleClass != null && styleClass.trim().length() > 0)
            styleClassVal += " " + styleClass;

        if (style != null && style.trim().length() > 0)
            writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);

        writer.writeAttribute(HTML.CLASS_ATTR, styleClassVal, null);
    }
}
