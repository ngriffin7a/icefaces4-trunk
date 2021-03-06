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

package org.icefaces.ace.component.checkboxbutton;



import java.io.IOException;
import java.lang.String;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;

import org.icefaces.ace.api.ButtonGroupMember;
import org.icefaces.component.PassthroughAttributes;
import org.icefaces.ace.util.*;

import org.icefaces.util.EnvUtils;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.util.JavaScriptRunner;

@MandatoryResourceComponent(tagName="checkboxButton", value="org.icefaces.ace.component.checkboxbutton.CheckboxButton")
public class CheckboxButtonRenderer extends InputRenderer {
    private final static String[] PASSTHROUGH_ATTRIBUTES = ((PassthroughAttributes) CheckboxButton.class.getAnnotation(PassthroughAttributes.class)).value();
    private static final Logger logger =
            Logger.getLogger(CheckboxButtonRenderer.class.toString());

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        CheckboxButton checkbox = (CheckboxButton) uiComponent;
        String clientId = uiComponent.getClientId();
        String hiddenValue = String.valueOf(requestParameterMap.get(clientId+"_hidden"));

        if (null==hiddenValue || hiddenValue.equals("null")){
            return;
        }else {
            boolean submittedValue = isChecked(hiddenValue);
            checkbox.setSubmittedValue(submittedValue);
        }

