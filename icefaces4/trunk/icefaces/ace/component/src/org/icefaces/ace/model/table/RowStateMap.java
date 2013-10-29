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

import org.icefaces.ace.util.CollectionUtils;
import org.icefaces.ace.util.collections.EntrySetToKeyListTransformer;
import org.icefaces.ace.util.collections.Predicate;

import javax.faces.component.UIComponent;
import javax.faces.model.DataModel;
import java.io.Serializable;
import java.util.*;

/**
 * A Map<Object, RowState> that defines the ace:dataTable feature state of the row objects of a given ace:dataTable.
 *
 * States can be added at initialization, or will be created as needed during row object rendering if no state exists.
 */
public class RowStateMap implements Map<Object, RowState>, Serializable {
    private Map<Object, RowState> map = new HashMap<Object, RowState>();

    private static Predicate selectedPredicate = new SelectedPredicate();
    private static Predicate selectablePredicate = new SelectablePredicate();
    private static Predicate editablePredicate = new EditablePredicate();
    private static Predicate expandablePredicate = new ExpandablePredicate();
    private static Predicate expandedPredicate = new ExpandedPredicate();
    private static Predicate visiblePredicate = new VisiblePredicate();
    private static Predicate rowExpansionPredicate = new RowExpansionPredicate();
    private static Predicate panelExpansionPredicate = new PanelExpansionPredicate();
    private static Predicate hasSelectedCellsPredicate = new SelectedCellsPredicate();


    /**
     * Put a new state for a specific row object. Overwrites old states.
     * @param o row object
     * @param s new state
     * @return old state or null if none existed
     */
    public RowState put(Object o, RowState s) {
        return map.put(o, s);
    }

    /**
     * Put a empty state for a specific row object. Overwrites old states.
     * @param o row object
     */
    public void add(Object o) {
        map.put(o, new RowState());
    }

    /**
     * Get the number of states held in this map
     * @return map entry count
     */
    public int size() {
        return map.size();
    }

    /**
     * Get if this map is empty
     * @return if this map empty
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Get if this map contains a state for a particular row object
     * @param key row object
     * @return if this map contains the row object param
     */
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    /**
     * Get if this map contains a particular row state
     * @param value row state
     * @return if this map contains the row state param
     */
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    /**
     * Get the row state for a particular row object
     * @param o row object
     * @return row state
     */
    public RowState get(Object o) {
        RowState s = map.get(o);
        if (s == null) {
            add(o);
            s = map.get(o);
        }
        return s;
    }

    /**
     * Remove the row state for a particular row object. A new empty state will
     * be added for the row if it is rendered in a coming request.
     * @param o
     * @return
     */
    public RowState remove(Object o) {
        return map.remove(o);
    }

    /**
     * Add all entries from the input map to this map.
     * @param m input map
     */
    public void putAll(Map m) {
        map.putAll(m);
    }

    /**
     * Remove all row states from this map.
     */
    public void clear() {
        map.clear();
    }

    /**
     * Get the set of all row objects with attached RowStates
     * @return set of row objects that have state
     */
    public Set keySet() {
        return map.keySet();
    }

    /**
     * Get the collection of all RowStates
     * @return the state of every row in this map
     */
    public Collection values() {
        return map.values();
    }

    /**
     * Get the collection of all Map Entry objects, defining a key row object and value row state.
     * @return row object state entry collection
     */
    public Set entrySet() {
        return map.entrySet();
    }

    /**
     * Replace a row object with one that hashes identically but is a distinct copy.
     * Useful for updating the row state map when replacing the data set with a copy.
     * @param o
     */
    public void updateData(Object o) {
        map.put(o, map.remove(o));
    }

    /**
     * This method will look up state entries with keys in the input list, and update their
     * key with the copy from the input list.
     *
     * This method is useful in applications where row data is updated by replacing a row
     * with an object copy that is equal() but contains differences that must be reflected in methods
     * that return row data from the rowStateMap. Without updating, in this circumstance, the key is still a
     * reference to the old copy of the data, and though the state will be correctly correlated to the new key,
     * methods returning the key, will return the old key.
     *
     * @param data - A list of row data equal() to some key already in the rowStateMap, but with changes that require the current key to be replaced.
     */
    public void updateData(List data) {
        List<RowState> values = new ArrayList<RowState>(data.size());
        for (Object d : data) {
            values.add(map.remove(d));
        }
        int i = 0;
        for (RowState v : values) {
            if (v != null) map.put(data.get(i), v);
            //else {
                // System.out.println("Updating a null state!");
            //}
            i++;
        }
    }

