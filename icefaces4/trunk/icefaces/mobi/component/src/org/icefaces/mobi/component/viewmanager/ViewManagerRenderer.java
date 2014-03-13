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
package org.icefaces.mobi.component.viewmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlBody;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.icefaces.impl.event.BridgeSetup;
import org.icefaces.mobi.util.HTML;
import org.icefaces.mobi.util.Utils;


public class ViewManagerRenderer extends Renderer{
	
    private static final Logger LOG = Logger.getLogger(ViewManagerRenderer.class.getName());

    @Override
    public void decode(FacesContext context, UIComponent component) {
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        ViewManager vm = (ViewManager)uiComponent;
        validate(vm, facesContext);
        Stack<String> history = vm.getHistoryStack();
        if( history == null ){
            vm.setHistory(new Stack<String>());
            history = vm.getHistoryStack();
        }
        
        String selected = vm.getSelected();
        
        String formId = vm.getId() + "_form";
        for(int i = 0; i < vm.getChildCount(); i++) {
            UIComponent comp = (UIComponent) vm.getChildren().get(i);
            if (comp.getId().equals(formId)) {
                vm.getChildren().remove(i);
                break;
            }
        }
        
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = vm.getClientId();
        
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute("data-title", vm.getTitle(), null);
        String styleClass = ViewManager.STYLECLASS;
        String userClass = vm.getStyleClass();
        if( userClass != null )
            styleClass += " " + userClass;
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, "styleClass");
        String userStyle = vm.getStyle();
        if( userStyle != null )
            writer.writeAttribute(HTML.STYLE_ATTR, userStyle, "style");
        
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, vm.getClientId()+"_header", null);
        writer.writeAttribute(HTML.CLASS_ATTR, "mobi-vm-header", null);
        String headerStyle = vm.getHeaderStyle();
        if( headerStyle != null){
            writer.writeAttribute(HTML.STYLE_ATTR, headerStyle, null);
        }
        writer.startElement(HTML.H1_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_title", "pageTitle");
        if( selected == null || selected.length() == 0 || vm.getSelectedView() == null || ViewManager.MENU_ID.equals(selected))
            writer.write(vm.getTitle());
        else{
            writer.write(vm.getSelectedView().getTitle());
        }
        writer.endElement(HTML.H1_ELEM);
        writer.startElement(HTML.ANCHOR_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_back", null);
        writer.writeAttribute(HTML.CLASS_ATTR, "mobi-vm-back", null);
        writer.writeAttribute(HTML.ONCLICK_ATTR, "ice.mobi.viewManager.goBack(this);", null);
        writer.writeAttribute("data-backbutton-label", vm.getBackButtonLabel(), null);
        writer.writeAttribute(HTML.STYLE_ATTR, "display:none", null);
        writer.endElement(HTML.ANCHOR_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_menu", null);
        writer.writeAttribute("data-view", ViewManager.MENU_ID, null);
        writer.writeAttribute("data-title", vm.getTitle(), null);
        writer.writeAttribute(HTML.CLASS_ATTR, "mobi-vm-view mobi-vm-menu", null);
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "mobi-vm-view-content", null);
        writer.startElement(HTML.UL_ELEM, null);
        List<UIComponent> children = vm.getChildren();
        for( UIComponent child : children ){
            View view = (View)child;
            if( view.isIncludeInMenu() ){
                String divider = view.getDivider();
                if( divider != null ){
                    writer.startElement(HTML.LI_ELEM, null);
                    writer.writeAttribute(HTML.CLASS_ATTR, "mobi-vm-menu-divider", null);
                    writer.write(divider);
                    writer.endElement(HTML.LI_ELEM);
                }
                writer.startElement(HTML.LI_ELEM, null);
                writer.startElement(HTML.ANCHOR_ELEM, null);
                writer.writeAttribute(HTML.ONCLICK_ATTR, "ice.mobi.viewManager.showView('" + view.getId() + "');", null);
                if( view.getMenuIcon() != null ){
                    writer.startElement("i", null);
                    writer.writeAttribute(HTML.CLASS_ATTR, "mobi-vm-menu-icon icon-" + view.getMenuIcon(), null);
                    writer.endElement("i");
                }
                writer.write(view.getTitle());
                writer.endElement(HTML.ANCHOR_ELEM);
                writer.endElement(HTML.LI_ELEM);
            }
        }
        writer.endElement(HTML.UL_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        Utils.renderChildren(facesContext, uiComponent);
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        ViewManager vm = (ViewManager)uiComponent;
        
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, "mobiImageCache", null);
        writer.writeAttribute(HTML.CLASS_ATTR, "mobi-vm-image-cache", null);
        writer.endElement(HTML.DIV_ELEM);
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, uiComponent.getClientId() + "_controller", null);
        
        createAndRenderProxyForm(vm, facesContext, writer);
        
        writer.startElement(HTML.SCRIPT_ELEM, null);
        String state = "ice.mobi.viewManager.setState('" + vm.getTransitionType() 
            + "','" + vm.getId() + "_form'," + vm.getHistoryAsJSON() + ");";
        writer.write(state);
        writer.endElement(HTML.SCRIPT_ELEM);
        
        if( ViewManager.TRANSITION_TYPE_FLIP.equals(vm.getTransitionType()) ){
            writer.startElement(HTML.STYLE_ATTR, null);
            writer.write(".mobi-vm  > div > div:not(.mobi-vm-header) {transform: rotateY(180deg);}");
            writer.endElement(HTML.STYLE_ATTR);
        }
        
        writer.endElement(HTML.SPAN_ELEM);
    	writer.endElement(HTML.DIV_ELEM);
    }
    
    private void createAndRenderProxyForm(ViewManager vm, FacesContext context, ResponseWriter writer)
        throws IOException{
        Application application = context.getApplication();
        HtmlForm form = (HtmlForm)application.createComponent(HtmlForm.COMPONENT_TYPE);
        String formId = vm.getId() + "_form";
        form.setId(formId);
        //form.setTransient(true);
        vm.getChildren().add(form);
        form.encodeBegin(context);
        String viewId = BridgeSetup.getViewID(context.getExternalContext());
        if( viewId != null ){
            writer.startElement(HTML.INPUT_ELEM, null);
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
            writer.writeAttribute(HTML.NAME_ATTR, "ice.view", null);
            writer.writeAttribute(HTML.VALUE_ATTR, viewId, null);
            writer.writeAttribute(HTML.AUTOCOMPLETE_ATTR, "off", null);
            writer.endElement(HTML.INPUT_ELEM);
        }
        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
        writer.writeAttribute(HTML.ID_ATTR, "mobi_vm_selected", null);
        writer.writeAttribute(HTML.NAME_ATTR, vm.getClientId()+"_selected", null);
        writer.writeAttribute(HTML.VALUE_ATTR, vm.getSelected(), null);
        writer.writeAttribute(HTML.AUTOCOMPLETE_ATTR, "off", null);
        writer.endElement(HTML.INPUT_ELEM);
        form.encodeEnd(context);
    }
    
    private void validate(ViewManager vm, FacesContext context){
        //parent must be the <h:body> compoent
        if( ! (vm.getParent() instanceof HtmlBody)){
           throw new IllegalStateException("The viewManager component must be the direct child of the <h:body> tag");
        }
        if( countViewManagers(context.getViewRoot()) > 1){
           throw new IllegalStateException("Only one viewManager is allowed on a page");
        }
    }
    
    private int countViewManagers(UIComponent uic){
        int count = 0;
        if( uic instanceof ViewManager ){
            count++;
        }
        Iterator<UIComponent> iter = uic.getFacetsAndChildren();
        while( iter.hasNext() ){
            UIComponent child = iter.next();
            count += countViewManagers(child);
        }
        return count;
    }
    
    
    
    
    


}