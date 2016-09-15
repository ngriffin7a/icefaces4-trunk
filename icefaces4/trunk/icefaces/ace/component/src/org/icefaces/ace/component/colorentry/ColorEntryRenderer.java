/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
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
package org.icefaces.ace.component.colorentry;


import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;
import org.icefaces.ace.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


@MandatoryResourceComponent(tagName="colorEntry", value="ColorEntry")
public class ColorEntryRenderer extends InputRenderer {
    @Override
     public void decode(FacesContext facesContext, UIComponent component) {
         ColorEntry picker = (ColorEntry) component;
         String clientId = picker.getClientId(facesContext);
         Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
         if(picker.isDisabled() || picker.isReadonly()) {
                 return;
 		 }
         String submittedValue = params.get(clientId + "_input");
         picker.setSubmittedValue(submittedValue);
         decodeBehaviors(facesContext, component);
     }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ColorEntry picker = (ColorEntry) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = picker.getClientId(context);
        /* TODO aria support */
        boolean ariaEnabled = EnvUtils.isAriaEnabled(context);
        writer.startElement("span", component);
        writer.writeAttribute("id", clientId, "clientId");
        renderResetSettings(context, component);

        Map<String, Object> labelAttributes = getLabelAttributes(component);
        labelAttributes.put("fieldClientId", clientId + "_input");

        writer.startElement("span", null);

        writeLabelAndIndicatorBefore(labelAttributes);

        writer.startElement("input", null);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("id", clientId + "_input", null);
        writer.writeAttribute("data-ice-clear-ignore", "true", null);
        if (ariaEnabled) {
            writer.writeAttribute("role", "region", null);
        }
        ComponentUtils.enableOnElementUpdateNotify(writer, clientId);

        String embeddedLabel = null;
        String nameToRender = clientId + "_input";
        String valueToRender = null;

        boolean validity = picker.isValid();
    	//	if (picker.isValid() || picker.getSubmittedValue() == null) {
        if (picker.getSubmittedValue() == null || isValueEmpty(String.valueOf(picker.getSubmittedValue()))) {
            valueToRender = picker.isRedisplay() ? ComponentUtils.getStringValueToRender(context, picker) : "";
        } else {
            valueToRender = (String) picker.getSubmittedValue();
        }
        boolean hasLabel = (Boolean) labelAttributes.get("hasLabel");
        String labelPosition = (String) labelAttributes.get("labelPosition");
        String label = (String) labelAttributes.get("label");
        writer.writeAttribute("name", nameToRender, null);
        writer.writeAttribute("value", valueToRender , null);

        if (ariaEnabled) {
            final ColorEntry comp = (ColorEntry) component;
            Map<String, Object> ariaAttributes = new HashMap<String, Object>() {{
                put("readonly", comp.isReadonly());
                put("required", comp.isRequired());
                put("disabled", comp.isDisabled());
                put("invalid", !comp.isValid());
            }};
            writeAriaAttributes(ariaAttributes, labelAttributes);
        }

        if(picker.isDisabled()) writer.writeAttribute("disabled", "disabled", "disabled");
        if(picker.isReadonly()) writer.writeAttribute("readonly", "readonly", "readonly");
        String style = "display:none;" +picker.getStyle();
        writer.writeAttribute("class", ColorEntry.HIDE_INPUT_CLASS, "class");
        if(style != null) writer.writeAttribute("style", style, "style");
        String preferredFormat = picker.getValueFormat().HEX3.getValue();
        if (picker.getValueFormat()!=null){
            preferredFormat = picker.getValueFormat().getValue();
        }
        int maxSelectionSize = picker.getMaxSelectionSize();
        JSONBuilder jb = JSONBuilder.create();
        jb.beginFunction("ice.ace.lazy")
                .item("ColorEntry")
                .beginArray()
                .item(clientId)
                .beginMap()
                .beginMap("options")
                .entry("preferredFormat", preferredFormat)
                .entry("showPalette", picker.isShowPalette())
                .entry("showInput", picker.isShowInput())
                .entry("showPaletteOnly", picker.isShowPaletteOnly())
                .entry("togglePaletteOnly", picker.isTogglePaletteOnly())
                .entry("showInitial", picker.isShowInitial())
                .entry("showInput", picker.isShowInput())
                .entry("showButtons", picker.isShowButtons())
                .entry("disabled", picker.isDisabled())
                .entry("chooseText", picker.getSelectButtonLabel())
                .entry("disabled", picker.isDisabled())
                .entry("cancelText", picker.getCancelButtonText())
                .entry("maxSelectionSize", maxSelectionSize)
                .entry("showSelectionPalette", picker.isShowSelectionPalette());

   /*     if (picker.getColor() !=null) {
            jb.entry("color", picker.getColor());
        } */
        if (!picker.isRequired()){
            jb.entry("allowEmpty", "true");
        }
        if (picker.getTogglePaletteMoreText() !=null){
            jb.entry("togglePaletteMoreText", picker.getTogglePaletteMoreText());
        }
        if (picker.getTogglePaletteLessText() !=null){
            jb.entry("togglePaletteLessText", picker.getTogglePaletteLessText());
        }
        if (picker.getStyleClass() !=null ){
            jb.entry("replacerClassName", picker.getStyleClass());
        }
        if (picker.getPaletteList()!=null){
            jb.beginArray("palette") ;
            List<String[]> list = picker.getPaletteList();
            for (String[] palette: list){
                jb.beginArray();
                for (String colorVal: palette){
                    jb.item(colorVal);
                }
                jb.endArray();
            }
            jb.endArray();
        }

        jb.endMap();

        encodeClientBehaviors(context, picker, jb);

        jb.endMap().endArray().endFunction();
        String script = jb.toString() + "});";
        writer.endElement("input");
        writeLabelAndIndicatorAfter(labelAttributes);
        writer.endElement("span");
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("ice.ace.jq(function() {");
        writer.write(script);
        writer.endElement("script");

        writer.startElement("span", null);
        writer.writeAttribute("style", "display:none;", null);
        writer.writeAttribute("data-hashcode", script.hashCode(), null);
        writer.endElement("span");

        writer.endElement("span");
    }

    protected void renderResetSettings(FacesContext context, UIComponent component) throws IOException {
    		ResponseWriter writer = context.getResponseWriter();
    		ColorEntry picker = (ColorEntry) component;

    		String clientId = picker.getClientId(context);

    		JSONBuilder jb = JSONBuilder.create();
    		jb.beginArray();
    		jb.item("ColorEntry");
    		jb.beginArray();
    		jb.item(clientId);

    		jb.endArray();
    		jb.endArray();

    		writer.writeAttribute("data-ice-reset", jb.toString(), null);
    }
}
