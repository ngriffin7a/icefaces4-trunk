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

package org.icefaces.samples.showcase.example.ace.radioButton;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;

@ComponentExample(
    title = "example.ace.radioButton.title",
    description = "example.ace.radioButton.description",
    example = "/resources/examples/ace/radioButton/radiobutton.xhtml"
)
@ExampleResources(
    resources ={
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                         title="radiobutton.xhtml",
                         resource = "/resources/examples/ace/radioButton/radiobutton.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                         title="RadioButtonBean.java",
                         resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/radioButton/RadioButtonBean.java")
    }
)

@Menu(
    title = "menu.ace.radioButton.subMenu.title",
    menuLinks = {
        @MenuLink(title = "menu.ace.radioButton.subMenu.main", isDefault = true,
                  exampleBeanName = RadioButtonBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.radioButton.subMenu.custom",
                  exampleBeanName = RadioButtonCustomBean.BEAN_NAME)
    }
)
@ManagedBean(name= RadioButtonBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class RadioButtonBean extends ComponentExampleImpl<RadioButtonBean> implements Serializable {

    public static final String BEAN_NAME = "radioButton";
    private boolean selected1 = true;
	private boolean selected2 = false;

    public RadioButtonBean() {
        super(RadioButtonBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public boolean isSelected1() {
        return selected1;
    }

    public void setSelected1(boolean selected) {
        this.selected1 = selected;
    }
	
    public boolean isSelected2() {
        return selected2;
    }

    public void setSelected2(boolean selected) {
        this.selected2 = selected;
    }
    
    public String getBoxValueDescription1() {
        if(selected1)
            return "Selected";
        else
            return "Not Selected";
    }
	
    public String getBoxValueDescription2() {
        if(selected2)
            return "Selected";
        else
            return "Not Selected";
    }
}
