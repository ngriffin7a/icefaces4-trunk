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

package org.icefaces.samples.showcase.example.compat.progress;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.util.FacesUtils;

@ComponentExample(
        parent = ProgressBean.BEAN_NAME,
        title = "example.compat.progress.style.title",
        description = "example.compat.progress.style.description",
        example = "/resources/examples/compat/progress/progressStyle.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="progressStyle.xhtml",
                    resource = "/resources/examples/compat/"+
                               "progress/progressStyle.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ProgressStyle.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/progress/ProgressStyle.java")
        }
)
@ManagedBean(name= ProgressStyle.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ProgressStyle extends ComponentExampleImpl<ProgressStyle> implements Serializable {
	
	public static final String BEAN_NAME = "progressStyle";
	
	private SelectItem[] availableClasses = new SelectItem[] {
	    new SelectItem("progressRed", "Red Color"),
	    new SelectItem("progressPurple", "Purple Image"),
	    new SelectItem("progressThick", "Extra Thick")
	};
	private String styleClass = availableClasses[0].getValue().toString();
	
	public ProgressStyle() {
		super(ProgressStyle.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
    
    public void startTask(ActionEvent event)
    {
        LongTaskManager threadBean = (LongTaskManager)FacesUtils.getManagedBean(LongTaskManager.BEAN_NAME);
        threadBean.startThread(10, 10, 650, 3);
    }

	public SelectItem[] getAvailableClasses() { return availableClasses; }
	public String getStyleClass() { return styleClass; }
	
	public void setStyleClass(String styleClass) { this.styleClass = styleClass; }
}
