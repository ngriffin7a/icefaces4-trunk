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

import com.icesoft.faces.component.tree.IceUserObject;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.faces.event.ActionEvent;

/**
 * <p>The <code>NodeUserObject</code> represents a nodes user object.  This
 * particular IceUserobject implementation store extra information on how many
 * times the parent node is clicked on.  It is also responsible for copying and
 * delete its self.</p>
 * <p/>
 * <p>In this example pay particularly close attention to the
 * <code>wrapper</code> instance variable on IceUserObject.  The <code>wrapper</code>
 * allows for direct manipulations of the parent tree. </p>
 */
public class DynamicNodeUserObject extends IceUserObject {
    private static final long serialVersionUID = 6153551662875793151L;
    private TreeBean treeBean;

    public DynamicNodeUserObject(DefaultMutableTreeNode wrapper, TreeBean tree) {
        super(wrapper);

        treeBean = tree;

        setLeafIcon("xmlhttp/css/xp/css-images/tree_document.gif");
        setBranchContractedIcon("xmlhttp/css/xp/css-images/tree_folder_close.gif");
        setBranchExpandedIcon("xmlhttp/css/xp/css-images/tree_folder_open.gif");
    }

    public DynamicNodeUserObject(DefaultMutableTreeNode wrapper, TreeBean tree, String name, boolean isLeaf) {
        super(wrapper);
        treeBean = tree;
        setLeafIcon("xmlhttp/css/xp/css-images/tree_document.gif");
        setBranchContractedIcon("xmlhttp/css/xp/css-images/tree_folder_close.gif");
        setBranchExpandedIcon("xmlhttp/css/xp/css-images/tree_folder_open.gif");
        setText(name);  
        setLeaf(isLeaf);        
    }



    /**
     * Generates a label for the node based on an incrementing int.
     *
     * @return the generated label (eg. 'Node 5')
     */
    private String generateLabel(){
    	return  "Node " + Integer.toString(treeBean.getIncreasedLabelCount());
    }

    /**
     * Deletes this not from the parent tree.
     *
     * @param event that fired this method
     */
    public void deleteNode(ActionEvent event) {
        ((DefaultMutableTreeNode) getWrapper().getParent()).remove(getWrapper());
    }
    
    private void copyChildren(MutableTreeNode orig, MutableTreeNode clone) {
        int childCount = orig.getChildCount();
        while (childCount != 0) {
            childCount--;                                          
            DefaultMutableTreeNode oldChild = (DefaultMutableTreeNode)orig.getChildAt(childCount);
            DynamicNodeUserObject oldUserObject = (DynamicNodeUserObject)oldChild.getUserObject();
            DefaultMutableTreeNode newChild = (DefaultMutableTreeNode)oldChild.clone();   
            newChild.setUserObject(new DynamicNodeUserObject(newChild, treeBean, oldUserObject.getText(), oldUserObject.isLeaf()));
            copyChildren(oldChild, newChild);
            clone.insert((MutableTreeNode)newChild, 0);
        }        
    }

    /**
     * Copies this node and adds a it as a child node.
     *
     * @param event that fired this method
     */
    public void copyNode(ActionEvent event) {
        DefaultMutableTreeNode clonedWrapper = new DefaultMutableTreeNode();
        DefaultMutableTreeNode originalWrapper = getWrapper();
        
        DynamicNodeUserObject originalUserObject = (DynamicNodeUserObject) originalWrapper.getUserObject();
        DynamicNodeUserObject clonedUserObject = new DynamicNodeUserObject(clonedWrapper, treeBean, generateLabel(), originalUserObject.isLeaf());
        
        clonedWrapper.setUserObject(clonedUserObject);               
        copyChildren(originalWrapper, clonedWrapper);        
        
        MutableTreeNode parentNode = ((MutableTreeNode)(originalWrapper.getParent()));
        if (parentNode != null) parentNode.insert(clonedWrapper, 0);
        else originalWrapper.insert(clonedWrapper, 0);
    }

    /**
     * Registers a user click with this object and updates the selected node in the TreeBean.
     *
     * @param event that fired this method
     */
    public void nodeClicked(ActionEvent event) {
    	treeBean.setSelectedNodeObject(this); 
    }
}
