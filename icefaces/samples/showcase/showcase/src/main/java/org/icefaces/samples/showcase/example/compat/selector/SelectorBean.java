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

package org.icefaces.samples.showcase.example.compat.selector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.selector.title",
        description = "example.compat.selector.description",
        example = "/resources/examples/compat/selector/selector.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="selector.xhtml",
                    resource = "/resources/examples/compat/"+
                               "selector/selector.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="SelectorBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/selector/SelectorBean.java")
        }
)
@Menu(
	title = "menu.compat.selector.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.selector.subMenu.main",
                    isDefault = true,
                    exampleBeanName = SelectorBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.selector.subMenu.type",
                    exampleBeanName = SelectorType.BEAN_NAME),
            @MenuLink(title = "menu.compat.selector.subMenu.events",
                    exampleBeanName = SelectorEvents.BEAN_NAME),
            @MenuLink(title = "menu.compat.selector.subMenu.toggle",
                    exampleBeanName = SelectorToggle.BEAN_NAME),
            @MenuLink(title = "menu.compat.selector.subMenu.pre",
                    exampleBeanName = SelectorPre.BEAN_NAME)
})
@ManagedBean(name= SelectorBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SelectorBean extends ComponentExampleImpl<SelectorBean> implements Serializable {
	
    public static final String BEAN_NAME = "selector";
    private List<SelectableCar> data;
    private int rows;

    public SelectorBean() {
        super(SelectorBean.class);
        data = new ArrayList<SelectableCar>(DataTableData.getDefaultSelectableData());
        rows = DataTableData.DEFAULT_ROWS;
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public List<SelectableCar> getData() { return data; }
    public int getRows() {return rows;}
    public void setRows(int rows) {this.rows = rows;}
}
