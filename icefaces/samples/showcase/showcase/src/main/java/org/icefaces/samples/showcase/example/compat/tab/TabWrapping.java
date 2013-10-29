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

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = TabBean.BEAN_NAME,
        title = "example.compat.tab.wrapping.title",
        description = "example.compat.tab.wrapping.description",
        example = "/resources/examples/compat/tab/tabWrapping.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tabWrapping.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tab/tabWrapping.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TabWrapping.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tab/TabWrapping.java")
        }
)
@ManagedBean(name= TabWrapping.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabWrapping extends ComponentExampleImpl<TabWrapping> implements Serializable {
	
    public static final String BEAN_NAME = "tabWrapping";

    public TabWrapping() {
            super(TabWrapping.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
