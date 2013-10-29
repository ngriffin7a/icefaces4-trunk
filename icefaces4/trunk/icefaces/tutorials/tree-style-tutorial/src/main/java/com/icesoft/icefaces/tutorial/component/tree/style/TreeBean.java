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

package com.icesoft.icefaces.tutorial.component.tree.style;

import com.icesoft.faces.component.tree.IceUserObject;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;

/**
 * <p/>
 * A basic backing bean for a ice:tree component.  The only instance variable
 * needed is a DefaultTreeModel Object which is bound to the icefaces tree
 * component in the jspx code.</p>
 * <p/>
 * The tree created by this backing bean is used to control the selected
 * panel in a ice:panelStack.
 * </p>
 */
public class TreeBean implements Serializable {

    private static final long serialVersionUID = -8894395398518628053L;
    // tree default model, used as a value for the tree component
    private DefaultTreeModel model;

    // default node icons for xp thme
    private static final String XP_BRANCH_CONTRACTED_ICON = "./xmlhttp/css/xp/css-images/tree_folder_open.gif";
    private static final String XP_BRANCH_EXPANDED_ICON = "./xmlhttp/css/xp/css-images/tree_folder_close.gif";
    private static final String XP_BRANCH_LEAF_ICON = "./xmlhttp/css/xp/css-images/tree_document.gif";

    public TreeBean() {

        // create root node with its children expanded
        DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode();
        IceUserObject rootObject = new IceUserObject(rootTreeNode);
        rootObject.setText("Root Node");
        rootObject.setExpanded(true);
        rootObject.setBranchContractedIcon(XP_BRANCH_CONTRACTED_ICON);
        rootObject.setBranchExpandedIcon(XP_BRANCH_EXPANDED_ICON);
        rootObject.setLeafIcon(XP_BRANCH_LEAF_ICON);
        rootTreeNode.setUserObject(rootObject);

        // model is accessed by by the ice:tree component
        model = new DefaultTreeModel(rootTreeNode);

        // add some child notes
        for (int i = 0; i < 3; i++) {
            DefaultMutableTreeNode branchNode = new DefaultMutableTreeNode();
            IceUserObject branchObject = new IceUserObject(branchNode);
            branchObject.setText("node-" + i);
            branchObject.setBranchContractedIcon(XP_BRANCH_CONTRACTED_ICON);
            branchObject.setBranchExpandedIcon(XP_BRANCH_EXPANDED_ICON);
            branchObject.setLeafIcon(XP_BRANCH_LEAF_ICON);
            branchObject.setLeaf(true);
            branchNode.setUserObject(branchObject);
            rootTreeNode.add(branchNode);
            // add some more sub children
            for (int k = 0; k < 2; k++) {
                DefaultMutableTreeNode subBranchNode = new DefaultMutableTreeNode();
                IceUserObject subBranchObject = new IceUserObject(subBranchNode);
                subBranchObject.setText("sub-node-" + i + "-" + k);
                subBranchObject.setBranchContractedIcon(XP_BRANCH_CONTRACTED_ICON);
                subBranchObject.setBranchExpandedIcon(XP_BRANCH_EXPANDED_ICON);
                subBranchObject.setLeafIcon(XP_BRANCH_LEAF_ICON);
                branchObject.setLeaf(true);
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
}
