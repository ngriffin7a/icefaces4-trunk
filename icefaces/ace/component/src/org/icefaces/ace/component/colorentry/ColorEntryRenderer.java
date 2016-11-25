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
import org.icefaces.ace.util.PassThruAttributeWriter;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.util.JavaScriptRunner;

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
       // System.out.println(" check in decode submittedValue="+picker.getSubmittedValue().toString()+" color="+picker.getColor());
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
        Map paramMap = context.getExternalContext().getRequestParameterMap();
        String iceFocus = (String) paramMap.get("ice.focus");
        boolean ariaEnabled = EnvUtils.isAriaEnabled(context);
        String type = popup ? "text" : "hidden";
        Map<String, Object> labelAttributes = getLabelAttributes(component);
        labelAttributes.put("fieldClientId", clientId + "_input");
        if (popup) {
             writeLabelAndIndicatorBefore(labelAttributes);
         }

        writer.startElement(HTML.SPAN_ELEM, component);
        writer.writeAttribute("id", clientId, "clientId");
      //  renderResetSettings(context, component);
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
            writer.startElement("div", null);
            writer.writeAttribute("id", clientId + "_inline", null);
            writer.writeAttribute("class", "ice-ace-datetimeentry", null);
            writer.endElement("div");
        }

        writeLabelAndIndicatorBefore(labelAttributes);

        writer.startElement("input", null);
        writer.writeAttribute("id", inputId, null);
        writer.writeAttribute("name", inputId, null);
        PassThruAttributeWriter.renderHtml5PassThroughAttributes(writer, picker) ;
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
        if (popup){
            showCloseButton=false;
        }
        if (popup && ariaEnabled) {
            writer.writeAttribute("role", "textbox", null);
        }
        String styleClasses = (themeForms() ? ColorEntry.INPUT_STYLE_CLASS : "");
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
    /*    writer.writeAttribute("data-ice-clear-ignore", "true", null);
        if (ariaEnabled) {
            writer.writeAttribute("role", "region", null);
        } */
   //     ComponentUtils.enableOnElementUpdateNotify(writer, clientId);


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
        writer.endElement("input");
        String preferredFormat = picker.getColorFormat().HEX3.getValue();
        if (picker.getColorFormat()!=null){
            preferredFormat = picker.getColorFormat().getValue();
        }
        String showOn = picker.getShowOn();
        String parts = picker.getPresetParts().toLowerCase().trim();
        if (parts.equals("inline")){
            showOn="focus alt click";
        }

        String iconSrc = picker.getPopupIcon() != null ? getResourceURL(context, picker.getPopupIcon()) : getResourceRequestPath(context, ColorEntry.POPUP_ICON);

        JSONBuilder jb = JSONBuilder.create();
        jb.beginFunction("ice.ace.create")
                .item("ColorEntryInit")
                .beginArray()
      /*          .item(clientId) */
                .beginMap()
                .beginMap("options")
                .entry("id", clientId)
                .entry("colorFormat", preferredFormat)

                .entry("parts", parts)
                .entry("disabled", picker.isDisabled());
    /*    if (valueToRender !=null & valueToRender.length()>0) {
            jb.entry("color", valueToRender);
        }  */
        if (!parts.equals("inline")){
            jb.entry("autoOpen", !picker.isRenderAsPopup());
            jb.entry("alpha", picker.isAlpha());
            jb.entry("buttonImage", iconSrc);
            jb.entry("buttonImageOnly", picker.isPopupIconOnly());
            jb.entry("showCloseButton", showCloseButton);
            jb.entry("showCancelButton", picker.isShowCancelButton());
            jb.entry("buttonColorize", picker.isButtonColorize());
            jb.entry("showNoneButton", picker.isShowNoneButton());
            if (!showOn.equalsIgnoreCase("focus")) {
                //          String iconSrc = picker.getPopupIcon() != null ? getResourceURL(context, picker.getPopupIcon()) : getResourceRequestPath(context, picker.POPUP_ICON);
                jb.entry("showOn", showOn)
                        .entry("buttonImage", iconSrc)
                        .entry("buttonImageOnly", picker.isPopupIconOnly());
            }
            if (picker.getEffect() !=null) {
                 jb.entry("showAnim", picker.getEffect())
                 .entry("duration", picker.getEffectDuration());
             }
        }
        if (picker.getLimit()!=null ){
            jb.entry("limit", picker.getLimit());
        }
        if (!picker.isRequired()){
            jb.entry("allowEmpty", "true");
        }


   /*     if (picker.getPaletteList()!=null){
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
        }   */


        jb.endMap();

        encodeClientBehaviors(context, picker, jb);
      //  jb.endArray().endFunction();
        jb.endMap().endArray().endFunction();
       // String script = jb.toString() + "});";
        String script = jb.toString();
 //  System.out.println(" script="+script);

        writeLabelAndIndicatorAfter(labelAttributes);
        writer.endElement("span");
        writer.startElement("span", null);
        writer.writeAttribute("id", clientId+"_script", null);
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
     //   writer.write("ice.ace.jq(function() {");
        writer.write(script);

        writer.endElement("script");

        writer.startElement("span", null);
        writer.writeAttribute("style", "display:none;", null);
        writer.writeAttribute("data-hashcode", script.hashCode(), null);
        writer.endElement("span");

        writer.endElement(HTML.SPAN_ELEM);
      //  JavaScriptRunner.runScript(context, "ice.ace.registerLazyComponent('" + clientId + "');");
        // getting unexpected identifier error with previous line
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
