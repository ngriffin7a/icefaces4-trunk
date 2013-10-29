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

package com.icesoft.icefaces.tutorial.component.tree.dynamic;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

/**
 * <p/>
 * The TreeBean class is responsible for store the notion of a selected node.
 * A node becomes selected when a user click on it; the command button that throws
 * the ActionEvent is listened to by one DynamicNodeUserObject Object.  This
 * via a TreeBean call back sets its self as the selected node.
 * </p>
 * <p>The TreeBean can then copy or delete the selected node from the
 * DefaultTreeModel using the methods #deleteSelectedNode and #copySelectedNode. </p>
 */
public class TreeBean implements Serializable {

    private static final long serialVersionUID = 6197956477293382208L;
    // tree default model, used as a value for the tree component
    private DefaultTreeModel model;

    // root node label, used to insure that it can't be deleted.
    private static final String ROOT_NODE_TEXT = "Root Node";

    // label count increases one for each new node
    private int labelCount = 0;

    // object reference used to delete and copy the node
    private DynamicNodeUserObject selectedNodeObject = null;

    public TreeBean() {

        // create root node with its children expanded
        DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode();
        DynamicNodeUserObject rootObject =
                new DynamicNodeUserObject(rootTreeNode, this);
        rootObject.setText(ROOT_NODE_TEXT);
        rootTreeNode.setUserObject(rootObject);

        // model is accessed by by the ice:tree component
        model = new DefaultTreeModel(rootTreeNode);

        // add some child notes
        for (int i = 0; i < 3; i++) {
            DefaultMutableTreeNode branchNode = new DefaultMutableTreeNode();
            DynamicNodeUserObject branchObject =
                    new DynamicNodeUserObject(branchNode, this);
            branchObject.setText("node-" + i);
            branchNode.setUserObject(branchObject);
            rootTreeNode.add(branchNode);
            // add some more sub children
            for (int k = 0; k < 2; k++) {
                DefaultMutableTreeNode subBranchNode = new DefaultMutableTreeNode();
                DynamicNodeUserObject subBranchObject =
                        new DynamicNodeUserObject(subBranchNode, this);
                subBranchObject.setText("sub-node-" + i + "-" + k);
                subBranchObject.setLeaf(true);
                subBranchNode.setUserObject(subBranchObject);
                branchNode.add(subBranchNode);
            }
        }
    }

    /**
     * Gets the tree's default model.
     *
     * @return tree model.
     */
    public DefaultTreeModel getModel() {
        return model;
    }

    /**
     * Gets the tree node.
     *
     * @return the tree node
     */
    public DynamicNodeUserObject getSelectedNodeObject() {
        return selectedNodeObject;
    }

    /**
     * Sets the tree node.
     *
     * @param selectedNodeObject the new tree node
     */
    public void setSelectedNodeObject(DynamicNodeUserObject selectedNodeObject) {
        this.selectedNodeObject = selectedNodeObject;
    }

    /**
     * Deletes the selected tree node. The node
     * object reference is set to null so that
     * the delete and copy buttons will be disabled.
     *
     * @param event that fired this method
     * @see #isDeleteDisabled(), isCopyDisabled()
     */
    public void deleteSelectedNode(ActionEvent event){
        // can't delete the root node; this check is a failsafe in case
        // the delete method is somehow activated despite the button being disabled
        if (selectedNodeObject != null &&
                !selectedNodeObject.getText().equals(ROOT_NODE_TEXT)){
            selectedNodeObject.deleteNode(event);
            selectedNodeObject = null;
        }
    }

    /**
     * Copies the selected node in the tree.
     *
     * @param event that fired this method
     */
    public void copySelectedNode(ActionEvent event){
        if (selectedNodeObject != null)
            selectedNodeObject.copyNode(event);
    }

    /**
     * Determines whether the delete button is disabled.
     * The delete button should be disabled if the node that was
     * previously selected was deleted or if no node is otherwise selected.
     * The root node is a special case and cannot be deleted.
     *
     * @return the disabled status of the delete button
     */
    public boolean isDeleteDisabled (){
        //can't delete the root node
        return selectedNodeObject == null ||
                selectedNodeObject.getText().equals(ROOT_NODE_TEXT);
    }

    /**
     * Determines whether the copy button is disabled.
     * This should only occur when there is no node selected,
     * which occurs at initialization and when a node is deleted.
     *
     * @return the disabled status of the copy button
     */
    public boolean isCopyDisabled (){
        return selectedNodeObject == null;
    }


    /**
     * Increment the label count and return it.
     *
     * @return the new label count
     */
    public int getIncreasedLabelCount() {
        return ++labelCount;
    }

}
