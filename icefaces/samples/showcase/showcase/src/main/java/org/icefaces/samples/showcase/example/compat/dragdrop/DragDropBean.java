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

package org.icefaces.samples.showcase.example.compat.dragdrop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;
import com.icesoft.faces.component.dragdrop.DndEvent;
import com.icesoft.faces.component.dragdrop.DropEvent;

import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.dragdrop.title",
        description = "example.compat.dragdrop.description",
        example = "/resources/examples/compat/dragdrop/dragdrop.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dragdrop.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dragdrop/dragdrop.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DragDropBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dragdrop/DragDropBean.java")
        }
)
@Menu(
	title = "menu.compat.dragdrop.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.dragdrop.subMenu.main",
                    isDefault = true,
                    exampleBeanName = DragDropBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.dragdrop.subMenu.events",
                    exampleBeanName = DragDropEvents.BEAN_NAME),
            @MenuLink(title = "menu.compat.dragdrop.subMenu.effect",
                    exampleBeanName = DragDropEffect.BEAN_NAME)
})
@ManagedBean(name= DragDropBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DragDropBean extends ComponentExampleImpl<DragDropBean> implements Serializable {
	
	public static final String BEAN_NAME = "dragdrop";
	
    private Effect dropEffect;
	private int toReturn;
	private double totalPrice = 0.0;
	private List<DragDropItem> purchased = new ArrayList<DragDropItem>(0);
	
	public DragDropBean() {
                    super(DragDropBean.class);
                    init();
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	private void init() {
                    dropEffect = new Highlight("#FDA505");
	    dropEffect.setFired(true);
	}
	
	public Effect getDropEffect() { return dropEffect; }
	public int getToReturn() { return toReturn; }
	public double getTotalPrice() { return totalPrice; }
	public List<DragDropItem> getPurchased() { return purchased; }
	public boolean getHasPurchased() { return purchased.size() > 0; }
	
	public void setDropEffect(Effect dropEffect) { this.dropEffect = dropEffect; }
	public void setToReturn(int toReturn) { this.toReturn = toReturn; }
	public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
	public void setPurchased(List<DragDropItem> purchased) { this.purchased = purchased; }
	
	public double recalculateTotal() {
	    totalPrice = 0.0;
	    
        for (DragDropItem currentItem : purchased) {
            totalPrice += currentItem.getTotalPrice();
        }
        
        return totalPrice;
	}
	
	public DragDropItem getItemById(int id) {
	    for (DragDropItem currentItem : purchased) {
	        if (id == currentItem.getId()) {
	            return currentItem;
	        }
	    }
	    
	    return null;
	}
	
	public void addItem(int id) {
	    DragDropData data =
                    (DragDropData)FacesUtils.getManagedBean(DragDropData.BEAN_NAME);
                    
        DragDropItem toBuy = data.buyItem(id);
        if (toBuy != null) {
            // Check if we already have the item
            // If we do then simply increase the quantity
            if (!addQuantityToPurchased(id)) {
                // Otherwise add a new instance
                purchased.add(toBuy);
            }
            
            recalculateTotal();
        }
	}
	
	public void removeItem(int id, boolean all) {
	    DragDropItem toRemove = getItemById(id);
	    
	    if (toRemove != null) {
	        if (!all) {
	            toRemove.reduceQuantity();
	        }
	        else {
	            toRemove.reduceQuantity(toRemove.getQuantity());
	        }
            
            if (toRemove.isOutOfStock()) {
                purchased.remove(toRemove);
            }
        }
	}
	
	private boolean addQuantityToPurchased(int id) {
	    return addQuantityToPurchased(id, 1);
	}
	
	private boolean addQuantityToPurchased(int id, int quantity) {
	    DragDropItem toAdd = getItemById(id);
	    
	    if (toAdd != null) {
            toAdd.increaseQuantity(quantity);
	            
            return true;
	    }
	    
	    return false;
	}
	
	public void dropListener(DropEvent event) {
	    // Check if the item was dropped
	    // In that case we want to try to add it
	    if (DndEvent.DROPPED == event.getEventType()) {
	        if ((event.getTargetDragValue() != null) &&
	            (event.getTargetDragValue() instanceof Integer)) {
                addItem((Integer)event.getTargetDragValue());
            }
	    }
	    // Otherwise just fire an effect to let the user see they are over the drop target
	    else if (DndEvent.HOVER_START == event.getEventType()) {
	        dropEffect.setFired(false);
	    }
	}
	
	public String returnItemAction() {
	    if (getHasPurchased()) {
	        // First return the item to the main inventory
            DragDropData data =
                (DragDropData)FacesUtils.getManagedBean(DragDropData.BEAN_NAME);
	        data.returnItem(toReturn);
	        
	        // Then remove our local purchased item
	        removeItem(toReturn, false);
	        
	        // Finally recalculate the total
	        recalculateTotal();
	    }
	    
	    return null;
	}
	
	public void returnAll(ActionEvent event) {
	    if (getHasPurchased()) {
	        // Get a full list of the items to return
	        List<Integer> returnIds = new ArrayList<Integer>(purchased.size());
	        for (DragDropItem currentItem : purchased) {
	            returnIds.add(currentItem.getId());
	        }
	        
            DragDropData data =
                (DragDropData)FacesUtils.getManagedBean(DragDropData.BEAN_NAME);
            
            // Return each item one by one, for their full quantity
            for (Integer toReturn : returnIds) {
                data.returnItem(toReturn,
                                getItemById(toReturn).getQuantity());
                removeItem(toReturn, true);
            }
            
            // Finally recalculate the total
            recalculateTotal();
	    }
	}
}
