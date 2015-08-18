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

package org.icefaces.samples.showcase.example.ace.radioButtons;

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
    title = "example.ace.radioButtons.title",
    description = "example.ace.radioButtons.description",
    example = "/resources/examples/ace/radioButtons/radioButtons.xhtml"
)
@ExampleResources(
    resources ={
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                         title="radioButtons.xhtml",
                         resource = "/resources/examples/ace/radioButtons/radioButtons.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                         title="RadioButtonsBean.java",
                         resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/radioButtons/RadioButtonsBean.java")
    }
)

@Menu(
    title = "menu.ace.radioButtons.subMenu.title",
    menuLinks = {
        @MenuLink(title = "menu.ace.radioButtons.subMenu.main", isDefault = true,
                  exampleBeanName = RadioButtonsBean.BEAN_NAME),
		@MenuLink(title = "menu.ace.radioButtons.subMenu.label", exampleBeanName = RadioButtonsLabelBean.BEAN_NAME),
		@MenuLink(title = "menu.ace.radioButtons.subMenu.indicator", exampleBeanName = RadioButtonsIndicatorBean.BEAN_NAME),
		@MenuLink(title = "menu.ace.radioButtons.subMenu.reqStyle", exampleBeanName = RadioButtonsReqStyleBean.BEAN_NAME)
    }
)

@ManagedBean(name= RadioButtonsBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class RadioButtonsBean extends ComponentExampleImpl<RadioButtonsBean> implements Serializable {

    public static final String BEAN_NAME = "radioButtons";
	public String getBeanName() { return BEAN_NAME; }

	private List<String> selected = new ArrayList<String>();
	private boolean mutuallyExclusive = false;

    public RadioButtonsBean() {
        super(RadioButtonsBean.class);
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

	public boolean isMutuallyExclusive() { return mutuallyExclusive; }
	public void setMutuallyExclusive(boolean mutuallyExclusive) { this.mutuallyExclusive = mutuallyExclusive; }
}
