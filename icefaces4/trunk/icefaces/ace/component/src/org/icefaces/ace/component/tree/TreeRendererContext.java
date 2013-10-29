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

package org.icefaces.ace.component.tree;

import org.icefaces.ace.model.tree.LazyNodeDataModel;

import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import java.lang.String;

public class TreeRendererContext {
    private Tree tree;
    private boolean expansion;
    private boolean selection;
    private boolean multipleSelection;
    private boolean lazy;
    private boolean reordering;
    private TreeSelectionMode treeSelectionMode;
    private TreeExpansionMode treeExpansionMode;
    private String dotURL;
    private String widgetVar;

    public TreeRendererContext(Tree tree, String widgetVar) {
        this.tree = tree;
        expansion = tree.isExpansion();
        selection = tree.isSelection();
        reordering = tree.isReordering();
        multipleSelection = tree.isSelectMultiple();
        treeSelectionMode = tree.getSelectionMode();
        treeExpansionMode = tree.getExpansionMode();
        lazy = tree.getValue() instanceof LazyNodeDataModel;
        this.widgetVar = widgetVar;

        ResourceHandler rh = FacesContext.getCurrentInstance()
                .getApplication().getResourceHandler();

        dotURL = rh.createResource("tree/dot.png","icefaces.ace", "image/png").getRequestPath();
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public boolean isSelection() {
        return selection;
    }

    public boolean isMultipleSelection() {
        return multipleSelection;
    }

    public TreeSelectionMode getSelectionMode() {
        return treeSelectionMode;
    }

    public TreeExpansionMode getExpansionMode() {
        return treeExpansionMode;
    }

    public boolean isExpansion() {
        return expansion;
    }

    public String getDotURL() {
        return dotURL;
    }

    public boolean isLazy() {
        return lazy;
    }

    public boolean isReordering() {
        return reordering;
    }

    public String getWidgetVar() {
        return widgetVar;
    }

    public void setWidgetVar(String widgetVar) {
        this.widgetVar = widgetVar;
    }
}
