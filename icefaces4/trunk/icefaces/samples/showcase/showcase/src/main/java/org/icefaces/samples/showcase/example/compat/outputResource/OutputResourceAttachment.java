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

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = OutputResourceBean.BEAN_NAME,
        title = "example.compat.outputResource.attachment.title",
        description = "example.compat.outputResource.attachment.description",
        example = "/resources/examples/compat/outputResource/outputResourceAttachment.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="outputResourceAttachment.xhtml",
                    resource = "/resources/examples/compat/"+
                               "outputResource/outputResourceAttachment.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="OutputResourceAttachment.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/outputResource/OutputResourceAttachment.java")
        }
)
@ManagedBean(name= OutputResourceAttachment.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class OutputResourceAttachment extends ComponentExampleImpl<OutputResourceAttachment> implements Serializable {
	
	public static final String BEAN_NAME = "outputResourceAttachment";
	
	private boolean attachment = true;
	
	public OutputResourceAttachment() {
		super(OutputResourceAttachment.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public boolean getAttachment() { return attachment; }
	
	public void setAttachment(boolean attachment) { this.attachment = attachment; }
}
