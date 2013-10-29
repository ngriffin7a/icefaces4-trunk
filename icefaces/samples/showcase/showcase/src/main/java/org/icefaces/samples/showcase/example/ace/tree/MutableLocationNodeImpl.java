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

package org.icefaces.samples.showcase.example.ace.tree;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;

public class MutableLocationNodeImpl implements MutableTreeNode {
    String name;
    String type;
    Integer population;

    public MutableLocationNodeImpl(String name, String type, Integer population) {
        this.name = name;
        this.type = type;
        this.population = population;
    }

    public void insert(MutableTreeNode mutableTreeNode, int i) {

    }

    public void remove(int i) {

    }

    public void remove(MutableTreeNode mutableTreeNode) {

    }

    public void setUserObject(Object o) {

    }

    public void removeFromParent() {

    }

    public void setParent(MutableTreeNode mutableTreeNode) {

    }

    public TreeNode getChildAt(int i) {
        return null;
    }

    public int getChildCount() {
        return 0;
    }

    public TreeNode getParent() {
        return null;
    }

    public int getIndex(TreeNode treeNode) {
        return 0;
    }

    public boolean getAllowsChildren() {
        return false;
    }

    public boolean isLeaf() {
        return false;
    }

    public Enumeration children() {
        return null;
    }
}
