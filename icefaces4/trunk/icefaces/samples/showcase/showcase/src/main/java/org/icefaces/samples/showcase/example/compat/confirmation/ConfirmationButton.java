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
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = ConfirmationBean.BEAN_NAME,
        title = "example.compat.confirmation.button.title",
        description = "example.compat.confirmation.button.description",
        example = "/resources/examples/compat/confirmation/confirmationButton.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="confirmationButton.xhtml",
                    resource = "/resources/examples/compat/"+
                               "confirmation/confirmationButton.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ConfirmationButton.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/confirmation/ConfirmationButton.java")
        }
)
@ManagedBean(name= ConfirmationButton.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ConfirmationButton extends ComponentExampleImpl<ConfirmationButton> implements Serializable {
	
	public static final String BEAN_NAME = "confirmationButton";
	
	private SelectItem[] availableTypes = new SelectItem[] {
	    new SelectItem("normal", "Both Buttons"),
	    new SelectItem("acceptOnly", "Accept Only"),
	    new SelectItem("cancelOnly", "Cancel Only")
	};
	
	private String text;
	private String type = availableTypes[0].getValue().toString();
	
	public ConfirmationButton() {
		super(ConfirmationButton.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public SelectItem[] getAvailableTypes() { return availableTypes; }
	public String getText() { return text; }
	public String getType() { return type; }
	
	public void setText(String text) { this.text = text; }
	public void setType(String type) { this.type = type; }
	
	public void clearText(ActionEvent event) {
	    setText(null);
	}
}
