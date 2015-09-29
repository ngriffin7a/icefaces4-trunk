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

package org.icefaces.samples.showcase.example.ace.autocompleteentry;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.dataGenerators.ImageSet;

@ManagedBean(name= AutoCompleteEntryFacetBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AutoCompleteEntryFacetBean implements Serializable
{
    public static final String BEAN_NAME = "autoCompleteEntryFacetBean";
	public String getBeanName() { return BEAN_NAME; }
	private ImageSet.ImageInfo house;

    public AutoCompleteEntryFacetBean() 
    {
		house = ImageSet.getImage(ImageSet.ImageSelect.HOUSE);
    }
    
	public List<City> cities;
	private String selectedText;
	
	public String getSelectedText() {
	    return selectedText;
	}
	
	public void setSelectedText(String selectedText) {
	    this.selectedText = selectedText;
	}
	
	public City getSelectedCity() { 
		if (selectedText != null) {
			return AutoCompleteEntryData.getCitiesMap().get(selectedText);
		}
		
		return null; 
	}
	
    public List<City> getCities(){
		if (cities == null) {
			cities = AutoCompleteEntryData.getCities();
		}
        return cities;
    }

    public ImageSet.ImageInfo getHouse() {
        return house;
    }
}