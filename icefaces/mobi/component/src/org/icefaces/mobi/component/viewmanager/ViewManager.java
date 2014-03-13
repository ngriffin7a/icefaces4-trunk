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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;

@ListenerFor(systemEventClass = javax.faces.event.PostAddToViewEvent.class)
public class ViewManager extends ViewManagerBase {
	
    private static final Logger LOG = Logger.getLogger(ViewManager.class.getName());
	
    public static final String STYLECLASS = "mobi-vm";
    public static final String MENU_ID = "mobiViewManagerMenu";
    public static final String TRANSITION_TYPE_VERTICAL = "vertical";
    public static final String TRANSITION_TYPE_HORIZONTAL = "horizontal";
    public static final String TRANSITION_TYPE_FLIP = "flip";
    public static final String TRANSITION_TYPE_FADE = "fade";
    public static final String TRANSITION_TYPE_PAGETURN = "pageturn";
    private static List<String> TRANSITION_TYPES = new ArrayList<String>();
    static{
        TRANSITION_TYPES.add(TRANSITION_TYPE_HORIZONTAL);
        TRANSITION_TYPES.add(TRANSITION_TYPE_VERTICAL);
        TRANSITION_TYPES.add(TRANSITION_TYPE_FLIP);
        TRANSITION_TYPES.add(TRANSITION_TYPE_FADE);
        TRANSITION_TYPES.add(TRANSITION_TYPE_PAGETURN);
    }
    
    public boolean isCanGoBack(){
       return getHistory().size() > 1;
    }

    public void goBack(AjaxBehaviorEvent evt){
        if( getHistory().size() > 1 ){
            String prev = getHistoryStack().pop();
            if( prev != null ){
               setSelected(prev);
            }
        }
    }
   
    public void setSelected(String selected){
        super.setSelected(selected);
        Stack<String> history = getHistory();
        if( history.size() == 1 && history.get(0).equals(selected)){
            return;
        }
        if( history.size() > 1 && getHistory().contains(selected)){
            Stack<String> newHistory = new Stack<String>();
            newHistory.addAll(getHistory().subList(0, getHistory().indexOf(selected)+1));
            setHistory(newHistory);
        }
        else {
            getHistory().push(selected);
        }
    }
    
    public String getSelected(){
        String selected = super.getSelected();
        if( selected == null || selected.length() == 0 ){
            selected = ViewManager.MENU_ID;
            super.setSelected(selected);
        }
        return selected;
    }
   
    public void setTransitionType(String transitionType){
        if( !TRANSITION_TYPES.contains(transitionType)){
            LOG.warning("invalid transitionType '" + transitionType 
                + "', reverting to default '" + TRANSITION_TYPE_HORIZONTAL + "'");
            transitionType = TRANSITION_TYPE_HORIZONTAL;
        }
        super.setTransitionType(transitionType);
    }
    
    public void processEvent(ComponentSystemEvent event)
        throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        String view = context.getExternalContext().getRequestParameterMap()
            .get(this.getClientId()+"_selected");
        if( view != null && !view.equals(this.getSelected()) ){
            this.setSelected(view);
        }
    }
    
    public View getSelectedView(){
        String selected = getSelected();
        if( selected == null || selected.length() == 0 ){
            return null;
        }
        return getView(selected);
    }
    
    public View getView(String id){
        List<UIComponent> children = this.getChildren();
        for( UIComponent child : children ){
            View view = (View)child;
            if( view.getId().equals(id))
                return view;
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public Stack<String> getHistory(){
        Stack<String> history = super.getHistory();
        if( history == null || history.size() == 0 ){
            history = new Stack<String>();
            history.add(getSelected());
            setHistory(history);
        }
        return history;
    }
    
    public String getHistoryAsJSON(){
        String h = "[";
        for( int i = 0 ; i < getHistory().size() ; i++ ){
            String v = getHistoryStack().get(i);
            h += "'" + v + "'";
            if( i < getHistory().size() -1 )
                h += ",";
        }
        h += "]";
        return h;
    }
    
    public Stack<String> getHistoryStack(){
        return (Stack<String>)getHistory();
    }
    
    


}
