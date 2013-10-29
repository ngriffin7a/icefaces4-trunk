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

package org.icefaces.samples.showcase.example.compat.eventphase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.eventphase.title",
        description = "example.compat.eventphase.description",
        example = "/resources/examples/compat/eventphase/eventphase.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="eventphase.xhtml",
                    resource = "/resources/examples/compat/"+
                               "eventphase/eventphase.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="EventPhaseBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/eventphase/EventPhaseBean.java")
        }
)
@Menu(
	title = "menu.compat.eventphase.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.eventphase.subMenu.main",
                    isDefault = true,
                    exampleBeanName = EventPhaseBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.eventphase.subMenu.change",
                    exampleBeanName = EventPhaseChange.BEAN_NAME),
            @MenuLink(title = "menu.compat.eventphase.subMenu.action",
                    exampleBeanName = EventPhaseAction.BEAN_NAME)
})
@ManagedBean(name= EventPhaseBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class EventPhaseBean extends ComponentExampleImpl<EventPhaseBean> implements Serializable {
	
	public static final String BEAN_NAME = "eventphase";
	
	private static final int NOT_FOUND_INDEX = -1;
    private static final int LOG_SIZE = 10;
	
	private String[] availableProvinces = new String[] {
	    "Alberta",
	    "British Columbia",
	    "Manitoba",
	    "New Brunswick",
	    "Newfoundland",
	    "Northwest Territories",
	    "Nova Scotia",
	    "Nunavut",
	    "Ontario",
	    "Prince Edward Island",
	    "Quebec",
	    "Saskatchewan",
	    "Yukon"
	};
	private String[] availableCities = new String[] {
	    "Edmonton",
	    "Victoria",
	    "Winnipeg",
	    "Fredericton",
	    "St. John's",
	    "Yellowknife",
	    "Halifax",
	    "Iqaluit",
	    "Toronto",
	    "Charlottetown",
	    "Quebec City",
	    "Regina",
	    "Whitehorse"
	};
	
	private List<String> eventLog = new ArrayList<String>(LOG_SIZE);
	private boolean enable = true;
	private String province = availableProvinces[0];
	private String city = availableCities[0];
	private boolean provinceChanged = false;
	
	public EventPhaseBean() {
		super(EventPhaseBean.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public String[] getAvailableProvinces() { return availableProvinces; }
	public String[] getAvailableCities() { return availableCities; }
	
	public int getLogSize() { return LOG_SIZE; }
	public List<String> getEventLog() { return eventLog; }
	public boolean getEnable() { return enable; }
	public String getProvince() { return province; }
	public String getCity() { return city; }
	
	public void setEventLog(List<String> eventLog) { this.eventLog = eventLog; }
	public void setEnable(boolean enable) { this.enable = enable; }
	public void setProvince(String province) {
	    addEvent("Set Province from '" + this.province + "' to '" + province + "'.");
	    
	    this.province = province;
	}
	public void setCity(String city) {
	    addEvent("Set City from '" + this.city + "' to '" + city + "'.");
	    
	    this.city = city;
	}
	
	private void addEvent(String text) {
	    eventLog.add(0, System.currentTimeMillis() + "> " + text);
	    
	    // Cap the list at the displayed row size
	    if (eventLog.size() > LOG_SIZE) {
	        eventLog = eventLog.subList(0, LOG_SIZE);
	    }
	}
	
	private int lookupProvinceIndex(String province) {
	    for (int i = 0; i < availableProvinces.length; i++) {
	        if (province.equals(availableProvinces[i])) {
	            return i;
	        }
	    }
	    
	    return NOT_FOUND_INDEX;
	}
	
	private int lookupCityIndex(String city) {
	    for (int i = 0; i < availableCities.length; i++) {
	        if (city.equals(availableCities[i])) {
	            return i;
	        }
	    }
	    
	    return NOT_FOUND_INDEX;
	}	
	
	public void provinceChanged(ValueChangeEvent event) {
	    addEvent("Province changed from '" + event.getOldValue() + "' to '" + event.getNewValue() + "'.");
        int provinceIndex = lookupProvinceIndex(event.getNewValue().toString());
	    
	    if (provinceIndex != NOT_FOUND_INDEX) {
	        provinceChanged = true;
	        
	        addEvent("Found matching city '" + availableCities[provinceIndex] + "', going to set.");
	        
	        setCity(availableCities[provinceIndex]);
	    }
	}
	
	public void cityChanged(ValueChangeEvent event) {
	    if (!provinceChanged) {
            addEvent("City changed from '" + event.getOldValue() + "' to '" + event.getNewValue() + "'.");
            
            int cityIndex = lookupCityIndex(event.getNewValue().toString());
            
            if (cityIndex != NOT_FOUND_INDEX) {
                addEvent("Found matching province '" + availableProvinces[cityIndex] + "', going to set.");
                
                setProvince(availableProvinces[cityIndex]);
            }
        }
        else {
            addEvent("Province and city were both changed. Only applying province."); 
        }
        
        provinceChanged = false;
	}
}
