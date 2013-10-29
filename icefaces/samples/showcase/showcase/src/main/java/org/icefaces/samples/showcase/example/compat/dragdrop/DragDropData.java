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
import java.util.Random;
import java.text.NumberFormat;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= DragDropData.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DragDropData implements Serializable {
    
	public static final String BEAN_NAME = "dragdropData";
	
	private NumberFormat priceFormatter;
	private Random randomizer = new Random(System.nanoTime());
	
	private List<DragDropItem> items;
	
	public DragDropData() {
	    super();
                    init();
	}
	
	private void init() {
	    priceFormatter = NumberFormat.getInstance();
	    priceFormatter.setGroupingUsed(false);
	    priceFormatter.setMinimumFractionDigits(2);
	    priceFormatter.setMaximumFractionDigits(2);
	    
	    items = generateItems();
	}
	
	public List<DragDropItem> getItems() { return items; }
	
	public void setItems(List<DragDropItem> items) { this.items = items; }
	
	private List<DragDropItem> generateItems() {
	    List<DragDropItem> toReturn = new ArrayList<DragDropItem>(4);
	    
	    toReturn.add(new DragDropItem(1, "Laptop", "laptop.png",
	                                  generatePrice(500, 2000),
	                                  generateQuantity(10, 30)));
	    toReturn.add(new DragDropItem(2, "Monitor", "monitor.png",
	                                  generatePrice(300, 400),
	                                  generateQuantity(2, 5)));
	    toReturn.add(new DragDropItem(3, "Desktop", "desktop.png",
	                                  generatePrice(200, 1000),
	                                  generateQuantity(100, 40)));
	    toReturn.add(new DragDropItem(4, "PDA", "pda.png",
	                                  generatePrice(80, 200),
	                                  generateQuantity(20, 20)));
	    
	    return toReturn;
	}
	
	private double generatePrice(int min, int max) {
	    return Double.parseDouble(priceFormatter.format(
	                ((double)(min+randomizer.nextInt(max))) + randomizer.nextDouble()));
	}
	
	private int generateQuantity(int min, int max) {
	    return min+randomizer.nextInt(max);
	}
	
	public DragDropItem getItemById(int id) {
	    for (DragDropItem currentItem : items) {
	        if (id == currentItem.getId()) {
	            return currentItem;
	        }
	    }
	    
	    return null;
	}
	
	public DragDropItem buyItem(int id) {
	    return buyItem(id, 1);
	}
	
	public DragDropItem buyItem(int id, int quantity) {
	    DragDropItem toBuy = getItemById(id);
	    
	    if (toBuy != null) {
            toBuy.reduceQuantity(quantity);
	            
            return new DragDropItem(toBuy, quantity);
	    }
	    
	    return null;
	}
	
	public void returnItem(int id) {
	    returnItem(id, 1);
	}
	
	public void returnItem(int id, int quantity) {
	    DragDropItem toReturn = getItemById(id);
	    
	    if (toReturn != null) {
            toReturn.increaseQuantity(quantity);
	    }
	}
}
