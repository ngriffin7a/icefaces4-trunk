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


import org.icefaces.render.MandatoryResourceComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.IOException;
import java.lang.String;
import java.util.*;
import org.icefaces.ace.util.*;
import javax.faces.component.UIParameter;
import javax.faces.convert.ConverterException;
import org.icefaces.util.EnvUtils;
import org.icefaces.ace.renderkit.InputRenderer;
import javax.faces.model.SelectItem;
import javax.faces.convert.Converter;
import javax.el.ExpressionFactory;
import javax.el.ELException;
import java.lang.reflect.Array;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;

@MandatoryResourceComponent(tagName = "radioButtons", value = "org.icefaces.ace.component.radiobuttons.RadioButtons")
public class RadioButtonsRenderer extends InputRenderer {
    private enum EventType {
        HOVER, FOCUS
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {

//		componentIsDisabledOrReadonly ?

        RadioButtons radioButtons = (RadioButtons) component;
		String clientId = radioButtons.getClientId(context);

		Map<String, String> requestParameterValues =
			  context.getExternalContext().getRequestParameterMap();
		if (requestParameterValues.containsKey(clientId)) {
			String newValue = requestParameterValues.get(clientId);
			radioButtons.setSubmittedValue(newValue);
		} else {
			radioButtons.setSubmittedValue(null);
		}
		decodeBehaviors(context, radioButtons);
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        RadioButtons radioButtons = (RadioButtons) component;
        String style = (style = radioButtons.getStyle()) == null ? "" : style.trim();
        String styleClass = (styleClass = radioButtons.getStyleClass()) == null ? "" : styleClass.trim();
        styleClass += (styleClass.length() > 0 ? " " : "") + "ice-ace-radiobuttons ui-widget ui-widget-content ui-corner-all " +getStateStyleClasses(radioButtons);

        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId(context), "id");
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
		renderResetSettings(facesContext, clientId);
//        ComponentUtils.enableOnElementUpdateNotify(writer, clientId);

        writer.writeAttribute(HTML.CLASS_ATTR, "ice-ace-radiobutton", null);
        encodeScript(facesContext, writer, radioButtons, clientId, EventType.HOVER, item.isDisabled());

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

        // First Wrapper
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, firstWrapperClass, null);

        // Second Wrapper
        writer.startElement(HTML.SPAN_ELEM, null);

        if (ariaEnabled) {
			writer.writeAttribute(HTML.ROLE_ATTR, "radio", null);
			writer.writeAttribute(HTML.ARIA_DISABLED_ATTR, item.isDisabled(), null);
		}

        // Button Element
        writer.startElement(HTML.BUTTON_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
        String buttonId = clientId + "_button";
        writer.writeAttribute(HTML.ID_ATTR, buttonId, null);
        writer.writeAttribute(HTML.NAME_ATTR, buttonId, null);
		String selectedClass = "";
		selectedClass = (selected ? "ice-ace-radiobutton-selected" : "");
		writer.writeAttribute(HTML.CLASS_ATTR, "ui-corner-all " + selectedClass, null);

		if (ariaEnabled) writer.writeAttribute(HTML.TABINDEX_ATTR, "0", null);
        encodeButtonStyle(writer, item.isDisabled());
        encodeScript(facesContext, writer, radioButtons, clientId, EventType.FOCUS, item.isDisabled());

        if (label != null && "inField".equalsIgnoreCase(radioButtons.getLabelPosition())) {
            writer.startElement(HTML.SPAN_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, "ui-label", null);
            writer.write(label);
            writer.endElement(HTML.SPAN_ELEM);
        } else {
            writer.startElement(HTML.SPAN_ELEM, null);
            encodeIconStyle(writer, selected);
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
					|| "bottom".equalsIgnoreCase(labelPosition)) {
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
        writer.endElement("input");

		// register radio button with group
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeText("ice.ace.radiobutton.register('"+clientId+"','"+radioButtons.getClientId(facesContext)+"');", null);
		writer.endElement("script");

        writer.endElement(HTML.DIV_ELEM);
		
		Utils.registerLazyComponent(facesContext, clientId, getScript(facesContext, writer, radioButtons, clientId, item.isDisabled()));
	}

    private void encodeScript(FacesContext facesContext, ResponseWriter writer,
                              RadioButtons radioButtons, String clientId, EventType type, boolean disabled) throws IOException {

        String eventType = "";
        if (EventType.HOVER.equals(type))
            eventType = HTML.ONMOUSEOVER_ATTR;
        else if (EventType.FOCUS.equals(type))
            eventType = HTML.ONFOCUS_ATTR;

        writer.writeAttribute(eventType, "if (!document.getElementById('" + clientId + "').widget) "+ getScript(facesContext, writer, radioButtons, clientId, disabled), null);
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

    private void encodeButtonStyle(ResponseWriter writer, boolean disabled) throws IOException {
        String buttonClasses = "";
        String disabledClass = "ui-state-disabled";

        if (disabled) {
            buttonClasses += disabledClass + " ";
        }

        if (!buttonClasses.equals("")) {
            writer.writeAttribute(HTML.CLASS_ATTR, buttonClasses.trim(), null);
        }
    }

    private void encodeIconStyle(ResponseWriter writer, boolean value) throws IOException {
        String iconClass = "fa";
        String selectedStyle = "fa-dot-circle-o";
        String unselectedStyle = "fa-circle-o";
		String largeStyle = "fa-lg";

        if (value) {
            iconClass += " " + selectedStyle + " " + largeStyle;
        } else {
            iconClass += " " + unselectedStyle + " " + largeStyle;
        };

        writer.writeAttribute(HTML.CLASS_ATTR, iconClass, null);
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

	protected void renderResetSettings(FacesContext context, String clientId) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		JSONBuilder jb = JSONBuilder.create();
		jb.beginArray();
		jb.item("radiobutton");
		jb.beginArray();
		jb.item(clientId);
		jb.item(EnvUtils.isAriaEnabled(context));
		jb.item(true);
		jb.endArray();
		jb.endArray();

		writer.writeAttribute("data-ice-reset", jb.toString(), null);
	}
}
