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
        title = "example.compat.progress.label.title",
        description = "example.compat.progress.label.description",
        example = "/resources/examples/compat/progress/progressLabel.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="progressLabel.xhtml",
                    resource = "/resources/examples/compat/"+
                               "progress/progressLabel.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ProgressLabel.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/progress/ProgressLabel.java")
        }
)
@ManagedBean(name= ProgressLabel.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ProgressLabel extends ComponentExampleImpl<ProgressLabel> implements Serializable {
	
	public static final String BEAN_NAME = "progressLabel";
	
	private SelectItem[] availablePositions = new SelectItem[] {
	    new SelectItem("embed", "Embedded"),
	    new SelectItem("left", "Left"),
	    new SelectItem("right", "Right"),
	    new SelectItem("top", "Top Left"),
	    new SelectItem("topcenter", "Top Center"),
	    new SelectItem("topright", "Top Right"),
	    new SelectItem("bottom", "Bottom Left"),
	    new SelectItem("bottomcenter", "Bottom Center"),
	    new SelectItem("bottomright", "Bottom Right")
	};
	
	private String label = "In Progress";
	private String complete = "Progress Complete!";
	private String position = availablePositions[0].getValue().toString();
	
	public ProgressLabel() {
		super(ProgressLabel.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
    
    public void startTask(ActionEvent event)
    {
        LongTaskManager threadBean = (LongTaskManager)FacesUtils.getManagedBean(LongTaskManager.BEAN_NAME);
        threadBean.startThread(10, 10, 650, 2);
    }

	public SelectItem[] getAvailablePositions() { return availablePositions; }
	public String getLabel() { return label; }
	public String getComplete() { return complete; }
	public String getPosition() { return position; }
	
	public void setLabel(String label) { this.label = label; }
	public void setComplete(String complete) { this.complete = complete; }
	public void setPosition(String position) { this.position = position; }
}
