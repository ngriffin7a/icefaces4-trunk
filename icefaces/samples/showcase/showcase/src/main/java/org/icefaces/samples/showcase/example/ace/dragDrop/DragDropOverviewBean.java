
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

package org.icefaces.samples.showcase.example.ace.dragDrop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= DragDropOverviewBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DragDropOverviewBean implements Serializable
{
    public static final String BEAN_NAME = "dragDropOverviewBean";
	public String getBeanName() { return BEAN_NAME; }
    
    private List<DragDropItem> items;
    
    public DragDropOverviewBean()
    {
        populateListWithItems();
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
