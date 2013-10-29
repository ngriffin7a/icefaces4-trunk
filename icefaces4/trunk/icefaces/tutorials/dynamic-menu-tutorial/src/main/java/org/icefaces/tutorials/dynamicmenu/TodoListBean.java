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

package org.icefaces.tutorials.dynamicmenu;

import org.icefaces.ace.component.ajax.AjaxBehavior;
import org.icefaces.ace.component.menuitem.MenuItem;
import org.icefaces.ace.component.submenu.Submenu;
import org.icefaces.ace.event.SelectEvent;
import org.icefaces.ace.event.UnselectEvent;
import org.icefaces.ace.model.DefaultMenuModel;
import org.icefaces.ace.model.MenuModel;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.MethodExpressionActionListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoListBean implements Serializable {

	private MenuModel defaultMenu;
	private MenuModel newMenu;
	private MenuModel inProgressMenu;
	private MenuModel stoppedMenu;
	private MenuModel doneMenu;
	private List<Item> items;
	private Item selectedItem = null;
	
	public TodoListBean() {
		items = new ArrayList<Item>();
		items.add(new Item("Sample task A"));
		items.add(new Item("Sample task B"));
		items.add(new Item("Sample task C"));
		items.add(new Item("Sample task D"));
		constructMenus();
	}

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
	
	private void constructMenus() {
	
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ExpressionFactory expressionFactory = FacesContext.getCurrentInstance().getApplication().getExpressionFactory();
		
		// default menu
		defaultMenu = new DefaultMenuModel();
		
		Submenu defaultSubmenu = new Submenu();
		defaultSubmenu.setLabel("Menu");
		defaultMenu.addSubmenu(defaultSubmenu);
		
		// 'New' menu
		newMenu = new DefaultMenuModel();
		
		Submenu newSubmenu = new Submenu();
		newSubmenu.setLabel("Menu");
		newMenu.addSubmenu(newSubmenu);
		
		MenuItem newStart = new MenuItem();
		newStart.setId("start");			
		newStart.setValue("Start");
		newStart.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(elContext, "#{todoListBean.start}", null, new Class[] { ActionEvent.class })));
		addAjaxBehaviorTo(newStart);
		newSubmenu.getChildren().add(newStart);
		
		MenuItem newDone = new MenuItem();
		newDone.setId("new-done");			
		newDone.setValue("Done");
		newDone.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(elContext, "#{todoListBean.done}", null, new Class[] { ActionEvent.class })));
		addAjaxBehaviorTo(newDone);
		newSubmenu.getChildren().add(newDone);	
		
		// 'In Progress' menu
		inProgressMenu = new DefaultMenuModel();
		
		Submenu inProgressSubmenu = new Submenu();
		inProgressSubmenu.setLabel("Menu");
		inProgressMenu.addSubmenu(inProgressSubmenu);
		
		MenuItem inProgressStop = new MenuItem();
		inProgressStop.setId("stop");			
		inProgressStop.setValue("Stop");
		inProgressStop.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(elContext, "#{todoListBean.stop}", null, new Class[] { ActionEvent.class })));
		addAjaxBehaviorTo(inProgressStop);
		inProgressSubmenu.getChildren().add(inProgressStop);
		
		MenuItem inProgressDone = new MenuItem();
		inProgressDone.setId("inprogress-done");			
		inProgressDone.setValue("Done");
		inProgressDone.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(elContext, "#{todoListBean.done}", null, new Class[] { ActionEvent.class })));
		addAjaxBehaviorTo(inProgressDone);
		inProgressSubmenu.getChildren().add(inProgressDone);
		
		// 'Stopped' menu
		stoppedMenu = new DefaultMenuModel();
		
		Submenu stoppedSubmenu = new Submenu();
		stoppedSubmenu.setLabel("Menu");
		stoppedMenu.addSubmenu(stoppedSubmenu);
		
		MenuItem stoppedRestart = new MenuItem();
		stoppedRestart.setId("restart");			
		stoppedRestart.setValue("Restart");
		stoppedRestart.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(elContext, "#{todoListBean.start}", null, new Class[] { ActionEvent.class })));
		addAjaxBehaviorTo(stoppedRestart);
		stoppedSubmenu.getChildren().add(stoppedRestart);
		
		MenuItem stoppedDone = new MenuItem();
		stoppedDone.setId("stopped-done");			
		stoppedDone.setValue("Done");
		stoppedDone.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(elContext, "#{todoListBean.done}", null, new Class[] { ActionEvent.class })));
		addAjaxBehaviorTo(stoppedDone);
		stoppedSubmenu.getChildren().add(stoppedDone);
		
		// 'Done' menu
		doneMenu = new DefaultMenuModel();
		
		Submenu doneSubmenu = new Submenu();
		doneSubmenu.setLabel("Menu");
		doneMenu.addSubmenu(doneSubmenu);
		
		MenuItem doneRemove = new MenuItem();
		doneRemove.setId("remove");			
		doneRemove.setValue("Remove");
		doneRemove.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(elContext, "#{todoListBean.remove}", null, new Class[] { ActionEvent.class })));
		addAjaxBehaviorTo(doneRemove);
		doneSubmenu.getChildren().add(doneRemove);
	}
	
	private void addAjaxBehaviorTo(MenuItem menuItem) {
		AjaxBehavior ajaxBehavior = new AjaxBehavior();
		ajaxBehavior.setExecute("@form");
		ajaxBehavior.setRender("@form");
		menuItem.addClientBehavior("activate", ajaxBehavior);
	}
	
    public MenuModel getMenu() {
		
		if (selectedItem == null) {
			return defaultMenu;
		} else if ("New".equals(selectedItem.getStatus())) {
			return newMenu;
		} else if ("In Progress".equals(selectedItem.getStatus())) {
			return inProgressMenu;
		} else if ("Stopped".equals(selectedItem.getStatus())) {
			return stoppedMenu;
		} else if ("Done".equals(selectedItem.getStatus())) {
			return doneMenu;
		}
		
        return defaultMenu;
    }
	
	public void rowSelectListener(SelectEvent event) {
		selectedItem = (Item) event.getObject();
	}
	
	public void rowDeselectListener(AjaxBehaviorEvent event) {
		selectedItem = null;
	}
	
	public void start(ActionEvent event) {
		selectedItem.setStatus("In Progress");
	}
	
	public void stop(ActionEvent event) {
		selectedItem.setStatus("Stopped");
	}
	
	public void restart(ActionEvent event) {
		selectedItem.setStatus("In Progress");
	}
	
	public void done(ActionEvent event) {
		selectedItem.setStatus("Done");
		selectedItem.finished = new Date();
	}
	
	public void remove(ActionEvent event) {
		items.remove(selectedItem);
		selectedItem = null;
	}
	
	
	private String newItem = "";
	
	public String getNewItem() {
		return newItem;
	}
	
	public void setNewItem(String newItem) {
		this.newItem = newItem;
	}
	
	public void addNewItem(ActionEvent event) {
		items.add(new Item(newItem));
		newItem = "";
	}
	
	public static class Item implements Serializable {
		
		private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		private String description;
		private String status;
		private Date created;
		private Date finished;

		public Item(String description) {
			this.description = description;
			this.status = "New";
			this.created = new Date();
			this.finished = null;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
		
		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
		
		public String getCreated() {
			return dateFormat.format(created);
		}
		
		public String getFinished() {
			if (finished == null) return "";
			return dateFormat.format(finished);
		}
	}
}

