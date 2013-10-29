
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

package org.icefaces.samples.showcase.example.ace.dragDrop;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.icefaces.samples.showcase.example.compat.dragdrop.DragDropItem;

@ComponentExample(
        title = "example.ace.dragDrop.title",
        description = "example.ace.dragDrop.description",
        example = "/resources/examples/ace/dragDrop/dragDropOverview.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dragDropOverview.xhtml",
                    resource = "/resources/examples/ace/dragDrop/dragDropOverview.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DragDropOverview.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dragDrop/DragDropOverviewBean.java")
        }
)
@Menu(
	title = "menu.ace.maskedEntry.subMenu.main",
	menuLinks = {
	        @MenuLink(title = "menu.ace.dragDrop.subMenu.main",
	                isDefault = true,
                    exampleBeanName = DragDropOverviewBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dragDrop.subMenu.draggable",
                    exampleBeanName = DraggableOverviewBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dragDrop.subMenu.dataTableIntegration",
                    exampleBeanName = DataTableIntegrationBean.BEAN_NAME)
    }
)
@ManagedBean(name= DragDropOverviewBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DragDropOverviewBean extends ComponentExampleImpl<DragDropOverviewBean> implements Serializable
{
    public static final String BEAN_NAME = "dragDropOverviewBean";
    
    private List<DragDropItem> items;
    
    public DragDropOverviewBean()
    {
        super(DragDropOverviewBean.class);
        populateListWithItems();
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    private void populateListWithItems()
    {
        items = new ArrayList<DragDropItem>();
        items.add(new DragDropItem(1, "Laptop", "/resources/css/images/dragdrop/laptop.png", 999.99d, "electronicDevice"));
        items.add(new DragDropItem(3, "Monitor", "/resources/css/images/dragdrop/monitor.png", 259.99d, "electronicDevice"));
        items.add(new DragDropItem(5, "Aubergine", "/resources/css/images/dragdrop/aubergine.png", 2.99d, "food"));
        items.add(new DragDropItem(6, "Capsicum", "/resources/css/images/dragdrop/capsicum.png", 3.99d, "food"));
    }

    public List<DragDropItem> getItems() {
        return items;
    }

    public void setItems(List<DragDropItem> items) {
        this.items = items;
    }
}
