/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;

public class DefaultMenuModel implements MenuModel, Serializable {

	private List<UIComponent> submenus;
	private List<UIComponent> menuItems;
	private List<UIComponent> menus;
	
	public DefaultMenuModel() {
		submenus = new ArrayList<UIComponent>();
		menuItems = new ArrayList<UIComponent>();
		menus = new ArrayList<UIComponent>();
	}

	public List<UIComponent> getSubmenus() {
		return submenus;
	}
	
	public void addSubmenu(UIComponent submenu) {
		submenus.add(submenu);
		addMenu(submenu);
	}

	public void addMenuItem(UIComponent menuItem) {
		menuItems.add(menuItem);
		addMenu(menuItem);
	}

	public List<UIComponent> getMenuItems() {
		return menuItems;
	}
	
	public void addMenu(UIComponent menu) {
		menus.add(menu);
	}

	public List<UIComponent> getMenus() {
		return menus;
	}
}