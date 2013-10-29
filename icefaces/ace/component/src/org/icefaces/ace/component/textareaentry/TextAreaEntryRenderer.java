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

package org.icefaces.ace.component.textareaentry;

import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.ace.util.Utils;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@MandatoryResourceComponent(tagName = "textAreaEntry", value = "org.icefaces.ace.component.textareaentry.TextAreaEntry")
public class TextAreaEntryRenderer extends InputRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        TextAreaEntry textAreaEntry = (TextAreaEntry) component;

        if (textAreaEntry.isDisabled() || textAreaEntry.isReadonly()) {
            return;
        }

        decodeBehaviors(context, textAreaEntry);

        String clientId = textAreaEntry.getClientId(context);
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String submittedValue = requestParameterMap.get(clientId + "_input");
        if (submittedValue == null && requestParameterMap.get(clientId + "_label") != null) {
            submittedValue = "";
        }

        if (submittedValue != null) {
            textAreaEntry.setSubmittedValue(submittedValue);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        Map<String, Object> domUpdateMap = new HashMap<String, Object>();
        TextAreaEntry textAreaEntry = (TextAreaEntry) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = textAreaEntry.getClientId(context);
        domUpdateMap.clear();

        writer.startElement("span", component);
        writer.writeAttribute("id", clientId, "clientId");

        Map<String, Object> labelAttributes = getLabelAttributes(component);

        writer.startElement("span", textAreaEntry);
        writer.writeAttribute("id", clientId + "_markup", null);
        writer.writeAttribute("class", "ui-textareaentry-container", null);
        encodeLabelAndInput(component, labelAttributes, domUpdateMap);
        writer.endElement("span");

        domUpdateMap.putAll(labelAttributes);
        writer.startElement("span", textAreaEntry);
        writer.writeAttribute("data-hashcode", domUpdateMap.hashCode(), null);
        writer.writeAttribute("style", "display: none;", null);
        writer.endElement("span");
        writer.endElement("span");
    }

    @Override
    protected void writeInputField(UIComponent component, Map<String, Object> labelAttributes, Map<String, Object> domUpdateMap) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ResponseWriter writer = context.getResponseWriter();
        Map paramMap = context.getExternalContext().getRequestParameterMap();
        TextAreaEntry textAreaEntry = (TextAreaEntry) component;
        boolean ariaEnabled = EnvUtils.isAriaEnabled(context);

        String clientId = textAreaEntry.getClientId(context);

        writer.startElement("textarea", null);
        writer.writeAttribute("id", clientId + "_input", null);
        writer.writeAttribute("name", clientId + "_input", null);
        if (ariaEnabled) {
            writer.writeAttribute("role", "textbox", null);
        }

        String iceFocus = (String) paramMap.get("ice.focus");
        String inFieldLabel = (String) labelAttributes.get("inFieldLabel");
        String value = ComponentUtils.getStringValueToRender(context, component);
        String defaultClass = themeForms() ? TextAreaEntry.THEME_INPUT_CLASS : TextAreaEntry.PLAIN_INPUT_CLASS;
        defaultClass += getStateStyleClasses(textAreaEntry);
        if (isValueBlank(value) && !isValueBlank(inFieldLabel) && !clientId.equals(iceFocus)) {
            writer.writeAttribute("name", clientId + "_label", null);
            value = inFieldLabel;
            defaultClass += " " + IN_FIELD_LABEL_STYLE_CLASS;
            labelAttributes.put("labelIsInField", true);
        }
        defaultClass += textAreaEntry.isResizable() ? " ui-textareaentry-resizable" : " ui-textareaentry-non-resizable";

        renderPassThruAttributes(context, textAreaEntry, HTML.TEXTAREA_ATTRS);

        if (ariaEnabled) {
            final TextAreaEntry compoent = (TextAreaEntry) component;
            Map<String, Object> ariaAttributes = new HashMap<String, Object>() {{
                put("multiline", true);
                put("readonly", compoent.isReadonly());
                put("required", compoent.isRequired());
                put("disabled", compoent.isDisabled());
                put("invalid", !compoent.isValid());
            }};
            writeAriaAttributes(ariaAttributes, labelAttributes);
        }

		String placeholder = textAreaEntry.getPlaceholder();
		if (placeholder != null) writer.writeAttribute("placeholder", placeholder, null);
		if (textAreaEntry.isDisabled()) writer.writeAttribute("disabled", "disabled", "disabled");
        if (textAreaEntry.isReadonly()) writer.writeAttribute("readonly", "readonly", "readonly");
        String style = textAreaEntry.getStyle();
        if (style != null) writer.writeAttribute("style", style, "style");

        String styleClass = textAreaEntry.getStyleClass();
        Utils.writeConcatenatedStyleClasses(writer, defaultClass, styleClass);
        domUpdateMap.put("defaultClass", defaultClass);
        domUpdateMap.put("styleClass", styleClass);
        domUpdateMap.put("value", value);

        if (value != null) {
            writer.writeText(value, "value");
        }
		
        JSONBuilder jb = JSONBuilder.create();
        jb.initialiseVar(resolveWidgetVar(textAreaEntry))
          .beginFunction("ice.ace.lazy")
                .item("TextAreaEntry")
                .beginArray()
                .item(clientId)
          .beginMap()
          .entryNonNullValue("inFieldLabel", (String) labelAttributes.get("inFieldLabel"))
          .entry("inFieldLabelStyleClass", IN_FIELD_LABEL_STYLE_CLASS);
        jb.entry("maxlength", textAreaEntry.getMaxlength());
		if (placeholder != null) jb.entry("placeholder", placeholder);
		else jb.entry("placeholder", "");

        encodeClientBehaviors(context, textAreaEntry, jb);

        if (!themeForms()) {
            jb.entry("theme", false);
        }

        jb.endMap().endArray().endFunction();
        writer.writeAttribute("onfocus", jb.toString(), null);
		
        writer.endElement("textarea");
    }

    public static void printMap(Map<?, ?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue() + ";");
        }
    }
}
