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
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.model.colorEntry.ColorEntryLayout;

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
         boolean popup = picker.isRenderAsPopup();
         String submittedValue = params.get(clientId + "_input");
         String hidden = params.get(clientId+"_hidden");
         if (picker.getColorFormat()!=null){
             String preferredFormat = picker.getColorFormat().getValue();
             if (!isValueEmpty(preferredFormat) && preferredFormat.startsWith("HSL")) {
                String hexVal = params.get(clientId + "_hiddenHex");
                if (!isValueBlank(hexVal)) {
                    picker.setHexVal(hexVal);
                }
            }
         }
         if (isValueBlank(hidden))hidden= params.get(clientId+"_hidden2");
         if (!popup){
           //   is inline so get value from hidden input field"+hidden2
             submittedValue= hidden;
         }
         picker.setSubmittedValue(submittedValue);
         decodeBehaviors(facesContext, component);
     }


    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ColorEntry picker = (ColorEntry) component;
        ResponseWriter writer = context.getResponseWriter();

        String clientId = picker.getClientId(context);
        String valueToRender = getValueAsString(context,picker);
        String inputId = clientId + "_input";
        boolean popup = picker.isRenderAsPopup();

        String showOn = picker.getShowOn();   //default is focus
        boolean customParts=false;
        List<String> partsList = picker.getCustomParts();
        if (partsList !=null  && partsList.size()> 0) {
           customParts = true;
        }
        Map paramMap = context.getExternalContext().getRequestParameterMap();
        String iceFocus = (String) paramMap.get("ice.focus");
        boolean ariaEnabled = EnvUtils.isAriaEnabled(context);
        String type = popup ? "text" : "hidden";
        String iconSrc = picker.getPopupIcon() != null ? getResourceURL(context, picker.getPopupIcon()) : getResourceRequestPath(context, ColorEntry.POPUP_ICON);
        if (picker.isPopupIconOnly()){
            type="hidden";
        }
        Map<String, Object> labelAttributes = getLabelAttributes(component);
        labelAttributes.put("fieldClientId", clientId + "_input");
        if (popup) {
             writeLabelAndIndicatorBefore(labelAttributes);
        }

        writer.startElement(HTML.DIV_ELEM, component);
        writer.writeAttribute("id", clientId, "clientId");
        renderResetSettings(context, component);
        ComponentUtils.enableOnElementUpdateNotify(writer, clientId);
        String style = picker.getStyle();
        if(style != null) {
            writer.writeAttribute("style", style, null);
        }
        String styleClass = picker.getStyleClass();
        if(styleClass != null){
            writer.writeAttribute("class", styleClass, null);
        }
        //inline container
        if(!popup) {
            writer.startElement("span", null);
            writer.writeAttribute("id", inputId, null);
            writer.writeAttribute("style", "display:inline-block", null);
            writer.endElement("span");
        }

        writer.startElement("input", null);
        String inputFieldId=clientId+"hidden";
        String preferredFormat = picker.getColorFormat().HEX.getValue();
        if (picker.getColorFormat()!=null){
            preferredFormat = picker.getColorFormat().getValue();
        }

        if (popup){
            inputFieldId=inputId;
            String borderColor = "#f2eaea";
            if (valueToRender!=null && valueToRender.length()>0) {
                borderColor=valueToRender ;
                if (preferredFormat.startsWith("HEX")){
                    if (!borderColor.startsWith("#")){
                        borderColor="#"+borderColor;
                    }
                }
                if (preferredFormat.startsWith("HSL") && !isValueEmpty(picker.getHexVal())){
                     borderColor= picker.getHexVal();
                }
                String popupStyleBorder = "border-left-color: "+borderColor+" !important;";
                writer.writeAttribute("style", popupStyleBorder, null);
            }
        }
        writer.writeAttribute("id", inputFieldId, null);
        writer.writeAttribute("name", inputFieldId, null);
        writer.writeAttribute("type", type, null);
 		String tabindex = picker.getTabindex();
 		if (tabindex != null)
 			writer.writeAttribute("tabindex", tabindex, null);
 		String accesskey = picker.getAccesskey();
 		if (accesskey != null) {
 			writer.writeAttribute("accesskey", accesskey, null);
 			if (tabindex == null) writer.writeAttribute("tabindex", "0", null);
 		}
        boolean showCloseButton = picker.isShowCloseButton();

        if (popup && ariaEnabled) {
            writer.writeAttribute("role", "textbox", null);
        }
        String inputStyleClass = ColorEntry.INPUT_STYLE_CLASS;
        if (isValueBlank(valueToRender))  {
            inputStyleClass = ColorEntry.INPUT_EMPTY_STYLE_CLASS;
        }
        String styleClasses = (themeForms() ? inputStyleClass : "");
        if(!isValueBlank(valueToRender)) {
            writer.writeAttribute("value", valueToRender, null);
        } else if (popup && !clientId.equals(iceFocus)) {
            String inFieldLabel = (String) labelAttributes.get("inFieldLabel");
            if (!isValueBlank(inFieldLabel)) {
                writer.writeAttribute("name", clientId + "_label", null);
                writer.writeAttribute("value", inFieldLabel, null);
                styleClasses += " " + IN_FIELD_LABEL_STYLE_CLASS;
                labelAttributes.put("labelIsInField", true);
            }
        }
        writer.writeAttribute("data-ice-clear-ignore", "true", null);
        if (ariaEnabled) {
            writer.writeAttribute("role", "region", null);
        }

      /*  boolean hasLabel = (Boolean) labelAttributes.get("hasLabel");
        String labelPosition = (String) labelAttributes.get("labelPosition");
        String label = (String) labelAttributes.get("label"); */

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

        if (popup) {
            if(!isValueBlank(styleClasses)) {
                writer.writeAttribute("class", styleClasses, null);
            }
        }
        writer.endElement("input");
        if (popup){
            writeLabelAndIndicatorAfter(labelAttributes);
        }

        //String iconSrc = picker.getPopupIcon() != null ? getResourceURL(context, picker.getPopupIcon()) : getResourceRequestPath(context, ColorEntry.POPUP_ICON);

        JSONBuilder jb = JSONBuilder.create();
        jb.beginFunction("ice.ace.create")
                .item("ColorEntryInit")
                .beginArray()
      /*          .item(clientId) */
                .beginMap()
                .beginMap("options")
                .entry("id", clientId)
                .entry("colorFormat", preferredFormat)
                .entry("disabled", picker.isDisabled());
        if (valueToRender!=null && valueToRender.length()>0){
            jb.entry("color", valueToRender) ;
        }
        if (picker.getTitle()!=null && picker.getTitle().length()>0){
            jb.entry("title", picker.getTitle());
        }
        if (customParts) {  /* custom layout */
            jb.beginArray("parts");
            for (String entry: partsList){
                jb.item(entry);
            }
            jb.endArray();
            List<ColorEntryLayout> customLayout =picker.getCustomLayout();
            if (customLayout!=null && customLayout.size()> 0){
                jb.beginMap("layout");
                for (ColorEntryLayout layout: customLayout){
                    jb.beginArray(layout.getPart());
                    jb.item(layout.getColumn());
                    jb.item(layout.getRow());
                    jb.item(layout.getColspan());
                    jb.item(layout.getRowspan());
                    jb.endArray();
                }
                jb.endMap();
            }
        }

        if (popup){
         //   jb.entry("autoOpen", !picker.isRenderAsPopup());
            jb.entry("alpha", picker.isAlpha());
            jb.entry("buttonImage", iconSrc);
            boolean buttonImageOnly = picker.isPopupIconOnly();
            if (picker.getButtonText()==null || picker.getButtonText().length()<1){
                buttonImageOnly = true;
            }
            jb.entry("showCloseButton", showCloseButton);
            jb.entry("showCancelButton", picker.isShowCancelButton());
            jb.entry("buttonColorize", picker.isButtonColorize());
            jb.entry("showNoneButton", picker.isShowNoneButton());
            if (!showOn.equalsIgnoreCase("focus")) {
                jb.entry("showOn", showOn)
                        .entry("buttonImage", iconSrc)
                        .entry("buttonImageOnly", buttonImageOnly);
            }
            if (picker.getEffect() !=null && picker.getEffect().length()> 0) {
                 jb.entry("showAnim", picker.getEffect()) ;
                String duration = picker.getEffectDuration();
                if (duration !=null && duration.length()>0){
                    jb.entry("duration", duration);
                }
            }
        } else{              //default inline
            jb.entry("inline", true);
            jb.entry("inlineForm", true);
        }

        if (picker.getLimit()!=null ){
            jb.entry("limit", picker.getLimit());
        }
        if (!picker.isRequired()){
            jb.entry("allowEmpty", "true");
        }


        jb.endMap();

        encodeClientBehaviors(context, picker, jb);
        jb.endMap().endArray().endFunction();
        String script = jb.toString();
   //System.out.println(" script="+script);

        writeLabelAndIndicatorAfter(labelAttributes);

        writer.startElement("span", null);
        writer.writeAttribute("id", clientId+"_script", null);
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write(script);

        writer.endElement("script");
        writer.endElement("span");
        writer.startElement("span", null);
        writer.writeAttribute("style", "display:none;", null);
        writer.writeAttribute("data-hashcode", script.hashCode(), null);
        writer.endElement("span");
        if (!popup) {
            createHiddenField(writer, clientId + "_hidden2");
        }
        if (preferredFormat.startsWith("HSL")){
            createHiddenField(writer, clientId+"_hiddenHex") ;
        }
        writer.endElement(HTML.DIV_ELEM);

    }

    private void createHiddenField(ResponseWriter writer, String id) throws IOException {
        writer.startElement("input", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", id, null);
        writer.endElement("input");
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
    protected String getValueAsString(FacesContext context,ColorEntry picker){
        Object submittedValue = picker.getSubmittedValue();
       		if(submittedValue != null) {
       			return submittedValue.toString();
       		}

       		Object value = picker.getValue();
       		if(value == null) {
       			return null;
       		} else {
       			//first ask the converter
       			if(picker.getConverter() != null) {
       				return picker.getConverter().getAsString(context, picker, value);
       			}else {
                    return String.valueOf(value);
                }

       		}
    }
}
