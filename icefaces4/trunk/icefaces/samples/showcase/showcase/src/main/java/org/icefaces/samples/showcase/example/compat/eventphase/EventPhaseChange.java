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
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = EventPhaseBean.BEAN_NAME,
        title = "example.compat.eventphase.change.title",
        description = "example.compat.eventphase.change.description",
        example = "/resources/examples/compat/eventphase/eventphaseChange.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="eventphaseChange.xhtml",
                    resource = "/resources/examples/compat/"+
                               "eventphase/eventphaseChange.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="EventPhaseChange.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/eventphase/EventPhaseChange.java")
        }
)
@ManagedBean(name= EventPhaseChange.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class EventPhaseChange extends ComponentExampleImpl<EventPhaseChange> implements Serializable {
	
	public static final String BEAN_NAME = "eventphaseChange";
	
	private static final int LOG_SIZE = 10;
	private static final int MIN_NUMBER = -1000;
	private static final int MAX_NUMBER = 1000;
	
	private Random randomizer = new Random(System.nanoTime());
	private List<String> eventLog = new ArrayList<String>(LOG_SIZE);
	private boolean enable = false;
	private boolean useRandom = false;
	private int number = 0;
	
	public EventPhaseChange() {
		super(EventPhaseChange.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public int getLogSize() { return LOG_SIZE; }
	public List<String> getEventLog() { return eventLog; }
	public boolean getEnable() { return enable; }
	public boolean isUseRandom() { return useRandom; }
	public int getMinNumber() { return MIN_NUMBER; }
	public int getMaxNumber() { return MAX_NUMBER; }
	public int getNumber() { return number; }
	
	public void setEventLog(List<String> eventLog) { this.eventLog = eventLog; }
	public void setEnable(boolean enable) { this.enable = enable; }
	public void setUseRandom(boolean useRandom) { this.useRandom = useRandom; }
	public void setNumber(int number) {
	    addEvent("Set Number from " + this.number + " to " + number + ".");
	    
	    this.number = number;
	}
	
	public void checkboxChanged(ValueChangeEvent event) {
	    addEvent("Checkbox valueChangeListener called with number " + event.getNewValue() + ".");
	    
	    if (event.getNewValue() != null) {
	        if ((Boolean)event.getNewValue()) {
	            int toSet = randomizer.nextInt(MAX_NUMBER);
	            
	            addEvent("Generating random number " + toSet + ", going to set it.");
	            
	            setNumber(toSet);
	        }
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
