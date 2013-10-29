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

package org.icefaces.samples.showcase.example.compat.collapsible;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.collapsible.title",
        description = "example.compat.collapsible.description",
        example = "/resources/examples/compat/collapsible/collapsible.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="collapsible.xhtml",
                    resource = "/resources/examples/compat/"+
                               "collapsible/collapsible.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CollapsibleBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/collapsible/CollapsibleBean.java")
        }
)
@Menu(
	title = "menu.compat.collapsible.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.collapsible.subMenu.main",
                    isDefault = true,
                    exampleBeanName = CollapsibleBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.collapsible.subMenu.toggle",
                    exampleBeanName = CollapsibleToggle.BEAN_NAME),
            @MenuLink(title = "menu.compat.collapsible.subMenu.expanded",
                    exampleBeanName = CollapsibleExpanded.BEAN_NAME),
            @MenuLink(title = "menu.compat.collapsible.subMenu.event",
                    exampleBeanName = CollapsibleEvent.BEAN_NAME),
            @MenuLink(title = "menu.compat.collapsible.subMenu.multiple",
                    exampleBeanName = CollapsibleMultiple.BEAN_NAME)
})
@ManagedBean(name= CollapsibleBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CollapsibleBean extends ComponentExampleImpl<CollapsibleBean> implements Serializable {
	
	public static final String BEAN_NAME = "collapsible";
	
	public CollapsibleBean() {
		super(CollapsibleBean.class);
	}

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
