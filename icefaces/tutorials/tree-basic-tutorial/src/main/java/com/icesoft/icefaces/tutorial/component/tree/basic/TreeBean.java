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

package com.icesoft.icefaces.tutorial.component.tree.basic;

import com.icesoft.faces.component.tree.IceUserObject;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;

/**
 * <p>
 * A basic backing bean for a ice:tree component.  The only instance variable
 * needed is a DefaultTreeModel Object which is bound to the icefaces tree
 * component in the jspx code.</p>
 * <p>
 * The tree created by this backing bean is very simple, containing only text
 * nodes.  The plus and minus icons which expand the tree are rendered because
 * of attributes set at the component level.
 * </p>
 *
 */
public class TreeBean implements Serializable {

    private static final long serialVersionUID = 6256505532910172146L;
    // tree default model, used as a value for the tree component
    private DefaultTreeModel model;

    public TreeBean(){
        // create root node with its children expanded
        DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode();
        IceUserObject rootObject = new IceUserObject(rootTreeNode);
        rootObject.setText("Root Node");
        rootObject.setExpanded(true);
        rootTreeNode.setUserObject(rootObject);

        // model is accessed by by the ice:tree component
        model =  new DefaultTreeModel(rootTreeNode);

        // add some child notes
        for (int i = 0; i < 3; i++) {
            DefaultMutableTreeNode branchNode = new DefaultMutableTreeNode();
            IceUserObject branchObject = new IceUserObject(branchNode);
            branchObject.setText("node-" + i);
            branchNode.setUserObject(branchObject);
            branchObject.setLeaf(true);
            rootTreeNode.add(branchNode);
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
