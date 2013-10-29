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

package org.icefaces.samples.showcase.example.compat.tab;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = TabBean.BEAN_NAME,
        title = "example.compat.tab.control.title",
        description = "example.compat.tab.control.description",
        example = "/resources/examples/compat/tab/tabControl.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tabControl.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tab/tabControl.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TabControl.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tab/TabControl.java")
        }
)
@ManagedBean(name= TabControl.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabControl extends ComponentExampleImpl<TabControl> implements Serializable {
	
    public static final String BEAN_NAME = "tabControl";

    private SelectItem[] availableIndexes = new SelectItem[] {
        new SelectItem(0, "Account"),
        new SelectItem(1, "Support"),
        new SelectItem(2, "Forum")
    };
    private int index = (Integer)availableIndexes[availableIndexes.length-1].getValue();

    public TabControl() {
            super(TabControl.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public SelectItem[] getAvailableIndexes() { return availableIndexes; }
    public int getIndex() { return index; }

    public void setIndex(int index) { this.index = index; }
}
