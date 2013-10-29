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

package org.icefaces.samples.showcase.example.ace.checkboxButton;

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
    title = "example.ace.checkboxButton.title",
    description = "example.ace.checkboxButton.description",
    example = "/resources/examples/ace/checkboxButton/checkboxbutton.xhtml"
)
@ExampleResources(
    resources ={
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                         title="checkboxbutton.xhtml",
                         resource = "/resources/examples/ace/checkboxButton/checkboxbutton.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                         title="CheckboxButtonBean.java",
                         resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/checkboxButton/CheckboxButtonBean.java")
    }
)

@Menu(
    title = "menu.ace.checkboxButton.subMenu.title",
    menuLinks = {
        @MenuLink(title = "menu.ace.checkboxButton.subMenu.main", isDefault = true,
                  exampleBeanName = CheckboxButtonBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.checkboxButton.subMenu.custom",
                  exampleBeanName = CheckboxButtonCustomBean.BEAN_NAME)
    }
)
@ManagedBean(name= CheckboxButtonBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CheckboxButtonBean extends ComponentExampleImpl<CheckboxButtonBean> implements Serializable {

    public static final String BEAN_NAME = "checkboxButton";
    private boolean selected = true;

    public CheckboxButtonBean() {
        super(CheckboxButtonBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public String getBoxValueDescription() {
        if(selected)
            return "selected";
        else
            return "unselected";
    }
}
