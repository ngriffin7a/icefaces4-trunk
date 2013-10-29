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

package com.icesoft.faces.component.menupopup;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ContextActionEvent;
import com.icesoft.faces.component.DisplayEvent;
import com.icesoft.faces.component.menubar.MenuBar;
import com.icesoft.faces.component.ext.taglib.Util;
import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import javax.faces.component.UIComponent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.ActionEvent;

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;;


/**
 * MenuPopup is a JSF component class representing the ICEfaces context menu popup.
 * <p>The menuPopup component extends the menuBar, to provide a menu that is
 * displayed when the user right-mouse clicks, or context-clicks, on another
 * component, which knows to invoke the menuPopup because has specified to do so
 * via its menuPopup attribute, which references the menuPopup's id.
 * <p/>
 * <p>
 * The menuPopup behaves, as is defined, the same as menuBar, except for some
 * minor differences:
 * </p>
 * 1. menuPopup does not have a visible attribute, because that would interfere
 *    with the mechanism for dynamically displaying the popup menu
 * 2. menuPopup does not have an orientation attribute, because it is always
 *    shown vertically
 * 3. menuPopup's default style class is iceMnuPop, instead of menuBar's iceMnuBar
 *
 * @author Mark Collette
 */
@ICEResourceDependencies({
        @ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser= BrowserType.ALL, browserOverride={}),
        @ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class MenuPopup extends MenuBar {
	private Boolean blockMenuOnInput;
	
    private MethodBinding displayListener;
    final static String DISPLAY_LISTENER_ID = "_dynamic"; 
    /**
     * default no args constructor
     */
    public MenuPopup() {
        super();
    }
    
    /**
     * <p>Return the value of the <code>COMPONENT_FAMILY</code> of this
     * component.</p>
     *
     * @return String component family
     */
    public String getFamily() {
        return "com.icesoft.faces.MenuPopup";
    }

    /**
     * @return String component type
     */
    public String getComponentType() {
        return "com.icesoft.faces.MenuPopup";
    }

    /* (non-Javadoc)
     * @see javax.faces.component.UIComponentBase#getRendererType()
     */
    public String getRendererType() {
        return "com.icesoft.faces.View";
    }

    /**
     * <p>Orientation is fixed to vertical</p>
     *
     * @return String orientation
     */
    public String getOrientation() {
        return ORIENTATION_VERTICAL;
    }

    /**
     * <p>Orientation is fixed to vertical</p>
     *
     * @param orient
     */
    public void setOrientation(String orient) {
    }
    
    public String getTopSubMenuStyleClass() {
        return Util.getQualifiedStyleClass(this, 
                                CSS_DEFAULT.MENU_BAR_TOP_SUB_MENU_STYLE);
    }
    
    public String getComponentRootStyle() {
        return CSS_DEFAULT.MENU_POPUP_STYLE;
    }
    
    public void queueEvent(FacesEvent e) {
        if(e instanceof ActionEvent) {
            UIComponent contextTarget = (UIComponent) getAttributes().get("contextTarget");
            Object contextValue = getAttributes().get("contextValue");
//System.out.println("MenuPopup.queueEvent()  e: " + e);
//System.out.println("MenuPopup.queueEvent()  contextTarget: " + contextTarget);
//System.out.println("MenuPopup.queueEvent()  contextValue: " + contextValue);
            getAttributes().remove("contextTarget");
            getAttributes().remove("contextValue");
            FacesEvent tempEvent = new ContextActionEvent(e.getComponent(), contextTarget, contextValue);
            //preserve phaseId
            tempEvent.setPhaseId(e.getPhaseId()); 
            e = tempEvent;
        }
        super.queueEvent(e);
    }
    
    //
    // hideOn attribute
    //
    private String hideOn;
    
    /**
     * <p>Set the value of the <code>hideOn</code> property.</p>
     *
     * @param hideOn
     */
    public void setHideOn(String hideOn) {
        this.hideOn = hideOn;
    }

    /**
     * <p>Return the value of the <code>hideOn</code> property.</p>
     *
     * @return String hideOn
     */
    public String getHideOn() {
        if (hideOn != null) {
            return hideOn;
        }
        ValueBinding vb = getValueBinding("hideOn");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }
    
    public Object saveState(FacesContext context) {
    
        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = hideOn;
        values[2] = saveAttachedState(context, displayListener);    
        values[3] = blockMenuOnInput;        
        return values;
    }
    
    public void restoreState(FacesContext context, Object state) {
    
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        hideOn = (String) values[1];
        displayListener = (MethodBinding)restoreAttachedState(context, values[2]);
        blockMenuOnInput = (Boolean) values[3];;
        
    }
    
    /**
     * <p>Return the value of the <code>displayListener</code> property.</p>
     */
    public MethodBinding getDisplayListener() {
        return displayListener;
    }

    /**
     * <p>Set the value of the <code>displayListener</code> property.</p>
     */
    public void setDisplayListener(MethodBinding displayListener) {
        this.displayListener = displayListener;
   }
    
    public boolean isBlockMenuOnInput() {
        if (blockMenuOnInput != null) {
            return blockMenuOnInput.booleanValue();
        }
        ValueBinding vb = getValueBinding("blockMenuOnInput");
        Boolean boolVal = vb != null ?
                (Boolean) vb.getValue(getFacesContext()) : null;
        return boolVal != null ? boolVal.booleanValue() : false;
	}

	public void setBlockMenuOnInput(boolean blockMenuOnInput) {
		this.blockMenuOnInput = new Boolean(blockMenuOnInput);
	}

	public void broadcast(FacesEvent event)
    throws AbortProcessingException {
        super.broadcast(event);
        if (displayListener != null) {
            Object[] displayEvent = {(DisplayEvent) event};
            displayListener.invoke(getFacesContext(), displayEvent);
        }
    }    
}

