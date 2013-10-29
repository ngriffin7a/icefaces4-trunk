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

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

@ManagedBean(name= AutoCompleteEntryData.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AutoCompleteEntryData implements Serializable
{
    public static final String BEAN_NAME = "autoCompleteEntryData";
	
    public static final String CITIES_FILENAME = "World_Cities_Location_table.txt";
	public static final String RESOURCE_PATH = "/resources/autocompleteentry/";
    
	private static List<City> cities;
	private static List<SelectItem> simpleCities;
	private static Map<String, City> citiesMap;

    public static List<City> getCities() {
		if (cities == null) {
			cities = readLocationsFile();
		}
        return cities;
    }
	
    public static List<SelectItem> getSimpleCities() {
		if (simpleCities == null) {
			simpleCities = new ArrayList<SelectItem>();
			for (City city : getCities()) {
				simpleCities.add(new SelectItem(city.getName()));
			}
		}
        return simpleCities;
    }
	
    public static Map<String, City> getCitiesMap() {
		if (citiesMap == null) {
			citiesMap = new HashMap<String, City>();
			for (City city : getCities()) {
				citiesMap.put(city.getName(), city);
			}
		}
        return citiesMap;
    }
	
	private static List<City> readLocationsFile() {
	    InputStream fileIn = null;
	    BufferedReader in = null;
                    
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            fileIn= ec.getResourceAsStream(RESOURCE_PATH + CITIES_FILENAME);
            
            if (fileIn != null) {
                // Wrap in a buffered reader so we can parse it
                in = new BufferedReader(new InputStreamReader(fileIn));
                
                // Populate our list of cities from the file
                List<City> loadedCities = new ArrayList<City>();
                String read;
                while ((read = in.readLine()) != null) {
                    loadedCities.add(parseCity(read));
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
		City errorCity = new City();
		errorCity.setName("Error Loading City List");
		errorCity.setCountry("ERROR");
        List<City> errorReturn = new ArrayList<City>(1);
        errorReturn.add(errorCity);
        return errorReturn;
	}
	
	private static City parseCity(String line) {
		City city = new City();
		
		StringTokenizer st = new StringTokenizer(line, ";");
		if (st.countTokens() == 6) {
			st.nextToken();
			city.setCountry(st.nextToken().replace("\"", ""));
			city.setName(st.nextToken().replace("\"", ""));
			
			String latitude = st.nextToken().replace("\"", "");
			try {
				city.setLatitude(Double.valueOf(latitude).doubleValue());
			} catch (Exception e) {
				city.setLatitude(0);
			}
			
			String longitude = st.nextToken().replace("\"", "");
			try {
				city.setLongitude(Double.valueOf(longitude).doubleValue());
			} catch (Exception e) {
				city.setLongitude(0);
			}
			
			String altitude = st.nextToken().replace("\"", "");
			try {
				city.setAltitude(Double.valueOf(altitude).doubleValue());
			} catch (Exception e) {
				city.setAltitude(0);
			}
		}
		
		return city;
	}
}