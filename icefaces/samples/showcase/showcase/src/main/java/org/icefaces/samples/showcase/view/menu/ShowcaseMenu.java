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

package org.icefaces.samples.showcase.view.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.samples.showcase.view.menu.data.CentralDataList;
import org.icefaces.util.JavaScriptRunner;

@ManagedBean(name=ShowcaseMenu.BEAN_NAME,eager=true)
@ApplicationScoped
public class ShowcaseMenu implements Serializable {
	public static final String BEAN_NAME = "showcaseMenu";
    private static final Logger logger = Logger.getLogger(ShowcaseMenu.class.toString());
	
	public static final String HOME_PAGE = "showcase.jsf";
	
	// The menu is made up of a hierarchy of CategoryGroup -> ComponentGroup(s) -> Demo(s)
	private List<CategoryGroup> categories = CentralDataList.generate();
	private List<SelectItem> searchItems;
	private ComponentGroup defaultComponent;
	private Demo defaultDemo;
	
	@PostConstruct
	public void init() {
		initShowcaseMenu();
		initSearchItems();
	}
	
	private void initShowcaseMenu() {
		// Set our default component group and demo, which is used to initially display to the user, revert on error, etc.
		if (categories != null) {
			try{
				defaultComponent = categories.get(0).getComponents().get(0);
				defaultDemo = defaultComponent.getFirstDemo();
			}catch (IndexOutOfBoundsException failedDefault) {
				logger.log(Level.WARNING, "Failed to figure out a default component group and demo.");
			}
		}
	}
	
	private void initSearchItems() {
		// Loop through categories, build selectitems
		searchItems = new ArrayList<SelectItem>();
		for (CategoryGroup loopCategory : categories) {
			for (ComponentGroup loopComponent : loopCategory.getComponents()) {
				// First we want to add a generic search term for each demo
				if (loopComponent.getHasDemos()) {
					for (Demo loopDemo : loopComponent.getDemos()) {
						searchItems.add(new SelectItem(ParamHandler.URL_PARAM_GROUP + "=" + loopComponent.getName() + "&" + ParamHandler.URL_PARAM_DEMO + "=" + loopDemo.getName(),
								                       loopComponent.getName() + " - " + loopDemo.getName()));
					}
				
					// Then if we have keywords we'll add them and link to the Overview
					if (loopComponent.getKeywords() != null) {
						// Check if we have multiple keywords, in which case we'll split
						String[] keywords = { loopComponent.getKeywords() };
						if (loopComponent.getKeywords().contains(",")) {
							keywords = loopComponent.getKeywords().split(",");
						}
						
						for (String loopKeyword : keywords) {
							searchItems.add(new SelectItem(ParamHandler.URL_PARAM_GROUP + "=" + loopComponent.getName() + "&" + ParamHandler.URL_PARAM_DEMO + "=" + loopComponent.getFirstDemo().getName(),
									        loopKeyword + " (" + loopComponent.getName() + ")"));
						}
					}
				}
			}
		}
	}
	
	public String clickComponentGroup() {
		UserMenuState menuState = (UserMenuState)FacesUtils.getManagedBean(UserMenuState.BEAN_NAME);
		
		if ((menuState.getSelectedComponent() != null) && (menuState.getSelectedComponent().getHasDemos())) {
			menuState.setSelectedDemo(menuState.getSelectedComponent().getFirstDemo());
		}
		
		return null;
	}
	
	public void searchChanged(ValueChangeEvent event) {
		// Check our incoming value (from the search field dropdown) for a valid grp=component&exp=demo
		// We'll parse this and find a matching ComponentGroup / Demo object and set it properly to our UserMenuState
		if ((event.getNewValue() != null) &&
				(event.getNewValue().toString().contains(ParamHandler.URL_PARAM_GROUP)) &&
				(event.getNewValue().toString().contains(ParamHandler.URL_PARAM_DEMO))) {
			// Trim grp=ace:dateTimeEntry&exp=Overview to "ace:dateTimeEntry" (for newComponent) and "Overview" (for newDemo)
			// This means trimming out "grp=" and "&exp=", which is why we have the fancy substring work
			try{
				String value = event.getNewValue().toString();
				String newComponent = value.substring(value.indexOf(ParamHandler.URL_PARAM_GROUP)+ParamHandler.URL_PARAM_GROUP.length()+1,
												      value.indexOf(ParamHandler.URL_PARAM_DEMO)-1);
				String newDemo = value.substring(value.indexOf(ParamHandler.URL_PARAM_DEMO)+ParamHandler.URL_PARAM_DEMO.length()+1);
				
				UserMenuState menuState = (UserMenuState)FacesUtils.getManagedBean(UserMenuState.BEAN_NAME);
				CategoryGroup loopCategory = null;
				for (int i = 0; i < categories.size(); i++) {
					loopCategory = categories.get(i);
					
					for (ComponentGroup loopComponent : loopCategory.getComponents()) {
						if (loopComponent.getName().equals(newComponent)) {
							menuState.setSelectedComponent(loopComponent);
							menuState.setActiveIndex(i);
							
							// Also set our default demo, as we can override it if we have the "exp" param further in
							if (loopComponent.getHasDemos()) {
								for (Demo loopDemo : loopComponent.getDemos()) {
									if (loopDemo.getName().equals(newDemo)) {
										menuState.setSelectedDemo(loopDemo);
										
										break;
									}
								}
								
								// By default fallback to the first demo
								if (menuState.getSelectedDemo() == null) {
									menuState.setSelectedDemo(loopComponent.getFirstDemo());
								}
							}
							
							break;
						}
					}
				}
				
				// Update our URL if we have a valid component/demo
				if ((menuState.getSelectedComponent() != null) && (menuState.getSelectedDemo() != null)) {
					// If this component requires a full page refresh we'll redirect
					// We just use the value from the search field since it's properly formatted as grp=component&exp=demo
					// Note we mark the menu state as checked already, as we don't need to process the params again
					if (menuState.getSelectedComponent().isFullPageRefresh()) {
						FacesUtils.redirectBrowser(HOME_PAGE + "?" + value);
					}
					// Otherwise run our Javascript method to update the URL so it's bookmarkable
					// We'll also clear the search field so it's ready for new input later
					else {
						JavaScriptRunner.runScript(FacesContext.getCurrentInstance(),
								"updateAddressBarURL('" + menuState.getSelectedComponent().getName() + "','" + menuState.getSelectedDemo().getName() + "');document.getElementById('" + event.getComponent().getClientId() + "_input').value = '';");
					}
				}
			}catch (IndexOutOfBoundsException malformedValue) {
				logger.log(Level.WARNING, "Malformed search field value of : " + event.getNewValue());
			}
		}
	}
	
	public List<CategoryGroup> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryGroup> categories) {
		this.categories = categories;
	}
	
	public List<SelectItem> getSearchItems() {
		return searchItems;
	}

	public void setSearchItems(List<SelectItem> searchItems) {
		this.searchItems = searchItems;
	}

	public ComponentGroup getDefaultComponent() {
		return defaultComponent;
	}

	public void setDefaultComponent(ComponentGroup defaultComponent) {
		this.defaultComponent = defaultComponent;
	}

	public Demo getDefaultDemo() {
		return defaultDemo;
	}

	public void setDefaultDemo(Demo defaultDemo) {
		this.defaultDemo = defaultDemo;
	}

	public String getUrlParamGroup() {
		return ParamHandler.URL_PARAM_GROUP;
	}

	public String getUrlParamDemo() {
		return ParamHandler.URL_PARAM_DEMO;
	}
}