    /**
     * This method will remove all entries from the map with keys in this list.
     * @param data the row data to remove states for
     */
    public void removeStates(List data) {
        Set keys = map.keySet();
        keys.removeAll(data);
    }

    /**
     * This method will remove all entries from the map but those with keys in this list.
     * @param data the row data list to keep states for
     */
    public void filterStates(List data) {
        Set keys = map.keySet();
        keys.retainAll(data);
    }

    /**
     * This method will remove all entries from the map but those with keys in this data model.
     * @param model The data model to keep states for
     */
    public void filterStates(DataModel model) {
        Set keys = map.keySet();
        ArrayList rowDataList = new ArrayList<Object>();
        int rowsToProcess = model.getRowCount();

        // TODO: lazy handling?
        while (rowsToProcess > 0) {
            rowsToProcess--;
            model.setRowIndex(rowsToProcess);
            if (model.isRowAvailable()) rowDataList.add(model.getRowData());
        }

        keys.retainAll(rowDataList);
    }

    /**
     * Synchronize the expanded row state with the expanded state stored in the node of the data model.
     * Data model expansion state overwrites pre-existing state.
     * @param dataModel tree data model
     */
    public void setExpandableByTreeModel(TreeDataModel dataModel) {
        String currentRootId = "";
        int rowCount = dataModel.getRowCount();
        int i = 0;
        // Handle row and loop down the tree if expanded.
        while (i < rowCount) {
            dataModel.setRowIndex(i++);
            try {
                do {
                    // Decodes row/node in tree case.
                    // Handle recursive case
                    RowState currentModel = get(dataModel.getRowData());
                    
                    if (dataModel.getCurrentRowChildCount() == 0) {
                        currentModel.setExpandable(false);
                    }

                    if (dataModel.getCurrentRowChildCount() > 0) {
                        currentModel.setExpandable(true);
                        currentRootId =  currentRootId.equals("") ? (dataModel.getRowIndex()+"") : (currentRootId + "." + dataModel.getRowIndex());
                        dataModel.setRootIndex(currentRootId);
                        dataModel.setRowIndex(0);
                    } else if (dataModel.getRowIndex() < dataModel.getRowCount()-1) {
                        dataModel.setRowIndex(dataModel.getRowIndex() + 1);
                    } else if (!currentRootId.equals("")) {
                        // changing currrent node id to reflect pop
                        int lastSiblingRowIndex = dataModel.pop();
                        // if we are the last child of a set of siblings we've popped back to,
                        // continue popping until an uncounted sibling exists
                        while (lastSiblingRowIndex == (dataModel.getRowCount() - 1) && dataModel.isRootIndexSet()) {
                            lastSiblingRowIndex = dataModel.pop();
                            currentRootId = (currentRootId.lastIndexOf('.') != -1)  ?
                                currentRootId.substring(0,currentRootId.lastIndexOf('.')) :
                                "";
                        }
                        dataModel.setRowIndex(lastSiblingRowIndex + 1);
                        currentRootId = (currentRootId.lastIndexOf('.') != -1)  ?
                                currentRootId.substring(0,currentRootId.lastIndexOf('.')) :
                                "";
                    }
                    // Break out of expansion recursion to continue root node
                    if (currentRootId.equals("")) break;
                } while (true);
            } finally {
                dataModel.setRowIndex(-1);
                dataModel.setRootIndex(null);
            }
        }
    }

