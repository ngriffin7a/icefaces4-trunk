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

package org.icefaces.samples.showcase.example.compat.positioned;

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
        parent = PositionedBean.BEAN_NAME,
        title = "example.compat.positioned.sort.title",
        description = "example.compat.positioned.sort.description",
        example = "/resources/examples/compat/positioned/positionedSort.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="positionedSort.xhtml",
                    resource = "/resources/examples/compat/"+
                               "positioned/positionedSort.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PositionedSort.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/positioned/PositionedSort.java")
        }
)
@ManagedBean(name= PositionedSort.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PositionedSort extends ComponentExampleImpl<PositionedSort> implements Serializable {
	
	public static final String BEAN_NAME = "positionedSort";
	
	public PositionedSort() {
		super(PositionedSort.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public void sortAscending(ActionEvent event) {
	    PositionedData.sortAscending();
	}
	
	public void sortDescending(ActionEvent event) {
	    PositionedData.sortDescending();
	}
	
	public void sortRandom(ActionEvent event) {
	    PositionedData.sortRandom();
	}
}
