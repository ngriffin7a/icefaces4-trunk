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

import javax.faces.model.SelectItem;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ComponentExample(
        title = "example.ace.autocompleteentry.title",
        description = "example.ace.autocompleteentry.description",
        example = "/resources/examples/ace/autocompleteentry/autoCompleteEntry.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="autoCompleteEntry.xhtml",
                    resource = "/resources/examples/ace/autocompleteentry/autoCompleteEntry.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="AutoCompleteEntryBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/autocompleteentry/AutoCompleteEntryBean.java")
        }
)
@Menu(
	title = "menu.ace.autocompleteentry.subMenu.main",
	menuLinks = {
	        @MenuLink(title = "menu.ace.autocompleteentry.subMenu.main",
	                isDefault = true,
                    exampleBeanName = AutoCompleteEntryBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.autocompleteentry.subMenu.select",
                    exampleBeanName = AutoCompleteEntrySelectBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.autocompleteentry.subMenu.facet",
                    exampleBeanName = AutoCompleteEntryFacetBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.autocompleteentry.subMenu.match",
                    exampleBeanName = AutoCompleteEntryMatchBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.autocompleteentry.subMenu.rows",
                    exampleBeanName = AutoCompleteEntryRowsBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.autocompleteentry.subMenu.lazy",
                    exampleBeanName = AutoCompleteEntryLazyBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.autocompleteentry.subMenu.label",
                    exampleBeanName = AutoCompleteEntryLabelBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.autocompleteentry.subMenu.indicator",
                    exampleBeanName = AutoCompleteEntryIndicatorBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.autocompleteentry.subMenu.reqStyle",
                    exampleBeanName = AutoCompleteEntryReqStyleBean.BEAN_NAME)
    }
)
@ManagedBean(name= AutoCompleteEntryBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AutoCompleteEntryBean extends ComponentExampleImpl<AutoCompleteEntryBean> implements Serializable 
{
    public static final String BEAN_NAME = "autoCompleteEntryBean";

    public static final String CITIES_FILENAME = "City-Names.txt";
	public static final String RESOURCE_PATH = "/resources/selectinputtext/";
	
	public AutoCompleteEntryBean() {
		super(AutoCompleteEntryBean.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public List<SelectItem> cities;

    public List<SelectItem> getCities(){
		if (cities == null) {
			cities = new ArrayList<SelectItem>();
			for (String city : readCityFile()) {
				cities.add(new SelectItem(city));
			}
		}
        return cities;
    }
	
	private String selectedText = null; // Text the user is typing in
	public String getSelectedText() { return selectedText; }
	public void setSelectedText(String selectedText) { this.selectedText = selectedText; }
	
	/**
	 * Method to read the list of cities from the file CITIES_FILENAME
	 *  (which should be a text file with one city per line)
	 * The read list will be stored as cities and can be referenced by any Autocomplete demo
	 */
	private static List<String> readCityFile() {
	    InputStream fileIn = null;
	    BufferedReader in = null;
                    
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            fileIn= ec.getResourceAsStream(AutoCompleteEntryBean.RESOURCE_PATH + CITIES_FILENAME);
            
            if (fileIn != null) {
                // Wrap in a buffered reader so we can parse it
                in = new BufferedReader(new InputStreamReader(fileIn));
                
                // Populate our list of cities from the file
                List<String> loadedCities = new ArrayList<String>(5000);
                String read;
                while ((read = in.readLine()) != null) {
                    loadedCities.add(read);
                }
                
                return loadedCities;
            }
        }catch (Exception failedRead) {
            failedRead.printStackTrace();
        }finally {
            // Close the stream if we can
            try{
                if (in != null) {
                    in.close();
                }
            }catch (Exception failedClose) {
                failedClose.printStackTrace();
            }
        }
        
        // Return an informative list if something went wrong in the process
        List<String> errorReturn = new ArrayList<String>(1);
        errorReturn.add("Error Loading City List");
        return errorReturn;
	}
}
