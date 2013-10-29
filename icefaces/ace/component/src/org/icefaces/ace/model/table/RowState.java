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

import javax.faces.component.UIComponent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the view state of an individual row object within an ace:dataTable.
 * Associated with row objects by entries in a RowStateMap where the keys are row
 * objects and the values are instances of this class.
 */
public class RowState implements Serializable {
    public enum ExpansionType { PANEL, ROW, NONE }
    ExpansionType expansionType = ExpansionType.PANEL;

    // Type of row, used for per-row-class column rendering
    String type = "default";

    boolean selected = false;
    boolean selectable = true;
    boolean expanded = false;
    boolean expandable = true;
    boolean editable = true;
    boolean visible = true;
    List<String> activeCellEditorIds;
    List<String> selectedColumnIds;


    public RowState() {}

    /**
     * Get the expansion mode defined for this row.
     * @return ExpansionType enum
     */
    public ExpansionType getExpansionType() {
        return expansionType;
    }

    /**
     * Set the expansion mode (row, panel or none) for this row.
     * @param expansionType expansion mode enum
     */
    public void setExpansionType(ExpansionType expansionType) {
        this.expansionType = expansionType;
    }

    /**
     * Get the String defining the set of columns rendered by this row. Column
     * sets are defined by ColumnGroup components, whose type attributes are
     * matched to this field to determine which column set is rendered. The String
     * "default" matches all Columns outside of ColumnGroups. Not currently
     * implemented, all values behave like "default".
     *
     * @return column set name
     */
    public String getType() {
        return type;
    }


    /**
     * Set the String defining the set of columns rendered by this row. Column
     * sets are defined by ColumnGroup components, whose type attributes are
     * matched to this field to determine which column set is rendered. The String
     * "default" matches all Columns outside of ColumnGroups. Not currently
     * implemented, all values behave like "default".
     *
     * @param type column set name
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the selected state of the row.
     * @return if the row is selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Set the selected state of the row.
     * @param selected is the row selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Get the selectable state of the row.
     * @return if the row or child columns can be selected
     */
    public boolean isSelectable() {
        return selectable;
    }

    /**
     * Set the selectable state of the row.
     * @param selectable if the row or child columns can be selected
     */
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    /**
     * Get the expanded state of the row.
     * @return is the row expanded
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     * Set the expanded state of the row.
     * @param expanded is the row expanded
     */
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /**
     * Get the expandibility of the row.
     * @return is the row expandible
     */
    public boolean isExpandable() {
        return expandable;
    }

    /**
     * Set the expandibility of the row.
     * @param expandable is the row expandible
     */
    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    /**
     * Get the editibility of the row.
     * @return is the row editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Set the editibility of the row.
     * @param editable is the row editable
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Get if the row is visible.
     * @return is the row visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Set if the row is visible.
      * @param visible is the row visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Get the backing List of Strings containing the Ids of the CellEditor components active in this row. The reference returned is not to a List copy but to the actual list defining the selection state.
     * @return the List<String> of UIComponent Ids defining the active CellEditor components
     */
    public List<String> getActiveCellEditorIds() {
        if (activeCellEditorIds == null) activeCellEditorIds = new ArrayList<String>();
        return activeCellEditorIds;
    }

    /**
     * Get the backing List of Strings containing the Ids of the Column components selected in this row. The reference returned is not to a List copy but to the actual list defining the selection state.
     * @return the List<String> of UIComponent Ids defining the selected Column components
     */
    public List<String> getSelectedColumnIds() {
        if (selectedColumnIds == null) selectedColumnIds = new ArrayList<String>();
        return selectedColumnIds;
    }

    /**
     * Set a List of Strings containing the Ids of the Column components selected in this row.
     * @param selectedColumnIds
     */
    public void setSelectedColumnIds(List<String> selectedColumnIds) {
        this.selectedColumnIds = selectedColumnIds;
    }

    /**
     * Convenience method to take the id of the Column component and add it to the list of selected columns
     * in the RowState for the given row object.
     * @param column
     */
    public void addSelectedColumn(UIComponent column) {
        if (column != null) {
            String id = column.getId();
            if (!getSelectedColumnIds().contains(id))
                getSelectedColumnIds().add(id);
        }
    }

    /**
     * Convenience method to take the id of the Column component and remove it from the list of selected columns
     * in the RowState for the given row object.
     * @param column
     */
    public void removeSelectedColumn(UIComponent column) {
        if (column != null) {
            getSelectedColumnIds().remove(column.getId());
        }
    }

    /**
     * Set a List of Strings containing the Ids of the CellEditor components active in this row.
     * @param activeCellEditorIds
     */
    public void setActiveCellEditorIds(List<String> activeCellEditorIds) {
        this.activeCellEditorIds = activeCellEditorIds;
    }

    /**
     * Convenience method to take the id of the CellEditor component and add it to the list of active editors
     * in the row state for a given row object.
     * @param editor
     */
    public void addActiveCellEditor(UIComponent editor) {
        if (editor != null) {
            String id = editor.getId();
            if (!getActiveCellEditorIds().contains(id))
                getActiveCellEditorIds().add(id);
        }
    }

    /**
     * Convenience method to take the id of the CellEditor component and remove it from the list of active editors
     * in the row state for a given row object.
     * @param editor
     */
    public void removeActiveCellEditor(UIComponent editor) {
        if (editor != null) {
            getActiveCellEditorIds().remove(editor.getId());
        }
    }
}
