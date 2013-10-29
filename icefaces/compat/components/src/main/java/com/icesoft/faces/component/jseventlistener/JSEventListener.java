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

package com.icesoft.faces.component.jseventlistener;

import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;
import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

@ICEResourceDependencies({
	@ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={}),
	@ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class JSEventListener extends UICommand{
    public static final String COMPONENT_TYPE =
        "com.icesoft.faces.JSEventListener";
    public static final String RENDERER_TYPE = "com.icesoft.faces.JSEventListenerRenderer";
    private String events;
    private String handler;
    private String style;
    private String styleClass;
    
    public JSEventListener() {
        setRendererType(RENDERER_TYPE);
    }
    
    public String getRendererType() {
        return RENDERER_TYPE;
    }
    
    public String getComponentType() {
        return COMPONENT_TYPE;
    }
    
    public boolean getRendersChildren() {
        return true;
    } 
    
    public void setEvents(String events) {
        this.events = events;
    }
    
    public String getEvents() {
        if (events != null) {
            return events;
        }
        ValueBinding vb = getValueBinding("events");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    public String getHandler() {
        if (handler != null) {
            return handler;
        }
        ValueBinding vb = getValueBinding("handler");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    } 
    
    /**
     * <p>Set the value of the <code>style</code> property.</p>
     *
     * @param style
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * <p>Return the value of the <code>style</code> property.</p>
     *
     * @return String style
     */
    public String getStyle() {
        if (style != null) {
            return style;
        }
        ValueBinding vb = getValueBinding("style");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }    
    
    /**
     * <p>Set the value of the <code>styleClass</code> property.</p>
     *
     * @param styleClass
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     *
     * @return String styleClass
     */
    public String getStyleClass() {
        return Util.getQualifiedStyleClass(this, 
                styleClass,
                CSS_DEFAULT.JSEVENT_LISTENER_DEFAULT_STYLE_CLASS,
                "styleClass");
                                             
    }    
    
    public Object saveState(FacesContext context) {
        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        values[1] = events;
        values[2] = handler;
        values[3] = style;
        values[4] = styleClass;        
        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        events = (String) values[1];
        handler = (String) values[2];
        style = (String) values[3];
        styleClass = (String) values[4];        
    }    
}
