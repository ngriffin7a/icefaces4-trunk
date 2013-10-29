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

import org.icefaces.ace.model.tree.NodeState;
import org.icefaces.ace.model.tree.NodeStateCreationCallback;
import org.icefaces.ace.model.tree.NodeStateMap;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.util.JavaScriptRunner;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.lang.System;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ComponentExample(
        title = "example.ace.tree.title",
        description = "example.ace.tree.description",
        example = "/resources/examples/ace/tree/tree.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="tree.xhtml",
                        resource = "/resources/examples/ace/tree/tree.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="TreeBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                "/example/ace/tree/TreeBean.java")
        }
)
@Menu(
        title = "menu.ace.tree.subMenu.title",
        menuLinks = {
            @MenuLink(title = "menu.ace.tree.subMenu.main", isDefault = true, exampleBeanName = TreeBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.tree.subMenu.lazy", exampleBeanName = TreeLazyBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.tree.subMenu.client", exampleBeanName = TreeClientBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.tree.subMenu.reorder", exampleBeanName = TreeReorderBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.tree.subMenu.selection", exampleBeanName = TreeSelectionBean.BEAN_NAME)
//            @MenuLink(title = "menu.ace.tree.subMenu.nested", exampleBeanName = TreeNestedBean.BEAN_NAME)
        }
)
@ManagedBean(name= TreeBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TreeBean extends ComponentExampleImpl<TreeBean> implements Serializable {
    public static final String BEAN_NAME = "treeBean";
    private List<LocationNodeImpl> treeRoots = TreeDataFactory.getTreeRoots();
    private NodeStateMap stateMap;

    private NodeStateCreationCallback contractProvinceInit = new NodeStateCreationCallback() {
        public NodeState initializeState(NodeState newState, Object node) {
            LocationNodeImpl loc = (LocationNodeImpl) node;
            if (loc.getType().equals("country"))
                newState.setExpanded(true);
            return newState;
        }
    };


    public TreeBean() {
        super(TreeBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public List<LocationNodeImpl> getTreeRoots() {
        return treeRoots;
    }

    public void print(String text) {
        JavaScriptRunner.runScript(FacesContext.getCurrentInstance(),
                "alert('"+text+"');");
    }

    public NodeStateMap getStateMap() {
        return stateMap;
    }

    public void setStateMap(NodeStateMap stateMap) {
        this.stateMap = stateMap;
    }

    public NodeStateCreationCallback getContractProvinceInit() {
        return contractProvinceInit;
    }

    public void setContractProvinceInit(NodeStateCreationCallback contractProvinceInit) {
        this.contractProvinceInit = contractProvinceInit;
    }

    /* Proxy method to avoid JBossEL accessing stateMap like map for method invocations */
    public List getSelected() {
        if (stateMap == null) return Collections.emptyList();
        return stateMap.getSelected();
    }

}
