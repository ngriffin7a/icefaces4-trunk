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

/*
 * Generated, Do Not Modify
 */

package org.icefaces.ace.component.column;

import javax.faces.context.FacesContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import java.io.Serializable;

import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.model.filter.*;
import org.icefaces.ace.component.celleditor.CellEditor;
import org.icefaces.ace.model.table.RowStateMap;

public class Column extends ColumnBase implements IProxiableColumn, Serializable {
	private static final String OPTIMIZED_PACKAGE = "org.icefaces.ace.component.";
    private int currGroupLength;
    // Toggled to true appropriately before first group rendering
    private boolean oddGroup = false;
    
    public Column() {
		setRendererType(null);
	}

    private CellEditor cellEditor = null;
    private FilterConstraint filterConstraint = null;
    
    private final static String STARTS_WITH_MATCH_MODE = "startsWith";
    private final static String ENDS_WITH_MATCH_MODE = "endsWith";
    private final static String CONTAINS_MATCH_MODE = "contains";
    private final static String EXACT_MATCH_MODE = "exact";

    public FilterConstraint getFilterConstraint() {
        String filterMatchMode = getFilterMatchMode();

        if(filterConstraint == null) {
            if(filterMatchMode.equals(STARTS_WITH_MATCH_MODE)) {
                filterConstraint = new StartsWithFilterConstraint();
            } else if(filterMatchMode.equals(ENDS_WITH_MATCH_MODE)) {
                filterConstraint = new EndsWithFilterConstraint();
            } else if(filterMatchMode.equals(CONTAINS_MATCH_MODE)) {
                filterConstraint = new ContainsFilterConstraint();
            } else if(filterMatchMode.equals(EXACT_MATCH_MODE)) {
                filterConstraint = new ExactFilterConstraint();
            }
        }

        return filterConstraint;
    }

	protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

    public CellEditor getCellEditor() {
        if (cellEditor != null) return cellEditor;

        for (UIComponent child : getChildren()) {
            if (child instanceof CellEditor) {
                cellEditor = (CellEditor)child;
                return cellEditor;
            }
        }

        return null;
    }

    public boolean hasCellEditor() {
        return getCellEditor() != null;
    }

    @Override
    public void setSortPriority(Integer i) {
        DataTable table = findParentTable();
        if (table != null) table.applySorting();
        super.setSortPriority(i);
    }

    public DataTable findParentTable() {
        UIComponent parent = getParent();

        while(parent != null)
            if (parent instanceof DataTable) return (DataTable) parent;
            else parent = parent.getParent();

        return null;
    }

    public int getCurrGroupLength() {
        return currGroupLength;
    }

    public void setCurrGroupLength(int currGroupLength) {
        this.currGroupLength = currGroupLength;
    }

    public boolean isOddGroup() {
        return oddGroup;
    }

    public void setOddGroup(boolean oddGroup) {
        this.oddGroup = oddGroup;
    }

    @Override
    public java.lang.Object getSortBy() {
        Object retVal = super.getSortBy();
        if (retVal == null && super.isSortWhenGrouping()) return super.getGroupBy();
        else return retVal;
    }

    @Override
    public ValueExpression getValueExpression(String name) {
        ValueExpression retVal = super.getValueExpression(name);
        if (retVal == null && name.equals("sortBy") && super.isSortWhenGrouping())
            return super.getValueExpression("groupBy");
        else
            return retVal;
    }

    public boolean hasSortPriority() {
        Integer pri = getSortPriority();
        return (pri != null && pri > 0);
    }

    /**
     * This works around the fact that rendered is not an ACE generated property,
     * but comes from UIComponent, so is not writable to EL from the setter.
     */
    public void updateRendered(boolean rendered) {
        ValueExpression valueExpression = getValueExpression("rendered");
        if (valueExpression != null) {
            valueExpression.setValue(
                FacesContext.getCurrentInstance().getELContext(), rendered);
        } else {
            setRendered(rendered);
        }
    }

    /**
     * The AutoAdjustRenderedColspan code should not call body column
     * updateRendered if their rendered property has been set to anything.
     * We can't determine if set with non-EL, but if it's false, then it's not
     * the default value, so was set. And if it was defaulted or intentionally
     * set to true, then we're probably only unrendering a column if it's
     * necessary. We just really want to avoid rendering columns specifically
     * set not to.
     */
    public boolean isLikelySpecifiedRendered() {
        return (getValueExpression("rendered") != null || !isRendered());
    }

    public boolean isPropertySet(String finder) {
        return super.isPropertySet(finder);
    }
}