        decodeBehaviors(facesContext, checkbox);
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        CheckboxButton checkbox = (CheckboxButton) uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
		Map<String, Object> labelAttributes = getLabelAttributes(uiComponent);
		labelAttributes.put("fieldClientId", clientId + "_button");
        String firstWrapperClass = "yui-button yui-checkboxbutton-button ui-button ui-widget";
        String secondWrapperClass = "first-child";
        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);

        // Root Container
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
		renderResetSettings(facesContext, uiComponent);
        ComponentUtils.enableOnElementUpdateNotify(writer, clientId);
        String script = getScript(facesContext, writer, checkbox, clientId);
        writer.writeAttribute("data-init", "if (!document.getElementById('" + clientId + "').widget) " + script, null);
        encodeScript(writer, EventType.HOVER);
        encodeRootStyle(writer, checkbox);

		writeLabelAndIndicatorBefore(labelAttributes);

        // First Wrapper
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, firstWrapperClass, null);

        // Second Wrapper
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, secondWrapperClass, null);

        if (ariaEnabled)
            encodeAriaAttributes(writer, checkbox);

        // Button Element
        writer.startElement(HTML.BUTTON_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
		if (labelAttributes.get("label") != null
			&& !"inField".equals(labelAttributes.get("labelPosition"))) {
			writer.writeAttribute("aria-labelledby", "label_" + clientId, null);
		}
        String buttonId = clientId + "_button";
        writer.writeAttribute(HTML.ID_ATTR, buttonId, null);
        writer.writeAttribute(HTML.NAME_ATTR, buttonId, null);
		String selectedClass = "";
		Object value = checkbox.getValue();
		if (value != null) selectedClass = (((Boolean) value) ? "ice-checkboxbutton-checked" : "ice-checkboxbutton-unchecked");
		else selectedClass = "ice-checkboxbutton-unchecked";
		writer.writeAttribute(HTML.CLASS_ATTR, "ui-corner-all ui-widget-content " + selectedClass, null);

        String accesskey = checkbox.getAccesskey();
        if (accesskey != null) writer.writeAttribute("accesskey", accesskey, null);

        encodeButtonTabIndex(writer, checkbox, ariaEnabled);
        encodeButtonStyleClass(writer, isChecked(String.valueOf(checkbox.getValue())), checkbox.isDisabled());
        encodeButtonStyle(writer, checkbox);
        encodeScript(writer, EventType.FOCUS);

        renderPassThruAttributes(facesContext, checkbox, PASSTHROUGH_ATTRIBUTES);

        if (checkbox.getLabel() != null && "inField".equalsIgnoreCase(checkbox.getLabelPosition())) {
            writer.startElement(HTML.SPAN_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, "ui-label", null);
            writer.write(checkbox.getLabel());
            writer.endElement(HTML.SPAN_ELEM);
        } else {
            writer.startElement(HTML.SPAN_ELEM, null);
            encodeIconStyle(writer, isChecked(String.valueOf(checkbox.getValue())));
            writer.endElement(HTML.SPAN_ELEM);
        }

        writer.endElement(HTML.BUTTON_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.SPAN_ELEM);

		writeLabelAndIndicatorAfter(labelAttributes);
    }

    private void encodeAriaAttributes(ResponseWriter writer, CheckboxButton checkbox) throws IOException {
        writer.writeAttribute(HTML.ROLE_ATTR, "checkbox", null);
        writer.writeAttribute(HTML.ARIA_DESCRIBED_BY_ATTR, checkbox.getLabel(), null);
        writer.writeAttribute(HTML.ARIA_DISABLED_ATTR, checkbox.isDisabled(), null);
    }

    private void encodeButtonTabIndex(ResponseWriter writer, CheckboxButton checkbox, boolean ariaEnabled) throws IOException {
        Integer tabindex = checkbox.getTabindex();

        if (ariaEnabled && tabindex == null)
            tabindex = 0;

        if (tabindex != null)
            writer.writeAttribute(HTML.TABINDEX_ATTR, tabindex, null);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        CheckboxButton checkbox = (CheckboxButton) uiComponent;
        Object val = checkbox.getValue();

        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("autocomplete", "off", null);
        writer.writeAttribute("name",clientId+"_hidden", null);
        writer.writeAttribute("value",val, null);
        writer.writeAttribute("data-ice-clear-ignore", "true", null);
        writer.endElement("input");

        JavaScriptRunner.runScript(facesContext, "ice.ace.checkboxbutton.register('"+clientId+"','"+getGroupId(facesContext, checkbox)+"');");

        writer.endElement(HTML.DIV_ELEM);

        JavaScriptRunner.runScript(facesContext, "ice.ace.registerLazyComponent('" + clientId + "');");
    }

    private String getScript(FacesContext facesContext, ResponseWriter writer,
                              CheckboxButton checkbox, String clientId) throws IOException {
        String groupId = getGroupId(facesContext, checkbox);

        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);
        JSONBuilder jb = JSONBuilder.create();
        List<UIParameter> uiParamChildren = Utils.captureParameters(checkbox);

        jb.beginFunction("ice.ace.lazy")
          .item("checkboxbutton")
          .beginArray()
          .item(clientId)
          .beginMap()
          .entry("groupId", groupId)
          .entry("ariaEnabled", ariaEnabled);

        if (checkbox.isDisabled())
            jb.entry("disabled", true);

        if (uiParamChildren != null) {
            jb.beginMap("uiParams");
            for (UIParameter p : uiParamChildren)
                jb.entry(p.getName(), (String)p.getValue());
            jb.endMap();
        }

        encodeClientBehaviors(facesContext, checkbox, jb);

        jb.endMap().endArray().endFunction();

		return jb.toString();
	}

	private String getGroupId(FacesContext facesContext, CheckboxButton checkbox) {
        String groupId = checkbox.getGroup();
        List<String> groupLookInCtx = ComponentUtils.findInFacesContext(checkbox, facesContext);
        if (!groupLookInCtx.isEmpty()){  //at least one buttonGroup is in the view
            if (groupId !=null){
                groupId = groupId.trim();
                if (groupLookInCtx.contains(groupId)) {
                    for(String sid: groupLookInCtx){
                        if (sid.toLowerCase().contains(groupId.toLowerCase())){
                            groupId=sid;
                        }
                    }
                }  else {
                    //does it end in the groupId --so incomplete?
                    groupId= ComponentUtils.findInHeirarchy((ButtonGroupMember)checkbox, facesContext);
                }
            } else { //have at least one buttonGroup, but groupId is not set
                groupId= ComponentUtils.findInHeirarchy((ButtonGroupMember)checkbox, facesContext);
                if (groupId.length()< 1){
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("groupId of:-"+groupId+" not found in view.");
                    }
                }
            }
        }else {  //no buttonGroups in the view buttons are just non-managed buttons
            groupId="";
        }

		return groupId;
	}

    protected void encodeScript(ResponseWriter writer,
                                EventType type) throws IOException {

        String eventType = "";
        if (EventType.HOVER.equals(type))
            eventType = HTML.ONMOUSEOVER_ATTR;
        else if (EventType.FOCUS.equals(type))
            eventType = HTML.ONFOCUS_ATTR;

        writer.writeAttribute(eventType, "ice.ace.evalInit(this);", null);
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

    protected void encodeButtonStyleClass(ResponseWriter writer, boolean value, boolean disabled) throws IOException{
        String buttonClasses = "";
        String disabledClass = "ui-state-disabled";
        if (disabled){
            buttonClasses += disabledClass + " ";
        }
        if (!buttonClasses.equals("")) {
               writer.writeAttribute(HTML.CLASS_ATTR, buttonClasses.trim(), null);
        }
    }

    private void encodeButtonStyle(ResponseWriter writer, CheckboxButton checkbox) throws IOException {
		String style = checkbox.getStyle();
		if (style != null && style.trim().length() > 0)
			writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
    }

    protected void encodeIconStyle(ResponseWriter writer, boolean value) throws IOException {
        String iconClass = "fa";
        String selectedStyle = "fa-check-square-o";
        String unselectedStyle = "fa-square-o";
		String largeStyle = "fa-lg";
       // Boolean val = (Boolean)checkbox.getValue();

        if ((Boolean)value != null && value) {
            iconClass += " " + selectedStyle + " " + largeStyle;
        } else {
            iconClass += " " + unselectedStyle + " " + largeStyle;
        };

        writer.writeAttribute(HTML.CLASS_ATTR, iconClass, null);
    }

    private void encodeRootStyle(ResponseWriter writer, CheckboxButton checkbox) throws IOException {
        String styleClass = checkbox.getStyleClass();
        String styleClassVal = "ice-checkboxbutton";

        if (styleClass != null && styleClass.trim().length() > 0)
            styleClassVal += " " + styleClass;

        writer.writeAttribute(HTML.CLASS_ATTR, styleClassVal, null);
    }

	protected void renderResetSettings(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		String clientId = component.getClientId(context);

		JSONBuilder jb = JSONBuilder.create();
		jb.beginArray();
		jb.item("checkboxbutton");
		jb.beginArray();
		jb.item(clientId);
		jb.item(EnvUtils.isAriaEnabled(context));
		jb.endArray();
		jb.endArray();

		writer.writeAttribute("data-ice-reset", jb.toString(), null);
	}

    protected enum EventType {
        HOVER, FOCUS
    }
}
