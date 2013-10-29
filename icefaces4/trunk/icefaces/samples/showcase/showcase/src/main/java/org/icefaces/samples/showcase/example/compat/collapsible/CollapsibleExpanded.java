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
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = CollapsibleBean.BEAN_NAME,
        title = "example.compat.collapsible.expanded.title",
        description = "example.compat.collapsible.expanded.description",
        example = "/resources/examples/compat/collapsible/collapsibleExpanded.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="collapsibleExpanded.xhtml",
                    resource = "/resources/examples/compat/"+
                               "collapsible/collapsibleExpanded.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CollapsibleExpanded.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/collapsible/CollapsibleExpanded.java")
        }
)
@ManagedBean(name= CollapsibleExpanded.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CollapsibleExpanded extends ComponentExampleImpl<CollapsibleExpanded> implements Serializable {
	
	public static final String BEAN_NAME = "collapsibleExpanded";
	
	private boolean expanded = true;
	
	public CollapsibleExpanded() {
		super(CollapsibleExpanded.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public boolean getExpanded() { return expanded; }
	
	public void setExpanded(boolean expanded) { this.expanded = expanded; }
}
