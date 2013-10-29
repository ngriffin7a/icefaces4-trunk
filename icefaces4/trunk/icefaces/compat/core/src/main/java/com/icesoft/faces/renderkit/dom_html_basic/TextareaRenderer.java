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

package com.icesoft.faces.renderkit.dom_html_basic;

import com.icesoft.faces.component.AttributeConstants;


import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;

public class TextareaRenderer extends BaseInputRenderer {
    protected static final String ONMOUSEDOWN_FOCUS = "this.focus();";

    private static final String[] passThruExcludes = new String[] {
        HTML.ROWS_ATTR, HTML.COLS_ATTR, HTML.ONMOUSEDOWN_ATTR };
    private static final String[] passThruAttributes =
        AttributeConstants.getAttributes(
            AttributeConstants.H_INPUTTEXTAREA, passThruExcludes);
           
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        super.encodeBegin(facesContext, uiComponent);
        DomBasicRenderer.validateParameters(facesContext, uiComponent, UIInput.class);
        
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        writer.startElement(HTML.TEXTAREA_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        
        renderHtmlAttributes(facesContext, writer, uiComponent);
        PassThruAttributeWriter.renderBooleanAttributes(
                writer, 
                uiComponent, 
                PassThruAttributeWriter.EMPTY_STRING_ARRAY);
    
        Object styleClass = uiComponent.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
    }

        renderNumericAttributeOrDefault(writer, uiComponent, HTML.ROWS_ATTR, "2");
        renderNumericAttributeOrDefault(writer, uiComponent, HTML.COLS_ATTR, "20");
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) 
            throws IOException {
        super.encodeChildren(facesContext, uiComponent);
        DomBasicRenderer.validateParameters(facesContext, uiComponent, UIInput.class);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        //it must call the super.encode to support effects and facesMessage recovery
        super.encodeEnd(facesContext, uiComponent);
        ResponseWriter writer = facesContext.getResponseWriter();

        String currentValue = getValue(facesContext, uiComponent);
        if (currentValue != null && currentValue.length() > 0) {
            writer.writeText(currentValue, null);
        }

        writer.endElement(HTML.TEXTAREA_ELEM);
        }
        
    protected void renderNumericAttributeOrDefault(
        ResponseWriter writer, UIComponent uiComponent,
        String attribName, String defaultValue)
            throws IOException {
        Object val = uiComponent.getAttributes().get(attribName);
        if (val == null || ((Integer)val).intValue() <= -1) {
            val = defaultValue;
            }
        writer.writeAttribute(attribName, val, attribName);
        }

    protected void renderHtmlAttributes(
        FacesContext facesContext, ResponseWriter writer, UIComponent uiComponent)
            throws IOException {
        PassThruAttributeWriter.renderHtmlAttributes(
            writer, uiComponent, passThruAttributes);

        //fix for ICE-2514
        String app = (String)uiComponent.getAttributes().get(HTML.ONMOUSEDOWN_ATTR);
        String rend = ONMOUSEDOWN_FOCUS;
        String combined = DomBasicRenderer.combinedPassThru(app, rend);
        writer.writeAttribute(HTML.ONMOUSEDOWN_ATTR, combined, HTML.ONMOUSEDOWN_ATTR);
    }
}