    /**
     * Get the list of selected row objects.
     * @return list of selected row objects
     */
    public List getSelected() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), selectedPredicate));
    }

    /**
     * Get the list of row objects with cells that are selected.
     * @return list of row objects with selected cells
     */
    public List getRowsWithSelectedCells() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), hasSelectedCellsPredicate));
    }

    /**
     * Get row objects that are selectable
     * @return the list of selectable row objects
     */
    public List getSelectable() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), selectablePredicate));
    }

    /**
     * Get the row objects that are editable
     * @return list of editable row objects
     */
    public List getEditable() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), editablePredicate));
    }

    /**
     * Get the row objects that are expanded
     * @return list of expanded row objects
     */
    public List getExpanded() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), expandedPredicate));
    }

    /**
     * Get the row objects that are expandible
     * @return list of expandible row objects
     */
    public List getExpandable() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), expandablePredicate));
    }

    /**
     * Get the row objects that are not invisible
     * @return list of visible row objects
     */
    public List getVisible() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), visiblePredicate));
    }

    /**
     * Get the row objects that have activated a particular CellEditor.
     * @param editor active CellEditor target
     * @return list of row objects with active editors
     */
    public List getEditing(UIComponent editor) {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), new EditingPredicate(editor)));
    }

    /**
     * Get the row objects that expand into other rows
     * @return list of row expanding row objects
     */
    public List getRowExpanders() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), rowExpansionPredicate));
    }

    /**
     * Get the row objects that expand into panels
     * @return list of panel expanding row objects
     */
    public List getPanelExpanders() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), panelExpansionPredicate));
    }


    /**
     * Set the selection state of all the row objects
     * @param value selected
     */
    public void setAllSelected(boolean value) {
        for (RowState s : map.values()) s.setSelected(value);
    }

    /**
     * Set the selectable state of all the row objects
     * @param value selectable
     */
    public void setAllSelectable(boolean value) {
        for (RowState s : map.values()) s.setSelectable(value);
    }

    /**
     * Set the editable state of all the row objects
     * @param value editable
     */
    public void setAllEditable(boolean value) {
        for (RowState s : map.values()) s.setEditable(value);
    }

    /**
     * Set the expanded state of all the row objects
     * @param value expanded
     */
    public void setAllExpanded(boolean value) {
        for (RowState s : map.values()) s.setExpanded(value);
    }

    /**
     * Set the expandable state of all the row objects
     * @param value expandable
     */
    public void setAllExpandable(boolean value) {
        for (RowState s : map.values()) s.setExpandable(value);
    }

    /**
     * Set the visible state of all the row objects
     * @param value visible
     */
    public void setAllVisible(boolean value) {
        for (RowState s : map.values()) s.setVisible(value);
    }

    /**
     * Set the expansion mode of all the row objects to row
     */
    public void setAllRowExpansion() {
        for (RowState s : map.values()) {
            s.setExpansionType(RowState.ExpansionType.ROW);
        }
    }

    /**
     * Set the expansion mode of all the row objects to panel
     */
    public void setAllPanelExpansion() {
    for (RowState s : map.values()) {
            s.setExpansionType(RowState.ExpansionType.PANEL);
        }
    }

    /**
     * Set the editing state for the given CellEditor
     * @param editor the CellEditor
     * @param add activate or remove
     */
    public void setAllEditing(UIComponent editor, boolean add) {
        for (RowState s : map.values())
            if (add) s.addActiveCellEditor(editor);
            else s.removeActiveCellEditor(editor);
    }


    
    static class SelectedPredicate implements Predicate {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((RowState)((Entry)o).getValue()).isSelected()) return true;
            return false;
        }
    }
    static class SelectablePredicate implements Predicate {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((RowState)((Entry)o).getValue()).isSelectable()) return true;
            return false;
        }
    }
    static class ExpandedPredicate implements Predicate {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((RowState)((Entry)o).getValue()).isExpanded()) return true;
            return false;
        }
    }
    static class ExpandablePredicate implements Predicate {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((RowState)((Entry)o).getValue()).isExpandable()) return true;
            return false;
        }
    }
    static class EditablePredicate implements Predicate {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((RowState)((Entry)o).getValue()).isEditable()) return true;
            return false;
        }
    }
    static class VisiblePredicate implements Predicate {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((RowState)((Entry)o).getValue()).isVisible()) return true;
            return false;
        }
    }
    static class RowExpansionPredicate implements Predicate {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((RowState)((Entry)o).getValue()).getExpansionType().equals(RowState.ExpansionType.ROW))
                    return true;
            return false;
        }
    }
    static class PanelExpansionPredicate implements Predicate {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((RowState)((Entry)o).getValue()).getExpansionType().equals(RowState.ExpansionType.PANEL))
                    return true;
            return false;
        }
    }
    static class SelectedCellsPredicate implements Predicate {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((RowState)((Entry)o).getValue()).getSelectedColumnIds().size() > 0)
                    return true;
            return false;
        }
    }


    private class EditingPredicate implements Predicate {
        String id;
        
        public EditingPredicate(UIComponent editor) {
            id = editor.getId();
        }

        public boolean evaluate(Object object) {
            if (object instanceof Entry)
                if (((RowState)((Entry)object).getValue()).activeCellEditorIds.contains(object))
                    return true;

            return false;
        }
    }
}
