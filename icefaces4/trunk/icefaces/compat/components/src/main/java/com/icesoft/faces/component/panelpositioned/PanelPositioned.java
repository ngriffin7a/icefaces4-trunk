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

package com.icesoft.faces.component.panelpositioned;

import java.util.Arrays;
import java.util.List;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.dragdrop.DndEvent;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.component.panelseries.UISeries;
import com.icesoft.faces.context.effects.JavascriptContext;
import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

/**
 * Panel Positioned Componenet class
 */
@ICEResourceDependencies({
        @ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser= BrowserType.ALL, browserOverride={}),
        @ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class PanelPositioned extends UISeries {

    public static final String COMPONENT_TYPE =
            "com.icesoft.faces.dragdrop.PanelPositioned";
    public static final String DEFAULT_RENDERER_TYPE =
            "com.icesoft.faces.dragdrop.PanelPositionedRenderer";
    public static final String COMPONENT_FAMILY =
            "com.icesoft.faces.dragdrop.PanelPositionedFamily";

    private String styleClass;
    private String style;
    private MethodBinding listener;
    private MethodBinding beforeChangedListener;    
    private String overlap;
    private String constraint;
    private String handle;
    private String hoverclass;
    private boolean disabled = false;
    private boolean disabledSet = false;
    private String enabledOnUserRole = null;
    private String renderedOnUserRole = null;    


    public PanelPositioned() {
        super();
        setRendererType(DEFAULT_RENDERER_TYPE);
        JavascriptContext.includeLib(JavascriptContext.ICE_EXTRAS,
                                     FacesContext.getCurrentInstance());
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }


    public String getRendererType() {
        return DEFAULT_RENDERER_TYPE;
    }

    public String getStyleClass() {
        return Util.getQualifiedStyleClass(this, 
                                styleClass,
                                CSS_DEFAULT.POSITIONED_PANEL_DEFAULT_CLASS,
                                "styleClass", 
                                isDisabled());
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyle() {
        if (style != null) {
            return style;
        }
        ValueBinding vb = getValueBinding("style");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public MethodBinding getListener() {
        return listener;
    }

    public void setListener(MethodBinding listener) {
        this.listener = listener;
    }

    /**
     *  @deprecated
     */
    public String getOverlap() {
        if (overlap != null) {
            return overlap;
        }
        ValueBinding vb = getValueBinding("overlap");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * Either 'vertical' or 'horizontal'. For floating sortables or horizontal
     * lists, choose 'horizontal'. Vertical lists should use 'vertical'.
     * @deprecated
     * @param overlap
     */
    public void setOverlap(String overlap) {
        this.overlap = overlap;
    }

    public String getConstraint() {
        if (constraint != null) {
            return constraint;
        }
        ValueBinding vb = getValueBinding("constraint");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * If set to 'horizontal' or 'vertical' the drag will be constrained to take
     * place only horizontally or vertically.
     *
     * @param constraint
     */
    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

    public String getHandle() {
        if (handle != null) {
            return handle;
        }
        ValueBinding vb = getValueBinding("handle");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * The handle used to drag. The first child/grandchild/etc. element found
     * within the element that has this CSS class value will be used as the
     * handle.
     *
     * @param handle
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     *  @deprecated
     */
    public String getHoverclass() {
        if (hoverclass != null) {
            return hoverclass;
        }
        ValueBinding vb = getValueBinding("hoverclass");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * If set, the Droppable will have this additional CSS class when an
     * accepted Draggable is hovered over it.
     *  @deprecated
     * @param hoverclass
     */
    public void setHoverclass(String hoverclass) {
        this.hoverclass = hoverclass;
    }


    public void broadcast(FacesEvent event)
            throws AbortProcessingException {


        if (event instanceof PanelPositionedEvent) {
            try {

                PanelPositionedEvent de = (PanelPositionedEvent) event;
                MethodBinding beforeListener = de.getBeforeChangedListener();
                if (beforeListener!= null){
                    Object[] oa = {de};
                    beforeListener.invoke(FacesContext.getCurrentInstance(), oa);    
                }
                if (((PanelPositionedEvent)event).isCanceled()) {
                	return;
                }
                de.process(); // Copy over the list values now
                MethodBinding mb = de.getListener();
                if (mb == null) return;
                Object[] oa = {de};
                mb.invoke(FacesContext.getCurrentInstance(), oa);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.broadcast(event);
    }

    /* (non-Javadoc)
     * @see javax.faces.component.UIComponent#queueEvent(javax.faces.event.FacesEvent)
     */
    public void queueEvent(FacesEvent event) {
        if (event instanceof DndEvent) {

            event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        }
        super.queueEvent(event);
    }

    public Object saveState(FacesContext context) {
        Object[] values = new Object[13];
        values[0] = super.saveState(context);
        values[1] = styleClass;
        values[2] = style;
        values[3] = saveAttachedState(context, listener);
        values[4] = hoverclass;
        values[5] = handle;
        values[6] = renderedOnUserRole;
        values[7] = constraint;
        values[8] = enabledOnUserRole;
        values[9] = overlap;
        values[10] = disabled ? Boolean.TRUE : Boolean.FALSE;
        values[11] = disabledSet ? Boolean.TRUE : Boolean.FALSE;
        values[12] = saveAttachedState(context, beforeChangedListener);
        return values;
    }

    public void restoreState(FacesContext context, Object stateIn) {
        Object[] state = (Object[]) stateIn;
        super.restoreState(context, state[0]);
        styleClass = (String) state[1];
        style = (String) state[2];
        listener = (MethodBinding) restoreAttachedState(context, state[3]);
        hoverclass =(String)state[4];
        handle = (String)state[5];
        renderedOnUserRole =(String)state[6];
        constraint = (String)state[7];
        enabledOnUserRole = (String)state[8];
        overlap = (String)state[9];
        disabled = ((Boolean) state[10]).booleanValue();
        disabledSet = ((Boolean) state[11]).booleanValue();     
        beforeChangedListener = (MethodBinding) restoreAttachedState(context, state[12]);        
    }

    //Array type support added to the component
    public Object getValueAsList() {
        Object value = getValue();
        if (value instanceof Object[]) {
            return (Object)Arrays.asList((Object[]) value);
        } else if (value instanceof List) {
            return value;
        } 
        return null;
    }
    

    //if the array type is being used, then update the backing bean if exist    
    public void setArrayValue(Object[] obj) {
        try {
            ValueBinding vb = getValueBinding("value");
            if (vb != null) {
                vb.setValue(getFacesContext(), obj);
            }
        } catch (Exception e) {}
    }
    
    
    /**
     * @param disabled
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        this.disabledSet = true;
    }

    /**
     * @return the value of disabled
     */
    public boolean isDisabled() {
        if (!Util.isEnabledOnUserRole(this)) {
            return true;
        }
        if (disabledSet) {
            return disabled;
        }
        ValueBinding vb = getValueBinding("disabled");
        Boolean v =
                vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }
    
    /**
     * <p>Set the value of the <code>enabledOnUserRole</code> property.</p>
     */
    public void setEnabledOnUserRole(String enabledOnUserRole) {
        this.enabledOnUserRole = enabledOnUserRole;
    }

    /**
     * <p>Return the value of the <code>enabledOnUserRole</code> property.</p>
     */
    public String getEnabledOnUserRole() {
        if (enabledOnUserRole != null) {
            return enabledOnUserRole;
        }
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public void setRenderedOnUserRole(String renderedOnUserRole) {
        this.renderedOnUserRole = renderedOnUserRole;
    }

    /**
     * <p>Return the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public String getRenderedOnUserRole() {
        if (renderedOnUserRole != null) {
            return renderedOnUserRole;
        }
        ValueBinding vb = getValueBinding("renderedOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Return the value of the <code>rendered</code> property.</p>
     */
    public boolean isRendered() {
        if (!Util.isRenderedOnUserRole(this)) {
            return false;
        }
        return super.isRendered();
    }    
    
    
    /**
     *  @deprecated
     */
    public int getFirst() {
        return super.getFirst();
    }
    
    /**
     *  @deprecated
     */    
    public void setFirst(int first) {
        super.setFirst(first);
    }
    
    /**
     *  @deprecated
     */    
    public int getRows() {
        return super.getRows();
    }

    /**
     *  @deprecated
     */
    public void setRows(int rows) {
        super.setRows(rows);
    }
    
    public MethodBinding getBeforeChangedListener() {
        return beforeChangedListener;
    }

    public void setBeforeChangedListener(MethodBinding beforeChangedListener) {
        this.beforeChangedListener = beforeChangedListener;
    }    
}
