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

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.util.FacesUtils;

@ManagedBean(name=UserMenuState.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class UserMenuState implements Serializable {
	public static final String BEAN_NAME = "userMenuState";
	
	private ComponentGroup selectedComponent;
	private Demo selectedDemo;
	private int activeIndex = 0;
	private boolean sourceCodeCollapsed = true;
	
	@PostConstruct
	public void initUserMenuState() {
		// Initialize to the default component and demo
		ShowcaseMenu menu = (ShowcaseMenu)FacesUtils.getManagedBean(ShowcaseMenu.BEAN_NAME);
		selectedComponent = menu.getDefaultComponent();
		selectedDemo = menu.getDefaultDemo();
		activeIndex = 0;
	}
	
	public ComponentGroup getSelectedComponent() {
		return selectedComponent;
	}
	public void setSelectedComponent(ComponentGroup selectedComponent) {
		this.selectedComponent = selectedComponent;
	}
	public Demo getSelectedDemo() {
		return selectedDemo;
	}
	public void setSelectedDemo(Demo selectedDemo) {
		this.selectedDemo = selectedDemo;
	}
	public int getActiveIndex() {
		return activeIndex;
	}
	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}
	// Note we don't do anything with the searchFieldHolder getter/setter
	// This is because changing the backing value would mean the component is considered changed
	//  and therefore we'd get a 50kb+ update on the client
	// What we do instead is process the search field in the valueChangeListener and don't store the
	//  current value at all
	public String getSearchFieldHolder() {
		return null;
	}
	public void setSearchFieldHolder(String searchField) {
	}
	public boolean isSourceCodeCollapsed() {
		return sourceCodeCollapsed;
	}
	public void setSourceCodeCollapsed(boolean sourceCodeCollapsed) {
		this.sourceCodeCollapsed = sourceCodeCollapsed;
	}
}
