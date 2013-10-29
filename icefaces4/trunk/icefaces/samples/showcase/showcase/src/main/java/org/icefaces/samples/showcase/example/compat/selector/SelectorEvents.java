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

package org.icefaces.samples.showcase.example.compat.selector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import com.icesoft.faces.component.ext.ClickActionEvent;
import com.icesoft.faces.component.ext.RowSelectorEvent;
import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = SelectorBean.BEAN_NAME,
        title = "example.compat.selector.events.title",
        description = "example.compat.selector.events.description",
        example = "/resources/examples/compat/selector/selectorEvents.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="selectorEvents.xhtml",
                    resource = "/resources/examples/compat/"+
                               "selector/selectorEvents.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="SelectorEvents.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/selector/SelectorEvents.java")
        }
)
@ManagedBean(name= SelectorEvents.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SelectorEvents extends ComponentExampleImpl<SelectorEvents> implements Serializable {
	
    public static final String BEAN_NAME = "selectorEvents";

    private static final int ROW_SIZE = 10;
    private List<String> eventLog;
    private List<SelectableCar> data;

    public SelectorEvents() {
            super(SelectorEvents.class);
            eventLog = new ArrayList<String>(ROW_SIZE);
            data = new ArrayList<SelectableCar>(DataTableData.getDefaultSelectableData());
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    private void addEvent(String toLog) {
        eventLog.add(0, toLog);

        // Cap the list at the displayed row size
        if (eventLog.size() > ROW_SIZE) {
            eventLog = eventLog.subList(0, ROW_SIZE);
        }
    }

    public void clickListener(ClickActionEvent event) {
        StringBuilder sb = new StringBuilder(30);
        sb.append("Fired clickListener for row index ");
        sb.append(event.getRow());
        sb.append(". Double click? ");
        sb.append(event.isDblClick());

        addEvent(sb.toString());
    }

    public void selectionListener(RowSelectorEvent event) {
        StringBuilder sb = new StringBuilder(30);
        sb.append("Fired selectionListener for row index ");
        sb.append(event.getRow());
        sb.append(". Changed to selected? ");
        sb.append(event.isSelected());

        addEvent(sb.toString());
    }
    
    public int getRowSize() { return ROW_SIZE; }
    public List<String> getEventLog() { return eventLog; }
    public void setEventLog(List<String> eventLog) { this.eventLog = eventLog; }
    public List<SelectableCar> getData() { return data; }
}
