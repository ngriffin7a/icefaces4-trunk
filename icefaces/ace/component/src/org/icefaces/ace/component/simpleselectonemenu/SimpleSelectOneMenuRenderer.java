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

package org.icefaces.ace.component.simpleselectonemenu;

import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.component.PassthroughAttributes;
import org.icefaces.impl.util.DOMUtils;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@MandatoryResourceComponent(tagName="simpleSelectOneMenu", value="org.icefaces.ace.component.simpleselectonemenu.SimpleSelectOneMenu")
public class SimpleSelectOneMenuRenderer extends InputRenderer {
    private static final String[] PASSTHROUGH_ATTRIBUTES = ((PassthroughAttributes) SimpleSelectOneMenu.class.getAnnotation(PassthroughAttributes.class)).value();

    public boolean getRendersChildren() {
        return true;
    }

	public void decode(FacesContext facesContext, UIComponent uiComponent) {
		SimpleSelectOneMenu simpleSelectOneMenu = (SimpleSelectOneMenu) uiComponent;
		simpleSelectOneMenu.setItemList(null);
        Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = simpleSelectOneMenu.getClientId(facesContext);
        String value = (String) requestMap.get(clientId + "_input");
		
		String emptyStringsAsNull = facesContext.getExternalContext()
			.getInitParameter("javax.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL");
		if (emptyStringsAsNull != null && "true".equalsIgnoreCase(emptyStringsAsNull) && "".equals(value)) {
			// if we call setSubmittedValue(null), instead, conversion and validation are skipped anyway
			simpleSelectOneMenu.setValue(null);
			simpleSelectOneMenu.queueEvent(new ValueChangeEvent (uiComponent, simpleSelectOneMenu.getValue(), null));
		} else {
			simpleSelectOneMenu.setSubmittedValue(value);
		}
		
		decodeBehaviors(facesContext, simpleSelectOneMenu);
	}
	
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        SimpleSelectOneMenu simpleSelectOneMenu = (SimpleSelectOneMenu) uiComponent;
        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);
        Map<String, Object> labelAttributes = getLabelAttributes(uiComponent);
		labelAttributes.put("fieldClientId", clientId + "_input");
        String inFieldLabelStyleClass = "";

		String inputClientId = clientId + "_input";

		// root
        writer.startElement("span", uiComponent);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("class", "ui-simpleselectonemenu " + simpleSelectOneMenu.getStyleClass(), null);
		renderResetSettings(facesContext, uiComponent);

		writeLabelAndIndicatorBefore(labelAttributes);
		
		// select field
		writer.startElement("select", null);
		writer.writeAttribute("id", inputClientId, null);
		writer.writeAttribute("name", inputClientId, null);
		String stateClass = simpleSelectOneMenu.isDisabled() ? "ui-state-disabled" : "ui-state-default";
		String styleClass = stateClass + getStateStyleClasses(simpleSelectOneMenu) + inFieldLabelStyleClass;
		writer.writeAttribute("class", "ui-widget ui-inputfield " + styleClass, null);
		String style = simpleSelectOneMenu.getStyle();
		writer.writeAttribute("style", style, null);

        ComponentUtils.renderPassThroughAttributes(writer, simpleSelectOneMenu, PASSTHROUGH_ATTRIBUTES);

        Map<String, Object> ariaAttributes = null;
		if (ariaEnabled) {
			writer.writeAttribute("role", "select", null);
            ariaAttributes = new HashMap<String, Object>();
			ariaAttributes.put("required", simpleSelectOneMenu.isRequired());
			ariaAttributes.put("disabled", simpleSelectOneMenu.isDisabled());
			ariaAttributes.put("invalid", !simpleSelectOneMenu.isValid());
            writeAriaAttributes(ariaAttributes, labelAttributes);
        }

		String accesskey = simpleSelectOneMenu.getAccesskey();
		if (accesskey != null) writer.writeAttribute("accesskey", accesskey, null);
		String dir = simpleSelectOneMenu.getDir();
		if (dir != null) writer.writeAttribute("dir", dir, null);
		boolean disabled = simpleSelectOneMenu.isDisabled();
		if (disabled) writer.writeAttribute("disabled", "disabled", null);
		String lang = simpleSelectOneMenu.getLang();
		if (lang != null) writer.writeAttribute("lang", lang, null);
		String tabindex = simpleSelectOneMenu.getTabindex();
		if (tabindex != null) writer.writeAttribute("tabindex", tabindex, null);
		String title = simpleSelectOneMenu.getTitle();
		if (title != null) writer.writeAttribute("title", title, null);
		
		StringBuffer hashBuffer = new StringBuffer();
		hashBuffer.append(styleClass).append(style).append(accesskey).append(dir).append(disabled)
			.append(lang).append(tabindex).append(title).append(simpleSelectOneMenu.isValid())
			.append(simpleSelectOneMenu.isRequired());
		if (ariaEnabled && ariaAttributes != null) {
			hashBuffer.append(labelAttributes.get("label"))
			.append(labelAttributes.get("labelPosition")).append(ariaAttributes.get("autocomplete"))
			.append(ariaAttributes.get("multiline")).append(ariaAttributes.get("readonly"));
		}

		populateList(facesContext, simpleSelectOneMenu, hashBuffer);
		
		writer.endElement("select");
		
		writeLabelAndIndicatorAfter(labelAttributes);

		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
		if (!simpleSelectOneMenu.getClientBehaviors().isEmpty()) {
			// script
			JSONBuilder jb = JSONBuilder.create();
			jb.beginMap()
			.entry("p", ""); // dummy property
			encodeClientBehaviors(facesContext, simpleSelectOneMenu, jb);
			jb.endMap();
			
			writer.writeText("ice.ace.SimpleSelectOneMenu('" + clientId + "', " + jb.toString() + ");", null);
		}
		writer.writeText("ice.ace.setResetValue('"+clientId+"', "
			+ "ice.ace.jq(ice.ace.escapeClientId('"+clientId+"')).find('select').val());", null);
		// hashcode to detect changes in the <select> element and cause an update of this script element 
		writer.writeText("// " + hashBuffer.toString().hashCode(), null);
		writer.endElement("script");
		
		writer.endElement("span");
    }

    public void populateList(FacesContext facesContext, SimpleSelectOneMenu simpleSelectOneMenu,
		StringBuffer hashBuffer) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		Object submittedValue = simpleSelectOneMenu.getSubmittedValue();
        boolean readonly = simpleSelectOneMenu.isReadonly();
        Object value = submittedValue != null ? submittedValue : simpleSelectOneMenu.getValue();
		String convertedValue = value != null ? getConvertedValueForClient(facesContext, simpleSelectOneMenu, value) : null;
		String clientId = simpleSelectOneMenu.getClientId(facesContext);
		boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);
        simpleSelectOneMenu.populateItemList();
        Iterator matches = simpleSelectOneMenu.getItemListIterator();
		if (matches.hasNext()) {
			StringBuffer sb = new StringBuffer();
			SelectItem item = null;
			boolean selectedFound = false;
			String role = "";
			if (ariaEnabled) role = " role=\"option\"";
			while (matches.hasNext()) {
				item = (SelectItem) matches.next();
				String itemLabel = item.getLabel();
				Object itemValue = item.getValue();
				if (itemValue != null) {
					try {
						itemValue = getConvertedValueForClient(facesContext, simpleSelectOneMenu, item.getValue());
					} catch (Exception e) {
						itemValue = item.getValue().toString();
					}
				}
				String selected = "";
				if (!selectedFound && value != null && convertedValue.equals((String) itemValue)) {
					selected = " selected=\"selected\"";
					selectedFound = true;
				}
				itemLabel = itemLabel == null ? itemValue.toString() : itemLabel;
				itemLabel = item.isEscape() ? DOMUtils.escapeAnsi(itemLabel) : itemLabel;
                boolean isSelected = String.valueOf(convertedValue).equals(itemValue);
                if (item.isDisabled() || (!isSelected && readonly)) {
					sb.append("<option disabled=\"disabled\" value=\"" + (itemValue == null ? "" : itemValue) + "\"" + selected + role + ">").append(itemLabel).append("</option>");
					writer.startElement("option", null);
					writer.writeAttribute("disabled", "disabled", null);
					writer.writeAttribute("value", (itemValue == null ? "" : itemValue), null);
					if (isSelected) writer.writeAttribute("selected", "selected", null);
					if (ariaEnabled) writer.writeAttribute("role", "option", null);
					writer.writeText(itemLabel, null);
					writer.endElement("option");
				} else {
					sb.append("<option value=\"" + (itemValue == null ? "" : itemValue) + "\"" + selected + role + ">").append(itemLabel).append("</option>");
					writer.startElement("option", null);
					writer.writeAttribute("value", (itemValue == null ? "" : itemValue), null);
					if (isSelected) writer.writeAttribute("selected", "selected", null);
					if (ariaEnabled) writer.writeAttribute("role", "option", null);
					writer.writeText(itemLabel, null);
					writer.endElement("option");
				}
			}
			String result = sb.toString();
			hashBuffer.append(result);
		}
    }
		
	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {

	}
	
	public String getConvertedValueForClient(FacesContext context, UIComponent component, Object value) throws ConverterException {
		SimpleSelectOneMenu simpleSelectOneMenu = (SimpleSelectOneMenu) component;
		Converter converter = simpleSelectOneMenu.getConverter();
		
		if(converter != null) {
			return converter.getAsString(context, simpleSelectOneMenu, value);
		} else {
			ValueExpression ve = simpleSelectOneMenu.getValueExpression("value");

			if(ve != null) {
				Class<?> valueType = ve.getType(context.getELContext());
				Converter converterForType = context.getApplication().createConverter(valueType);

				if(converterForType != null) {
					if (converterForType instanceof javax.faces.convert.EnumConverter && "".equals(value)) return converterForType.getAsString(context, simpleSelectOneMenu, null);
					return converterForType.getAsString(context, simpleSelectOneMenu, value);
				}
			}
		}
		
		return (value != null ? value.toString() : "");
	}

	protected void renderResetSettings(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		String clientId = component.getClientId(context);

		JSONBuilder jb = JSONBuilder.create();
		jb.beginArray();
		jb.item("SimpleSelectOneMenu");
		jb.beginArray();
		jb.item(clientId);
		jb.endArray();
		jb.endArray();

		writer.writeAttribute("data-ice-reset", jb.toString(), null);
	}
}
