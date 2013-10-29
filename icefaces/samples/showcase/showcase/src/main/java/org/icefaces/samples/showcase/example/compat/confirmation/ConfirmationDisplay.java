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

package org.icefaces.samples.showcase.example.compat.confirmation;

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
        parent = ConfirmationBean.BEAN_NAME,
        title = "example.compat.confirmation.display.title",
        description = "example.compat.confirmation.display.description",
        example = "/resources/examples/compat/confirmation/confirmationDisplay.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="confirmationDisplay.xhtml",
                    resource = "/resources/examples/compat/"+
                               "confirmation/confirmationDisplay.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ConfirmationDisplay.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/confirmation/ConfirmationDisplay.java")
        }
)
@ManagedBean(name= ConfirmationDisplay.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ConfirmationDisplay extends ComponentExampleImpl<ConfirmationDisplay> implements Serializable {
	
	public static final String BEAN_NAME = "confirmationDisplay";
	
	private String text;
	private boolean draggable = true;
	
	public ConfirmationDisplay() {
		super(ConfirmationDisplay.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public String getText() { return text; }
	public boolean getDraggable() { return draggable; }
	
	public void setText(String text) { this.text = text; }
	public void setDraggable(boolean draggable) { this.draggable = draggable; }
	
	public void clearText(ActionEvent event) {
	    setText(null);
	}
}
