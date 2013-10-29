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
        title = "example.compat.tree.events.title",
        description = "example.compat.tree.events.description",
        example = "/resources/examples/compat/tree/treeEvents.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="treeEvents.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tree/treeEvents.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TreeEvents.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tree/TreeEvents.java")
        }
)
@ManagedBean(name= TreeEvents.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TreeEvents extends ComponentExampleImpl<TreeEvents> implements Serializable {

    public static final String BEAN_NAME = "treeEvents";

    private String listenerStatus = "No navigation have been clicked yet.";
    private String leafStatus = "No nodes have been clicked yet.";
    private String clicked = null;

    public TreeEvents() {
                super(TreeEvents.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getListenerStatus() { return listenerStatus; }
    public String getLeafStatus() { return leafStatus; }
    public String getClicked() { return clicked; }

    public void setListenerStatus(String listenerStatus) { this.listenerStatus = listenerStatus; }
    public void setLeafStatus(String leafStatus) { this.leafStatus = leafStatus; }
    public void setClicked(String clicked) { this.clicked = clicked; }

    public void actionListener(ActionEvent event) {
        listenerStatus = System.currentTimeMillis() + ": Expanded or contracted a folder using navigation.";
    }

    public String leafClicked() {
        leafStatus = System.currentTimeMillis() + ": Clicked a node named '" + clicked + "'.";

        return null;
    }
}
