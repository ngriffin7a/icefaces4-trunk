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

import com.icesoft.faces.component.ext.HtmlCommandLink;

import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.util.List;
import java.util.Iterator;


/**
 *
 */
public abstract class MenuItemBase extends UICommand
        implements NamingContainer {

    protected static String DEFAULT_CSS_IMAGE_DIR = "";

    public MenuItemBase() {
    }

    /* (non-Javadoc)
     * @see javax.faces.component.UIComponent#processDecodes(javax.faces.context.FacesContext)
     */
    public void processDecodes(FacesContext context) {
        if (context == null) {
            throw new NullPointerException("context");
        }
        if (!isRendered()) {
            return;
        }

        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processDecodes(context);
        }
        
        try {
            decode(context);
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }
    }

    /* (non-Javadoc)
     * @see javax.faces.component.UIComponent#queueEvent(javax.faces.event.FacesEvent)
     */
    public void queueEvent(FacesEvent event) {
        super.queueEvent(event);
    }

    /* (non-Javadoc)
     * @see javax.faces.component.UIComponent#broadcast(javax.faces.event.FacesEvent)
     */
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);
        return;

    }
    
    boolean isChildrenMenuItem() {
        if(getChildCount() == 0)
            return false;
        Iterator children = getChildren().iterator();
        while (children.hasNext()) {
            UIComponent child = (UIComponent) children.next();
            if (child instanceof MenuItem) {
                return true;
            }
            else if(child instanceof MenuItems) {
                List menuItemsChildren = (List) child.getAttributes().get("value");
                if(menuItemsChildren != null && menuItemsChildren.size() > 0)
                    return true;
            }
        }
        return false;
    }
    
    public boolean invokeOnComponent(FacesContext context, String clientId,
            ContextCallback callback)
        throws FacesException {
        return true;
    }    

}
