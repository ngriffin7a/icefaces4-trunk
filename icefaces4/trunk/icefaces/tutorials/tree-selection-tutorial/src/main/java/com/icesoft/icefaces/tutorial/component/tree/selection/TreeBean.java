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

package com.icesoft.icefaces.tutorial.component.tree.selection;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

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
public class TreeBean implements java.io.Serializable {

    private static final long serialVersionUID = 2553857920953007974L;
    // tree default model, used as a value for the tree component
    private DefaultTreeModel model;


    public TreeBean() {

        // create root node with its children expanded
        DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode();
        PanelSelectUserObject rootObject = new PanelSelectUserObject(rootTreeNode);
        rootObject.setText("ICEsoft Products");
        rootObject.setDisplayPanel("splash");
        rootObject.setExpanded(true);
        rootTreeNode.setUserObject(rootObject);

        // model is accessed by by the ice:tree component
        model = new DefaultTreeModel(rootTreeNode);

        // add a node to sue ICEfaces information
        DefaultMutableTreeNode branchNode = new DefaultMutableTreeNode();
        PanelSelectUserObject branchObject = new PanelSelectUserObject(branchNode);
        branchObject.setText("ICEfaces");
        branchObject.setDisplayPanel("icefaces");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        rootTreeNode.add(branchNode);

        // add a node to sue ICEbrowser information
        branchNode = new DefaultMutableTreeNode();
        branchObject = new PanelSelectUserObject(branchNode);
        branchObject.setText("ICEbrowser");
        branchObject.setDisplayPanel("icebrowser");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        rootTreeNode.add(branchNode);

        // add a node to sue ICEpdf information
        branchNode = new DefaultMutableTreeNode();
        branchObject = new PanelSelectUserObject(branchNode);
        branchObject.setText("ICEpdf");
        branchObject.setDisplayPanel("icepdf");
        branchObject.setLeaf(true);
        branchNode.setUserObject(branchObject);
        rootTreeNode.add(branchNode);
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
