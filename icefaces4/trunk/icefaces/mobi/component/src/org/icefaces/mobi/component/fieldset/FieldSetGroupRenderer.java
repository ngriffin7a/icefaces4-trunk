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

package org.icefaces.mobi.component.fieldset;


import org.icefaces.mobi.util.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.logging.Logger;


public class FieldSetGroupRenderer extends Renderer {
    private static final Logger logger = Logger.getLogger(FieldSetGroupRenderer.class.getName());
 
      
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        FieldSetGroup field = (FieldSetGroup) uiComponent;
        //write out root tag
        writer.startElement(HTML.FIELDSET_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);

        // apply default style class
        StringBuilder styleClass = new StringBuilder(FieldSetGroup.FIELDSET_CLASS);
        // optional inset style class
        if (field.isInset()){
            styleClass.append(" ").append(FieldSetGroup.FIELDSETINSET_CLASS);
        }
        // user specified style class
        String userDefinedClass = field.getStyleClass();
        if (userDefinedClass != null && userDefinedClass.length() > 0){
            styleClass.append(" ").append(userDefinedClass);
        }
        writer.writeAttribute("class", styleClass.toString(), "styleClass");

        // write out any users specified style attributes.
        writer.writeAttribute(HTML.STYLE_ATTR, field.getStyle(), "style");
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.FIELDSET_ELEM);
     }
}
