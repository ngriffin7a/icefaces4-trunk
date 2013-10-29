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

package org.icefaces.samples.showcase.example.compat.outputResource;

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
        parent = OutputResourceBean.BEAN_NAME,
        title = "example.compat.outputResource.type.title",
        description = "example.compat.outputResource.type.description",
        example = "/resources/examples/compat/outputResource/outputResourceType.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="outputResourceType.xhtml",
                    resource = "/resources/examples/compat/"+
                               "outputResource/outputResourceType.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="OutputResourceType.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/outputResource/OutputResourceType.java")
        }
)
@ManagedBean(name= OutputResourceType.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class OutputResourceType extends ComponentExampleImpl<OutputResourceType> implements Serializable {
	
	public static final String BEAN_NAME = "outputResourceType";
	
	private static final String TYPE_LINK = "link";
	private static final String IMAGE_URL = "";
	
	private SelectItem[] availableTypes = new SelectItem[] {
	    new SelectItem(TYPE_LINK, "Image"),
	    new SelectItem("button", "Button"),
	    new SelectItem("text", "Text")
	};
	private String type = availableTypes[0].getValue().toString();
	private String image = IMAGE_URL;
	
	public OutputResourceType() {
		super(OutputResourceType.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public boolean getTypeImage() { return TYPE_LINK.equals(type); }
	public SelectItem[] getAvailableTypes() { return availableTypes; }
	public String getType() { return type; }
	
	public void setType(String type) { this.type = type; }
}
