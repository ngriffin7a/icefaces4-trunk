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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean(name= AutoCompleteEntryBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AutoCompleteEntryBean implements Serializable 
{
    public static final String BEAN_NAME = "autoCompleteEntryBean";
	public String getBeanName() { return BEAN_NAME; }

    public static final String CITIES_FILENAME = "City-Names.txt";
	public static final String RESOURCE_PATH = "/resources/selectinputtext/";
	
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
