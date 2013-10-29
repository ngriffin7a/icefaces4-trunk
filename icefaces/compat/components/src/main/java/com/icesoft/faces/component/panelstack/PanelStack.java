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

package com.icesoft.faces.component.panelstack;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;
import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.PhaseId;
import java.util.Iterator;


/**
 * Manage a stack of JSF components and allow for one child component to be
 * choosen for rendering. The behaviour is similar to the CardLayout of Java
 * Swing. Property <code>selectedPanel</code> defines the id of the child to be
 * rendered. If no child panel is selected or if the selected panel can not be
 * found the first child is rendered.
 *
 * @version beta 1.0
 */
@ICEResourceDependencies({
        @ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser= BrowserType.ALL, browserOverride={}),
        @ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class PanelStack extends HtmlPanelGroup {

    public static final String COMPONENT_TYPE = "com.icesoft.faces.PanelStack";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    private static final String DEFAULT_RENDERER_TYPE =
            "com.icesoft.faces.PanelStack";
    private String style = null;
    private String selectedPanel = null;
    private String styleClass = null;
    private String renderedOnUserRole = null;

    public PanelStack() {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    /**
     * <p>Return the value of the <code>COMPONENT_FAMILY</code> of this
     * component.</p>
     */
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * <p>Set the value of the <code>style</code> property.</p>
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * <p>Return the value of the <code>style</code> property.</p>
     */
    public String getStyle() {
        if (style != null) {
            return style;
        }
        ValueBinding vb = getValueBinding("style");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>selectedPanel</code> property.</p>
     */
    public void setSelectedPanel(String selectedPanel) {
        this.selectedPanel = selectedPanel;
    }

    /**
     * <p>Return the value of the <code>selectedPanel</code> property.</p>
     */
    public String getSelectedPanel() {
        if (selectedPanel != null) {
            return selectedPanel;
        }
        ValueBinding vb = getValueBinding("selectedPanel");
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
     * <p>Set the value of the <code>styleClass</code> property.</p>
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     */
    public String getStyleClass() {
        return Util.getQualifiedStyleClass(this,
                styleClass,
                CSS_DEFAULT.PANEL_STACK_BASE,
                "styleClass");
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
     * <p>Return the value of the <code>rowClass</code> property.</p>
     */
    public String getRowClass() {
        return Util.getQualifiedStyleClass(this, CSS_DEFAULT.PANEL_STACK_ROW);
    }

    /**
     * <p>Return the value of the <code>columnName</code> property.</p>
     */
    public String getColumnClass() {
        return Util.getQualifiedStyleClass(this, CSS_DEFAULT.PANEL_STACK_COL);
    }

    private transient Object values[];
    /**
     * <p>Gets the state of the instance as a <code>Serializable</code>
     * Object.</p>
     */
    public Object saveState(FacesContext context) {
        if(values == null){
            values = new Object[5];
        }
        values[0] = super.saveState(context);
        values[1] = selectedPanel;
        values[2] = styleClass;
        values[3] = style;
        values[4] = renderedOnUserRole;
        return ((Object) (values));
    }

    /**
     * <p>Perform any processing required to restore the state from the entries
     * in the state Object.</p>
     */
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        selectedPanel = (String) values[1];
        styleClass = (String) values[2];
        style = (String) values[3];
        renderedOnUserRole = (String)values[4];
    }


    public static final String LAST_SELECTED_PANEL = "PanelStack-lastPanel";

    /**
     * @param context
     * @param phaseId
     */
    public void applyPhase(FacesContext context, PhaseId phaseId) {
        if (context == null) {
            throw new NullPointerException("Null context in PanelTabSet");
        }
        Iterator it = getFacetsAndChildren();

        while (it.hasNext()) {
            UIComponent childOrFacet =
                    (UIComponent) it.next();
            String selectedPanel = getSelectedPanel();
            String lastSelectedPanel = (String) context.getExternalContext().getRequestMap().get(LAST_SELECTED_PANEL + getClientId(context));
            boolean changed = lastSelectedPanel != null && !lastSelectedPanel.equals(selectedPanel);
            if (childOrFacet.getId().equals(selectedPanel)) {
                if (!(changed && phaseId == PhaseId.APPLY_REQUEST_VALUES)) {
                    applyPhase(context, childOrFacet, phaseId);
                }
            }
        }

    }

    /**
     * @param context
     * @param component
     * @param phaseId
     */
    public void applyPhase(FacesContext context, UIComponent component,
                           PhaseId phaseId) {
        if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
            component.processDecodes(context);
        } else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
            component.processValidators(context);
        } else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
            component.processUpdates(context);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /* (non-Javadoc)
    * @see javax.faces.component.UIComponent#processDecodes(javax.faces.context.FacesContext)
    */
    public void processDecodes(javax.faces.context.FacesContext context) {

        if (context == null) {
            throw new NullPointerException("context");
        }

        if (!isRendered()) {
            return;
        }

        decode(context);
        applyPhase(context, PhaseId.APPLY_REQUEST_VALUES);
    }

    /* (non-Javadoc)
    * @see javax.faces.component.UIComponent#processValidators(javax.faces.context.FacesContext)
    */
    public void processValidators(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!isRendered()) {
            return;
        }
        applyPhase(context, PhaseId.PROCESS_VALIDATIONS);
    }


    /* (non-Javadoc)
    * @see javax.faces.component.UIComponent#processUpdates(javax.faces.context.FacesContext)
    */
    public void processUpdates(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!isRendered()) {
            return;
        }
        applyPhase(context, PhaseId.UPDATE_MODEL_VALUES);
    }

}
