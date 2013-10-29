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

package org.icefaces.samples.showcase.example.compat.autocomplete;

import java.io.Serializable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name= AutocompleteData.BEAN_NAME)
@ApplicationScoped
public class AutocompleteData implements Serializable {
    public static final String BEAN_NAME = "autocompleteData";
    
    public static final String CITIES_FILENAME = "City-Names.txt";
	public static List<String> cities;

	public static final String RESOURCE_PATH = "/resources/selectinputtext/";

    private static List<String> getCities(){
        if( cities == null ){
            cities = readCityFile();
        }
        return cities;
    }
	
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
            fileIn= ec.getResourceAsStream(AutocompleteData.RESOURCE_PATH + CITIES_FILENAME);
            
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
	
	/**
	 * Wrapper method to allow full defaults to be passed to the wrapList method
	 * This will return the full list of Cities wrapped in SelectItems
	 */
	public static List<SelectItem> wrapList() {
	    return wrapList(null, getCities());
	}
	
	/**
	 * Wrapper method to allow the default list and passed filter to be used with the wrapList method
	 * This will return the filtered list of Cities wrapped in SelectItems
	 */
	public static List<SelectItem> wrapList(String filter) {
	    return wrapList(filter, getCities());
	}
	
	/**
	 * Wrapper method to allow the full passed list to be used with the wrapList method
	 * This will return the full passed list wrapped in SelectItems
	 */
	public static List<SelectItem> wrapList(List<String> toWrap) {
	    return wrapList(null, toWrap);
	}
	
	/**
	 * Method to wrap a list of Strings in a list of SelectItems
	 * In addition this will filter the wrapped items based on the passed String
	 * The filtering will check (case insensitively) if the start of each String matches the filter
	 *
	 *@param filter to trim the list by
	 *@param toWrap list of Strings to wrap in SelectItems
	 *@return wrapped and filtered SelectItem list
	 */
	public static List<SelectItem> wrapList(String filter, List<String> toWrap) {
	    // Ensure we have a valid list to wrap and filter
	    if ((toWrap == null) || (toWrap.size() == 0)) {
	        return new ArrayList<SelectItem>(0);
	    }
	    
	    // Return a blank list for a non-null BUT blank filter
	    // This happens when the user has cleared their text
	    // In the backend we always pass null when we don't want a filter, not a blank
	    if ((filter != null) && (filter.trim().length() == 0)) {
	        return new ArrayList<SelectItem>(0);
	    }
	    
	    // Prepare the filter for case insensitive comparisons
	    if (filter != null) {
	        filter = filter.toLowerCase();
	    }
	    
	    // Loop through the list of passed Strings
	    List<SelectItem> toReturn = new ArrayList<SelectItem>(toWrap.size());
	    for (String currentWrap : toWrap) {
	        // Filter if we have something to filter on
	        if (filter != null) {
	            if (currentWrap.toLowerCase().startsWith(filter)) {
	                toReturn.add(new SelectItem(currentWrap));
	            }
	        }
	        // Otherwise just add the item, as we'll wrap the full list if we don't have a filter
	        else {
	            toReturn.add(new SelectItem(currentWrap));
	        }
	    }
	    
	    // Populate with a less filtered version if we didn't have any matches
	    if ((toReturn.size() == 0) && (filter != null)) {
	        return wrapList(String.valueOf(filter.charAt(0)), toWrap);
	    }
	    
	    return toReturn;
	}
}
