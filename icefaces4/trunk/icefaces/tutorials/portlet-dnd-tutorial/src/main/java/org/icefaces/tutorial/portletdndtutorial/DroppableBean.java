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

package org.icefaces.tutorial.portletdndtutorial;

import org.icefaces.ace.event.DragDropEvent;
import org.icefaces.application.PushRenderer;

import java.util.*;

public class DroppableBean implements java.io.Serializable {

    private WindowScopedBean windowScopedBean;

	private List<Item> items;
	
	public DroppableBean() {
		items = new ArrayList<Item>();
	}
	
	public void handleDrop(DragDropEvent e) {
		Item item = windowScopedBean.getDraggedItem();
		if (item != null) {
			items.add(item);
			List<Item> originalList = windowScopedBean.getOriginalList();
			if (originalList != null) {
				originalList.remove(item);
			}
		}
		PushRenderer.render("tutorial");
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public void setWindowScopedBean(WindowScopedBean windowScopedBean) {
		this.windowScopedBean = windowScopedBean;
	}
}