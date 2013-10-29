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
import java.util.List;

@ComponentExample(
        parent = TreeBean.BEAN_NAME,
        title = "example.ace.tree.reorder.title",
        description = "example.ace.tree.reorder.description",
        example = "/resources/examples/ace/tree/treeReorder.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="treeReorder.xhtml",
                        resource = "/resources/examples/ace/tree/treeReorder.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="TreeReorderBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                "/example/ace/tree/TreeReorderBean.java")
        }
)
@ManagedBean(name= TreeReorderBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TreeReorderBean extends ComponentExampleImpl<TreeReorderBean> implements Serializable {
    public static final String BEAN_NAME = "treeReorderBean";

    public TreeReorderBean() {
        super(TreeReorderBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    private List<LocationNodeImpl> treeRoots = TreeDataFactory.getTreeRoots();

    private NodeStateCreationCallback expandAllInit = new ExpandAllNodeInitCallback();

    public List<LocationNodeImpl> getTreeRoots() {
        return treeRoots;
    }

    public NodeStateCreationCallback getExpandAllInit() {
        return expandAllInit;
    }
}
