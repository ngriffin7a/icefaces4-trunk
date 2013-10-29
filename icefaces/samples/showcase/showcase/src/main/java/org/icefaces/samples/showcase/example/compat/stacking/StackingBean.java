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

package org.icefaces.samples.showcase.example.compat.stacking;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.stacking.title",
        description = "example.compat.stacking.description",
        example = "/resources/examples/compat/stacking/stacking.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="stacking.xhtml",
                    resource = "/resources/examples/compat/"+
                               "stacking/stacking.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="StackingBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/stacking/StackingBean.java")
        }
)
@Menu(
	title = "menu.compat.stacking.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.stacking.subMenu.main",
                    isDefault = true,
                    exampleBeanName = StackingBean.BEAN_NAME)
})
@ManagedBean(name= StackingBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class StackingBean extends ComponentExampleImpl<StackingBean> implements Serializable {
	
    public static final String BEAN_NAME = "stacking";

    private SelectItem[] availableDemos = new SelectItem[] {
        new SelectItem("address", "Address Form"),
        new SelectItem("auction", "Auction Monitor"),
        new SelectItem("memory",  "Memory Game"),
        new SelectItem("mobile", "Mobile Ajax"),
        new SelectItem("webmc", "WebMC Presenter")
    };
    private String selectedDemo = availableDemos[0].getValue().toString();
    private String selectedDescription = availableDemos[0].getLabel();

    public StackingBean() {
            super(StackingBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public SelectItem[] getAvailableDemos() { return availableDemos; }
    public String getSelectedDemo() { return selectedDemo; }

    public void setSelectedDemo(String selectedDemo) {
    this.selectedDemo = selectedDemo;
    for (SelectItem si : availableDemos) {
        if (si.getValue().equals(selectedDemo)) {
            selectedDescription = si.getLabel();
            return;
        }
    }
    selectedDescription = selectedDemo;
}

    public String getSelectedDescription() { return selectedDescription; }
}
