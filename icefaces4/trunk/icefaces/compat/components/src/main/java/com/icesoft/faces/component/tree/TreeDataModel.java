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

package com.icesoft.faces.component.tree;

import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import java.util.HashMap;
import java.util.Map;

/**
 * TreeDataModel is an implementation of DataModel that wraps a
 * DefaultTreeModel.
 */
public class TreeDataModel extends DataModel {

    private int rowIndex = -1;
    private TreeModel treeModel;
    private Map rowIndexMap;

    /**
     * Default no args contructor
     */
    public TreeDataModel() {
        this(null);
    }

    /**
     * @param treeModel
     */
    public TreeDataModel(TreeModel treeModel) {
        super();
        setWrappedData(treeModel);
        rowIndexMap = new HashMap();
        setChildCount();

    }

    /* (non-Javadoc)
    * @see javax.faces.model.DataModel#isRowAvailable()
    */
    public boolean isRowAvailable() {
        if (treeModel == null) {
            return (false);
        } else if ((rowIndex >= 0) && (rowIndex < childCount)) {
            return (true);
        } else {
            return (false);
        }
    }

    /* (non-Javadoc)
     * @see javax.faces.model.DataModel#getRowCount()
     */
    public int getRowCount() {
        return childCount;
    }

    private int childCount = -1;

    private int setChildCount(DefaultMutableTreeNode treeNode) {

        int count = treeNode.getChildCount();
        for (int i = 0; i < count; i++) {
            DefaultMutableTreeNode child =
                    (DefaultMutableTreeNode) treeNode.getChildAt(i);
            ((IceUserObject) child.getUserObject())
                    .setRowIndex(treeNodeRowIndex++);
            addNodeToMap(child,
                         ((IceUserObject) child.getUserObject()).getRowIndex());

            if (((IceUserObject) child.getUserObject()).isExpanded()) {
                childCount += treeNode.getChildCount();
                setChildCount(child);
            }
        }
        return childCount;
    }

    private int treeNodeRowIndex = -1;

    private void setChildCount() {
        DefaultMutableTreeNode root =
                (DefaultMutableTreeNode) treeModel.getRoot();
        rowIndexMap.clear();
        treeNodeRowIndex = 0;
        //There will be a root always
        childCount = 1;
        ((IceUserObject) root.getUserObject()).setRowIndex(treeNodeRowIndex++);
        addNodeToMap(root,
                     ((IceUserObject) root.getUserObject()).getRowIndex());
        if (((IceUserObject) root.getUserObject()).isExpanded()) {
            childCount += root.getChildCount();

        }
    }

    private void addNodeToMap(DefaultMutableTreeNode node,
                              int treeNodeRowIndex) {


        rowIndexMap.put(new Integer(treeNodeRowIndex), node);
    }

    /* (non-Javadoc)
    * @see javax.faces.model.DataModel#getRowData()
    */
    public Object getRowData() {
        if (treeModel == null) {
            return (null);
        } else if (!isRowAvailable()) {
            throw new IllegalArgumentException();
        } else {
            return (rowIndexMap.get(new Integer(rowIndex)));
        }
    }

    /* (non-Javadoc)
     * @see javax.faces.model.DataModel#getRowIndex()
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /* (non-Javadoc)
     * @see javax.faces.model.DataModel#setRowIndex(int)
     */
    public void setRowIndex(int rowIndex) {
        if (rowIndex < -1) {
            throw new IllegalArgumentException();
        }
        int old = this.rowIndex;
        this.rowIndex = rowIndex;
        if (treeModel == null) {
            return;
        }
        DataModelListener[] listeners = getDataModelListeners();
        if ((old != this.rowIndex) && (listeners != null)) {
            Object rowData = null;
            if (isRowAvailable()) {
                rowData = getRowData();
            }
            DataModelEvent event =
                    new DataModelEvent(this, this.rowIndex, rowData);
            int n = listeners.length;
            for (int i = 0; i < n; i++) {
                if (null != listeners[i]) {
                    listeners[i].rowSelected(event);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.faces.model.DataModel#getWrappedData()
     */
    public Object getWrappedData() {
        return treeModel;
    }

    /* (non-Javadoc)
     * @see javax.faces.model.DataModel#setWrappedData(java.lang.Object)
     */
    public void setWrappedData(Object data) {
        if (data == null) {
            treeModel = null;
            setRowIndex(-1);
        } else {
            treeModel = (TreeModel) data;
            rowIndex = -1;
            setRowIndex(0);
        }

    }

}
