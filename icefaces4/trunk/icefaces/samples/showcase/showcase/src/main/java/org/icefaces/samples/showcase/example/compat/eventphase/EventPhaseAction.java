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
import javax.faces.event.ActionEvent;
import javax.faces.component.UICommand;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = EventPhaseBean.BEAN_NAME,
        title = "example.compat.eventphase.action.title",
        description = "example.compat.eventphase.action.description",
        example = "/resources/examples/compat/eventphase/eventphaseAction.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="eventphaseAction.xhtml",
                    resource = "/resources/examples/compat/"+
                               "eventphase/eventphaseAction.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="EventPhaseAction.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/eventphase/EventPhaseAction.java")
        }
)
@ManagedBean(name= EventPhaseAction.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class EventPhaseAction extends ComponentExampleImpl<EventPhaseAction> implements Serializable {
	
	public static final String BEAN_NAME = "eventphaseAction";
	
	private static final int LOG_SIZE = 10;
	
	private List<String> eventLog = new ArrayList<String>(LOG_SIZE);
	private boolean enable = true;
	private String text;
	
	public EventPhaseAction() {
		super(EventPhaseAction.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public int getLogSize() { return LOG_SIZE; }
	public List<String> getEventLog() { return eventLog; }
	public boolean getEnable() { return enable; }
	public String getText() { return text; }
	
	public void setEventLog(List<String> eventLog) { this.eventLog = eventLog; }
	public void setEnable(boolean enable) { this.enable = enable; }
	public void setText(String text) { this.text = text; }
	
	public void fireListener(ActionEvent event) {
	    if ((event.getComponent() != null) &&
	        (event.getComponent() instanceof UICommand)) {
	        addEvent("Successfully fired actionListener from button \"" + ((UICommand)event.getComponent()).getValue() + "\".");
        }
        else {
            addEvent("Successfully fired actionListener from unknown source.");
        }
	}
	
	private void addEvent(String text) {
	    eventLog.add(0, System.currentTimeMillis() + "> " + text);
	    
	    // Cap the list at the displayed row size
	    if (eventLog.size() > LOG_SIZE) {
	        eventLog = eventLog.subList(0, LOG_SIZE);
	    }
	}	
}
