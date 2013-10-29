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

package org.icefaces.samples.showcase.example.compat.menuPopup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.FacesEvent;
import javax.faces.event.ActionEvent;

import com.icesoft.faces.component.DisplayEvent;

import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = MenuPopupBean.BEAN_NAME,
        title = "example.compat.menuPopup.events.title",
        description = "example.compat.menuPopup.events.description",
        example = "/resources/examples/compat/menuPopup/menuPopupEvents.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuPopupEvents.xhtml",
                    resource = "/resources/examples/compat/"+
                               "menuPopup/menuPopupEvents.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuPopupEvents.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/menuPopup/MenuPopupEvents.java")
        }
)
@ManagedBean(name= MenuPopupEvents.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuPopupEvents extends ComponentExampleImpl<MenuPopupEvents> implements Serializable {
	
	public static final String BEAN_NAME = "menuPopupEvents";
	
	private static final int ROW_SIZE = 10;
	private static final String PARAM_NAME = "name";
	
	private List<String> eventLog = new ArrayList<String>(ROW_SIZE);
	
	public MenuPopupEvents() {
		super(MenuPopupEvents.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public int getRowSize() { return ROW_SIZE; }
	public String getParamName() { return PARAM_NAME; }
	public List<String> getEventLog() { return eventLog; }
	
	public void setEventLog(List<String> eventLog) { this.eventLog = eventLog; }
	
	public void displayListener(DisplayEvent event) {
	    addEvent(event, "Display");
	}
	
	public void actionListener(ActionEvent event) {
	    addEvent(event, FacesUtils.getRequestParameter(PARAM_NAME));
	}
	
	private void addEvent(FacesEvent event, String text) {
	    eventLog.add(0, "Fired event " + event.getClass() + " for '" + text + "'");
	    
	    // Cap the list at the displayed row size
	    if (eventLog.size() > ROW_SIZE) {
	        eventLog = eventLog.subList(0, ROW_SIZE);
	    }
	}
}
