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

package org.icefaces.samples.showcase.example.ace.autocompleteentry;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ComponentExample(
        parent = AutoCompleteEntryBean.BEAN_NAME,
        title = "example.ace.autocompleteentry.facet.title",
        description = "example.ace.autocompleteentry.facet.description",
        example = "/resources/examples/ace/autocompleteentry/autoCompleteEntryFacet.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="autoCompleteEntryFacet.xhtml",
                    resource = "/resources/examples/ace/autocompleteentry/autoCompleteEntryFacet.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="AutoCompleteEntryFacetBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/autocompleteentry/AutoCompleteEntryFacetBean.java")
        }
)
@ManagedBean(name= AutoCompleteEntryFacetBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AutoCompleteEntryFacetBean extends ComponentExampleImpl<AutoCompleteEntryFacetBean> implements Serializable
{
    public static final String BEAN_NAME = "autoCompleteEntryFacetBean";

    public AutoCompleteEntryFacetBean() 
    {
        super(AutoCompleteEntryFacetBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
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
}