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

package org.icefaces.ace.component.textentry;

import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.ace.util.ClientDescriptor;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.ace.util.Utils;
import org.icefaces.ace.util.PassThruAttributeWriter;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

@MandatoryResourceComponent(tagName="textEntry", value="org.icefaces.ace.component.textentry.TextEntry")
public class TextEntryRenderer extends InputRenderer {
    @Override
	public void decode(FacesContext context, UIComponent component) {
		TextEntry textEntry = (TextEntry) component;

        if(textEntry.isDisabled() || textEntry.isReadonly()) {
            return;
        }

        decodeBehaviors(context, textEntry);

		String clientId = textEntry.getClientId(context);
        Map<String,String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String submittedValue = requestParameterMap.get(clientId + "_input");
        if (submittedValue == null && requestParameterMap.get(clientId + "_label") != null) {
            submittedValue = "";
        }

        if(submittedValue != null) {
            textEntry.setSubmittedValue(submittedValue);
        }
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		TextEntry textEntry = (TextEntry) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = textEntry.getClientId(context);
        boolean ariaEnabled = EnvUtils.isAriaEnabled(context);
		
		String type = textEntry.validateType(textEntry.getType());
        boolean isNumberType = type.equals("number");
        boolean isDateType = type.equals("date");

        writer.startElement("span", component);
        writer.writeAttribute("id", clientId, "clientId");

        String defaultClass = themeForms() ? TextEntry.THEME_INPUT_CLASS : TextEntry.PLAIN_INPUT_CLASS;
        String styleClass = textEntry.getStyleClass();

        defaultClass += getStateStyleClasses(textEntry);
        Map<String, Object> labelAttributes = getLabelAttributes(component);

        writer.startElement("span", textEntry);

        writeLabelAndIndicatorBefore(labelAttributes);

        writer.startElement("input", null);
        writer.writeAttribute("id", clientId + "_input", null);
        if (ariaEnabled) {
            writer.writeAttribute("role", "textbox", null);
        }

        PassThruAttributeWriter.renderNonBooleanAttributes(writer, textEntry, textEntry.getCommonInputAttributeNames());
        PassThruAttributeWriter.renderBooleanAttributes(writer, textEntry, textEntry.getBooleanAttNames());
        if (isNumberType) {
            PassThruAttributeWriter.renderNonBooleanAttributes(writer, textEntry, textEntry.getNumberAttributeNames());
        } else {
            ClientDescriptor client = Utils.getClientDescriptor();
            String typeVal = (String)textEntry.getAttributes().get("type");
            if( isDateType && client.isAndroidOS() && client.isICEmobileContainer() ){ //Android container borks date types
                typeVal = "text";
            }
            type = typeVal;
            PassThruAttributeWriter.renderNonBooleanAttributes(writer, textEntry, textEntry.getInputtextAttributeNames());
        }
        writer.writeAttribute("type", type, null);
        if (!isDateType) writer.writeAttribute("autocorrect", textEntry.getAutocorrect(), null);
        else writer.writeAttribute("autocorrect", "on", null);
        writer.writeAttribute("autocapitalize", textEntry.getAutocapitalize(), null);
        String embeddedLabel = null;
        String nameToRender = clientId + "_input";
        String valueToRender = ComponentUtils.getStringValueToRender(context, textEntry);
        boolean hasLabel = (Boolean) labelAttributes.get("hasLabel");
        String labelPosition = (String) labelAttributes.get("labelPosition");
        String label = (String) labelAttributes.get("label");
        boolean hasIndicator = (Boolean) labelAttributes.get("hasIndicator");
        String indicatorPosition = (String) labelAttributes.get("indicatorPosition");
        String indicator = (String) labelAttributes.get("indicator");
        if (valueToRender == null && isDateType){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                Date aDate = new Date();
                valueToRender =  sdf.format(aDate);
        }
        if ((valueToRender == null || valueToRender.trim().length() <= 0) && hasLabel && labelPosition.equals("inField")) {
            nameToRender = clientId + "_label";
            valueToRender = embeddedLabel = label;
            if (hasIndicator) {
                if (indicatorPosition.equals("labelLeft")) {
                    valueToRender = embeddedLabel = indicator + valueToRender;
                } else if (indicatorPosition.equals("labelRight")) {
                    valueToRender = embeddedLabel = valueToRender + indicator;
                }
            }
            defaultClass += " " + LABEL_STYLE_CLASS + "-infield";
        }
        writer.writeAttribute("name", nameToRender, null);
        writer.writeAttribute("value", valueToRender , null);

        renderPassThruAttributes(context, textEntry, HTML.INPUT_TEXT_ATTRS);

        if (ariaEnabled) {
            final TextEntry compoent = (TextEntry) component;
            Map<String, Object> ariaAttributes = new HashMap<String, Object>() {{
                put("readonly", compoent.isReadonly());
                put("required", compoent.isRequired());
                put("disabled", compoent.isDisabled());
                put("invalid", !compoent.isValid());
            }};
            writeAriaAttributes(ariaAttributes, labelAttributes);
        }

        if(textEntry.isDisabled()) writer.writeAttribute("disabled", "disabled", "disabled");
        if(textEntry.isReadonly()) writer.writeAttribute("readonly", "readonly", "readonly");
        String style = textEntry.getStyle();
        if(style != null) writer.writeAttribute("style", style, "style");

        Utils.writeConcatenatedStyleClasses(writer, defaultClass, styleClass);
		
        JSONBuilder jb = JSONBuilder.create();
        jb.initialiseVar(this.resolveWidgetVar(textEntry))
          .beginFunction("ice.ace.lazy")
          .item("TextEntry")
          .beginArray()
          .item(clientId)
          .beginMap()
          .entryNonNullValue("embeddedLabel", embeddedLabel)
          .entry("autoTab", textEntry.isAutoTab() && textEntry.getMaxlength() > 0);

        encodeClientBehaviors(context, textEntry, jb);

        if(!themeForms()) {
            jb.entry("theme", false);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(indicatorPosition).append(labelPosition);
        jb.entry("hashCode", sb.toString().hashCode());

        jb.endMap().endArray().endFunction();
        writer.writeAttribute("onfocus", jb.toString(), null);

        writer.endElement("input");

        writeLabelAndIndicatorAfter(labelAttributes);

        writer.endElement("span");

        writer.endElement("span");
	}
}
