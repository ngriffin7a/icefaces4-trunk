/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.samples.showcase.example.ace.checkboxButtons;

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

import java.util.*;

@ComponentExample(
    title = "example.ace.checkboxButtons.title",
    description = "example.ace.checkboxButtons.description",
    example = "/resources/examples/ace/checkboxButtons/checkboxButtons.xhtml"
)
@ExampleResources(
    resources ={
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                         title="checkboxButtons.xhtml",
                         resource = "/resources/examples/ace/checkboxButtons/checkboxButtons.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                         title="CheckboxButtonsBean.java",
                         resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/checkboxButtons/CheckboxButtonsBean.java")
    }
)

@Menu(
    title = "menu.ace.checkboxButtons.subMenu.title",
    menuLinks = {
        @MenuLink(title = "menu.ace.checkboxButtons.subMenu.main", isDefault = true,
                  exampleBeanName = CheckboxButtonsBean.BEAN_NAME)
    }
)
@ManagedBean(name= CheckboxButtonsBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CheckboxButtonsBean extends ComponentExampleImpl<CheckboxButtonsBean> implements Serializable {

    public static final String BEAN_NAME = "checkboxButtons";
	public String getBeanName() { return BEAN_NAME; }

	private List<String> selected = new ArrayList<String>();

    public CheckboxButtonsBean() {
        super(CheckboxButtonsBean.class);
		selected.add("One");
		selected.add("Three");
		selected.add("Five");
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public List<String> getSelected() { return selected; }
	public void setSelected(List<String> selected) { this.selected = selected; }
}
