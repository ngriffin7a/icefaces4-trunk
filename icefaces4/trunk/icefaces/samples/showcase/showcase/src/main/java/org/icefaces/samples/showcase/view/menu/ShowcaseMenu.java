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
import org.icefaces.samples.showcase.view.menu.legacy.LegacyUrlMapper;
import org.icefaces.util.JavaScriptRunner;

@ManagedBean(name=ShowcaseMenu.BEAN_NAME,eager=true)
@ApplicationScoped
public class ShowcaseMenu implements Serializable {
	public static final String BEAN_NAME = "showcaseMenu";
    private static final Logger logger = Logger.getLogger(ShowcaseMenu.class.toString());
	
	public static final String URL_PARAM_GROUP = "grp";
	public static final String URL_PARAM_DEMO = "exp";
	private static final String LEGACY_URL_PARAM = "aceMenu";
	private static final String HOME_PAGE = "showcase.jsf";
	
	// The menu is made up of a hierarchy of CategoryGroup -> ComponentGroup(s) -> Demo(s)
	private List<CategoryGroup> categories = CentralDataList.generate();
	private List<SelectItem> searchItems;
	private ComponentGroup defaultComponent;
	private Demo defaultDemo;
	
	@PostConstruct
	public void initShowcaseMenu() {
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
	
	public void initSearchItems() {
		// Loop through categories, build selectitems
		searchItems = new ArrayList<SelectItem>();
		for (CategoryGroup loopCategory : categories) {
			for (ComponentGroup loopComponent : loopCategory.getComponents()) {
				// First we want to add a generic search term for each demo
				if (loopComponent.getHasDemos()) {
					for (Demo loopDemo : loopComponent.getDemos()) {
						searchItems.add(new SelectItem(URL_PARAM_GROUP + "=" + loopComponent.getName() + "&" + URL_PARAM_DEMO + "=" + loopDemo.getName(),
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
							searchItems.add(new SelectItem(URL_PARAM_GROUP + "=" + loopComponent.getName() + "&" + URL_PARAM_DEMO + "=" + loopComponent.getFirstDemo().getName(),
									        loopKeyword + " (" + loopComponent.getName() + ")"));
						}
					}
				}
			}
		}
	}
	
	public void processParams() {
		// If we are reaching here from a postback, such as a user interacting with the page, we don't need to process the params
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		
		// Next determine if we have checked our params this lifecycle
		// If we did just skip any further checks, but mark false for any possible future checks
		UserMenuState menuState = (UserMenuState)FacesUtils.getManagedBean(UserMenuState.BEAN_NAME);
		if (menuState.getCheckedParam()) {
			menuState.setCheckedParam(false);
			return;
		}
		boolean needRefresh = false;
		
		// A few cases here as we process "grp" and "exp" from the URL
		// 1. "grp" is "aceMenu", which means it's a legacy link, so we'll check our CompatibleLinkMap to translate it
		// 2. We have a "grp" and no "exp", which means we need to grab the first default Demo for the "grp"
		// 3. We have both "grp" and "exp", which means find the ComponentGroup by "grp" and child Demo with a matching "exp" name
		if (menuState.getParamComponent() != null) {
			// We'll start by setting the default overview page
			// This helps in case we get a misformed or unknown legacy param or other URL param
			menuState.setSelectedComponent(defaultComponent);
			menuState.setSelectedDemo(defaultDemo);
			menuState.setActiveIndex(0);
			
			// See if we have the legacy URL param, which we need to convert
			if ((LEGACY_URL_PARAM.equals(menuState.getParamComponent())) && (menuState.getParamDemo() != null)) {
				menuState = LegacyUrlMapper.convert(categories, menuState);
			}
			
			CategoryGroup loopCategory = null;
			for (int i = 0; i < categories.size(); i++) {
				loopCategory = categories.get(i);
				
				for (ComponentGroup loopComponent : loopCategory.getComponents()) {
					if (loopComponent.getName().equals(menuState.getParamComponent())) {
						menuState.setSelectedComponent(loopComponent);
						menuState.setActiveIndex(i);
						
						// Also set our default demo, as we can override it if we have the "exp" param further in
						menuState.setSelectedDemo(loopComponent.getFirstDemo());
						
						break;
					}
				}
			}
			
			needRefresh = true;
		}
		
		// In this case we have both an "exp" param and a valid "grp" (that has been translated to an object)
		if ((menuState.getParamDemo() != null) && (menuState.getSelectedComponent() != null)) {
			for (Demo loopDemo : menuState.getSelectedComponent().getDemos()) {
				if (loopDemo.getName().equals(menuState.getParamDemo())) {
					menuState.setSelectedDemo(loopDemo);
					break;
				}
			}
		}
		
		// If we updated our demo we'll do some refreshing to get the proper state
		if (needRefresh) {
			// A bit of a corner case workaround here
			// We reach this method from an f:event and we change a dynamic ui:include to point to the demo content
			// The problem is ui:include is a TagHandler, which means it is processed BEFORE this method
			// So changing the demo here won't actually update the page, because the ui:include has already been processed
			// To get around this we have a workaround of redirecting back to our current page (basically a refresh)
			// This ensures the ui:include TagHandler is re-initialized with the proper demo content
			// Note as mentioned this is a corner case only applicable if the user manually changes the browser URL,
			//  we won't have to do this for menu navigation, external links, etc.
			String link = HOME_PAGE + "?" + URL_PARAM_GROUP + "=" + menuState.getSelectedComponent().getName();
			if (menuState.getSelectedDemo() != null) {
				link += "&" + URL_PARAM_DEMO + "=" + menuState.getSelectedDemo().getName();
			}
			FacesUtils.redirectBrowser(link);
			
			// Reset our parameters so we don't perform this step again
			// They'll still be in the URL for bookmarking, until the user navigates somewhere else
			menuState.setCheckedParam(true);
			menuState.setParamComponent(null);
			menuState.setParamDemo(null);
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
				(event.getNewValue().toString().contains(URL_PARAM_GROUP)) &&
				(event.getNewValue().toString().contains(URL_PARAM_DEMO))) {
			// Trim grp=ace:dateTimeEntry&exp=Overview to "ace:dateTimeEntry" (for newComponent) and "Overview" (for newDemo)
			// This means trimming out "grp=" and "&exp=", which is why we have the fancy substring work
			try{
				String value = event.getNewValue().toString();
				String newComponent = value.substring(value.indexOf(URL_PARAM_GROUP)+URL_PARAM_GROUP.length()+1,
												      value.indexOf(URL_PARAM_DEMO)-1);
				String newDemo = value.substring(value.indexOf(URL_PARAM_DEMO)+URL_PARAM_DEMO.length()+1);
				
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
						menuState.setCheckedParam(true);
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
		if (searchItems == null) {
			initSearchItems();
		}
		
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
		return URL_PARAM_GROUP;
	}

	public String getUrlParamDemo() {
		return URL_PARAM_DEMO;
	}
}
