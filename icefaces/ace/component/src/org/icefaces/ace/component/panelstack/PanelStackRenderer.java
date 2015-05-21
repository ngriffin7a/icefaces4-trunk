/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
package org.icefaces.ace.component.panelstack;


//import org.icefaces.mobi.component.contentpane.ContentPane;
//import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
//import org.icefaces.mobi.utils.HTML;
//import org.icefaces.mobi.utils.JSFUtils;
//import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icefaces.ace.component.stackpane.StackPane;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.HTML;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.lang.System;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

//import static org.icemobile.util.HTML.CLASS_ATTR;

public class PanelStackRenderer extends CoreRenderer {

    private static final Logger logger = Logger.getLogger(PanelStackRenderer.class.getName());

    @Override
    public void decode(FacesContext facesContext, UIComponent component) {
        PanelStack stack = (PanelStack) component;
        String clientId = stack.getClientId(facesContext);
        String indexStr = facesContext.getExternalContext()
                .getRequestParameterMap().get(clientId + "_hidden");
        
        if( null != indexStr && indexStr.length() > 0 ) {
            if (!indexStr.equals(stack.getCurrentId())){
                System.out.println(" setting stack id to="+indexStr);
                stack.setCurrentId(indexStr);
            }
        }
    }


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         String clientId = uiComponent.getClientId(facesContext);
         PanelStack container = (PanelStack) uiComponent;
         /* can use stack with contentNavBar so may need to write out javascript for menu */

            /* write out root tag.   */
         writer.startElement(HTML.DIV_ELEM, uiComponent);
         writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
         writer.writeAttribute(HTML.CLASS_ATTR, "ace-panelstack", null);

    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException{
        for (UIComponent child : uiComponent.getChildren()) {
             if (!(child instanceof StackPane) && logger.isLoggable(Level.FINER)){
                 logger.finer("all children must be of type stackPane");
                 return;
             }
        }
        super.renderChildren(facesContext, uiComponent);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         PanelStack stack = (PanelStack) uiComponent;
         String clientId = uiComponent.getClientId(facesContext);
         writer.startElement("span", uiComponent);
         writer.startElement("input", uiComponent);
         writer.writeAttribute("type", "hidden", null);
         writer.writeAttribute("id", clientId+"_hidden", null);
         writer.writeAttribute("name", clientId+"_hidden", null);
         String currentId = stack.getCurrentId();
      //  System.out.println(" currentId="+currentId);
         if( currentId != null && currentId.length() > 0 ) {
             writer.writeAttribute("value", currentId, null);
         }
         writer.endElement("input");
         writer.endElement("span");
         writer.endElement(HTML.DIV_ELEM);

    }


}
