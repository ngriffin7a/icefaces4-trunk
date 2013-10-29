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

import java.io.Serializable;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 * IceUserObject - the Object that constitutes a DefaultMutableTreeNode's
 * user-specified data.
 * <p/>
 * The TreeModel must contain a tree of DefaultMutableTreeNode instances. Each
 * DefaultMutableTreeNode instance encapsultes an IceUserObject. The
 * IceUserObject is the extension point for the application developer.</p>
 * <p/>
 * By default all nodes are folders unless the leaf attribute is set true. </p>
 * <p/>
 * If the IceUserObject does not provide sufficient state for representation of
 * the tree's nodes, then the application developer should extend the
 * IceUserObject and add state as required to their extension. When creating an
 * IceUserObject, the DefaultMutableTreeNode wrapper must be provided to the
 * constructor. Then the node's state can be set to the attributes on the
 * IceUserObject. </p>
 */
public class IceUserObject implements Serializable  {

	private static final long serialVersionUID = 1L;
    protected DefaultMutableTreeNode wrapper;
    protected String text;
    protected boolean expanded;
    protected String tooltip;
    protected String action;
    protected TreeNode treeNode;

    // icon fields
    protected String leafIcon;
    protected String branchExpandedIcon;
    protected String branchContractedIcon;
    protected String icon;

    // leaf field 
    protected boolean leaf;

    // rowIndex to support new TreeDataModel
    private int rowIndex;

    /**
     * @return rowIndex
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * @param rowIndex
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    //constructors

    /**
     * <p>Class constructor specifying the DefaultMutableTreeNode wrapper object
     * that will hold a reference to this IceUserObject.</p>
     *
     * @param wrapper
     */
    public IceUserObject(DefaultMutableTreeNode wrapper) {
        this.wrapper = wrapper;
    }

    /**
     * <p>Set the value of the boolean leaf attribute. Setting the leaf
     * attribute to true will force a tree node to be rendered as a leaf. By
     * default the leaf attribute is false therefore all tree nodes will default
     * to folders.</p>
     *
     * @param leaf
     */
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    /**
     * <p>Return false if the tree node is a folder.By default all tree nodes
     * are folders.</p>
     *
     * @return the boolean value of the leaf attribute.
     */
    public boolean isLeaf() {
        return this.leaf;
    }

    /**
     * <p>Return the value of the <code>action</code> property.</p>
     *
     * @return action
     */
    public String action() {
        return action;
    }

    // getters/setters

    /**
     * @param treeNode
     */
    public void setTreeNode(TreeNode treeNode) {
        this.treeNode = treeNode;
    }

    /**
     * @return treeNode
     */
    public TreeNode getTreeNode() {
        return this.treeNode;
    }

    /**
     * <p>Return the value of the <code>text</code> property.</p>
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * <p>Set the value of the <code>text</code> property.</p>
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return null
     */
    public String getFamily() {
        return null;
    }

    /**
     * <p>Return the value of the <code>expanded</code> property.</p>
     *
     * @return expanded
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     * <p>Set the value of the <code>expanded</code> property.</p>
     *
     * @param isExpanded
     */
    public void setExpanded(boolean isExpanded) {
        this.expanded = isExpanded;
    }

    /**
     * <p>Return the value of the <code>tooltip</code> property.</p>
     *
     * @return tootip
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * <p>Set the value of the <code>tooltip</code> property.</p>
     *
     * @param tooltipString
     */
    public void setTooltip(String tooltipString) {
        this.tooltip = tooltipString;
    }

    /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
    public String toString() {
        return text;
    }

    /**
     * <p>Return the value of the <code>leafIcon</code> property.</p>
     *
     * @return leafIcon
     */
    public String getLeafIcon() {
        return leafIcon;
    }

    /**
     * <p>Set the value of the <code>leafIcon</code> property.</p>
     *
     * @param leafIcon
     */
    public void setLeafIcon(String leafIcon) {
        this.leafIcon = leafIcon;
    }

    /**
     * <p>Return the value of the <code>branchContractedIcon</code>
     * property.</p>
     *
     * @return branchContractedIcon
     */
    public String getBranchContractedIcon() {
        return branchContractedIcon;
    }

    /**
     * <p>Set the value of the <code>branchContractedIcon</code> property.</p>
     *
     * @param branchContractedIcon
     */
    public void setBranchContractedIcon(String branchContractedIcon) {
        this.branchContractedIcon = branchContractedIcon;
    }

    /**
     * <p>Return the value of the <code>branchExpandedIcon</code> property.</p>
     *
     * @return branchExpandedIcon
     */
    public String getBranchExpandedIcon() {
        return branchExpandedIcon;
    }

    /**
     * <p>Set the value of the <code>branchExpandedIcon</code> property.</p>
     *
     * @param branchExpandedIcon
     */
    public void setBranchExpandedIcon(String branchExpandedIcon) {
        this.branchExpandedIcon = branchExpandedIcon;
    }

    /**
     * <p>Return the appropriate icon based on this node's leaf attribute or
     * expanded/collapsed state.</p> <p>By default the leaf attribute is
     * false.</p>
     *
     * @return String application-relative path to the image file
     */
    public String getIcon() {
        // leaf icon is rendered based on leaf attribute
        if (this.isLeaf()) {
            if (leafIcon != null) {
                return leafIcon;
            }
        } else if (isExpanded()) {
            if (branchExpandedIcon != null) {
                return branchExpandedIcon;
            }
        } else {
            if (branchContractedIcon != null) {
                return branchContractedIcon;
            }
        }
        return icon;
    }

    /**
     * <p>Return the value of the <code>wrapper</code> property.</p>
     *
     * @return wrapper
     */
    public DefaultMutableTreeNode getWrapper() {
        return wrapper;
    }

    /**
     * Set the DefaultMutableTreeNode instance that wraps this instance
     *
     * @param wrapper
     */
    public void setWrapper(DefaultMutableTreeNode wrapper) {
        this.wrapper = wrapper;
    }

    /**
     * <p>Return the value of the <code>action</code> property.</p>
     *
     * @return action
     */
    public String getAction() {
        return action;
    }

    /**
     * <p>Set the value of the <code>action</code> property.</p>
     *
     * @param action
     */
    public void setAction(String action) {
        this.action = action;
    }
}