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

package org.icefaces.samples.showcase.example.compat.dragdrop;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = DragDropBean.BEAN_NAME,
        title = "example.compat.dragdrop.effect.title",
        description = "example.compat.dragdrop.effect.description",
        example = "/resources/examples/compat/dragdrop/dragdropEffect.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dragdropEffect.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dragdrop/dragdropEffect.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DragDropEffect.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dragdrop/DragDropEffect.java")
        }
)
@ManagedBean(name= DragDropEffect.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DragDropEffect extends ComponentExampleImpl<DragDropEffect> implements Serializable {
	
	public static final String BEAN_NAME = "dragdropEffect";
	
	private SelectItem[] availableOptions = new SelectItem[] {
	    new SelectItem("revert", "Revert"),
	    new SelectItem("ghosting", "Ghost Duplicate"),
	    new SelectItem("solid", "Solid when Dragging")
	};
	private String[] options = new String[] { availableOptions[0].getValue().toString() };
	
	public DragDropEffect() {
		super(DragDropEffect.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public SelectItem[] getAvailableOptions() { return availableOptions; }
	public String[] getOptions() { return options; }
	public String getOptionsString() { return FacesUtils.join(options); }
	
	public void setOptions(String[] options) { this.options = options; }
}
