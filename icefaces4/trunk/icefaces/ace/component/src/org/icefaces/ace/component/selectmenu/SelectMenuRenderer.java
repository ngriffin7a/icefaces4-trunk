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

package org.icefaces.ace.component.selectmenu;

import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.ace.util.HTML;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.util.EnvUtils;
import org.icefaces.ace.event.TextChangeEvent;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.el.ELContext;
import javax.el.ValueExpression;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.IOException;

@MandatoryResourceComponent(tagName="selectMenu", value="org.icefaces.ace.component.selectmenu.SelectMenu")
public class SelectMenuRenderer extends InputRenderer {

    private static final String AUTOCOMPLETE_DIV = "_div";
	private static final String LABEL_CLASS = "ui-selectmenu-item-label";
	private static final String VALUE_CLASS = "ui-selectmenu-item-value";

    public boolean getRendersChildren() {
        return true;
    }

	public void decode(FacesContext facesContext, UIComponent uiComponent) {
		SelectMenu selectMenu = (SelectMenu) uiComponent;
		selectMenu.setItemList(null);
        Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = selectMenu.getClientId(facesContext);
        String value = (String) requestMap.get(clientId + "_input");
		
		selectMenu.setSubmittedValue(value);
		
		decodeBehaviors(facesContext, selectMenu);
	}
	
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        SelectMenu selectMenu = (SelectMenu) uiComponent;
        int width = selectMenu.getWidth();
        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);
        Map paramMap = facesContext.getExternalContext().getRequestParameterMap();
        Map<String, Object> labelAttributes = getLabelAttributes(uiComponent);
        String inFieldLabel = (String) labelAttributes.get("inFieldLabel");
        String inFieldLabelStyleClass = "";
        String iceFocus = (String) paramMap.get("ice.focus");
        String mousedownScript = (String) uiComponent.getAttributes().get("onmousedown");
        String onfocusCombinedValue = "ice.setFocus(this.id);";
        String onblurCombinedValue = "";
        Object onfocusAppValue = uiComponent.getAttributes().get("onfocus");
        Object onblurAppValue = uiComponent.getAttributes().get("onblur");
        Object onchangeAppValue = uiComponent.getAttributes().get("onchange");

		String inputClientId = clientId + "_input";

		Object _value = selectMenu.getValue();
		String value = _value != null ? _value.toString() : null;
        if (isValueBlank(value)) value = null;
        boolean labelIsInField = false;

        if (value == null && !isValueBlank(inFieldLabel) && !inputClientId.equals(iceFocus)) {
            inFieldLabelStyleClass = " " + IN_FIELD_LABEL_STYLE_CLASS;
            labelIsInField = true;
        }

		// root
        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("class", "ui-selectmenu ui-widget " + selectMenu.getStyleClass(), null);
		String dir = selectMenu.getDir();
		if (dir != null) writer.writeAttribute("dir", dir, null);
		String lang = selectMenu.getLang();
		if (lang != null) writer.writeAttribute("lang", lang, null);
		String title = selectMenu.getTitle();
		if (title != null) writer.writeAttribute("title", title, null);

		writeLabelAndIndicatorBefore(labelAttributes);
		
		// value field
		writer.startElement("a", null);
		boolean disabled = selectMenu.isDisabled();
		String disabledClass = "";
		if (disabled) disabledClass = " ui-state-disabled ";
		writer.writeAttribute("class", "ui-widget-content ui-corner-all ui-selectmenu-value " + disabledClass, null);
        writer.writeAttribute("style", "display: inline-block; width: " + width + "px;", null);
        writer.writeAttribute("id", clientId + "_drop", null);
		String tabindex = selectMenu.getTabindex();
		if (tabindex != null) writer.writeAttribute("tabindex", tabindex, null);
		else writer.writeAttribute("tabindex", "0", null);
		if (ariaEnabled) {
			writer.writeAttribute("role", "select", null);
            final SelectMenu component = (SelectMenu) uiComponent;
            Map<String, Object> ariaAttributes = new HashMap<String, Object>() {{
                put("required", component.isRequired());
                put("disabled", component.isDisabled());
                put("invalid", !component.isValid());
            }};
            writeAriaAttributes(ariaAttributes, labelAttributes);
        }
		
		// text span
		writer.startElement("span", null);
		writer.writeAttribute("style", selectMenu.getStyle(), null);
		writer.writeAttribute("class", "ui-inputfield ui-widget-content ui-corner-left " + getStateStyleClasses(selectMenu) + inFieldLabelStyleClass, null);
		writer.endElement("span");
		
		// down arrow span
		writer.startElement("div", null);
		writer.writeAttribute("class", "ui-state-default ui-corner-right ui-selectmenu-button", null);
		writer.startElement("div", null);
		writer.endElement("div");
		writer.startElement("div", null);
		writer.writeAttribute("class", "ui-icon ui-icon-triangle-1-s", null);
		writer.endElement("div");
		writer.endElement("div");
		
		writer.endElement("a");
		
		writeLabelAndIndicatorAfter(labelAttributes);

		writer.startElement("input", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("name", inputClientId, null);
        writer.writeAttribute("value", value, null);
        writer.writeAttribute(HTML.AUTOCOMPLETE_ATTR, "off", null);
        writer.endElement("input");

        String divId = clientId + AUTOCOMPLETE_DIV;

        writer.startElement("div", null);
        writer.writeAttribute("id", divId, null);
        writer.writeAttribute("class", "ui-selectmenu-list", null);
        writer.writeAttribute("style", "display:none;z-index:500;", null);
        writer.endElement("div");

        encodeScript(facesContext, writer, clientId, selectMenu,
                paramMap, inFieldLabel, inputClientId, labelIsInField);

		writer.endElement("div");
    }

    private void encodeScript(FacesContext facesContext, ResponseWriter writer, String clientId, SelectMenu selectMenu, Map paramMap, String inFieldLabel, String inputClientId, boolean labelIsInField) throws IOException {
        String divId = clientId + AUTOCOMPLETE_DIV;
        Object sourceId = paramMap.get("ice.event.captured");
        boolean isEventSource = sourceId != null && sourceId.toString().equals(inputClientId);

        // script
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);

        if (!selectMenu.isDisabled() && !selectMenu.isReadonly()) {
			JSONBuilder jb = JSONBuilder.create();

            jb.beginFunction("ice.ace.create")
            .item("SelectMenu")
			
            .beginArray()
            .item(clientId)
            .item(divId)
            .item("ui-widget-content")
            .item("ui-state-hover")
			.item("ui-state-active")
            .item(selectMenu.getHeight())
            .beginMap()
            .entry("p", ""); // dummy property
            encodeClientBehaviors(facesContext, selectMenu, jb);
            jb.endMap();
			
            jb.beginMap()
            .entryNonNullValue("inFieldLabel", inFieldLabel)
            .entry("inFieldLabelStyleClass", IN_FIELD_LABEL_STYLE_CLASS)
            .entry("labelIsInField", labelIsInField);
            jb.endMap();
			
			// effects
			jb.beginMap()
			.entry("show", selectMenu.getShowEffect())
			.entry("showLength", selectMenu.getShowEffectLength())
			.entry("hide", selectMenu.getHideEffect())
			.entry("hideLength", selectMenu.getHideEffectLength())
			.endMap();

			jb.endArray();
            jb.endFunction();

            writer.writeText(jb.toString(), null);
		}

        writer.endElement("script");
		
		populateList(facesContext, selectMenu);

        // field update script
		Object value = selectMenu.getValue();
		if (value == null) value = "";
        writer.startElement("span", null);
        writer.writeAttribute("id", clientId + "_fieldupdate", null);
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeText("(function() {", null);
        writer.writeText("var instance = ice.ace.SelectMenus[\"" + clientId + "\"];", null);
        writer.writeText("instance.updateValue('"
			+ escapeJavascriptString(getConvertedValueForClient(facesContext, selectMenu, value))
			+ "');", null);
        writer.writeText("})();", null);
        writer.endElement("script");
        writer.endElement("span");
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {

    }

    public void populateList(FacesContext facesContext, SelectMenu selectMenu) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = selectMenu.getClientId(facesContext);
		boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);
        selectMenu.populateItemList();
        Iterator matches = selectMenu.getItemListIterator();
        writer.startElement("div", null);
		writer.writeAttribute("id", clientId + "_update", null);
        if (selectMenu.getSelectFacet() != null) {

            UIComponent facet = selectMenu.getSelectFacet();
			ValueExpression itemValue = selectMenu.getValueExpression("itemValue");
			ValueExpression itemDisabled = selectMenu.getValueExpression("itemDisabled");
			ELContext elContext = facesContext.getELContext();
			String listVar = selectMenu.getListVar();

            writer.startElement("div", null);
			writer.writeAttribute("style", "display: none;", null);
			writer.startElement("div", null);
            Map requestMap = facesContext.getExternalContext().getRequestMap();
            //set index to 0, so child components can get client id from selectMenu component
            selectMenu.setIndex(0);

            boolean first = true;
            while (matches.hasNext()) {
				requestMap.put(listVar, matches.next());
                boolean last = !matches.hasNext();
				Object value = itemValue.getValue(elContext);
				boolean disabled = false;
				
				try {
					disabled = (Boolean) itemDisabled.getValue(elContext);
				} catch (Exception e) {}

                String styleClass = "ui-selectmenu-facet";

				writer.startElement("div", null);
				if (ariaEnabled) writer.writeAttribute("role", "option", null);
                if (first) styleClass += " ui-corner-tl ui-corner-tr";
                if (last) styleClass += " ui-corner-bl ui-corner-br";
				if (disabled) styleClass += " ui-state-disabled";

                writer.writeAttribute("class", styleClass, null);
				
				writer.startElement("span", null); // span to display
				writer.writeAttribute("class", LABEL_CLASS, null);
				encodeParentAndChildren(facesContext, facet);
				writer.endElement("span");
				writer.startElement("span", null); // value span
				writer.writeAttribute("class", VALUE_CLASS, null);
				writer.writeAttribute("style", "visibility:hidden;display:none;", null);
				if (value != null) {
					try {
						value = getConvertedValueForClient(facesContext, selectMenu, value);
					} catch (Exception e) {
						value = value.toString();
					}
				}
                if (value != null) {
                    writer.writeText(value, null);
                }
                writer.endElement("span");
				selectMenu.resetId(facet);
				writer.endElement("div");
				
				requestMap.remove(listVar);
                first = false;
            }
            selectMenu.setIndex(-1);

			writer.endElement("div");
            String call = "ice.ace.SelectMenus[\"" + clientId +
                    "\"].setContent(ice.ace.jq(ice.ace.escapeClientId('" + clientId + "_update')).get(0).firstChild.innerHTML);";
            encodeDynamicScript(facesContext, selectMenu, call);
			writer.endElement("div");
        } else {
            if (matches.hasNext()) {
                StringBuffer sb = new StringBuffer("<div>");
                SelectItem item = null;
				String role = "";
				if (ariaEnabled) role = " role=\"option\"";

				boolean first = true;
                while (matches.hasNext()) {
                    item = (SelectItem) matches.next();
                    boolean last = !matches.hasNext();
					String itemLabel = item.getLabel();
					Object itemValue = item.getValue();
					if (itemValue != null) {
						try {
							itemValue = getConvertedValueForClient(facesContext, selectMenu, itemValue);
						} catch (Exception e) {
							itemValue = itemValue.toString();
						}
					}
					
					itemLabel = itemLabel == null ? itemValue.toString() : itemLabel;

					String styleClass = "";
					if (item.isDisabled()) {
                        styleClass += " ui-state-disabled";
					}
					if (first){
						styleClass += " ui-corner-tr ui-corner-tl";
					}
					if (last){
						styleClass += " ui-corner-br ui-corner-bl";
					}

                    sb.append("<div class=\""+ styleClass +"\" "+ role +">");

					// label span
					sb.append("<span class=\"" + LABEL_CLASS + "\">").append(itemLabel).append("</span>");
					// value span
					sb.append("<span class=\"" + VALUE_CLASS + "\" style=\"visibility:hidden;display:none;\">").append(itemValue).append("</span>");
					
					sb.append("</div>");
					first = false;
                }
                sb.append("</div>");
                String call = "ice.ace.SelectMenus[\"" + clientId + "\"]" +
                        ".setContent('" + escapeSingleQuote(sb.toString()) + "');";
                encodeDynamicScript(facesContext, selectMenu, call);
            }
        }
		writer.endElement("div");
    }
	
    public void encodeDynamicScript(FacesContext facesContext, UIComponent uiComponent, String call) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
		
		writer.startElement("span", null);
		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
		writer.writeText(call, null);
		writer.endElement("script");
		writer.endElement("span");
	}
		
	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {

	}

    private static String escapeSingleQuote(String text) {
        if (null == text) {
            return "";
        }
        char[] chars = text.toCharArray();
        StringBuilder buffer = new StringBuilder(chars.length);
        for (int index = 0; index < chars.length; index++) {
            char ch = chars[index];
            if (ch == '\'') {
                buffer.append("&#39;");
            } else {
                buffer.append(ch);
            }
        }

        return buffer.toString();
    }
	
	private static String escapeJavascriptString(String str) {
		if (str == null) return "";
		return str.replace("\\", "\\\\").replace("\'","\\'");
	}
	
    // taken from com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer
	public static void encodeParentAndChildren(FacesContext facesContext, UIComponent parent) throws IOException {
        parent.encodeBegin(facesContext);
        if (parent.getRendersChildren()) {
            parent.encodeChildren(facesContext);
        } else {
            if (parent.getChildCount() > 0) {
                Iterator children = parent.getChildren().iterator();
                while (children.hasNext()) {
                    UIComponent nextChild = (UIComponent) children.next();
                    if (nextChild.isRendered()) {
                        encodeParentAndChildren(facesContext, nextChild);
                    }
                }
            }
        }
        parent.encodeEnd(facesContext);
    }
	
	public String getConvertedValueForClient(FacesContext context, UIComponent component, Object value) throws ConverterException {
		SelectMenu selectMenu = (SelectMenu) component;
		Converter converter = selectMenu.getConverter();
		
		if(converter != null) {
			return converter.getAsString(context, selectMenu, value);
		} else {
			ValueExpression ve = selectMenu.getValueExpression("value");

			if(ve != null) {
				Class<?> valueType = ve.getType(context.getELContext());
				Converter converterForType = context.getApplication().createConverter(valueType);

				if(converterForType != null) {
					return converterForType.getAsString(context, selectMenu, value);
				}
			}
		}
		
		return (value != null ? value.toString() : "");
	}
}
