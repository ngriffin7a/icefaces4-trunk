
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
package org.icefaces.ace.component.stackpane;


import static org.icefaces.ace.util.HTML.ID_ATTR;
import static org.icefaces.ace.util.HTML.SPAN_ELEM;
import static org.icefaces.ace.util.HTML.DIV_ELEM;
import static org.icefaces.ace.util.HTML.CLASS_ATTR;
import static org.icefaces.ace.util.HTML.STYLE_ATTR;

import org.icefaces.util.CoreComponentUtils;
import org.icefaces.ace.renderkit.CoreRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.swing.text.html.HTML;
import java.io.IOException;


public class StackPaneRenderer extends CoreRenderer {

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
        UIComponent parent = uiComponent.getParent();
        StackPane pane = (StackPane) uiComponent;
        ResponseWriter writer =facesContext.getResponseWriter();
        boolean selected = pane.isSelected();
        encodeStackPaneBegin(pane, writer, selected);
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
          UIComponent parent = uiComponent.getParent();
          StackPane pane = (StackPane)uiComponent;
          ResponseWriter writer =facesContext.getResponseWriter();
          encodeStackPaneEnd(writer);
      }

    
    /* ****************** CONTENT STACK RENDERING ********************** */
    
    private void encodeStackPaneBegin(StackPane pane, ResponseWriter writer, boolean selected)
        throws IOException{
        String clientId = pane.getClientId();
        writer.startElement(DIV_ELEM, pane);
        writer.writeAttribute(ID_ATTR, clientId+"_wrp", null);
        if( selected ){
            writer.writeAttribute(CLASS_ATTR, 
                     StackPane.CONTENT_SELECTED, null);
                
        }
        else{
            writer.writeAttribute(CLASS_ATTR, 
                    StackPane.CONTENT_HIDDEN, null);
        }
        writer.writeAttribute("data-paneid", pane.getId(), null);

        writer.startElement(DIV_ELEM, pane);
        writer.writeAttribute(ID_ATTR, clientId, null);
        if (pane.getStyleClass()!=null){
            writer.writeAttribute(CLASS_ATTR, pane.getStyleClass(), null);
        }
        if (pane.getStyle()!=null){
            writer.writeAttribute(STYLE_ATTR, pane.getStyle(), null);
        }

    }
    
    public void encodeStackPaneEnd(ResponseWriter writer)
            throws IOException {
       writer.endElement(DIV_ELEM);
       writer.endElement(DIV_ELEM);
   }
    
    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException{
        StackPane pane = (StackPane)uiComponent;
        if (pane.isClient()){
            renderChildren(facesContext, uiComponent);
        }
        else if (pane.isSelected()){
            renderChildren(facesContext, uiComponent);
        }
    }


}
