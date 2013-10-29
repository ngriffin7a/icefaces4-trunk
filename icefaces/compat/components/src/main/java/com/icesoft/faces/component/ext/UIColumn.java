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

package com.icesoft.faces.component.ext;

import com.icesoft.faces.component.ext.taglib.Util;
import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.w3c.dom.Element;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

// We do not need this class, but Sun Studio Creator requires it.

@ICEResourceDependencies({
	@ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={}),
	@ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class UIColumn extends javax.faces.component.UIColumn {
    public static final String COMPONENT_TYPE = "com.icesoft.faces.Column";


    /**
     * <p>Return the family for this component.</p>
     */
    public String getFamily() {
        return "javax.faces.Column";
    }
    private String colspan = null;
    private String rowspan = null;
    private String style = null;
    private String styleClass = null;
    private String groupOn = null;
    
    // Temporary state, within a single render, for grouping
    private transient String previousGroupValue = null;
    private transient int groupCount;
    private transient Element groupedTd;
    
    // binding
    private String binding = null;

    public String getBinding() {
        return this.binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    // id
    private String id = null;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }


    private String renderedOnUserRole = null;
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
     * <p>Restore the state of this component.</p>
     */
    public void restoreState(FacesContext _context, Object _state) {
        Object _values[] = (Object[]) _state;
        super.restoreState(_context, _values[0]);
        this.binding = (String) _values[1];
        this.id = (String) _values[2];
        this.renderedOnUserRole = (String) _values[3];
        this.groupOn = (String)_values[4];
        this.style = (String)_values[5];
        this.rowspan = (String)_values[6];
        this.styleClass = (String)_values[7];
        this.colspan = (String)_values[8];
    }

    /**
     * <p>Save the state of this component.</p>
     */
    public Object saveState(FacesContext _context) {
        Object _values[] = new Object[9];
        _values[0] = super.saveState(_context);
        _values[1] = this.binding;
        _values[2] = this.id;
        _values[3] = this.renderedOnUserRole;
        _values[4] = this.groupOn;
        _values[5] = this.style;
        _values[6] = this.rowspan;
        _values[7] = this.styleClass;
        _values[8] = this.colspan;
        return _values;
    }

    /**
     * <p>Set the value of the <code>colspan</code> property.</p>
     */
    public void setColspan(String colspan) {
        this.colspan = colspan;
    }

    /**
     * <p>Return the value of the <code>colspan</code> property.</p>
     */
    public String getColspan() {
        if (colspan != null) {
            return colspan;
        }
        ValueBinding vb = getValueBinding("colspan");
        return vb != null ? (String) vb.getValue(getFacesContext()) :null;
    }
    
    /**
     * <p>Set the value of the <code>rowspan</code> property.</p>
     */
    public void setRowspan(String rowspan) {
        this.rowspan = rowspan;
    }

    /**
     * <p>Return the value of the <code>rowspan</code> property.</p>
     */
    public String getRowspan() {
        if (rowspan != null) {
            return rowspan;
        }
        ValueBinding vb = getValueBinding("rowspan");
        return vb != null ? (String) vb.getValue(getFacesContext()) :null;
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
     * <p>Set the value of the <code>styleClass</code> property.</p>
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     */
    public String getStyleClass() {
        if (styleClass != null) {
            return styleClass;
        }
        ValueBinding vb = getValueBinding("styleClass");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }    
    
    /**
     * <p>Set the value of the <code>groupOn</code> property.</p>
     */
    public void setGroupOn(String groupOn) {
        this.groupOn = groupOn;
    }

    /**
     * <p>Return the value of the <code>groupOn</code> property.</p>
     */
    public String getGroupOn() {
        if (groupOn != null) {
            return groupOn;
        }
        ValueBinding vb = getValueBinding("groupOn");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }  
    
    public boolean groupFound() {
        if (previousGroupValue == null) {
            previousGroupValue = getGroupOn();
            return false;
        }
        boolean result = previousGroupValue.equals(getGroupOn()); 
        previousGroupValue = getGroupOn();
        if (result) {
            groupCount++;
        } else {
            groupCount = 1;
        }
        return result;
    }
    
    public int getGroupCount() {
        return groupCount;
    }

    public Element getGroupedTd() {
        return groupedTd;
    }

    public void setGroupedTd(Element groupedTd) {
        this.groupedTd = groupedTd;
    }
    
    public void resetGroupState() {
        previousGroupValue = null;
        groupCount = 1;
    }
}
