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

package org.icefaces.samples.showcase.example.compat.tree;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = TreeBean.BEAN_NAME,
        title = "example.compat.tree.root.title",
        description = "example.compat.tree.root.description",
        example = "/resources/examples/compat/tree/treeRoot.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="treeRoot.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tree/treeRoot.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TreeRoot.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tree/TreeRoot.java")
        }
)
@ManagedBean(name= TreeRoot.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TreeRoot extends ComponentExampleImpl<TreeRoot> implements Serializable {
	
    public static final String BEAN_NAME = "treeRoot";

    private boolean hideRoot = true;

    public TreeRoot() {
            super(TreeRoot.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public boolean getHideRoot() { return hideRoot; }

    public void setHideRoot(boolean hideRoot) { this.hideRoot = hideRoot; }

    public void toggleRoot(ActionEvent event) {
        hideRoot = !hideRoot;
    }	
}
