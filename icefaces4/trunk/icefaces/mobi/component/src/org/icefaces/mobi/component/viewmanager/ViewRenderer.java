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
package org.icefaces.mobi.component.viewmanager;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.icefaces.ace.util.Utils;
import org.icefaces.mobi.util.HTML;

public class ViewRenderer extends Renderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {

    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
       View view = (View)uiComponent;
       ViewManager vm = (ViewManager)(uiComponent.getParent());
       validate(view);
       ResponseWriter writer = facesContext.getResponseWriter();
       writer.startElement(HTML.DIV_ELEM, view);
       writer.writeAttribute(HTML.ID_ATTR, view.getClientId(), null);
       writer.writeAttribute("data-view", view.isSplash() ? "splash" : view.getId(), null);
       String title = view.getTitle();
       if( title == null ){
           title = vm.getTitle();
       }
       if( title != null ){
           writer.writeAttribute("data-title", title, null);
       }
       writer.writeAttribute(HTML.CLASS_ATTR, "mobi-vm-view" + (view.isSplash() ? " mobi-vm-splash" : ""), null);
       writer.startElement(HTML.DIV_ELEM, null);
       writer.writeAttribute(HTML.ID_ATTR, view.getClientId()+"_content", null);
       writer.writeAttribute(HTML.CLASS_ATTR, "mobi-vm-view-content", null);
       writer.startElement(HTML.DIV_ELEM, null);
       writer.writeAttribute(HTML.ID_ATTR, view.getClientId()+"_inner", null);
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        Utils.renderChildren(facesContext, uiComponent);
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        View view = (View)uiComponent;
        ViewManager vm = (ViewManager)(uiComponent.getParent());
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "mobi-vm-nav-bar", null);
       
        List<View> views = view.getNavBarItemsToRender();
        String width = view.getNavBarItemChildWidth();
        String selected = vm.getSelected();
        for( View v : views ){
            renderNavBarItem(writer, v.getId(), width, selected.equals(v.getId()), 
               v.getMenuIcon(), v.getTitle());
        }
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }
    
    private void validate(View view){
       if( !(view.getParent() instanceof ViewManager) ){
    	   throw new IllegalStateException("The view component must be a child of the viewManager component");
       }
    }
    
    private void renderNavBarItem(ResponseWriter writer, String view, String width, boolean active, 
        String icon, String title)
        throws IOException{
        writer.startElement(HTML.ANCHOR_ELEM, null);
        if( active ){
            writer.writeAttribute(HTML.CLASS_ATTR, "active", null);
        }
        writer.writeAttribute(HTML.STYLE_ATTR, "width: " + width, null);
        if( !active )
            writer.writeAttribute(HTML.ONCLICK_ATTR, "ice.mobi.viewManager.showView('" + view + "');", null);
        if( icon != null ){
            writer.startElement("i",null);
            writer.writeAttribute(HTML.CLASS_ATTR, "icon-" + icon, null);
            writer.endElement("i");
        }
        writer.write(title);
        writer.endElement(HTML.ANCHOR_ELEM);
    }

}
