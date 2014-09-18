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

import org.icefaces.ace.model.tree.NodeStateMap;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ComponentExample(
        parent = TreeBean.BEAN_NAME,
        title = "example.ace.tree.selection.title",
        description = "example.ace.tree.selection.description",
        example = "/resources/examples/ace/tree/treeSelection.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="treeSelection.xhtml",
                        resource = "/resources/examples/ace/tree/treeSelection.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="TreeSelectionBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                "/example/ace/tree/TreeSelectionBean.java")
        }
)
@ManagedBean(name= TreeSelectionBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TreeSelectionBean extends ComponentExampleImpl<TreeSelectionBean> implements Serializable {
    public static final String BEAN_NAME = "treeSelectionBean";

    public TreeSelectionBean() {
        super(TreeSelectionBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    private List<LocationNodeImpl> treeRoots = TreeDataFactory.getTreeRoots();
    private NodeStateMap stateMap;
    private boolean singleSelect = false;

    public List<LocationNodeImpl> getTreeRoots() {
        return treeRoots;
    }

    public NodeStateMap getStateMap() {
        return stateMap;
    }

    public void setStateMap(NodeStateMap stateMap) {
        this.stateMap = stateMap;
    }

    public boolean isSingleSelect() {
        return singleSelect;
    }

    public void setSingleSelect(boolean singleSelect) {
        this.singleSelect = singleSelect;
    }

    /* Proxy method to avoid JBossEL accessing stateMap like map for method invocations */
    public List getSelected() {
        if (stateMap == null) return Collections.emptyList();
        return stateMap.getSelected();
    }
}
