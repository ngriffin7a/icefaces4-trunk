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

package com.icesoft.faces.component.menubar;

import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import javax.faces.el.ValueBinding;
import javax.faces.el.MethodBinding;
import javax.faces.event.ActionListener;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * MenuItems is the JSF component class that represents a heirarchy of ICEfaces
 * MenuItems. <p>This is the submenu component to use if you want to supply a
 * (potentially) dynamic heirarchy of menuItems.
 * <p/>
 * MenuItems extends the ICEfaces MenuItemBase component.
 * <p/>
 * By default this component is rendered by the "com.icesoft.faces.View"
 * renderer type.
 *
 * @author Chris Brown
 * @author gmccleary
 * @version 1.1
 */
@ICEResourceDependencies({
        @ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser= BrowserType.ALL, browserOverride={}),
        @ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class MenuItems extends MenuItemBase {

    private String value;

    /* (non-Javadoc)
    * @see javax.faces.component.UIComponent#getRendererType()
    */
    public String getRendererType() {
        return "com.icesoft.faces.View";
    }

    /* (non-Javadoc)
    * @see javax.faces.component.UIComponent#getFamily()
    */
    public String getFamily() {
        return "com.icesoft.faces.MenuNodes";
    }

    /**
     * A java.util.List of MenuItem objects. Use explicit ids with the MenuItem components you create with for ice:menuItems,
     * and create them in your bean constructor, so that each call to the bean getter method will return the same components.
     * @return java.util.List of MenuItem objects
     */
    public Object getValue() {

        if (value != null) {
            return value;
        }
        ValueBinding vb = getValueBinding("value");
        if (vb != null) {

            return (List) vb.getValue(getFacesContext());
        }
        return null;
    }

    public void setValue(Object value){
        this.value = (String)value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {

        this.value = value;
    }
    
    public List prepareChildren() {
        if (getChildCount() > 0) {
            getChildren().clear();
        }
        List children = (List) getValue();
        if(children != null && children.size() > 0) {
            // extract the actionListener and action methodBindings from the MenuItems
            // then attach them to the child MenuItem objects
            ActionListener[] als = getActionListeners();
            MethodBinding almb = getActionListener();
            MethodBinding amb = getAction();
            setParentsRecursive(this, children, als, almb, amb, true);
        }
        return children;
    }
    
    private void setParentsRecursive(UIComponent parent, List children,
                                     ActionListener[] als,
                                     MethodBinding almb, MethodBinding amb,
                                     boolean reparent) {
        for (int i = 0; i < children.size(); i++) {
            UIComponent nextChild = (UIComponent) children.get(i);
            if( !(nextChild instanceof MenuItemBase) ) {
                continue;
            }
            if (reparent) {
                // If it's the initial lifecycle, then we're just rendering,
                // and the parent is null. If it's a postback, then we do
                // this twice, on decode and on render, so it's either after
                // restore view and the MenuItem components might have last
                // been wired to the previous component tree, or it's render
                // and everything is probably correctly wired up, so this
                // will be a no-op.
                nextChild.setTransient(true);
                UIComponent oldParent = nextChild.getParent();
                if (oldParent != null) {
                    oldParent.getChildren().remove(nextChild);
                }
                nextChild.setParent(null);
                parent.getChildren().add(nextChild);
            }

            // here's where we attach the action and actionlistener methodBindings to the MenuItem
            MenuItemBase nextChildMenuItemBase = (MenuItemBase) nextChild;
            ActionListener[] listeners = nextChildMenuItemBase.getActionListeners();
            if (null != als && listeners.length == 0) {
                for(int j = 0; j < als.length; j++) {
//                    nextChildMenuItemBase.removeActionListener(als[j]);
                    nextChildMenuItemBase.addActionListener(als[j]);
                }
            }
            if (null != almb) {
                nextChildMenuItemBase.setActionListener(almb);
            }
            if (null != amb) {
                nextChildMenuItemBase.setAction(amb);
            }
            
            if (nextChild.getChildCount() > 0) {
                List grandChildren = nextChild.getChildren();
                setParentsRecursive(
                        nextChild, grandChildren, als, almb, amb, false);
            }
        }
    }
    
    public void processDecodes(FacesContext context) {
        if (context == null) {
            throw new NullPointerException("context");
        }
        if (!isRendered()) {
            return;
        }
        
        List list = prepareChildren();
        if(list != null) {
            for (int j = 0; j < list.size(); j++) {
                MenuItemBase item = (MenuItemBase) list.get(j);
                item.processDecodes(context);
            }
        }
        
//        super.processDecodes(context);
    }

    public Object saveState(FacesContext context) {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = value;
        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        value = (String) values[1];
    }
}
