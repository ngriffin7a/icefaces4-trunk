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
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.confirmation.title",
        description = "example.compat.confirmation.description",
        example = "/resources/examples/compat/confirmation/confirmation.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="confirmation.xhtml",
                    resource = "/resources/examples/compat/"+
                               "confirmation/confirmation.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ConfirmationBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/confirmation/ConfirmationBean.java")
        }
)
@Menu(
	title = "menu.compat.confirmation.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.confirmation.subMenu.main",
                    isDefault = true,
                    exampleBeanName = ConfirmationBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.confirmation.subMenu.label",
                    exampleBeanName = ConfirmationLabel.BEAN_NAME),
            @MenuLink(title = "menu.compat.confirmation.subMenu.button",
                    exampleBeanName = ConfirmationButton.BEAN_NAME),
            @MenuLink(title = "menu.compat.confirmation.subMenu.display",
                    exampleBeanName = ConfirmationDisplay.BEAN_NAME),
            @MenuLink(title = "menu.compat.confirmation.subMenu.mouse",
                    exampleBeanName = ConfirmationMouse.BEAN_NAME)
})
@ManagedBean(name= ConfirmationBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ConfirmationBean extends ComponentExampleImpl<ConfirmationBean> implements Serializable {
	
	public static final String BEAN_NAME = "confirmation";
	
	private Random randomizer = new Random(System.nanoTime());
	private int number = 0;
	
	public ConfirmationBean() {
		super(ConfirmationBean.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public int getNumber() { return number; }
	
	public void setNumber(int number) { this.number = number; }
	
	public void generateNumber(ActionEvent event) {
	    setNumber(1+randomizer.nextInt(1000));
	}
	
	public void clearNumber(ActionEvent event) {
	    setNumber(0);
	}
}
