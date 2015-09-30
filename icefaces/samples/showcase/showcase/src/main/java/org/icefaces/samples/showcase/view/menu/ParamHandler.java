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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.samples.showcase.view.menu.legacy.LegacyUrlMapper;

/**
 * Class designed to handle the URL parameters "grp" and "exp" used for bookmarkability and loading demos
 * Basically we will map those two params to variables, and upon init if we have values for them we'll try to
 *  figure out what demo we should load
 */
@ManagedBean(name=ParamHandler.BEAN_NAME)
@RequestScoped
public class ParamHandler implements Serializable {
	public static final String BEAN_NAME = "paramHandler";
	
	public static final String URL_PARAM_GROUP = "grp";
	public static final String URL_PARAM_DEMO = "exp";
	public static final String LEGACY_URL_PARAM = "aceMenu";
	
	@ManagedProperty("#{param." + URL_PARAM_GROUP + "}")
	private String paramComponent;
	
	@ManagedProperty("#{param." + URL_PARAM_DEMO + "}")
	private String paramDemo;
	
	@PostConstruct
	public void init() {
		ShowcaseMenu showcase = (ShowcaseMenu)FacesUtils.getManagedBean(ShowcaseMenu.BEAN_NAME);
		UserMenuState menuState = (UserMenuState)FacesUtils.getManagedBean(UserMenuState.BEAN_NAME);
		
		// A few cases here as we process "grp" and "exp" from the URL
		// 1. "grp" is "aceMenu", which means it's a legacy link, so we'll check our CompatibleLinkMap to translate it
		// 2. We have a "grp" and no "exp", which means we need to grab the first default Demo for the "grp"
		// 3. We have both "grp" and "exp", which means find the ComponentGroup by "grp" and child Demo with a matching "exp" name
		if (paramComponent != null) {
			// We'll start by setting the default overview page
			// This helps in case we get a misformed or unknown legacy param or other URL param
			menuState.setSelectedComponent(showcase.getDefaultComponent());
			menuState.setSelectedDemo(showcase.getDefaultDemo());
			menuState.setActiveIndex(0);
			
			// See if we have the legacy URL param, which we need to convert
			if ((LEGACY_URL_PARAM.equals(paramComponent)) && (paramDemo != null)) {
				menuState = LegacyUrlMapper.convert(paramComponent, paramDemo, showcase.getCategories(), menuState);
			}
			
			CategoryGroup loopCategory = null;
			for (int i = 0; i < showcase.getCategories().size(); i++) {
				loopCategory = showcase.getCategories().get(i);
				
				for (ComponentGroup loopComponent : loopCategory.getComponents()) {
					if (loopComponent.getName().equals(paramComponent)) {
						menuState.setSelectedComponent(loopComponent);
						menuState.setActiveIndex(i);
						
						// Also set our default demo, as we can override it if we have the "exp" param further in
						menuState.setSelectedDemo(loopComponent.getFirstDemo());
						
						break;
					}
				}
			}
		}
		
		// In this case we have both an "exp" param and a valid "grp" (that has been translated to an object)
		if ((paramDemo != null) && (menuState.getSelectedComponent() != null)) {
			for (Demo loopDemo : menuState.getSelectedComponent().getDemos()) {
				if (loopDemo.getName().equals(paramDemo)) {
					menuState.setSelectedDemo(loopDemo);
					break;
				}
			}
		}
		
		setParamComponent(null);
		setParamDemo(null);
	}

	public String getParamComponent() {
		return paramComponent;
	}

	public void setParamComponent(String paramComponent) {
		this.paramComponent = paramComponent;
	}

	public String getParamDemo() {
		return paramDemo;
	}

	public void setParamDemo(String paramDemo) {
		this.paramDemo = paramDemo;
    }

	public Demo getSelectedDemo() {
		// We just return the UserMenuState selected demo instead of maintaining this internally, due to our short bean scope
		return ((UserMenuState)FacesUtils.getManagedBean(UserMenuState.BEAN_NAME)).getSelectedDemo();
	}
}
