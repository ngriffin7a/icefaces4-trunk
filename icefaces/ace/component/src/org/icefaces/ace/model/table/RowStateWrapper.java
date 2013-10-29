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

package org.icefaces.ace.model.table;

import org.icefaces.ace.component.datatable.DataTable;

import java.util.ArrayList;
import java.util.List;

public class RowStateWrapper {
    Object row;

    // Type of row, used for per-row-class column rendering
    String type = "default";
    List<RowStateWrapper> children = new ArrayList<RowStateWrapper>();
    RowStateWrapper parent = null;
    DataTable table = null;
    boolean selected = false;
    boolean selectable = false;

    boolean expanded = false;
    boolean expandable = false;

    enum ExpansionType { PANEL, ROW }
    ExpansionType expansionType = ExpansionType.PANEL;

    boolean editable = false;
    boolean visible = true;


    public RowStateWrapper() {}
    public RowStateWrapper(Object row, DataTable table) {
        this.row = row;
        this.table = table;
    }


    public Object getRow() {
        return row;
    }
    public void setRow(Object row) {
        this.row = row;
    }
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean isExpanded() {
        return expanded;
    }
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
    public boolean isEditable() {
        return editable;
    }
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public List<RowStateWrapper> getChildren() {
        return children;
    }
    public void addChild(RowStateWrapper child) {
        children.add(child);
        child.setParent(this);
    }
    public RowStateWrapper getParent() {
        return parent;
    }
    public void setParent(RowStateWrapper rowState) {
        parent = rowState;
    }
    public int getChildCount() {
        if (children == null) return 0;
        return children.size();
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public boolean isSelectable() {
        return selectable;
    }
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }
    public boolean isExpandable() {
        return expandable;
    }
    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }
    public ExpansionType getExpansionType() {
        return expansionType;
    }
    public void setExpansionType(ExpansionType expansionType) {
        this.expansionType = expansionType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((row == null) ? 0 : row.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;

        RowStateWrapper other = (RowStateWrapper)obj;
        if (row == null) { if (other.row != null) return false; }
        else if (!row.equals(other.row)) return false;
        return true;
    }

    @Override
    public String toString() {
        if (row != null) return row.toString();
        return super.toString();
    }
}
