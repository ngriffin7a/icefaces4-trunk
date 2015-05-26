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


import org.icefaces.ace.util.Utils;
import org.icefaces.ace.api.StackPaneController;

import javax.el.ValueExpression;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@SuppressWarnings({"rawtypes","unchecked"})
public class StackPane extends StackPaneBase{
    
    private static final Logger logger =
            Logger.getLogger(StackPane.class.toString());
    
    public static final String CONTENT_SELECTED = "ace-contentpane ";
    public static final String CONTENT_HIDDEN = "ace-contentpane-hidden ";

    
    private transient Boolean first;

    @Override
    public void setClient(boolean client) {
        ValueExpression ve = getValueExpression(PropertyKeys.client.name() );
        if (ve != null) {
            // map of style values per clientId
            ve.setValue(getFacesContext().getELContext(), client );
        } else {
            StateHelper sh = getStateHelper();
            if (isDisconnected(this))  {
                String defaultKey = PropertyKeys.client.name() + "_defaultValues";
                Map clientDefaults = (Map) sh.get(defaultKey);
                if (clientDefaults == null) {
                    clientDefaults = new HashMap();
                    clientDefaults.put("defValue",client);
                    sh.put(defaultKey, clientDefaults);
                }
            }
        }
    }

    @Override
    public boolean isClient() {
        Boolean retVal = false;
        ValueExpression ve = getValueExpression( PropertyKeys.client.name() );
        if (ve != null) {
            Object o = ve.getValue( getFacesContext().getELContext() );
            if (o != null) {
                retVal = (Boolean) o;
            }
        } else {
            StateHelper sh = getStateHelper();
            String defaultKey = PropertyKeys.client.name() + "_defaultValues";
            Map defaultValues = (Map) sh.get(defaultKey);
            if (defaultValues != null) {
                if (defaultValues.containsKey("defValue" )) {
                    retVal = (Boolean) defaultValues.get("defValue");
                }
            }
        }
        return retVal;
    }
	private static boolean isDisconnected(UIComponent component) {
		UIComponent parent = component.getParent();
		if (parent != null && parent instanceof UIViewRoot) {
			return false;
		} else if (parent != null) {
			return isDisconnected(parent);
		} else {
			return true;
		}
	}

    public boolean isFirstPane(){
        if( first == null ){
            List<UIComponent> children = this.getParent().getChildren();
            if( children != null && children.size() > 0 && children.get(0).equals(this)){
                first = Boolean.TRUE;
            }
            else{
                first = Boolean.FALSE;
            }
        }
        return first.booleanValue();
    }
    
    public boolean isSelected(){
        UIComponent parent = this.getParent();
        while( !(parent instanceof StackPaneController) && parent != null ){
            parent = parent.getParent();
        }
        
        String selectedId= null;
        if (parent != null){
            StackPaneController paneController = (StackPaneController)parent;
            selectedId = paneController.getSelectedId();
            
            if (null == selectedId){
                UIComponent pComp = (UIComponent)parent;
                logger.warning("Parent controller of stackPane must have value for selectedId="+pComp.getClientId());
                return false;
            }
        }
        else {
            logger.warning("StackPane must be nested within a PanelStack: " + this.getClientId());
            return false;
        }
        String id = this.getId();
        boolean result = false;
        if( id != null ){
            result = id.equals(selectedId);
        }
        return result;
    }

    @Override
    public String toString() {
        return "StackPane [first=" + first + ", getId()=" + getId() + "]";
    }
    
  }
