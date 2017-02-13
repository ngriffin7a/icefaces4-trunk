/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2014 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: (ICE-6978) Used JSONBuilder to add the functionality of escaping JS output.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 */
package org.icefaces.ace.component.radiobuttons;


import org.icefaces.ace.component.radiobutton.RadioButtonRenderer;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.ace.util.SelectItemsIterator;
import org.icefaces.ace.util.Utils;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;
import org.icefaces.util.JavaScriptRunner;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@MandatoryResourceComponent(tagName = "radioButtons", value = "org.icefaces.ace.component.radiobuttons.RadioButtons")
public class RadioButtonsRenderer extends RadioButtonRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {

        RadioButtons radioButtons = (RadioButtons) component;
		String clientId = radioButtons.getClientId(context);

		Map<String, String> requestParameterValues =
			  context.getExternalContext().getRequestParameterMap();
		if (requestParameterValues.containsKey(clientId)) {
			String newValue = requestParameterValues.get(clientId);
			if ("".equals(newValue)) {
				if (radioButtons.isRequired()) radioButtons.setSubmittedValue("");
				else radioButtons.setSubmittedValue(null);

				radioButtons.setValue(null);
			} else
				radioButtons.setSubmittedValue(newValue);
		} else {
			radioButtons.setSubmittedValue(null);
		}
		decodeBehaviors(context, radioButtons);
    }

	public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue) throws ConverterException {
		return submittedValue == null ? "" : submittedValue.toString();
	}

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        RadioButtons radioButtons = (RadioButtons) component;
		String clientId = radioButtons.getClientId(context);
        String style = (style = radioButtons.getStyle()) == null ? "" : style.trim();
        String styleClass = (styleClass = radioButtons.getStyleClass()) == null ? "" : styleClass.trim();
        styleClass += (styleClass.length() > 0 ? " " : "") + "ice-ace-radiobuttons ui-widget ui-widget-content ui-corner-all " +getStateStyleClasses(radioButtons);

        writer.startElement("div", component);
        writer.writeAttribute("id", clientId, "id");
        if (style.length() > 0) {
            writer.writeAttribute("style", style, "style");
        }
        writer.writeAttribute("class", styleClass, "styleClass");

        UIComponent headerFacet = component.getFacet("header");
        String headerText = (headerText = radioButtons.getHeader()) == null ? "" : headerText.trim();

        if (headerFacet != null || headerText.length() > 0) {
            writer.startElement("div", null);
            writer.writeAttribute("class", "ui-widget-header ui-corner-top", null);
            if (headerFacet != null) {
                renderChild(context, headerFacet);
            } else if (headerText.length() > 0) {
                writer.write(headerText);
            }
            writer.endElement("div");
        }

		boolean required = radioButtons.isRequired();
		String indicatorPosition = radioButtons.getIndicatorPosition();
		String indicator = required ? radioButtons.getRequiredIndicator() : radioButtons.getOptionalIndicator();
		if ("left".equalsIgnoreCase(indicatorPosition)
			|| "top".equalsIgnoreCase(indicatorPosition)) {
				if (indicator != null) {
					writer.startElement("span", null);
					writer.writeAttribute("class", "ice-indicator", null);
					writer.write(indicator);
					writer.endElement("span");
				}
		}
		if ("top".equalsIgnoreCase(indicatorPosition)) {
			writer.startElement("br", null);
			writer.endElement("br");
		}

		// render buttons
		Object currentSelection = radioButtons.getValue();
        Converter converter = radioButtons.getConverter();
		SelectItemsIterator selectItemsIterator = new SelectItemsIterator(context, radioButtons);
		int i = 0;
		if (!radioButtons.isValid()) {
			while (selectItemsIterator.hasNext()) {
				encodeButton(context, radioButtons, i++, selectItemsIterator.next(), converter, radioButtons.getSubmittedValue());
			}
		} else {
			while (selectItemsIterator.hasNext()) {
				encodeButton(context, radioButtons, i++, selectItemsIterator.next(), converter, currentSelection);
			}
		}

		if ("bottom".equalsIgnoreCase(indicatorPosition)) {
			writer.startElement("br", null);
			writer.endElement("br");
		}
		if ("right".equalsIgnoreCase(indicatorPosition)
			|| "bottom".equalsIgnoreCase(indicatorPosition)) {
				if (indicator != null) {
					writer.startElement("span", null);
					writer.writeAttribute("class", "ice-indicator", null);
					writer.write(indicator);
					writer.endElement("span");
				}
		}

		writer.startElement("input", radioButtons);
		writer.writeAttribute("id", clientId + "_empty", null);
		writer.writeAttribute("type", "hidden", null);
		writer.writeAttribute("autocomplete", "off", null);
		Object value = radioButtons.getValue();
		if (value == null || "".equals(value)) {
			writer.writeAttribute("name", clientId, null);
		}
		writer.writeAttribute("value", "", null);
		writer.writeAttribute("data-ice-clear-ignore", "true", null);
		writer.endElement("input");

		writer.endElement("div");
    }

	private void encodeButton(FacesContext facesContext, RadioButtons radioButtons, int index, SelectItem item, Converter converter, Object currentSelection) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        String clientId = radioButtons.getClientId(facesContext) + ":" + index;
		String labelPosition = radioButtons.getLabelPosition();

		String label = item.getLabel();
		Object value = getConvertedValueForClient(facesContext, radioButtons, item.getValue());
		boolean selected = isSelected(item.getValue(), currentSelection);

        String firstWrapperClass = "ice-ace-radiobutton-main";
        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);

        // Root Container
        writer.startElement(HTML.DIV_ELEM, radioButtons);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
		renderResetSettings(facesContext, clientId, radioButtons);

        writer.writeAttribute(HTML.CLASS_ATTR, "ice-ace-radiobutton", null);
		String script = getScript(facesContext, writer, radioButtons, clientId, item.isDisabled());
		writer.writeAttribute("data-init", "if (!document.getElementById('" + clientId + "').widget) " + script, null);
        encodeScript(writer, EventType.HOVER);

		if (label != null) {
			if ("left".equalsIgnoreCase(labelPosition)
					|| "top".equalsIgnoreCase(labelPosition)) {
				writer.startElement("label", null);
				writer.writeAttribute("id", "label_" + clientId, null);
				writer.writeAttribute("for", clientId, null);
				writer.write(label);
				writer.endElement("label");
			}
			if ("top".equalsIgnoreCase(labelPosition)) {
				writer.startElement("br", null);
				writer.endElement("br");
			}
		}

        encodeButtonWrappers(writer, firstWrapperClass);

        if (ariaEnabled) {
			encodeAriaEnabled(writer, item.isDisabled());
		}

        // Button Element
		encodeButtonElementStart(writer,clientId);
		String selectedClass = "";
		selectedClass = (selected ? " ice-ace-radiobutton-selected" : "");
		writer.writeAttribute(HTML.CLASS_ATTR, "ui-corner-all ui-widget-content" + selectedClass, null);

		if (ariaEnabled) writer.writeAttribute(HTML.TABINDEX_ATTR, "0", null);
        encodeButtonStyle(writer, item.isDisabled());
		encodeScript(writer, EventType.FOCUS);

        if (label != null && "inField".equalsIgnoreCase(radioButtons.getLabelPosition())) {
            writer.startElement(HTML.SPAN_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, "ui-label", null);
            writer.write(label);
            writer.endElement(HTML.SPAN_ELEM);
        } else {
            writer.startElement(HTML.SPAN_ELEM, null);
            encodeIconStyle(writer, (Boolean)selected);
            writer.endElement(HTML.SPAN_ELEM);
        }

        writer.endElement(HTML.BUTTON_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.SPAN_ELEM);

		if (label != null) {
			if ("bottom".equalsIgnoreCase(labelPosition)) {
				writer.startElement("br", null);
				writer.endElement("br");
			}
			if ("right".equalsIgnoreCase(labelPosition)
					|| "bottom".equalsIgnoreCase(labelPosition)
					|| labelPosition == null || "".equals(labelPosition)) {
				writer.startElement("label", null);
				writer.writeAttribute("id", "label_" + clientId, null);
				writer.writeAttribute("for", clientId, null);
				writer.write(label);
				writer.endElement("label");
			}
		}

        writer.startElement("input", radioButtons);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("autocomplete", "off", null);
        if (selected) writer.writeAttribute("name", radioButtons.getClientId(facesContext), null);
        writer.writeAttribute("value", value, null);
		writer.writeAttribute("data-value", value, null);
        writer.writeAttribute("data-ice-clear-ignore", "true", null);
        writer.endElement("input");

        writer.endElement(HTML.DIV_ELEM);
		JavaScriptRunner.runScript(facesContext, "ice.ace.radiobutton.register('"+clientId+"','"+radioButtons.getClientId(facesContext)+"');");
		JavaScriptRunner.runScript(facesContext, "ice.ace.registerLazyComponent('" + clientId + "');");
	}

    private String getScript(FacesContext facesContext, ResponseWriter writer,
                              RadioButtons radioButtons, String clientId, boolean disabled) throws IOException {
        String groupId = radioButtons.getClientId(facesContext);

        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);
        JSONBuilder jb = JSONBuilder.create();
        List<UIParameter> uiParamChildren = Utils.captureParameters(radioButtons);

        jb.beginFunction("ice.ace.lazy")
          .item("radiobutton")
          .beginArray()
          .item(clientId)
          .beginMap()
          .entry("groupId", groupId)
          .entry("ariaEnabled", ariaEnabled)
          .entry("radioButtons", radioButtons.getClientId(facesContext));

		if (disabled) jb.entry("disabled", true);

        if (uiParamChildren != null) {
            jb.beginMap("uiParams");
            for (UIParameter p : uiParamChildren)
                jb.entry(p.getName(), (String)p.getValue());
            jb.endMap();
        }

        encodeClientBehaviors(facesContext, radioButtons, jb);

        jb.endMap().endArray().endFunction();

		return jb.toString();
	}

    protected boolean isSelected(Object itemValue, Object currentSelection) {

		if (currentSelection == null) return false;
		if (itemValue == null) return false;
		return currentSelection.equals(itemValue);
    }

	public String getConvertedValueForClient(FacesContext context, UIComponent component, Object value) throws ConverterException {
		RadioButtons radioButtons = (RadioButtons) component;
		Converter converter = radioButtons.getConverter();

		if(converter != null) {
			return converter.getAsString(context, radioButtons, value);
		} else {
			ValueExpression ve = radioButtons.getValueExpression("value");

			if(ve != null) {
				Class<?> valueType = ve.getType(context.getELContext());
				Converter converterForType = context.getApplication().createConverter(valueType);

				if(converterForType != null) {
					if (converterForType instanceof javax.faces.convert.EnumConverter && "".equals(value)) return converterForType.getAsString(context, radioButtons, null);
					return converterForType.getAsString(context, radioButtons, value);
				}
			}
		}

		return (value != null ? value.toString() : "");
	}

	public void renderResetSettings(FacesContext context, String clientId, RadioButtons radioButtons) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		JSONBuilder jb = JSONBuilder.create();
		jb.beginArray();
		jb.item("radiobutton");
		jb.beginArray();
		jb.item(clientId);
		jb.item(EnvUtils.isAriaEnabled(context));
		jb.item(radioButtons.getClientId(context));
		jb.endArray();
		jb.endArray();

		writer.writeAttribute("data-ice-reset", jb.toString(), null);
	}
}
