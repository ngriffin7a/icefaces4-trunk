/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
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
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.component.sliderentry;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.el.MethodExpression;
import javax.faces.FacesException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.icefaces.ace.event.SlideEndEvent;

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;

import javax.faces.event.ValueChangeEvent;

@MandatoryResourceComponent(tagName="sliderEntry", value="org.icefaces.ace.component.sliderentry.SliderEntry")
public class SliderEntryRenderer extends CoreRenderer{

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Map<String,String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        SliderEntry slider = (SliderEntry) component;
        String clientId = slider.getClientId(context);
		
        boolean foundInMap = false;
        if (requestParameterMap.containsKey(clientId+"_hidden")) {
        	foundInMap = true;
        }
        
        //"ice.event.captured" should be holding the event source id
        if (requestParameterMap.containsKey("ice.event.captured")
        		|| foundInMap) {

            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));

            if ("ice.ser".equals(requestParameterMap.get("ice.submit.type"))) {
                context.renderResponse();
                return; 
            }
            Object hiddenValue = requestParameterMap.get(clientId+"_hidden");
            if (hiddenValue == null) return;
			int submittedValue = 0;
            try {
                submittedValue = Float.valueOf(hiddenValue.toString()).intValue(); //Integer.valueOf(hiddenValue.toString());
                //log.finer("Decoded slider value [id:value] [" + clientId + ":" + hiddenValue + "]" );
            } catch (NumberFormatException nfe) {
                //log.warning("NumberFormatException  Decoding value for [id:value] [" + clientId + ":" + hiddenValue + "]");
            } catch (NullPointerException npe) {
                //log.warning("NullPointerException  Decoding value for [id:value] [" + clientId + ":" + hiddenValue + "]");
            }

            //If I am a source of event?
            if (clientId.equals(source) || foundInMap) {
                try {
                    int value = slider.getValue();
                    //if there is a value change, queue a valueChangeEvent    
                    if (value != submittedValue) { 
                        component.queueEvent(new ValueChangeEvent (component, 
                                       new Integer(value), new Integer(submittedValue)));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
		
		decodeBehaviors(context, slider);
    }

    @Override
	public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
		SliderEntry slider = (SliderEntry) component;
		
		encodeMarkup(facesContext, slider);
	}
	
	protected void encodeMarkup(FacesContext context, SliderEntry slider) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = slider.getClientId(context);
		
		String axis = slider.getAxis().toLowerCase();
		boolean showLabels = slider.isShowLabels();
		
		writer.startElement("div", slider);
		writer.writeAttribute("id", clientId , "id");
		String style = slider.getStyle();
		style = style == null ? "" : style;
		writer.writeAttribute("style", style, null);
		String styleClass = slider.getStyleClass();
		if (styleClass != null) writer.writeAttribute("class", styleClass, null);
		
		writer.startElement("input", slider);
		writer.writeAttribute("id", clientId + "_hidden" , "id");
		writer.writeAttribute("name", clientId + "_hidden" , "name");
		writer.writeAttribute("type", "hidden" , "type");
		writer.writeAttribute("value", slider.getValue() , "value");
        writer.writeAttribute(HTML.AUTOCOMPLETE_ATTR, "off", null);
		writer.endElement("input");

		// top/left label
		writer.startElement("div", slider);
		if (showLabels) {
			if ("y".equals(axis)) {
				writer.writeAttribute("class", "ice-ace-slider-label-top", null);
				writer.write("" + slider.getMax());
			} else {
				writer.writeAttribute("class", "ice-ace-slider-label-left", null);
				writer.writeAttribute("style", "float:left;", null);
				writer.write("" + slider.getMin());
			}
		}
		writer.endElement("div");
		
		// main div
		String length = slider.getLength();
		if (length.toLowerCase().indexOf("px") == -1) {
			length += "px";
		}
		String dimension = "y".equals(axis) ? "height" : "width";
		writer.startElement("div", slider);
		String sliderStyle = dimension + ":" + length + ";";
		if (showLabels && !"y".equals(axis)) {
			sliderStyle += "float:left;";
		}
		writer.writeAttribute("style", sliderStyle, null);
		writer.endElement("div");
		
		// right/bottom label
		writer.startElement("div", slider);
		if (showLabels) {
			if ("y".equals(axis)) {
				writer.writeAttribute("class", "ice-ace-slider-label-bottom", null);
				writer.write("" + slider.getMin());
			} else {
				writer.writeAttribute("class", "ice-ace-slider-label-right", null);
				writer.writeAttribute("style", "float:left;", null);
				writer.write("" + slider.getMax());
			}
		}
		writer.endElement("div");
		
		encodeScript(context, slider);
		
		// clear float styling
		if (showLabels && !"y".equals(axis)) {
			writer.startElement("div", slider);
			writer.writeAttribute("style", "clear:both;", null);
			writer.endElement("div");
		}
		
		Map<String, Object> domUpdateMap = new HashMap<String, Object>();
        domUpdateMap.put("value", slider.getValue());
        writer.startElement("span", null);
        writer.writeAttribute("data-hashcode", domUpdateMap.hashCode(), null);
        writer.writeAttribute("style", "display: none;", null);
        writer.endElement("span");

		writer.endElement("div");
	}

	protected void encodeScript(FacesContext context, SliderEntry slider) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = slider.getClientId(context);
        boolean ariaEnabled = EnvUtils.isAriaEnabled(context);
        int min = slider.getMin();
        int max = slider.getMax();
        float stepPercent = slider.getStepPercent();
        float step = ((max - min) * stepPercent) / (float) 100;
        Integer tabindex = slider.getTabindex();
        String orientation = "y".equals(slider.getAxis()) ? "vertical" : "horizontal";
        String length = slider.getLength();

		writer.startElement("script", slider);
		writer.writeAttribute("type", "text/javascript", null);

		JSONBuilder jb = JSONBuilder.create();
        jb.initialiseVar(this.resolveWidgetVar(slider))
          .beginFunction("ice.ace.create")
          .item("Slider")
          .beginArray()
	      .item(clientId)
	      .beginMap()
	      .entry("input", clientId + "_hidden")
          .entry("min", min)
          .entry("max", max)
          .entry("animate", slider.isAnimate())
          .entry("step", step)
          .entry("orientation", orientation)
          .entry("clickableRail", slider.isClickableRail())
          .entry("value", slider.getValue())
          .entry("ariaEnabled", ariaEnabled);

		if (length.toLowerCase().indexOf("px") == -1) {
			length += "px";
		}
		jb.entry("length", length);

		if(slider.isDisabled()) jb.entry("disabled", true);
        if(slider.getOnSlideStart() != null) jb.entry("onSlideStart", "function(event, ui) {" + slider.getOnSlideStart() + "}", true);
        if(slider.getOnSlide() != null) jb.entry("onSlide", "function(event, ui) {" + slider.getOnSlide() + "}", true);
        if(slider.getOnSlideEnd() != null) jb.entry("onSlideEnd", "function(event, ui) {" + slider.getOnSlideEnd() + "}", true);
        if(tabindex != null) jb.entry("tabindex", tabindex.toString());

        encodeClientBehaviors(context, slider, jb);
		
        jb.endMap().endArray().endFunction();
		writer.write(jb.toString());
	
		writer.endElement("script");
		
		// script to set value
		writer.startElement("span", slider);
		writer.writeAttribute("id", clientId + "_value", null);
		writer.startElement("script", slider);
		writer.writeAttribute("type", "text/javascript", null);
		writer.write("ice.ace.jq(ice.ace.escapeClientId('"+clientId+"')).children('div').eq(1).slider('value', "+slider.getValue()+");");
		writer.endElement("script");
		writer.endElement("span");
	}
}
