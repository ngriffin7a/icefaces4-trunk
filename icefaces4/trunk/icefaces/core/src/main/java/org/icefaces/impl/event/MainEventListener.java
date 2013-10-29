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

package org.icefaces.impl.event;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UICommand;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlPanelGroup;

import org.icefaces.util.EnvUtils;

public class MainEventListener implements SystemEventListener  {
    private static String RENDER_STARTED = 
            MainEventListener.class.getName() + "-RENDER_STARTED"; 

    public MainEventListener()  {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (EnvUtils.isAutoId(facesContext))  {
            facesContext.getApplication()
                .subscribeToEvent(PostAddToViewEvent.class, this);
            facesContext.getApplication()
                .subscribeToEvent(PreRenderViewEvent.class, this);
        }
        Application app = facesContext.getApplication();

        AjaxDisabledList disabledList = new AjaxDisabledList();
        app.subscribeToEvent(PreRenderComponentEvent.class, disabledList);

        if(EnvUtils.isMyFaces()){
            CommandLinkModifier clMod = new CommandLinkModifier();
            app.subscribeToEvent(PreRenderComponentEvent.class, clMod);
        }
    }

    public void processEvent(SystemEvent event)  {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(facesContext)) {
            return;
        }
        if (event instanceof PreRenderViewEvent)  {
            facesContext.getAttributes().put(RENDER_STARTED, RENDER_STARTED);
            return;
        }
        if (null != facesContext.getAttributes().get(RENDER_STARTED))  {
            //do not modify component IDs after rendering has begun
            return;
        }
        UIComponent component = ((PostAddToViewEvent)event).getComponent();
        String id = component.getId();
        if (null == id)  {
            return;
        }
        if (id.startsWith("j_id") && (shouldModifyId(component)))  {
            id = "_" + id.substring(4);
            component.setId(id);
            component.getAttributes().put("id", id);
        }
    }

    public boolean isListenerForSource(Object source)  {
        if (source instanceof UIViewRoot)  {
            return true;
        }
        return shouldModifyId(source);
    }

    public boolean shouldModifyId(Object source)  {
        //Existing ice: components already output ids
        if (source.getClass().getName().startsWith("com.icesoft"))  {
            return false;
        }
        //body is already a special case
        if (source.getClass().getName().equals(
                "javax.faces.component.html.HtmlBody"))  {
            return false;
        }
        boolean classCheck = (  (
            (source instanceof UIOutput) || 
            (source instanceof HtmlDataTable) || 
            (source instanceof HtmlPanelGroup) || 
            (source instanceof UICommand) ) && (!UIOutput.class.equals(source.getClass())) );
        return classCheck;
    }
}